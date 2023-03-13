package br.com.b1g.command.common.gateway.impl;

import br.com.b1g.command.common.command.Command;
import br.com.b1g.command.common.gateway.CommandFactory;
import org.springframework.beans.BeanUtils;

@SuppressWarnings("java:S3740")
public class ReflectionCommandFactory implements CommandFactory<Command> {

  @Override
  public Class<Command> getType() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Command apply(Class<Command> commandClass) {
    return BeanUtils.instantiateClass(commandClass);
  }
}
