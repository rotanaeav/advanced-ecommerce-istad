package co.istad.rotana.ecommerce.features.fileupload.dto;

import lombok.Builder;

@Builder
public record FileResponse(
        String url,
        String fileName,
        String fileType,
        Long fileSize
)
{}
