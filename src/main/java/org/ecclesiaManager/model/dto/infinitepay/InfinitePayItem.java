package org.ecclesiaManager.model.dto.infinitepay;

public record InfinitePayItem(
        String description,
        Integer quantity,
        Integer price
) {}
