package co.istad.rotana.ecommerce.features.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository
    extends JpaRepository<Product, String> {

    String code(String code);
}
