package co.istad.rotana.ecommerce.features.fileupload;

import co.istad.rotana.ecommerce.features.fileupload.dto.FileResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.File;
import java.util.Optional;

public interface FileUploadRepository extends JpaRepository<FileUpload, Integer> {
    Optional<FileUpload> findByName(String name);
}
