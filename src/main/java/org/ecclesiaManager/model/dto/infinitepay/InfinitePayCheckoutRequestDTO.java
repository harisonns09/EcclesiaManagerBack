package org.ecclesiaManager.model.dto.infinitepay;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record InfinitePayCheckoutRequestDTO(
        @JsonProperty("handle") String handle,
        @JsonProperty("items") List<InfinitePayItem> items,
        @JsonProperty("order_nsu") String orderNsu,
        @JsonProperty("return_url") String returnUrl,
        @JsonProperty("webhook_url") String webhookUrl,
        @JsonProperty("customer") InfinitePayCustomerDTO customer,
        @JsonProperty("metadata") InfinitePayMetadata metadata
) {}
