package org.ecclesiaManager.batch;

import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.ecclesiaManager.client.TelegramClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Named("comunicacaoWriter")
@ApplicationScoped
public class ComunicacaoWriter extends AbstractItemWriter {

    @Inject
    @RestClient
    TelegramClient telegramClient;

    // TODO: Em produção, o chat_id deve vir da Pessoa ou de uma Configuração por Igreja
    private static final String CHAT_ID = "721828992";


    @Override
    public void writeItems(List<Object> items) {
        for (Object item : items) {
            String msg = (String) item;
            // Exemplo de envio de e-mail simplificado
            telegramClient.sendMessage(CHAT_ID, msg);
            System.out.println("Enviado: " + msg);
        }
    }
}