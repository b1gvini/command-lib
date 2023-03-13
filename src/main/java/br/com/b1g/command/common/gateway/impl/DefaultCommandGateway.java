package br.com.b1g.command.common.gateway.impl;


import br.com.b1g.command.common.command.*;
import br.com.b1g.command.common.command.impl.DefaultInvoker;
import br.com.b1g.command.common.gateway.CommandFactory;
import br.com.b1g.command.common.gateway.CommandGateway;
import br.com.b1g.command.common.gateway.ReceiverResolver;

import java.util.UUID;

public class DefaultCommandGateway implements CommandGateway, CommandChainReceiver {

  private final ReceiverResolver receiverResolver;
  private CommandListener commandListener;
  @SuppressWarnings("java:S3740")
  private CommandFactory commandFactory = new ReflectionCommandFactory();

  public DefaultCommandGateway(ReceiverResolver receiverResolver) {
    this(receiverResolver, true);
  }

  public DefaultCommandGateway(ReceiverResolver receiverResolver, boolean bindAsChainReceiver) {
    this.receiverResolver = receiverResolver;
    if (bindAsChainReceiver) {
      this.receiverResolver.bind(CommandChain.class, this);
    }
  }

  @Override
  public <P, R> R invoke(Class<? extends Command<P, R>> commandClass, P parameter) {
    return this.invoke(commandClass, parameter, null);
  }

  @Override
  public <P, R> R invoke(Class<? extends Command<P, R>> commandClass, P parameter, String traceId) {
    Command<P, R> command = (Command<P, R>) commandFactory.apply(commandClass);
    command.setReceiver(receiverResolver.resolve(commandClass));
    command.setTraceId(traceId);
    Invoker<P, R> invoker = new DefaultInvoker<>();
    invoker
        .setExecutionId(UUID.randomUUID().toString())
        .setCommand(command)
        .setParameter(parameter)
        .setCommandListener(commandListener)
        .invoke();
    return invoker.getResult();
  }

  public void setCommandListener(CommandListener commandListener) {
    this.commandListener = commandListener;
  }

  public void setCommandFactory(CommandFactory commandFactory) {
    this.commandFactory = commandFactory;
  }
}
