package com.gym_app.api.service.external.membership.history;


import com.gym_app.api.dto.membership.membership.MembershipHistory;
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
    @Autowired
    ProfileService profileService;
    @Autowired
    UserService userService;


    public List<MembershipHistory> getMembershipHistory() throws EntityNotFoundException {
        UUID currentProfile = getCurrentProfile();
        List<MembershipHistory> membershipHistoryList = new ArrayList<>();
        Optional<MembershipHistory[]> optionalMembershipTypes = membershipHistoryServiceClient.requestMembershipHistory(currentProfile);

        if (optionalMembershipTypes.isPresent()) {
            Arrays.stream(optionalMembershipTypes.get()).forEach(
                    mbHistoryExt -> membershipHistoryList.add(
                            mbHistoryExt
                    )
            );
            return membershipHistoryList;
        }

        throw new EntityNotFoundException(getClass().getName());

    }

    public UUID getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Profile currentProfile = profileService.findProfileByUserId(userService.findByEmail(userDetails.getUsername()).getId());
        return currentProfile.getId();
    }

}
