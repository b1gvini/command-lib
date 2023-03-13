package br.com.b1g.command.common.gateway.annotation;

import br.com.b1g.command.common.gateway.CommandGateway;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DelegateCommandListernerRegistrar.class)
public @interface EnableCommandListener {
  String commandGatewayBean() default CommandGateway.BEAN_NAME;
}
