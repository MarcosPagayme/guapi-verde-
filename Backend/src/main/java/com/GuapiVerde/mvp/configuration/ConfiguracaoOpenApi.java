package com.GuapiVerde.mvp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class ConfiguracaoOpenApi {

    @Bean
    public OpenAPI guapiVerdeOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Guapi Verde API")
                        .description("API do Guapi Verde para divulgação de atrativos naturais, eventos, "
                                + "novidades, parceiros, campanhas e benefícios de Guapimirim.")
                        .version("1.0.0")
                        .contact(new Contact().name("Amanda Lisboa")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Informe o token JWT obtido no endpoint de login."))
                        .addResponses("Erro400", respostaDeErro("Requisição inválida ou regra de negócio violada"))
                        .addResponses("Erro401", respostaDeErro("Token JWT ausente ou inválido"))
                        .addResponses("Erro403", respostaDeErro("Perfil sem permissão para a operação"))
                        .addResponses("Erro404", respostaDeErro("Recurso não encontrado"))
                        .addResponses("Erro409", respostaDeErro("Recurso duplicado ou conflito de dados")));
    }

    private ApiResponse respostaDeErro(String descricao) {
        Schema<Object> schema = new Schema<>().$ref("#/components/schemas/ErrorResponse");
        MediaType mediaType = new MediaType().schema(schema);
        return new ApiResponse()
                .description(descricao)
                .content(new Content().addMediaType("application/json", mediaType));
    }
}
