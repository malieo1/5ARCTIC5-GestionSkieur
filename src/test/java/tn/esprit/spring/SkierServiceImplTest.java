package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.services.SkierServicesImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SkierServiceImplTest {

    @InjectMocks
    private SkierServicesImpl skierService;

    @Mock
    private ISkierRepository skierRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testRetrieveAllSkiers() {
        // Create skiers with specific attributes
        Skier skier1 = new Skier();
        skier1.setNumSkier(1L);  // Assuming numSkier is Long
        skier1.setFirstName("John");
        skier1.setLastName("Doe");
        skier1.setDateOfBirth(LocalDate.of(1993, 5, 15)); // Example date
        skier1.setCity("New York");

        Skier skier2 = new Skier();
        skier2.setNumSkier(2L);  // Assuming numSkier is Long
        skier2.setFirstName("Jane");
        skier2.setLastName("Smith");
        skier2.setDateOfBirth(LocalDate.of(1998, 8, 22));
        skier2.setCity("Los Angeles");

        List<Skier> skiers = new ArrayList<>();
        skiers.add(skier1);
        skiers.add(skier2);

        when(skierRepository.findAll()).thenReturn(skiers);

        List<Skier> result = skierService.retrieveAllSkiers();
        System.out.println("Retrieved Skiers: " + result);

        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify attributes of the first skier
        assertEquals(Long.valueOf(1), result.get(0).getNumSkier());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals(LocalDate.of(1993, 5, 15), result.get(0).getDateOfBirth());
        assertEquals("New York", result.get(0).getCity());

        // Verify attributes of the second skier
        assertEquals(Long.valueOf(2), result.get(1).getNumSkier());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals("Smith", result.get(1).getLastName());
        assertEquals(LocalDate.of(1998, 8, 22), result.get(1).getDateOfBirth());
        assertEquals("Los Angeles", result.get(1).getCity());

        verify(skierRepository, times(1)).findAll();
    }

}
