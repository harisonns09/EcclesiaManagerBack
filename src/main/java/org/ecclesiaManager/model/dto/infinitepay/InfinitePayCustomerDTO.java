package org.ecclesiaManager.model.dto.infinitepay;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InfinitePayCustomerDTO(
        String name,
        String email,
        @JsonProperty("phone_number") String phoneNumber
) {
}
