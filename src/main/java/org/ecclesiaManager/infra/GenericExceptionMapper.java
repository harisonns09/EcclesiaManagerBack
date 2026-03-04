package org.ecclesiaManager.infra;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger logger = LoggerFactory.getLogger(GenericExceptionMapper.class);

    @Override
    public Response toResponse(Exception ex) {

        // 1. Verifica se é uma exceção HTTP conhecida do Quarkus (404, 401, 405, etc.)
        if (ex instanceof WebApplicationException) {
            WebApplicationException webEx = (WebApplicationException) ex;
            int status = webEx.getResponse().getStatus();

            // Logamos apenas como WARN, pois 404/401 geralmente é erro do cliente, não pane do sistema
            if (status != 404) {
                logger.warn("Erro de requisição HTTP (Status: {})", status, ex);
            }

            Map<String, Object> problemDetail = Map.of(
                    "status", status,
                    "title", "Erro na requisição",
                    "detail", webEx.getMessage() != null ? webEx.getMessage() : "Verifique a requisição."
            );

            return Response.status(status).entity(problemDetail).build();
        }

        // 2. Para erros reais de código (NullPointerException, Banco de dados fora do ar, etc.)
        logger.error("Erro inesperado no sistema", ex);

        Map<String, Object> problemDetail = Map.of(
                "status", 500,
                "title", "Erro interno",
                "detail", "Ocorreu um erro inesperado. Contate o suporte."
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(problemDetail)
                .build();
    }
}