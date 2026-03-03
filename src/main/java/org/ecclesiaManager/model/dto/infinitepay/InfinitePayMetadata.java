package org.ecclesiaManager.model.dto.infinitepay;

public record InfinitePayMetadata(
        String customer_name,
        String customer_email,
        String original_amount,
        String numero_inscricao
) {}
