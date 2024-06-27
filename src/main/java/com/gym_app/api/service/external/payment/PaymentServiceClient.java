package com.gym_app.api.service.external.payment;

import com.gym_app.api.dto.membership.payment.PaymentDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceClient {
    private static final String BASE_URL = "http://localhost:8100";
    private static final String BASE_URI = "/payment";
    private static final RestTemplate SERVER = new RestTemplate();

    public String postRequest(PaymentDTO paymentDTO){
       return SERVER.postForObject(String.format("%s%s", BASE_URL, BASE_URI),
               paymentDTO,
                String.class);
    }

}
