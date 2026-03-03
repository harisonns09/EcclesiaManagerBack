package org.ecclesiaManager.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CNPJ;

public record IgrejaRequestDTO(

        @NotBlank(message = "O nome da igreja é obrigatório")
        String name,
        String instagram,
        @NotBlank(message = "O endereço é obrigatório")
        String address,
        @NotBlank(message = "A cidade é obrigatória")
        String city,
        @NotBlank(message = "O estado é obrigatório")
        String state,
        @NotBlank(message = "O cnpj é obrigatório")
        String cnpj

) {
}
