//package com.devsuperior.dscatalog.resources;
//
//import com.devsuperior.dscatalog.dto.ProductDTO;
//import com.devsuperior.dscatalog.resource.ProductResource;
//import com.devsuperior.dscatalog.services.ProductService;
//import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
//import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import java.time.Instant;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(ProductResource.class)
//public class ProductResourceTests {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProductService service;
//
//
//    private Long existId;
//    private Long noExistId;
//
//
//    private final ProductDTO productDTO = new ProductDTO();
//
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    public void setUp() {
//
//
//        existId = 1L;
//        noExistId = 100L;
//        productDTO.setId(existId);
//        productDTO.setName("tv 4k");
//        productDTO.setDescription("tv plana 43 polegadas, preta");
//        productDTO.setPrice(1800.00);
//        productDTO.setDate(Instant.parse("2023-08-27T16:56:00Z"));
//
//
//        PageImpl<ProductDTO> page = new PageImpl<>(List.of(productDTO));
//
//        Mockito.when(service.findAllPaged(any())).thenReturn(page);
//    }
//
//    @Test
//    public void findAllShouldReturnPage() throws Exception {
//        ResultActions result =
//                mockMvc.perform(get("/products")
//                        .accept(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isOk());
//
//    }
//
//    @Test
//    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
//
//        Mockito.when(service.findById(existId)).thenReturn(productDTO);
//
//        ResultActions result =
//                mockMvc.perform(get("/products/{id}", existId)
//                        .accept(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isOk());
//        result.andExpect(jsonPath("$.id").exists());
//
//    }
//
//    @Test
//    public void findByIdShouldReturnProductWhenIdDoesNotExists() throws Exception {
//
//        Mockito.doThrow(ResourceNotFoundException.class).when(service).findById(noExistId);
//
//        ResultActions result =
//                mockMvc.perform(get("/products/{id}", noExistId)
//                        .accept(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isNotFound());
//
//    }
//
//    @Test
//    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
//
//        String jsonBody = objectMapper.writeValueAsString(productDTO);
//
//
//        Mockito.when(service.update(eq(existId), any())).thenReturn(productDTO);
//        Mockito.when(service.update(eq(noExistId), any())).thenReturn(productDTO);
//
//        ResultActions result =
//                mockMvc.perform(put("/products/{id}", existId)
//                        .content(jsonBody)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isOk());
//        result.andExpect(jsonPath("$.id").exists());
//        result.andExpect(jsonPath("$.name").exists());
//        result.andExpect(jsonPath("$.description").exists());
//        result.andExpect(jsonPath("$.price").exists());
//        result.andExpect(jsonPath("$.date").exists());
//
//
//    }
//
//    @Test
//    public void updateShouldReturnNotFoundProductDTOWhenIdDoesNotExists() throws Exception {
//        String jsonBody = objectMapper.writeValueAsString(productDTO);
//
//        Mockito.doThrow(ResourceNotFoundException.class).when(service).update(eq(noExistId), any());
//
//        ResultActions result =
//                mockMvc.perform(put("/products/{id}", noExistId)
//                        .content(jsonBody)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON));
//        result.andDo(print());
//        result.andExpect(status().isNotFound());
//
//    }
//
//    @Test
//    public void deleteShouldDeleteObjectWhenIdExist() throws Exception {
//
//        Mockito.doNothing().when(service).delete(existId);
//
//        ResultActions result =
//                mockMvc.perform(delete("/products/{id}", existId)
//                        .accept(MediaType.APPLICATION_JSON));
//        result.andDo(print());
//        result.andExpect(status().isNoContent());
//
//    }
//
//    @Test
//    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {
//
//        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(noExistId);
//
//        ResultActions result =
//                mockMvc.perform(delete("/products/{id}", noExistId)
//                        .accept(MediaType.APPLICATION_JSON));
//        result.andDo(print());
//        result.andExpect(status().isNotFound());
//
//    }
//
//    @Test
//    public void deleteShouldThrowDataBaseExceptionWhenIdExist() throws Exception {
//
//        Mockito.doThrow(DatabaseException.class).when(service).delete(existId);
//
//        ResultActions result =
//                mockMvc.perform(delete("/products/{id}", existId)
//                        .accept(MediaType.APPLICATION_JSON));
//        result.andDo(print());
//        result.andExpect(status().isBadRequest());
//
//    }
//
//    @Test
//    public void insertShouldInsertObject() throws Exception {
//
//        String jsonBody = objectMapper.writeValueAsString(productDTO);
//
//        Mockito.when(service.insert(any())).thenReturn(productDTO);
//
//        ResultActions result =
//                mockMvc.perform(post("/products")
//                        .content(jsonBody)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON));
//        result.andDo(print());
//        result.andExpect(status().isCreated());
//        result.andExpect(jsonPath("$.id").exists());
//        result.andExpect(jsonPath("$.name").exists());
//        result.andExpect(jsonPath("$.description").exists());
//        result.andExpect(jsonPath("$.price").exists());
//        result.andExpect(jsonPath("$.date").exists());
//
//    }
//
//}
