package co.istad.rotana.ecommerce.features.category;

import co.istad.rotana.ecommerce.features.category.dto.CreateCategoryRequest;
import co.istad.rotana.ecommerce.features.category.dto.CategoryResponse;
import co.istad.rotana.ecommerce.features.category.dto.UpdateCategoryRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Page<CategoryResponse> findAll(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "25") int pageSize
    ) {
        log.info("findAll categories with pageNumber: {} and pageSize: {}", pageNumber, pageSize);

        return categoryService.findAll(pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public CategoryResponse findById(
            @PathVariable Integer id
    ) {
        log.info("findById category ID: {}", id);

        return categoryService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryResponse create(
            @Valid @RequestBody CreateCategoryRequest request
    ) {
        log.info("create category request: {}", request);

        return categoryService.create(request);
    }

    @PutMapping("/{id}")
    public CategoryResponse updateById(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        log.info("update category ID: {} with request: {}", id, request);

        return categoryService.updateById(id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(
            @PathVariable Integer id
    ) {
        log.info("delete category ID: {}", id);

        categoryService.deleteById(id);
    }

}