package org.ecclesiaManager.infra.audit;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@InterceptorBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {

    @Nonbinding // Avisa o CDI para ignorar este valor na hora de procurar o interceptador
    String action() default "";

    @Nonbinding // Avisa o CDI para ignorar este valor na hora de procurar o interceptador
    String entity() default "";
}