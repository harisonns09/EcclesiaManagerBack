package org.ecclesiaManager.model.dto.infinitepay;

public record InfinitePayCheckoutResponseDTO(
        String url,
        String id,
        String status
) {}