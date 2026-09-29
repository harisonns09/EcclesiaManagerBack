package org.ecclesiaManager.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.model.dto.PedidoRequestDTO;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.ecclesiaManager.model.dto.CheckoutRequestDTO;
import org.ecclesiaManager.model.dto.CheckoutResponseDTO;
import org.ecclesiaManager.model.dto.infinitepay.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @ConfigProperty(name = "infinitepay.webhookLoja-url")
    String webhookLojaUrlConfig;

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

    public CheckoutResponseDTO createProdutoCheckout(PedidoRequestDTO data) {

        BigDecimal unitPrice;
        if (data.quantidade() != null && data.quantidade() > 0) {
            unitPrice = data.amount().divide(new BigDecimal(data.quantidade()), 2, RoundingMode.HALF_UP);
        } else {
            unitPrice = data.amount();
        }
        int unitPriceInCents = unitPrice.multiply(new BigDecimal("100")).intValue();

        var item = new InfinitePayItem(
                data.description(),
                data.quantidade(),
                unitPriceInCents // Agora envia o preço unitário correto
        );

        var metadata = new InfinitePayMetadata(
                data.nomeComprador(),
                data.emailComprador(),
                data.amount().toString(),
                data.codigoCompra()
        );

        var custumer = new InfinitePayCustomerDTO(
                data.nomeComprador(),
                data.emailComprador(),
                data.telefoneComprador()
        );

        String orderNsu = data.codigoCompra();
        if (orderNsu == null || orderNsu.isEmpty()) {
            orderNsu = UUID.randomUUID().toString();
        }

        String returnUrl = redirectBase + "/" + data.produtoId() + "/compra?status=success&transactionId=" + orderNsu;

        var payload = new InfinitePayCheckoutRequestDTO(
                handle,
                List.of(item),
                orderNsu,
                returnUrl,
                webhookLojaUrlConfig,
                custumer,
                metadata
        );

        try (Client client = ClientBuilder.newClient()) {

            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
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
}