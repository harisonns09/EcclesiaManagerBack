package org.ecclesiaManager.model.dto;

public record CheckoutResponseDTO(
        String checkoutUrl,
        String transactionId
) {}