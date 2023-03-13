package br.com.b1g.command.common.command.impl;


import br.com.b1g.command.common.command.Command;
import br.com.b1g.command.common.command.CommandChain;
import br.com.b1g.command.common.command.CommandChainReceiver;

public abstract class AbstractCommandChain<P, R> extends AbstractCommand<P, R>
    implements CommandChain<P, R> {

  @Override
  public <T, V> V invoke(Class<? extends Command<T, V>> commandClass, T parameters) {
    return ((CommandChainReceiver) receiver).invoke(commandClass, parameters, this.getTraceId());
  }
}
