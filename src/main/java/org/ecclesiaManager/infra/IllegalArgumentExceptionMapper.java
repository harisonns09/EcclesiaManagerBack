package org.ecclesiaManager.infra;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Provider // Diz ao Quarkus que esta classe é um provedor de tratamento de exceção
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    private static final Logger logger = LoggerFactory.getLogger(IllegalArgumentExceptionMapper.class);

    @Override
    public Response toResponse(IllegalArgumentException ex) {
        logger.warn("Erro de validação (Bad Request): {}", ex.getMessage());

        // Criamos o Map para devolver o mesmo formato de erro
        Map<String, String> erroResponse = Map.of(
                "erro", "Dados Inválidos",
                "mensagem", ex.getMessage()
        );

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(erroResponse)
                .build();
    }
}