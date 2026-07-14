package co.istad.rotana.ecommerce.features.fileupload;

import co.istad.rotana.ecommerce.features.fileupload.dto.FileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUploadMapper {
    @Value("${file-upload.base-uri}")
    private String baseUri;
    public FileResponse mapFileUploadToFileResponse(FileUpload fileUpload) {
        return FileResponse.builder()
                .fileName(fileUpload.getName())
                .fileExtension(fileUpload.getExtension())
                .fileType(fileUpload.getContentType())
                .fileSize(fileUpload.getSize())
                .uri(baseUri+"/"+fileUpload.getName()+"."+fileUpload.getExtension())
                .build();
    }
}
