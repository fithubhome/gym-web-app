package com.gym_app.api.service;

import com.gym_app.api.dto.external.membership.MembershipTypeExternal;
import com.gym_app.api.service.external.MembershipTypeServiceClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Getter
public class MembershipTypeService {

    @Autowired
    private MembershipTypeServiceClient membershipTypeServiceClient;
//    private List<MembershipTypeExternal> membershipTypeExternalList = new ArrayList<>();

    public List<MembershipTypeExternal> getAllMembershipsType() throws EntityNotFoundException {
        List<MembershipTypeExternal> membershipTypeExternalList = new ArrayList<>();
        Optional<MembershipTypeExternal[]> optionalMembershipTypes = membershipTypeServiceClient.requestMembershipTypesExternal();

        if (optionalMembershipTypes.isPresent()) {
            Arrays.stream(optionalMembershipTypes.get()).forEach(
                    mbTypeExt -> membershipTypeExternalList.add(
                            mbTypeExt
                    )
            );
            return membershipTypeExternalList;
        }

        throw new EntityNotFoundException(getClass().getName());

    }

}

