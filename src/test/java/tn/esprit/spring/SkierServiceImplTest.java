package tn.esprit.spring;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.services.SkierServicesImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

 class SkierServiceImplTest {
    @InjectMocks
    private SkierServicesImpl skierService;

    @Mock
    private ISkierRepository skierRepository;

     @Test
     public void testRetrieveAllSkiers() {
         List<Skier> skiers = List.of(new Skier(), new Skier());
         Mockito.when(skierRepository.findAll()).thenReturn(skiers);

         List<Skier> result = skierService.retrieveAllSkiers();

         assertNotNull(result);
         assertEquals(2, result.size());
         Mockito.verify(skierRepository, times(1)).findAll();
     }
}
