package co.istad.rotana.ecommerce.features.fileupload;

import co.istad.rotana.ecommerce.features.fileupload.dto.FileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    FileResponse upload(MultipartFile file);
}
