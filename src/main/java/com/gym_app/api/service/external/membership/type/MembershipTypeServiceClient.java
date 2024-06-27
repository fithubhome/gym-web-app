package com.gym_app.api.service.external.membership.type;


import com.gym_app.api.dto.membership.membership.MembershipType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Getter
@Setter
public class MembershipTypeServiceClient {
    private static final String BASE_URL = "http://localhost:8105";
    private static final String BASE_URI = "/membershipType";
    private static final RestTemplate CLIENT = new RestTemplate();

    private MembershipType[] membershipTypeList;
    public Optional<MembershipType[]> requestMembershipTypesExternal() {
        try {
            ResponseEntity<MembershipType[]> response = CLIENT.getForEntity(
                    String.format("%s%s", BASE_URL, BASE_URI),
                    MembershipType[].class
            );
            membershipTypeList = response.getBody();
            return Optional.ofNullable(membershipTypeList);

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
