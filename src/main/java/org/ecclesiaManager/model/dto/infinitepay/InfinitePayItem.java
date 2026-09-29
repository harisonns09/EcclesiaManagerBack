package org.ecclesiaManager.model.dto.infinitepay;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InfinitePayItem(
        @JsonProperty("description") String description,
        @JsonProperty("quantity") Integer quantity,
        @JsonProperty("price") Integer price // Preço em cêntimos

) {}
