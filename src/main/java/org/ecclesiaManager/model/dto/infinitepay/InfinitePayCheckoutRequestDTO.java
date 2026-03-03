package org.ecclesiaManager.model.dto.infinitepay;

import java.util.List;

public record InfinitePayCheckoutRequestDTO(
        String handle,
        List<InfinitePayItem> items,
        String order_nsu,
        String redirect_url,
        String webhook_url,
        InfinitePayCustomerDTO customer,
        InfinitePayMetadata metadata
) {}
