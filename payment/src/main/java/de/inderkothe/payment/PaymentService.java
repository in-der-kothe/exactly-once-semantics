package de.inderkothe.payment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class PaymentService {

    private PaymentDao paymentDao;

    public void processPayment(PaymentDto paymentDto, String idempotenceKey) {
        try {
            paymentDao.save(PaymentEntity
                    .builder()
                    .idempotenceKey(idempotenceKey)
                    .src(paymentDto.src())
                    .dest(paymentDto.dest())
                    .amount(paymentDto.amount()).build()
            );
        } catch (DataIntegrityViolationException dive) {
            String message = dive.getMessage();
            if (message != null && message.contains(PaymentEntity.UNIQUE_IDEMPOTENCE_KEY_CONSTRAINT)) {
                log.error("duplicate detected...");
                throw new AlreadyProcessRequestException();
            }

        }

    }

    public Double stats() {
        return this.paymentDao.getAmountOfTransferredMoney().orElse(0.0);
    }

    public void reset() {
        this.paymentDao.deleteAll();
    }
}
