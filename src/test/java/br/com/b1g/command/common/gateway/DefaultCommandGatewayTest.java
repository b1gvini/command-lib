package br.com.b1g.command.common.gateway;

import br.com.b1g.command.common.command.CommandChain;
import br.com.b1g.command.common.command.impl.AbstractCommand;
import br.com.b1g.command.common.command.impl.AbstractCommandChain;
import br.com.b1g.command.common.command.impl.AbstractReceiver;
import br.com.b1g.command.common.gateway.impl.DefaultCommandGateway;
import br.com.b1g.command.common.gateway.impl.DefaultReceiverResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultCommandGatewayTest {

  static class ConcatCommand extends AbstractCommand<ConcatCommand.Request, String> {
    @Getter
    @Setter
    @AllArgsConstructor
    static class Request {
      private String valor1;
      private String valor2;
    }
  }

  static class HelloWorld extends AbstractCommandChain<String, String> {
    @Override
    public void execute() throws IllegalStateException {
      super.execute();
      String message =
          invoke(ConcatCommand.class, new ConcatCommand.Request("Hello ", getParameter()));
      setResult(message);
    }
  }

  static class ConcatCommandReceiver extends AbstractReceiver<ConcatCommand.Request, String> {

    @Override
    protected String doExecute(ConcatCommand.Request parameter) {
      return parameter.valor1.concat(parameter.valor2);
    }
  }

  @Test
  void shouldInvokeACommand() {
    String expected = "InvokeTest";

    DefaultReceiverResolver receiverResolver = new DefaultReceiverResolver();
    CommandGateway gateway = new DefaultCommandGateway(receiverResolver);

    receiverResolver.bind(ConcatCommand.class, new ConcatCommandReceiver());

    String actual =
        gateway.invoke(ConcatCommand.class, new ConcatCommand.Request("Invoke", "Test"));
    assertEquals(expected, actual);
  }

  @Test
  void shouldInvokeACommandChain() {
    String expected = "Hello Test";

    DefaultReceiverResolver receiverResolver = new DefaultReceiverResolver();
    DefaultCommandGateway gateway = new DefaultCommandGateway(receiverResolver);

    receiverResolver.bind(ConcatCommand.class, new ConcatCommandReceiver());
    receiverResolver.bind(CommandChain.class, gateway);

    String actual = gateway.invoke(HelloWorld.class, "Test");
    assertEquals(expected, actual);
  }
}
