package com.gym_app.api.service.external.membership.history;


import com.gym_app.api.dto.external.membershipapi.history.get.MembershipHistoryExternal;
import com.gym_app.api.model.Profile;
import com.gym_app.api.security.CustomUserDetails;
import com.gym_app.api.service.ProfileService;
import com.gym_app.api.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Getter
@Setter

public class MembershipHistoryService {

    @Autowired
    private MembershipHistoryServiceClient membershipHistoryServiceClient;

    public List<MembershipHistoryExternal> getMembershipHistory() throws EntityNotFoundException {
        UUID currentProfile = getCurrentProfile();
        List<MembershipHistoryExternal> MembershipHistoryExternalList = new ArrayList<>();
        Optional<MembershipHistoryExternal[]> optionalMembershipTypes = membershipHistoryServiceClient.requestMembershipHistory(currentProfile);

        if (optionalMembershipTypes.isPresent()) {
            Arrays.stream(optionalMembershipTypes.get()).forEach(
                    mbHistoryExt -> MembershipHistoryExternalList.add(
                            mbHistoryExt
                    )
            );
            return MembershipHistoryExternalList;
        }

        throw new EntityNotFoundException(getClass().getName());

    }


    @Autowired
    ProfileService profileService;
    @Autowired
    UserService userService;

    public UUID getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Profile currentProfile = profileService.findProfileByUserId(userService.findByEmail(userDetails.getUsername()).getId());
        return currentProfile.getId();
    }

}
