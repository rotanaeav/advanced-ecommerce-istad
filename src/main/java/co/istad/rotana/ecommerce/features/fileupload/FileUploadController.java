package co.istad.rotana.ecommerce.features.fileupload;

import co.istad.rotana.ecommerce.features.fileupload.dto.FileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadService fileUploadService;

    @GetMapping("/findAll")
    public Page<FileResponse> findAll(
            @RequestParam(required = false,defaultValue = "0") int pageNumber,
            @RequestParam(required = false,defaultValue = "25") int pageSize
    ){
       return fileUploadService.findAll(pageNumber,pageSize);
    }
    @GetMapping("/{fileName}")
    public FileResponse findByName(@PathVariable String fileName){
        return fileUploadService.findByName(fileName);
    }
    @DeleteMapping("/{fileName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByName(@PathVariable String fileName){
        fileUploadService.deleteByName(fileName);
    }
    @PostMapping("/multiple")
    public List<FileResponse> uploadMultiple(@RequestParam("files") List<MultipartFile> files){
        return fileUploadService.uploadMultiple(files);
    }

    @PostMapping
    public FileResponse upload(@RequestPart MultipartFile file){
        return fileUploadService.upload(file);
    }
}
