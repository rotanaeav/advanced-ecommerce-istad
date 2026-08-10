package co.istad.rotana.ecommerce.features.userprofile;

import co.istad.rotana.ecommerce.features.userprofile.dto.PatchUserProfileRequest;
import co.istad.rotana.ecommerce.features.userprofile.dto.UserProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PatchMapping("/me")
    public UserProfileResponse patchUserProfile(
            @Valid @RequestBody PatchUserProfileRequest patchUserProfileRequest
    ) {
        return userProfileService.patchUserProfile(patchUserProfileRequest);
    }

    @GetMapping("/me")
    public UserProfileResponse getUserProfile() {
        return userProfileService.getUserProfile();
    }

}
