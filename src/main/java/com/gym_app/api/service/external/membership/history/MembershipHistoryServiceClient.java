package com.gym_app.api.service.external.membership.history;

import com.gym_app.api.dto.membership.membership.MembershipHistory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
@Getter
@Setter
public class MembershipHistoryServiceClient {
    private static final String BASE_URL = "http://localhost:8105";
    private static final String BASE_URI = "/addpayment";
    private static final RestTemplate CLIENT = new RestTemplate();
    private MembershipHistory[] membershipHistoryList;

    public Optional<MembershipHistory[]> requestMembershipHistory(UUID currentProfile) {

        try {
            ResponseEntity<MembershipHistory[]> response = CLIENT.getForEntity(
                    String.format("%s%s/%s", BASE_URL, BASE_URI, currentProfile.toString()),
                    MembershipHistory[].class
            );
            membershipHistoryList = response.getBody();
            return Optional.ofNullable(membershipHistoryList);

        } catch (HttpClientErrorException ex) {
            // Handle HTTP client errors here
            System.err.println("Error fetching membership types: " + ex.getMessage());
            return Optional.empty();

        } catch (Exception e) {
            // Handle other exceptions here
            System.err.println("An error occurred: " + e.getMessage());
            return Optional.empty();
        }

    }
}
