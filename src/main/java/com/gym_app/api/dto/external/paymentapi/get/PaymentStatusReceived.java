package com.gym_app.api.dto.external.paymentapi.get;

import com.gym_app.api.dto.external.paymentapi.put.PaymentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


public class PaymentStatusReceived {

    private String paymentStatus;

}
