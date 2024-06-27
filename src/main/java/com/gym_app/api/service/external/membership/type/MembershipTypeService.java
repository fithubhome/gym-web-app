package com.gym_app.api.service.external.membership.type;

import com.gym_app.api.dto.membership.membership.MembershipType;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Getter
public class MembershipTypeService {
    @Autowired
    private MembershipTypeServiceClient membershipTypeServiceClient;

    public List<MembershipType> getAllMembershipsType() throws EntityNotFoundException {
        Optional<MembershipType[]> optionalMembershipTypes = membershipTypeServiceClient.requestMembershipTypesExternal();

        if (optionalMembershipTypes.isPresent()) {
            return new ArrayList<>(Arrays.asList(optionalMembershipTypes.get()));
        }

        throw new EntityNotFoundException(getClass().getName());

    }


}

