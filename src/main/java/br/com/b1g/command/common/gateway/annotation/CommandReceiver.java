package br.com.b1g.command.common.gateway.annotation;

import br.com.b1g.command.common.command.Command;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface CommandReceiver {

  @SuppressWarnings("java:S1452")
  Class<? extends Command<?, ?>> value();
}
