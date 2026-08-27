package com.GuapiVerde.mvp.configuration;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ConfigSecurity {
    @Bean
    public SecurityFilterChain filterSecurity(
            HttpSecurity http,
            JwtAuthenticationConverter conversorDeAuth)
            throws Exception {
        http
                .csrf(crsf -> crsf.disable())
                .sessionManagement(sessao -> sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(
                        "/api/auth/cadastro",
                        "/api/auth/login").permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/categorias-atrativos/**")
                        .permitAll()

                        .requestMatchers(
                                "/api/categorias-atrativos/**")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated())

                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(conversorDeAuth)));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager gerenciadorDeAutenticacao(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtEncoder codificadorJwt(@Value("${seguranca.jwt.chave}") String chave) {
        SecretKey chaveSecreta = new SecretKeySpec(chave.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        return NimbusJwtEncoder
                .withSecretKey(chaveSecreta)
                .algorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public JwtDecoder decodificadorJwt(
            @Value("${seguranca.jwt.chave}") String chave) {
        SecretKey chaveSecreta = new SecretKeySpec(
                chave.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256");

        NimbusJwtDecoder decodificador = NimbusJwtDecoder
                .withSecretKey(chaveSecreta)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        decodificador.setJwtValidator(
                JwtValidators.createDefaultWithIssuer("guapi-verde"));

        return decodificador;
    }

    @Bean
    public JwtAuthenticationConverter conversorDeAuth() {

        JwtGrantedAuthoritiesConverter conversorDePerfis = new JwtGrantedAuthoritiesConverter();

        conversorDePerfis.setAuthoritiesClaimName("perfil");
        conversorDePerfis.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter conversorDeAuth = new JwtAuthenticationConverter();

        conversorDeAuth.setJwtGrantedAuthoritiesConverter(conversorDePerfis);

        return conversorDeAuth;
    }
}
