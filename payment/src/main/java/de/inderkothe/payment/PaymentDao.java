package de.inderkothe.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

interface PaymentDao extends JpaRepository<PaymentEntity, UUID> {
    @Query(value = """
        SELECT SUM(amount)
          FROM payment;
""", nativeQuery = true)
    Optional<Double> getAmountOfTransferredMoney();
}
