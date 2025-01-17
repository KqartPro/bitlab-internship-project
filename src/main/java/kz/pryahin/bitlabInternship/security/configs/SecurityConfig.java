package kz.pryahin.bitlabInternship.security.configs;

import kz.pryahin.bitlabInternship.security.utils.KeycloakRolesConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("user/login", "user/refresh-token", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                                .requestMatchers("user/create", "user/set-roles", "user/delete-roles", "file/get-all", "file/get-all-lesson-attachments/**").hasRole("ADMIN")

                                .requestMatchers("file/upload").hasAnyRole("ADMIN", "TEACHER")
                                .requestMatchers("file/download/**").hasAnyRole("ADMIN", "TEACHER", "USER")

                                .requestMatchers(HttpMethod.POST, "/course/**", "/chapter/**", "/lesson/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/course/**", "/chapter/**", "/lesson/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/course/**", "/chapter/**", "/lesson/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/course/**", "/chapter/**", "/lesson/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/course/**", "/chapter/**", "/lesson/**").authenticated()

                                .anyRequest().authenticated()


                )

                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .oauth2ResourceServer(oauth -> oauth.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(keycloakRolesConverter()))
                )

                .build();
    }


    @Bean
    public KeycloakRolesConverter keycloakRolesConverter() {

        return new KeycloakRolesConverter();
    }
}
