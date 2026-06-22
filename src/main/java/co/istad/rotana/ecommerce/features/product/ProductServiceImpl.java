package co.istad.rotana.ecommerce.features.product;

import co.istad.rotana.ecommerce.features.category.Category;
import co.istad.rotana.ecommerce.features.product.dto.CreateProductRequest;
import co.istad.rotana.ecommerce.features.product.dto.PatchProductRequest;
import co.istad.rotana.ecommerce.features.product.dto.ProductResponse;
import co.istad.rotana.ecommerce.features.product.dto.UpdateProductRequest;
import co.istad.rotana.ecommerce.features.category.CategoryRepository;
import co.istad.rotana.ecommerce.util.GenerateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private final ProductMapper productMapper;


    @Override
    public void deleteByCode(String code) {
        // Validate product code
        Product product = productRepository.findById(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product code not found"
                ));
        productRepository.delete(product);
    }



    @Override
    public ProductResponse patchByCode(String code, PatchProductRequest patchProductRequest) {
        // TODO:
        // Validate product code
        Product product = productRepository.findById(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product code not found"
                ));

        // Validate category ID (exists or not)
        if (patchProductRequest.categoryId() != null) {
            Category category = categoryRepository
                    .findById(patchProductRequest.categoryId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Category ID not found"
                    ));
            product.setCategory(category);
        }

        productMapper.patchProductRequestToProduct(
                patchProductRequest,
                product
        );

        product = productRepository.save(product);

        return productMapper.productToProductResponse(product);
    }

    @Override
    public ProductResponse getProductByCode(String code) {
        return productRepository.findById(code)
                .map(productMapper::productToProductResponse)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product code not found"
                ));
    }


    @Override
    public ProductResponse updateByCode(String code, UpdateProductRequest updateProductRequest) {
        // TODO:
        // Validate product code
        Product product = productRepository.findById(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product code not found"
                ));

        productMapper.updateProductRequestToProduct(updateProductRequest, product);

        product = productRepository.save(product);

        return productMapper.productToProductResponse(product);
    }


    @Override
    public Page<ProductResponse> getProducts(int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return productRepository
                .findAll(pageable)
                .map(productMapper::productToProductResponse);
    }


    @Override
    public ProductResponse createNew(CreateProductRequest createProductRequest) {
        // TODO: write your business logic
        // 1. Validate category ID (exists or not)
        Category category = categoryRepository
                .findById(createProductRequest.categoryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category ID not found"
                ));

        // 2. Transfer data from DTO to Entity
        Product product = new Product();
        product.setName(createProductRequest.name());
        product.setPrice(createProductRequest.price());
        product.setQty(createProductRequest.qty());
        product.setDescription(createProductRequest.description());
        product.setCategory(category);

        // 3. System data
        product.setCode(GenerateUtil.randomProductCode());
        product.setIsAvailable(true);

        // 4. Save into database
        product = productRepository.save(product);

        // 5. Transfer data from Entity to DTO
        return productMapper.productToProductResponse(product);
    }

}
