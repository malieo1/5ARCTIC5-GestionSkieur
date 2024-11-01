package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.repositories.IPisteRepository;
import tn.esprit.spring.services.PisteServicesImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PisteServicesImplTest {

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteServices;

    private Piste piste;

    @BeforeEach
    void setUp() {
        piste = new Piste(1L, "Blue Trail", Color.BLUE, 3000, 15, null);
    }

    @Test
    void testRetrieveAllPistes() {
        when(pisteRepository.findAll()).thenReturn(Arrays.asList(piste));
        List<Piste> pistes = pisteServices.retrieveAllPistes();
        assertEquals(1, pistes.size());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testAddPiste() {
        when(pisteRepository.save(piste)).thenReturn(piste);
        Piste savedPiste = pisteServices.addPiste(piste);
        assertEquals(piste, savedPiste);
        verify(pisteRepository, times(1)).save(piste);
    }

    @Test
    void testRemovePiste() {
        doNothing().when(pisteRepository).deleteById(piste.getNumPiste());
        pisteServices.removePiste(piste.getNumPiste());
        verify(pisteRepository, times(1)).deleteById(piste.getNumPiste());
    }

    @Test
    void testRetrievePiste() {
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));
        Piste foundPiste = pisteServices.retrievePiste(1L);
        assertEquals(piste, foundPiste);
        verify(pisteRepository, times(1)).findById(1L);
    }

    // Additional tests can be added here for edge cases.
}
