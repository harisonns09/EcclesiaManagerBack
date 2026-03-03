package org.ecclesiaManager.infra.logging;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PerformanceLogged
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @AroundInvoke
    public Object logExecutionTime(InvocationContext context) throws Throwable {
        // Pega o nome da classe e do método (ex: PessoaController.getMembros)
        String methodName = context.getMethod().getDeclaringClass().getSimpleName() + "." + context.getMethod().getName();

        logger.info("REQ  -> {}", methodName);

        long start = System.currentTimeMillis();

        try {
            return context.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            logger.info("RESP <- {} :: {} ms", methodName, duration);
        }
    }
}