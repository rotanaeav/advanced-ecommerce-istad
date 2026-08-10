package co.istad.rotana.ecommerce.features.userprofile;

import co.istad.rotana.ecommerce.features.userprofile.dto.PatchUserProfileRequest;
import co.istad.rotana.ecommerce.features.userprofile.dto.UserProfileResponse;
import co.istad.rotana.ecommerce.security.AuthUtils;
import co.istad.rotana.ecommerce.security.KeycloakProperties;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProps;

    @Override
    public UserProfileResponse patchUserProfile(PatchUserProfileRequest patchUserProfileRequest) {
        String userId = AuthUtils.extractUserId();

        // Patch user profile in database
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User profile has not been found"
                ));
        userProfileMapper.toEntity(userProfile, patchUserProfileRequest);
        userProfileRepository.save(userProfile);

        // Update user profile in keycloak
        UserResource userResource = keycloak.realm(keycloakProps.getRealm())
                .users()
                .get(userId);
        UserRepresentation userRepresentation = fetchKeycloakUserRepresentation(userResource);
        userProfileMapper.toUserRepresentation(userRepresentation, patchUserProfileRequest);
        updateKeycloakUserRepresentation(userResource, userRepresentation);

        return userProfileMapper.buildUserProfileResponse(userRepresentation, userProfile);
    }


    @Override
    public UserProfileResponse getUserProfile() {
        // Get user profile from keycloak
        String userId = AuthUtils.extractUserId();
        UserResource userResource = keycloak.realm(keycloakProps.getRealm())
                .users()
                .get(userId);
        UserRepresentation userRepresentation = fetchKeycloakUserRepresentation(userResource);
        log.info("user profile: {}", userRepresentation);

        // Get user profile from database
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User profile has not been found"
                ));

        return userProfileMapper.buildUserProfileResponse(userRepresentation, userProfile);
    }

    private UserRepresentation fetchKeycloakUserRepresentation(UserResource userResource) {
        try {
            return userResource.toRepresentation();
        } catch (ForbiddenException | NotAuthorizedException e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to access this user profile in identity provider"
            );
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User profile has not been found in identity provider"
            );
        }
    }

    private void updateKeycloakUserRepresentation(
            UserResource userResource,
            UserRepresentation userRepresentation
    ) {
        try {
            userResource.update(userRepresentation);
        } catch (ForbiddenException | NotAuthorizedException e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to update this user profile in identity provider"
            );
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User profile has not been found in identity provider"
            );
        }
    }

}