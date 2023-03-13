package br.com.b1g.command.common.gateway;


import br.com.b1g.command.common.command.Command;

import java.util.function.Function;

public interface CommandFactory<T extends Command> extends Function<Class<T>, T> {

  Class<T> getType();
}
