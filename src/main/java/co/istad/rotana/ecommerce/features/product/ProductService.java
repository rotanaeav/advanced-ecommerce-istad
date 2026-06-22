package co.istad.rotana.ecommerce.features.product;

import co.istad.rotana.ecommerce.features.product.dto.CreateProductRequest;
import co.istad.rotana.ecommerce.features.product.dto.PatchProductRequest;
import co.istad.rotana.ecommerce.features.product.dto.ProductResponse;
import co.istad.rotana.ecommerce.features.product.dto.UpdateProductRequest;
import org.springframework.data.domain.Page;

public interface ProductService {

    void deleteByCode(String code);

    ProductResponse patchByCode(String code, PatchProductRequest patchProductRequest);

    ProductResponse getProductByCode(String code);

    ProductResponse updateByCode(String code, UpdateProductRequest updateProductRequest);

    Page<ProductResponse> getProducts(int pageNumber, int pageSize);

    ProductResponse createNew(CreateProductRequest createProductRequest);

}
