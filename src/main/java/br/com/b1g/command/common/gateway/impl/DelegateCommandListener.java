package br.com.b1g.command.common.gateway.impl;


import br.com.b1g.command.common.command.Command;
import br.com.b1g.command.common.command.CommandListener;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DelegateCommandListener implements CommandListener {

  private final Collection<CommandListener> commandListeners;

  public DelegateCommandListener(Collection<CommandListener> commandListeners) {
    this.commandListeners = commandListeners;
  }

  @Override
  public Consumer<Command<?, ?>> beforeExecute() {
    return command ->
        commandListeners.forEach(
            commandListener -> commandListener.beforeExecute().accept(command));
  }

  @Override
  public Consumer<Command<?, ?>> afterExecute() {
    return command ->
        commandListeners.forEach(commandListener -> commandListener.afterExecute().accept(command));
  }

  @Override
  public BiConsumer<Command<?, ?>, Throwable> onError() {
    return (command, error) ->
        commandListeners.forEach(
            commandListener -> commandListener.onError().accept(command, error));
  }
}
