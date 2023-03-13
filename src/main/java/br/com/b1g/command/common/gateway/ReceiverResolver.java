package br.com.b1g.command.common.gateway;


import br.com.b1g.command.common.command.Command;
import br.com.b1g.command.common.command.Receiver;

public interface ReceiverResolver {
  <P, R> Receiver<P, R> resolve(Class<? extends Command<P, R>> commandClass);

  @SuppressWarnings("rawtypes")
  <P, R> void bind(Class<? extends Command> commandClass, Receiver<P, R> receiver);
}
