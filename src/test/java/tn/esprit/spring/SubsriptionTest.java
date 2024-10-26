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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubsriptionTest {



    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private Subscription subscription;

    @BeforeEach
    void setUp() {
        subscription = new Subscription(1L, LocalDate.now(), LocalDate.now().plusMonths(1), 29.99F, TypeSubscription.MONTHLY);
    }
    @Test
    public void testSubscriptionCreation() {
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        assertEquals(subscription.getNumSub(), savedSubscription.getNumSub());
        assertEquals(subscription.getStartDate(), savedSubscription.getStartDate());
        assertEquals(subscription.getEndDate(), savedSubscription.getEndDate());
        assertEquals(subscription.getPrice(), savedSubscription.getPrice());
        assertEquals(subscription.getTypeSub(), savedSubscription.getTypeSub());
    }
}
