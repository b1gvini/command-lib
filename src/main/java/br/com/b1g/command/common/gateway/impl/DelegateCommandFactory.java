package br.com.b1g.command.common.gateway.impl;

import br.com.b1g.command.common.command.Command;
import br.com.b1g.command.common.exception.IllegalArgumentExceptionThrowHelper;
import br.com.b1g.command.common.gateway.CommandFactory;

import java.util.Map;

@SuppressWarnings("java:S3740")
public class DelegateCommandFactory implements CommandFactory<Command> {

  private final Map<Class<? extends Command>, CommandFactory> commandFactories;
  private final ReflectionCommandFactory reflectionCommandFactory = new ReflectionCommandFactory();

  public DelegateCommandFactory(Map<Class<? extends Command>, CommandFactory> commandFactories) {
    IllegalArgumentExceptionThrowHelper.throwIfMissingRequiredArgument(
        "commandFactories", commandFactories);

    this.commandFactories = commandFactories;
  }

  @Override
  public Command apply(Class<Command> commandClass) {
    if (commandFactories.containsKey(commandClass)) {
      return (Command) commandFactories.get(commandClass).apply(commandClass);
    }
    return reflectionCommandFactory.apply(commandClass);
  }

  @Override
  public Class<Command> getType() {
    throw new UnsupportedOperationException();
  }
}
