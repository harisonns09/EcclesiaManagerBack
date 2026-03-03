package org.ecclesiaManager.model.dto;

import org.ecclesiaManager.model.Igreja;

public record IgrejaResponseDTO(

        Long id,
        String name,
        String instagram,
        String address,
        String city,
        String state,
        String cnpj

) {

    public IgrejaResponseDTO(Igreja igreja){
        this(
                igreja.getId(),
                igreja.getName(),
                igreja.getInstagram(),
                igreja.getAddress(),
                igreja.getCity(),
                igreja.getState(),
                igreja.getCnpj()
        );
    }
}
