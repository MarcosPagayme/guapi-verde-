package com.GuapiVerde.mvp.dto;

import java.math.BigDecimal;

import com.GuapiVerde.mvp.enums.SituacaoAtrativo;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AtrativoEntrada(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 150, message = "O nome deve possuir no máximo 150 caracteres.")
        String nome,

        @NotBlank(message = "O resumo é obrigatório.")
        @Size(max = 300, message = "O resumo deve possuir no máximo 300 caracteres.")
        String resumo,

        @NotBlank(message = "A descrição é obrigatória.")
        String descricao,

        @Size(max = 255, message = "O endereço deve possuir no máximo 255 caracteres.")
        String endereco,

        @DecimalMin(value = "-90.000000", message = "A latitude mínima é -90.")
        @DecimalMax(value = "90.000000", message = "A latitude máxima é 90.")
        BigDecimal latitude,

        @DecimalMin(value = "-180.000000", message = "A longitude mínima é -180.")
        @DecimalMax(value = "180.000000", message = "A longitude máxima é 180.")
        BigDecimal longitude,

        @Size(max = 30, message = "O telefone deve possuir no máximo 30 caracteres.")
        String telefone,

        @Size(max = 255, message = "O site deve possuir no máximo 255 caracteres.")
        String site,

        @NotNull(message = "Informe se o atrativo é gratuito.")
        Boolean gratuito,

        @DecimalMin(value = "0.00", message = "O valor de entrada não pode ser negativo.")
        @Digits(
                integer = 8,
                fraction = 2,
                message = "O valor de entrada deve possuir no máximo duas casas decimais."
        )
        BigDecimal valorEntrada,

        @NotNull(message = "Informe se o atrativo é acessível.")
        Boolean acessivel,

        @NotNull(message = "A situação é obrigatória.")
        SituacaoAtrativo situacao,

        @NotNull(message = "A categoria é obrigatória.")
        @Positive(message = "O ID da categoria deve ser positivo.")
        Long categoriaAtrativoId
) {
}
