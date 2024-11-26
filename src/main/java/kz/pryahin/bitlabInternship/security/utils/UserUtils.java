package kz.pryahin.bitlabInternship.security.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Slf4j
public final class UserUtils {

	public static Jwt getCurrentUserToken() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication instanceof JwtAuthenticationToken) {
			return ((JwtAuthenticationToken) authentication).getToken();
		}
		log.warn("Couldn't extract User");
		return null;
	}


	public static String getCurrentUsername() {

		Jwt jwt = getCurrentUserToken();
		if (jwt != null) {
			return jwt.getClaimAsString("preferred_username");
		}
		log.warn("preferred_username is null");
		return null;
	}
}
