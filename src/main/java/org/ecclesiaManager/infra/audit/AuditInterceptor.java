package org.ecclesiaManager.infra.audit;

import io.quarkus.arc.Arc;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.ecclesiaManager.model.AuditLog;
import org.ecclesiaManager.repository.AuditLogRepository;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.ArrayList;
import java.util.List;

@Loggable
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 1)
public class AuditInterceptor {

    @Inject
    AuditLogRepository auditLogRepository;

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    JsonWebToken jwt;

    @AroundInvoke
    public Object logAuditoria(InvocationContext context) throws Exception {
        Loggable loggable = context.getMethod().getAnnotation(Loggable.class);

        // 1. O Padrão é SISTEMA para rotinas de Background (Batch)
        String username = "SISTEMA";

        // 2. Apenas tenta buscar o usuário se existir um contexto HTTP ativo
        if (Arc.container().requestContext().isActive()) {
            try {
                if (!securityIdentity.isAnonymous()) {
                    username = securityIdentity.getPrincipal().getName();
                }
            } catch (Exception ignored) {
                // Silencia caso ocorra algum erro inesperado na segurança
            }
        }

        Object result;
        try {
            result = context.proceed();
            salvarLog(loggable, username, context, "SUCCESS", null);
            return result;
        } catch (Exception e) {
            salvarLog(loggable, username, context, "FAILED", e.getMessage());
            throw e;
        }
    }

    private String obterIdentificadorAmigavel(Object obj) {
        if (obj == null) return "";

        if (obj instanceof Number || obj instanceof String || obj instanceof Boolean) {
            return obj.toString();
        }

        Class<?> clazz = obj.getClass();
        String[] camposParaBuscar = { "nome", "descricao", "nomeEvento", "titulo", "login", "username"};

        for (String campo : camposParaBuscar) {
            try {
                Object valor = null;
                try {
                    valor = clazz.getMethod(campo).invoke(obj);
                } catch (NoSuchMethodException e) {
                    String getter = "get" + campo.substring(0, 1).toUpperCase() + campo.substring(1);
                    valor = clazz.getMethod(getter).invoke(obj);
                }

                if (valor != null && !valor.toString().isEmpty()) {
                    return valor.toString();
                }
            } catch (Exception ignored) { }
        }

        return "";
    }

    private void salvarLog(Loggable loggable, String username, InvocationContext context, String status, String erro) {
        try {
            AuditLog log = new AuditLog();
            log.setAction(loggable.action());
            log.setEntityName(loggable.entity());
            log.setUsername(username);
            log.setStatus(status);
            log.setChurchId(buscarChurchId(context));

            List<String> identificadores = new ArrayList<>();
            for (Object p : context.getParameters()) {
                String iden = obterIdentificadorAmigavel(p);
                if (!iden.isEmpty()) identificadores.add(iden);
            }

            String identificadorFinal = identificadores.isEmpty() ? "Registro" : String.join(", ", identificadores);
            String detalhes = String.format("%s, %s, %s",
                    loggable.action(),
                    loggable.entity(),
                    identificadorFinal);

            if (status.equals("FAILED") && erro != null) {
                detalhes += " | ERRO: " + erro;
            }

            log.setDetails(detalhes);

            QuarkusTransaction.requiringNew().run(() -> {
                auditLogRepository.persist(log);
            });
        } catch (Exception ex) {
            System.err.println("Falha ao gravar auditoria: " + ex.getMessage());
        }
    }

    private Long buscarChurchId(InvocationContext context) {
        // Protege também o acesso ao JWT, pois ele sofre do mesmo problema em rotinas Batch
        if (Arc.container().requestContext().isActive()) {
            try {
                if (jwt != null && jwt.containsClaim("churchId")) {
                    return Long.parseLong(jwt.getClaim("churchId").toString());
                }
            } catch (Exception ignored) { }
        }

        for (Object arg : context.getParameters()) {
            if (arg instanceof Long) return (Long) arg;
        }
        return 1L;
    }
}