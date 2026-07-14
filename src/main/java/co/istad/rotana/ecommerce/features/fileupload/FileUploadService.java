package co.istad.rotana.ecommerce.features.fileupload;

import co.istad.rotana.ecommerce.features.fileupload.dto.FileResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {
    void deleteByName(String fileName);
    FileResponse findByName(String fileName);
    Page<FileResponse> findAll(int pageNumber, int pageSize);
    List<FileResponse> uploadMultiple(List<MultipartFile> files);
    FileResponse upload(MultipartFile file);
}
