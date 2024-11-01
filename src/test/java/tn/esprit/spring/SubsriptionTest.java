package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISubscriptionRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")  // Points to an application-test.properties file for H2 configuration
public class SubsriptionTest {

    @Autowired
    private ISubscriptionRepository subscriptionRepository;

    @Test
    public void testSubscriptionCreation() {
        Subscription subscription = new Subscription(1L, LocalDate.now(), LocalDate.now().plusMonths(1), 29.99F, TypeSubscription.MONTHLY);

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        assertEquals(subscription.getNumSub(), savedSubscription.getNumSub());
        assertEquals(subscription.getStartDate(), savedSubscription.getStartDate());
        assertEquals(subscription.getEndDate(), savedSubscription.getEndDate());
        assertEquals(subscription.getPrice(), savedSubscription.getPrice());
        assertEquals(subscription.getTypeSub(), savedSubscription.getTypeSub());
    }
}
