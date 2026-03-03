package org.ecclesiaManager.client;

import org.ecclesiaManager.model.dto.infinitepay.InfinitePayCheckoutRequestDTO;
import org.ecclesiaManager.model.dto.infinitepay.InfinitePayCheckoutResponseDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "infinitepay-api")
public interface InfinitePayClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    InfinitePayCheckoutResponseDTO createCheckout(
            @HeaderParam("Authorization") String token,
            InfinitePayCheckoutRequestDTO payload
    );
}