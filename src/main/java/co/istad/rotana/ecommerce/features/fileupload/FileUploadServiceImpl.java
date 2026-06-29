package co.istad.rotana.ecommerce.features.fileupload;

import co.istad.rotana.ecommerce.exception.FileUploadException;
import co.istad.rotana.ecommerce.features.fileupload.dto.FileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
@Service
public class FileUploadServiceImpl implements FileUploadService{
private final FileUploadRepository fileUploadRepository;
    @Value("${file-upload.server-path}")
    private String serverPath;
    @Value("${file-upload.base-uri}")
    private String baseUri;

    public FileUploadServiceImpl(FileUploadRepository fileUploadRepository) {
        this.fileUploadRepository = fileUploadRepository;
    }


    @Override
    public FileResponse upload(MultipartFile file) {

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

        return FileResponse.builder()
                .fileName(fileUpload.getName())
                .fileExtension(fileUpload.getExtension())
                .fileType(fileUpload.getContentType())
                .fileSize(fileUpload.getSize())
                .uri(baseUri+"/"+fileUpload.getName()+"."+fileUpload.getExtension())
                .build();
    }
}
