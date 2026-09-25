package org.ecclesiaManager.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.model.Pedido;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.ecclesiaManager.model.dto.CheckoutRequestDTO;
import org.ecclesiaManager.model.dto.CheckoutResponseDTO;
import org.ecclesiaManager.model.dto.infinitepay.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class InfinitePayService {

    @ConfigProperty(name = "infinitepay-api.url")
    String apiUrl;

    @ConfigProperty(name = "infinitepay.handle")
    String handle;

    @ConfigProperty(name = "infinitepay.redirect-base")
    String redirectBase;

    @ConfigProperty(name = "infinitepay.token")
    String apiToken;

    @ConfigProperty(name = "infinitepay.webhook-url")
    String webhookUrlConfig;

    public CheckoutResponseDTO createCheckoutLink(String eventId, CheckoutRequestDTO data) {

        int priceInCents = data.amount().multiply(new java.math.BigDecimal("100")).intValue();

        var item = new InfinitePayItem(
                "Inscricao - " + data.nome(),
                1,
                priceInCents
        );

        var metadata = new InfinitePayMetadata(
                data.nome(),
                data.email(),
                data.amount().toString(),
                data.numeroInscricao()
        );

        var custumer = new InfinitePayCustomerDTO(
                data.nome(),
                data.email(),
                data.telefone()
        );

        String orderNsu = data.numeroInscricao();
        if (orderNsu == null || orderNsu.isEmpty()) {
            orderNsu = UUID.randomUUID().toString();
        }

        String returnUrl = redirectBase + "/" + eventId + "/inscricao?status=success&transactionId=" + orderNsu;

        var payload = new InfinitePayCheckoutRequestDTO(
                handle,
                List.of(item),
                orderNsu,
                returnUrl,
                webhookUrlConfig,
                custumer,
                metadata
        );

        try (Client client = ClientBuilder.newClient()) {

            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiToken)
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

            if (response.getStatus() >= 200 && response.getStatus() < 300) {
                InfinitePayCheckoutResponseDTO responseBody = response.readEntity(InfinitePayCheckoutResponseDTO.class);

                if (responseBody != null && responseBody.url() != null) {
                    return new CheckoutResponseDTO(responseBody.url(), responseBody.id());
                } else {
                    throw new RuntimeException("InfinitePay retornou resposta vazia.");
                }
            } else {
                String errorBody = response.readEntity(String.class);
                throw new RuntimeException("Erro na InfinitePay: HTTP " + response.getStatus() + " - " + errorBody);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar link InfinitePay: " + e.getMessage());
        }
    }

    public String gerarLinkProdutoCheckout(Pedido pedido) {
        // Configurar Cliente
        InfinitePayCustomerDTO customer = new InfinitePayCustomerDTO(
                pedido.getComprador().getNome(),
                pedido.getComprador().getEmail(),
                null // phoneNumber - você pode adicionar isso se tiver
        );

        // Configurar Itens
        List<InfinitePayItem> items = new ArrayList<>();
        for (var item : pedido.getItens()) {
            InfinitePayItem infiniteItem = new InfinitePayItem(
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    item.getPrecoUnitario().multiply(new java.math.BigDecimal(100)).intValue()
            );
            items.add(infiniteItem);
        }

        // Metadados (CRUCIAL para o Webhook saber qual é o pedido)
        InfinitePayMetadata metadata = new InfinitePayMetadata(
                pedido.getComprador().getNome(),
                pedido.getComprador().getEmail(),
                pedido.getValorTotal().toString(),
                pedido.getId().toString()
        );

        String orderNsu = pedido.getId().toString();
        String returnUrl = redirectBase + "/meus-pedidos?status=success&pedidoId=" + orderNsu;

        InfinitePayCheckoutRequestDTO payload = new InfinitePayCheckoutRequestDTO(
                handle,
                items,
                orderNsu,
                returnUrl,
                webhookUrlConfig,
                customer,
                metadata
        );

        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiToken)
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

            if (response.getStatus() >= 200 && response.getStatus() < 300) {
                InfinitePayCheckoutResponseDTO responseBody = response.readEntity(InfinitePayCheckoutResponseDTO.class);
                if (responseBody != null && responseBody.url() != null) {
                    return responseBody.url();
                } else {
                    throw new RuntimeException("InfinitePay retornou resposta vazia.");
                }
            } else {
                String errorBody = response.readEntity(String.class);
                throw new RuntimeException("Erro na InfinitePay: HTTP " + response.getStatus() + " - " + errorBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar link InfinitePay: " + e.getMessage());
        }
    }
}