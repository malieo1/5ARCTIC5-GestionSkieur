package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.services.IPisteServices;
import tn.esprit.spring.controllers.PisteRestController;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PisteRestController.class)
class PisteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPisteServices pisteServices;

    @Test
    void testAddPiste() throws Exception {
        Piste piste = new Piste(1L, "Green Slope", Color.GREEN, 1200, 15, null);
        when(pisteServices.addPiste(any(Piste.class))).thenReturn(piste);

        mockMvc.perform(post("/piste/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"namePiste\":\"Green Slope\",\"color\":\"GREEN\",\"length\":1200,\"slope\":15}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numPiste").value(piste.getNumPiste()))
                .andExpect(jsonPath("$.namePiste").value(piste.getNamePiste()))
                .andExpect(jsonPath("$.color").value(piste.getColor().name()))
                .andExpect(jsonPath("$.length").value(piste.getLength()))
                .andExpect(jsonPath("$.slope").value(piste.getSlope()));
    }

    @Test
    void testGetAllPistes() throws Exception {
        Piste piste = new Piste(1L, "Green Slope", Color.GREEN, 1200, 15, null);
        when(pisteServices.retrieveAllPistes()).thenReturn(Collections.singletonList(piste));

        mockMvc.perform(get("/piste/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numPiste").value(piste.getNumPiste()))
                .andExpect(jsonPath("$[0].namePiste").value(piste.getNamePiste()))
                .andExpect(jsonPath("$[0].color").value(piste.getColor().name()))
                .andExpect(jsonPath("$[0].length").value(piste.getLength()))
                .andExpect(jsonPath("$[0].slope").value(piste.getSlope()));
    }

    @Test
    void testGetById() throws Exception {
        Piste piste = new Piste(1L, "Green Slope", Color.GREEN, 1200, 15, null);
        when(pisteServices.retrievePiste(1L)).thenReturn(piste);

        mockMvc.perform(get("/piste/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numPiste").value(piste.getNumPiste()))
                .andExpect(jsonPath("$.namePiste").value(piste.getNamePiste()))
                .andExpect(jsonPath("$.color").value(piste.getColor().name()))
                .andExpect(jsonPath("$.length").value(piste.getLength()))
                .andExpect(jsonPath("$.slope").value(piste.getSlope()));
    }

    @Test
    void testDeleteById() throws Exception {
        doNothing().when(pisteServices).removePiste(1L);

        mockMvc.perform(delete("/piste/delete/1"))
                .andExpect(status().isOk());

        verify(pisteServices, times(1)).removePiste(1L);
    }
}
