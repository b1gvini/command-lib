package br.com.b1g.command.common.gateway.impl;

import br.com.b1g.command.common.command.Command;
import br.com.b1g.command.common.command.CommandChain;
import br.com.b1g.command.common.command.Receiver;
import br.com.b1g.command.common.gateway.ReceiverResolver;
import br.com.b1g.command.common.gateway.exception.ReceiverNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class DefaultReceiverResolver implements ReceiverResolver {

  protected final Map<Class<? extends Command<?, ?>>, Receiver<?, ?>> receiverMap = new HashMap<>();

  @SuppressWarnings("unchecked")
  @Override
  public <P, R> Receiver<P, R> resolve(Class<? extends Command<P, R>> commandClass) {
    if (isCommandChain(commandClass)) {
      return resolveCommandChainReceiver();
    }
    assertReceiver(commandClass);
    return (Receiver<P, R>) receiverMap.get(commandClass);
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public <P, R> void bind(Class<? extends Command> commandClass, Receiver<P, R> receiver) {
    receiverMap.put((Class<? extends Command<?, ?>>) commandClass, receiver);
  }

  @SuppressWarnings({"unchecked"})
  protected <P, R> Receiver<P, R> resolveCommandChainReceiver() {
    assertReceiver(CommandChain.class);
    return (Receiver<P, R>) receiverMap.get(CommandChain.class);
  }

  protected <P, R> boolean isCommandChain(Class<? extends Command<P, R>> commandClass) {
    return CommandChain.class.isAssignableFrom(commandClass);
  }

  @SuppressWarnings({"rawtypes"})
  protected void assertReceiver(Class<? extends Command> commandClass) {
    if (!receiverMap.containsKey(commandClass)) {
      throw new ReceiverNotFoundException("No receiver found for Command [" + commandClass + "]");
    }
  }
}
