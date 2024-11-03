package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISubscriptionRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles
public class SubsriptionTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private Subscription subscription;

    @BeforeEach
    void setUp() {
        // Initialize the subscription object with test data
        subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusMonths(1));
        subscription.setPrice(29.99F);
        subscription.setTypeSub(TypeSubscription.MONTHLY);
    }

    @Test
    public void testSubscriptionCreation() {
        // Mocking the repository's save method to return the subscription instance
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        // Act: Save subscription using the mocked repository
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        // Assert: Verify that the saved subscription has the same properties as the original
        assertEquals(subscription.getNumSub(), savedSubscription.getNumSub());
        assertEquals(subscription.getStartDate(), savedSubscription.getStartDate());
        assertEquals(subscription.getEndDate(), savedSubscription.getEndDate());
        assertEquals(subscription.getPrice(), savedSubscription.getPrice());
        assertEquals(subscription.getTypeSub(), savedSubscription.getTypeSub());
    }
}
