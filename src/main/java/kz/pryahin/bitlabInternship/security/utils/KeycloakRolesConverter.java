package kz.pryahin.bitlabInternship.security.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRolesConverter implements Converter<Jwt, AbstractAuthenticationToken> {
	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
		return new JwtAuthenticationToken(jwt, authorities);
	}


	private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
		// Realm Access - это параметр по которому можно достать все Realm Roles из Jwt токена, сгенерированного Keycloak
		Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");

		if (realmAccess == null || realmAccess.isEmpty()) {
			return Collections.emptyList();
		}

		List<String> roles = (List<String>) realmAccess.get("roles");

		if (roles == null || roles.isEmpty()) {
			return Collections.emptyList();
		}

		return roles.stream()
			.map(roleName -> "ROLE_" + roleName)
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
	}
}
