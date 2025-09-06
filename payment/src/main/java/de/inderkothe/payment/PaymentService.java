package de.inderkothe.payment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PaymentService {

    private PaymentDao paymentDao;

    public void processPayment(PaymentDto paymentDto) {
        paymentDao.save(PaymentEntity
                .builder()
                .src(paymentDto.src())
                .dest(paymentDto.dest())
                .amount(paymentDto.amount()).build()
        );
    }

    public Double stats() {
        return this.paymentDao.getAmountOfTransferredMoney().orElse(0.0);
    }

    public void reset() {
        this.paymentDao.deleteAll();
    }
}
