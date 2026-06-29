package co.istad.rotana.ecommerce.features.fileupload;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.File;

public interface FileUploadRepository extends JpaRepository<FileUpload, Integer> {
}
