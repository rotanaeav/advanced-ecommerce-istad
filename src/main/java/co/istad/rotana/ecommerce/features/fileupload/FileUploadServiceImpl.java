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
    @Value("${file-upload.server-path}")
    private String serverPath;
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

        return FileResponse.builder()
                .fileName(fileName+"."+fileExtension)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .url("localhost:8080/api/v1/files/"+fileName+"."+fileExtension)
                .build();
    }
}
