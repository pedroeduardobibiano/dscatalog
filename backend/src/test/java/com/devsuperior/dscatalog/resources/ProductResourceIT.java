package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {

    @Autowired
    private MockMvc mockMvc;

    private Long existId;
    private Long noExistId;
    private Long countTotalProducts;


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        existId = 1L;
        noExistId = 1000L;
        countTotalProducts = 25L;


    }

    @Test
    public void FindAllShouldReturnSortedPageWhenSortByName() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.page.totalElements").value(countTotalProducts));
        result.andExpect(jsonPath("$.content").exists());
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
        result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));

    }

    @Test
    void updateShouldReturnProductDTOWhenIdExist() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Prato de vidro");
        productDTO.setDescription("prato feito de vidro, resistente");

        String jsonBody = objectMapper.writeValueAsString(productDTO);


        ResultActions result =
                mockMvc.perform(put("/products/{id}", existId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andDo(print());
        result.andExpect(jsonPath("$.id").value(existId));
        result.andExpect(jsonPath("$.name").value(productDTO.getName()));
        result.andExpect(jsonPath("$.description").value(productDTO.getDescription()));

    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionsWhenIdExist() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        String jsonBody = objectMapper.writeValueAsString(productDTO);


        ResultActions result =
                mockMvc.perform(put("/products/{id}", noExistId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
        result.andDo(print());

    }

}
