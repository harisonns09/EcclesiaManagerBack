package org.ecclesiaManager.client;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "telegram-api")
public interface TelegramClient {

    @POST
    @Path("/sendMessage")
    void sendMessage(
            @QueryParam("chat_id") String chatId,
            @QueryParam("text") String text
    );
}
