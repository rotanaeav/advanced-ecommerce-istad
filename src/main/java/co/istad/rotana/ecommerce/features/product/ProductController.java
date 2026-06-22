package co.istad.rotana.ecommerce.features.product;

import co.istad.rotana.ecommerce.features.product.dto.CreateProductRequest;
import co.istad.rotana.ecommerce.features.product.dto.PatchProductRequest;
import co.istad.rotana.ecommerce.features.product.dto.ProductResponse;
import co.istad.rotana.ecommerce.features.product.dto.UpdateProductRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("/{code}")
    public ProductResponse getProductByCode(
            @PathVariable String code
    ) {
        log.info("getProductByCode: {}", code);

        return productService.getProductByCode(code);
    }


    @GetMapping
    public Page<ProductResponse> getProducts(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "25") int pageSize
    ) {
        return productService.getProducts(pageNumber, pageSize);
    }



    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductResponse createNew(
            @Valid @RequestBody CreateProductRequest createProductRequest
    ) {
        log.info("createProductRequest: {}", createProductRequest);
        return productService.createNew(createProductRequest);
    }



    @PutMapping("/{code}")
    public ProductResponse updateByCode(
            @PathVariable String code,
            @Valid @RequestBody UpdateProductRequest updateProductRequest
    ) {
        log.info("updateProductRequest: {} and code: {}",
                updateProductRequest,
                code);
        return productService.updateByCode(code, updateProductRequest);
    }

    @PatchMapping("/{code}")
    public ProductResponse patchByCode(
            @PathVariable String code,
            @Valid @RequestBody PatchProductRequest patchProductRequest
    ) {
        log.info("patchProductRequest: {} and code: {}",
                patchProductRequest,
                code);
        return productService.patchByCode(code, patchProductRequest);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{code}")
    public void deleteByCode(@PathVariable String code) {
        log.info("deleteByCode: {}", code);
        productService.deleteByCode(code);
    }

}
