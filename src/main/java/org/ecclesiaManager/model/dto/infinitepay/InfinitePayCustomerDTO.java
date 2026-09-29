package org.ecclesiaManager.model.dto.infinitepay;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InfinitePayCustomerDTO(
        @JsonProperty("name") String firstName,
        @JsonProperty("email") String email,
        @JsonProperty("phone_number") String phoneNumber
) {}
