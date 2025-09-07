package de.inderkothe.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Builder
@Entity
@Data
@Table(name = "payment",
        uniqueConstraints = {
                @UniqueConstraint(name = PaymentEntity.UNIQUE_IDEMPOTENCE_KEY_CONSTRAINT,
                        columnNames = {PaymentEntity.IDEMPOTENCY_COLUMN_NAME})})
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {

    public static final String UNIQUE_IDEMPOTENCE_KEY_CONSTRAINT = "UNIQUE_IDEMPOTENCY_KEY_CONSTRAINT";

    public static final String IDEMPOTENCY_COLUMN_NAME = "idempotency_key";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = IDEMPOTENCY_COLUMN_NAME)
    private String idempotenceKey;

    private String src;

    private String dest;

    private Double amount;

}
