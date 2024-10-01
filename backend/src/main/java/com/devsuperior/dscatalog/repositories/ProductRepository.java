package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entites.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
            select distinct p.*
            from tb_product p
            left join tb_product_category on p.id = tb_product_category.product_id
            left join tb_category on tb_category.id = tb_product_category.category_id
            where (:category is null or tb_category.id = :category)
            AND (lower(p.name) LIKE lower(concat('%', :name ,'%')))
            """, nativeQuery = true)
    Page<Product> find(Long category, String name, Pageable pageable);
}
