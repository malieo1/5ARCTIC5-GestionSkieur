package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.repositories.IPisteRepository;
import tn.esprit.spring.services.PisteServicesImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PisteServicesImplTest {

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteServices;

    private Piste piste;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        piste = new Piste(1L, "Green Slope", Color.GREEN, 1200, 15, null);
    }

    @Test
    void testAddPiste() {
        when(pisteRepository.save(piste)).thenReturn(piste);
        Piste savedPiste = pisteServices.addPiste(piste);
        assertEquals(piste, savedPiste);
        verify(pisteRepository, times(1)).save(piste);
    }

    @Test
    void testRetrieveAllPistes() {
        when(pisteRepository.findAll()).thenReturn(Arrays.asList(piste));
        List<Piste> pistes = pisteServices.retrieveAllPistes();
        assertEquals(1, pistes.size());
        assertEquals(piste, pistes.get(0));
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testRetrievePiste() {
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));
        Piste foundPiste = pisteServices.retrievePiste(1L);
        assertEquals(piste, foundPiste);
        verify(pisteRepository, times(1)).findById(1L);
    }

    @Test
    void testRemovePiste() {
        doNothing().when(pisteRepository).deleteById(1L);
        pisteServices.removePiste(1L);
        verify(pisteRepository, times(1)).deleteById(1L);
    }
}
