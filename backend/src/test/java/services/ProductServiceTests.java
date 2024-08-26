package services;

import com.devsuperior.dscatalog.entites.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Long existingId;
    private Long noExistId;

    @BeforeEach
    void setUp() {
        existingId = 10L;
        noExistId = 100L;
    }


    @Test
    public void deleteShouldDeleteEntityWhenIdExists() {
        Product product = new Product();
        product.setId(existingId);

        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));

        Assertions.assertDoesNotThrow(() -> productService.delete(existingId));

        Mockito.verify(productRepository, Mockito.times(1)).findById(existingId);
        Mockito.verify(productRepository, Mockito.times(1)).delete(product);
    }


    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenDoesNotExists() {

        Mockito.when(productRepository.findById(noExistId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(noExistId);
        });

    Mockito.verify(productRepository, Mockito.times(1)).findById(noExistId);

    }

}
