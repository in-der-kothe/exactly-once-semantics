package de.inderkothe.payment;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
public class PaymentController {

    private final static String IDEMPOTENCE_KEY_NAME = "Payment-Idempotence-Key";

    private final PaymentService paymentService;

    @PostMapping("/payments/")
    public ResponseEntity<PaymentDto> createPayment(
            @RequestBody PaymentDto paymentDto,
            @RequestHeader(value = IDEMPOTENCE_KEY_NAME, required = false ) String idempotenceKey) {
        log.info("invoked with idempotence key {}", idempotenceKey);
        paymentService.processPayment(paymentDto, idempotenceKey);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/payments/")
    public ResponseEntity<Void> deletePayment() {
        paymentService.reset();
        return ResponseEntity.ok().build();
    }


    @GetMapping("/stats")
    public ResponseEntity<Double> stats() {
        return ResponseEntity.ok(paymentService.stats());
    }

}
