package br.com.b1g.command.common.gateway;

import br.com.b1g.command.common.command.Command;
import br.com.b1g.command.common.command.CommandListener;
import br.com.b1g.command.common.gateway.exception.GatewayException;

public interface CommandGateway {
  String BEAN_NAME = "commandGateway";

  <P, R> R invoke(Class<? extends Command<P, R>> commandClass, P parameter) throws GatewayException;

  <P, R> R invoke(Class<? extends Command<P, R>> commandClass, P parameter, String traceId)
      throws GatewayException;

  void setCommandListener(CommandListener commandListener);

  @SuppressWarnings("java:S3740")
  void setCommandFactory(CommandFactory commandFactory);
}
