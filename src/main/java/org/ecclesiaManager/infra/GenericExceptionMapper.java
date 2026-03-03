package org.ecclesiaManager.infra;

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
        logger.error("Erro inesperado no sistema", ex);

        // Simulando a estrutura do ProblemDetail do Spring (RFC 7807)
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