package br.com.b1g.command.common.command.impl;

import br.com.b1g.command.common.command.Command;
import br.com.b1g.command.common.command.CommandListener;
import br.com.b1g.command.common.command.Invoker;
import br.com.b1g.command.common.command.exception.CommandNotSetException;
import br.com.b1g.command.common.gateway.exception.GatewayException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DefaultInvokerTest {

  static class ConcatCommand extends AbstractCommand<ConcatCommand.Request, String> {
    @Getter
    @Setter
    @AllArgsConstructor
    static class Request {
      private String valor1;
      private String valor2;
    }
  }

  static class ConcatCommandReceiver extends AbstractReceiver<ConcatCommand.Request, String> {

    @Override
    protected String doExecute(ConcatCommand.Request parameter) {
      return parameter.valor1.concat(parameter.valor2);
    }
  }

  static class ErrorCommand extends AbstractCommand<String, String> {}

  static class ErrorCommandReceiver extends AbstractReceiver<String, String> {
    @Override
    protected String doExecute(String parameter) {
      throw new GatewayException("", "", 100);
    }
  }

  @Test
  void shouldFaildWhenCommandIsNotSet() {
    Invoker<String, String> invoker = new DefaultInvoker<>();
    assertThrows(CommandNotSetException.class, invoker::invoke);
  }

  @Test
  void shoudlInvokeACommand() {
    String expected = "InvokeTest";
    ConcatCommand command = new ConcatCommand();
    command.setReceiver(new ConcatCommandReceiver());

    String actual =
        new DefaultInvoker<ConcatCommand.Request, String>()
            .setParameter(new ConcatCommand.Request("Invoke", "Test"))
            .setCommand(command)
            .invoke()
            .getResult();

    assertEquals(expected, actual);
  }

  @Test
  void shoudlCallCommandListenerBeforeExecuteIfPresent() {
    ConcatCommand command = new ConcatCommand();
    command.setReceiver(new ConcatCommandReceiver());

    Consumer<Command<?, ?>> beforeExecute = mock(Consumer.class);

    new DefaultInvoker<ConcatCommand.Request, String>()
        .setParameter(new ConcatCommand.Request("Invoke", "Test"))
        .setCommand(command)
        .setCommandListener(
            new CommandListener() {
              @Override
              public Consumer<Command<?, ?>> beforeExecute() {
                return beforeExecute;
              }
            })
        .invoke();

    verify(beforeExecute, times(1)).accept(command);
  }

  @Test
  void shoudlCallCommandListenerAfterExecuteIfPresent() {
    ConcatCommand command = new ConcatCommand();
    command.setReceiver(new ConcatCommandReceiver());

    Consumer<Command<?, ?>> afterExecute = mock(Consumer.class);

    new DefaultInvoker<ConcatCommand.Request, String>()
        .setParameter(new ConcatCommand.Request("Invoke", "Test"))
        .setCommand(command)
        .setCommandListener(
            new CommandListener() {

              @Override
              public Consumer<Command<?, ?>> afterExecute() {
                return afterExecute;
              }
            })
        .invoke();

    verify(afterExecute, times(1)).accept(command);
  }

  @Test
  void shoudlCallCommandListenerOnErrorIfPresent() {
    ErrorCommand command = new ErrorCommand();
    command.setReceiver(new ErrorCommandReceiver());

    BiConsumer<Command<?, ?>, Throwable> onError = mock(BiConsumer.class);

    CommandListener listener =
        new CommandListener() {
          @Override
          public BiConsumer<Command<?, ?>, Throwable> onError() {
            return onError;
          }
        };

    Invoker<String, String> invoker =
        new DefaultInvoker<String, String>()
            .setParameter("Test")
            .setCommand(command)
            .setCommandListener(listener);

    assertThrows(GatewayException.class, invoker::invoke);

    verify(onError, times(1)).accept(eq(command), any(Throwable.class));
  }
}
