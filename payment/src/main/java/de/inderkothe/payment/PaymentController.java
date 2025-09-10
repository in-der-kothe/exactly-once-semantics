package de.inderkothe.payment;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payments/")
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto paymentDto) {
        log.info("create...");
        paymentService.processPayment(paymentDto);
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
