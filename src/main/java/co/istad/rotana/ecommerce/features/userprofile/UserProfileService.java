package co.istad.rotana.ecommerce.features.userprofile;

import co.istad.rotana.ecommerce.features.userprofile.dto.PatchUserProfileRequest;
import co.istad.rotana.ecommerce.features.userprofile.dto.UserProfileResponse;

public interface UserProfileService {
    UserProfileResponse getUserProfile();
    UserProfileResponse patchUserProfile(PatchUserProfileRequest patchUserProfileRequest);
}
