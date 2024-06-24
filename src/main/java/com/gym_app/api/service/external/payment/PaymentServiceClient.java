package com.gym_app.api.service.external.payment;

import com.gym_app.api.dto.external.membership.PaymentDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;

@Service
@Getter
@Setter

public class PaymentServiceClient {

    private static final String BASE_URL = "http://localhost:8100";
    private static final String BASE_URI = "/payment";
    private static final RestTemplate SERVER = new RestTemplate();

    public void postRequest(PaymentDto paymentDto){
        SERVER.postForObject(String.format("%s%s", BASE_URL, BASE_URI),
                paymentDto,
                String.class);
    }

}
