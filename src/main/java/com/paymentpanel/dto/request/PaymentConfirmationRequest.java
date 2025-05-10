package com.paymentpanel.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PaymentConfirmationRequest {
    @NotNull(message = "Payment confirmation flag is required")
    private Boolean paymentConfirmed;

    @NotNull(message = "Payment date is required")
    private LocalDateTime paymentDate;
}