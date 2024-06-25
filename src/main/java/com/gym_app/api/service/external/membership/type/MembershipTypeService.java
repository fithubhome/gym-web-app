package com.gym_app.api.service.external.membership.type;

import com.gym_app.api.dto.external.membershipapi.type.get.MembershipTypeExternal;
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

