package com.connectinghands.controller;

import com.connectinghands.dto.CreateOrphanageRequest;
import com.connectinghands.dto.OrphanageDto;
import com.connectinghands.dto.UpdateOrphanageRequest;
import com.connectinghands.service.OrphanageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrphanageController.class)
class OrphanageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrphanageService orphanageService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createOrphanage_ValidRequest_ReturnsOrphanage() throws Exception {
        CreateOrphanageRequest request = new CreateOrphanageRequest();
        request.setName("Test Orphanage");
        request.setAddress("123 Test St");
        request.setCity("Test City");
        request.setState("Test State");
        request.setCountry("Test Country");
        request.setCapacity(100);
        request.setAdminId(1L);

        OrphanageDto dto = new OrphanageDto();
        dto.setId(1L);
        dto.setName("Test Orphanage");
        when(orphanageService.createOrphanage(any(CreateOrphanageRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/orphanages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Orphanage"));
    }

    @Test
    void getOrphanage_ValidId_ReturnsOrphanage() throws Exception {
        OrphanageDto dto = new OrphanageDto();
        dto.setId(1L);
        dto.setName("Test Orphanage");
        when(orphanageService.getOrphanage(1L)).thenReturn(dto);

        mockMvc.perform(get("/orphanages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Orphanage"));
    }

    @Test
    void getAllOrphanages_ReturnsList() throws Exception {
        OrphanageDto dto1 = new OrphanageDto();
        dto1.setId(1L);
        dto1.setName("Orphanage 1");
        OrphanageDto dto2 = new OrphanageDto();
        dto2.setId(2L);
        dto2.setName("Orphanage 2");
        List<OrphanageDto> dtos = Arrays.asList(dto1, dto2);
        when(orphanageService.getAllOrphanages()).thenReturn(dtos);

        mockMvc.perform(get("/orphanages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Orphanage 1"))
                .andExpect(jsonPath("$[1].name").value("Orphanage 2"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateOrphanage_ValidRequest_ReturnsUpdatedOrphanage() throws Exception {
        UpdateOrphanageRequest request = new UpdateOrphanageRequest();
        request.setName("Updated Orphanage");

        OrphanageDto dto = new OrphanageDto();
        dto.setId(1L);
        dto.setName("Updated Orphanage");
        when(orphanageService.updateOrphanage(eq(1L), any(UpdateOrphanageRequest.class))).thenReturn(dto);

        mockMvc.perform(put("/orphanages/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Orphanage"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteOrphanage_ValidId_ReturnsOk() throws Exception {
        doNothing().when(orphanageService).deleteOrphanage(1L);
        mockMvc.perform(delete("/orphanages/1"))
                .andExpect(status().isOk());
    }
} 