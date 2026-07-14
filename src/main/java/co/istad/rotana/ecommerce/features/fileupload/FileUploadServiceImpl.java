package co.istad.rotana.ecommerce.features.fileupload;

import co.istad.rotana.ecommerce.exception.FileUploadException;
import co.istad.rotana.ecommerce.features.fileupload.dto.FileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    private final FileUploadRepository fileUploadRepository;
    private final FileUploadMapper fileUploadMapper;
    @Value("${file-upload.server-path}")
    private String serverPath;


    public FileUploadServiceImpl(FileUploadRepository fileUploadRepository, FileUploadMapper fileUploadMapper) {
        this.fileUploadRepository = fileUploadRepository;
        this.fileUploadMapper = fileUploadMapper;
    }


    @Override
    public void deleteByName(String fileName) {
        FileUpload foundFile = fileUploadRepository.findByName(fileName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "File not found!!!"
                ));

        Path path = Paths.get(String.format("%s%s.%s", serverPath, foundFile.getName(), foundFile.getExtension()));

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FileUploadException("File delete failed", e);
        }

        fileUploadRepository.delete(foundFile);

    }

    @Override
    public FileResponse findByName(String fileName) {

        return fileUploadRepository
                .findByName(fileName)
                .map(fileUploadMapper::mapFileUploadToFileResponse)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"File not found!!!"));
    }

    @Override
    public Page<FileResponse> findAll(int pageNumber, int pageSize) {
        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortById);
        Page<FileUpload> fileUploadPage =fileUploadRepository.findAll(pageable);
        return fileUploadPage
                .map(fileUploadMapper::mapFileUploadToFileResponse);
    }

    @Override
    public List<FileResponse> uploadMultiple(List<MultipartFile> files) {
        return files.stream()
                .map(this::saveFile)
                .collect(Collectors.toList());
    }

    @Override
    public FileResponse upload(MultipartFile file) {
        return saveFile(file);

    }
    private FileResponse saveFile(MultipartFile file) {
        String fileName = UUID.randomUUID().toString();
        String fileExtension = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf(".")+1);
        // create path obj
        Path path = Paths.get(String.format("%s%s.%s",serverPath,fileName,fileExtension));
        try {
            Files.copy(file.getInputStream(), path);
        }catch (IOException e) {
            throw new FileUploadException("File upload failed",e);
        }

        FileUpload fileUpload = new FileUpload();
        fileUpload.setName(fileName);
        fileUpload.setExtension(fileExtension);
        fileUpload.setSize(file.getSize());
        fileUpload.setContentType(file.getContentType());
        fileUploadRepository.save(fileUpload);

        return fileUploadMapper
                .mapFileUploadToFileResponse(fileUpload);
    }
}
