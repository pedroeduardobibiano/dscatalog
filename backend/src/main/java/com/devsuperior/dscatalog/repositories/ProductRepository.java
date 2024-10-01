package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entites.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
            SELECT DISTINCT p
            FROM Product p
            LEFT JOIN p.categories c
            WHERE (:category IS NULL OR c.id = :category)
            AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
            """)
    Page<Product> find(Long category, String name, Pageable pageable);

    @Query(value = """
        select obj from Product obj join fetch obj.categories
        where obj in :products
        """)
    List<Product> findProductsWithCategories(List<Product> products);

}
