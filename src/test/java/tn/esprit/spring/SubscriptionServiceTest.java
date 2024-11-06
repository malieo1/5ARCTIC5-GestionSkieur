package tn.esprit.spring;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServicesImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices; // Injecting the service with mocked dependencies

    @Mock
    private ISubscriptionRepository subscriptionRepository; // Mocked repository

    private Subscription subscription;

    @BeforeEach
    void setUp() {
        subscription = new Subscription();
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusMonths(1));
        subscription.setPrice(100.0f);
        subscription.setTypeSub(TypeSubscription.MONTHLY);
    }

    @Test
    void testAddSubscription() {
        // Arrange
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        // Act
        Subscription savedSubscription = subscriptionServices.addSubscription(subscription);

        // Assert
        assertNotNull(savedSubscription);
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testRetrieveSubscriptionById() {
        // Arrange
        Long id = 1L;
        when(subscriptionRepository.findById(id)).thenReturn(Optional.of(subscription));

        // Act
        Subscription retrievedSubscription = subscriptionServices.retrieveSubscriptionById(id);

        // Assert
        assertNotNull(retrievedSubscription);
        assertEquals(subscription.getNumSub(), retrievedSubscription.getNumSub());
        verify(subscriptionRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateSubscription() {
        // Arrange
        subscription.setPrice(120.0f);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        // Act
        Subscription updatedSubscription = subscriptionServices.updateSubscription(subscription);

        // Assert
        assertEquals(120.0f, updatedSubscription.getPrice());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testGetSubscriptionByType() {
        // Arrange
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(TypeSubscription.MONTHLY)).thenReturn(Set.of(subscription));

        // Act
        Set<Subscription> subscriptions = subscriptionServices.getSubscriptionByType(TypeSubscription.MONTHLY);

        // Assert
        assertFalse(subscriptions.isEmpty());
        assertTrue(subscriptions.stream().allMatch(sub -> sub.getTypeSub() == TypeSubscription.MONTHLY));
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(TypeSubscription.MONTHLY);
    }

    @Test
    void testRetrieveSubscriptionsByDates() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusMonths(2);
        when(subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate)).thenReturn(List.of(subscription));

        // Act
        List<Subscription> subscriptions = subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate);

        // Assert
        assertFalse(subscriptions.isEmpty());
        assertTrue(subscriptions.stream().allMatch(sub ->
                !sub.getStartDate().isBefore(startDate) && !sub.getEndDate().isAfter(endDate)
        ));
        verify(subscriptionRepository, times(1)).getSubscriptionsByStartDateBetween(startDate, endDate);
    }
}
