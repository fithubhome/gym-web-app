package com.gym_app.api.service.external.payment;

import com.gym_app.api.dto.external.paymentapi.put.PaymentResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Getter
@Setter

public class PaymentServiceClient {

    private static final String BASE_URL = "http://localhost:8100";
    private static final String BASE_URI = "/payment";
    private static final RestTemplate SERVER = new RestTemplate();

    public void postRequest(PaymentResponseDto paymentResponseDto){
        SERVER.postForObject(String.format("%s%s", BASE_URL, BASE_URI),
                paymentResponseDto,
                String.class);
    }

}
