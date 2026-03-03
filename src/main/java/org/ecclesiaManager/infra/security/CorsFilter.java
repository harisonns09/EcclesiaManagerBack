package org.ecclesiaManager.infra.security;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.List;

@Provider
@PreMatching
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final List<String> ORIGENS_PERMITIDAS = List.of(
            "http://localhost:5173", // O seu React rodando localmente
            "https://ecclesia-manager-ten.vercel.app/" // Exemplo: A URL de produção do seu React no futuro
    );

    private String getOrigemPermitida(String originRecebida) {
        if (originRecebida != null && ORIGENS_PERMITIDAS.contains(originRecebida)) {
            return originRecebida;
        }
        return ORIGENS_PERMITIDAS.get(0); // Cai para localhost como fallback seguro
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Responde ao Preflight (OPTIONS) do navegador imediatamente com status 200 OK
        if (requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {

            String origin = requestContext.getHeaderString("Origin");
            String allowedOrigin = getOrigemPermitida(origin);

            requestContext.abortWith(Response.status(Response.Status.OK)
                    .header("Access-Control-Allow-Origin", allowedOrigin)
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH")
                    .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-request-id")
                    .build());
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        // Injeta os cabeçalhos em todas as respostas normais (sucesso ou erro) da API
        String origin = requestContext.getHeaderString("Origin");
        String allowedOrigin = getOrigemPermitida(origin);

        responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", allowedOrigin);
        responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-request-id");
    }
}