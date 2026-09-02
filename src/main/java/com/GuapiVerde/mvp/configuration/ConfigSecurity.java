package com.GuapiVerde.mvp.configuration;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.GuapiVerde.mvp.security.ForbiddenAccessDeniedHandler;
import com.GuapiVerde.mvp.security.UnauthorizedAuthenticationEntryPoint;

@Configuration
public class ConfigSecurity {
    @Bean
    public SecurityFilterChain filterSecurity(
            HttpSecurity http,
            JwtAuthenticationConverter conversorDeAuth,
            CorsConfigurationSource corsConfigurationSource,
            UnauthorizedAuthenticationEntryPoint unauthorizedAuthenticationEntryPoint,
            ForbiddenAccessDeniedHandler forbiddenAccessDeniedHandler)
            throws Exception {
        http
                .csrf(crsf -> crsf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(sessao -> sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(excecoes -> excecoes
                        .authenticationEntryPoint(unauthorizedAuthenticationEntryPoint)
                        .accessDeniedHandler(forbiddenAccessDeniedHandler))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers(
                        "/api/auth/cadastro",
                        "/api/auth/login").permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/categorias-atrativos/**")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/atrativos",
                                "/api/atrativos/**")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/horarios-funcionamento",
                                "/api/horarios-funcionamento/**")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/imagens-atrativos",
                                "/api/imagens-atrativos/**")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/parceiros/administracao",
                                "/api/parceiros/administracao/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/parceiros",
                                "/api/parceiros/**")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/temporadas/administracao",
                                "/api/temporadas/administracao/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/temporadas",
                                "/api/temporadas/**")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/eventos/administracao",
                                "/api/eventos/administracao/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/eventos",
                                "/api/eventos/**")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/novidades/administracao",
                                "/api/novidades/administracao/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/novidades",
                                "/api/novidades/**")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/campanhas/administracao",
                                "/api/campanhas/administracao/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/campanhas",
                                "/api/campanhas/**")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/cupons/administracao",
                                "/api/cupons/administracao/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/cupons",
                                "/api/cupons/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/cupons",
                                "/api/cupons/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/cupons",
                                "/api/cupons/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/cupons",
                                "/api/cupons/**")
                        .authenticated()

                        .requestMatchers(
                                "/api/favoritos",
                                "/api/favoritos/**")
                        .authenticated()

                        .requestMatchers(
                                "/api/preferencias",
                                "/api/preferencias/**")
                        .authenticated()

                        .requestMatchers(
                                "/api/consentimentos",
                                "/api/consentimentos/**")
                        .authenticated()

                        .requestMatchers(
                                "/api/categorias-atrativos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/api/atrativos",
                                "/api/atrativos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/api/horarios-funcionamento",
                                "/api/horarios-funcionamento/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/api/imagens-atrativos",
                                "/api/imagens-atrativos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/api/parceiros",
                                "/api/parceiros/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/api/temporadas",
                                "/api/temporadas/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/api/eventos",
                                "/api/eventos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/api/novidades",
                                "/api/novidades/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/api/campanhas",
                                "/api/campanhas/**")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated())

                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint(unauthorizedAuthenticationEntryPoint)
                        .accessDeniedHandler(forbiddenAccessDeniedHandler)
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(conversorDeAuth)));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${aplicacao.cors.origens-permitidas}") String origensPermitidas) {
        List<String> origens = Arrays.stream(origensPermitidas.split(","))
                .map(String::trim)
                .filter(origem -> !origem.isEmpty())
                .distinct()
                .toList();

        if (origens.isEmpty() || origens.contains("*")) {
            throw new IllegalArgumentException(
                    "aplicacao.cors.origens-permitidas deve conter origens explicitas e nao pode usar '*'.");
        }

        CorsConfiguration configuracao = new CorsConfiguration();
        configuracao.setAllowedOrigins(origens);
        configuracao.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuracao.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        configuracao.setAllowCredentials(false);
        configuracao.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource fonte = new UrlBasedCorsConfigurationSource();
        fonte.registerCorsConfiguration("/**", configuracao);
        return fonte;
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
