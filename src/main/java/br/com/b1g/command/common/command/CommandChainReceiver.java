package br.com.b1g.command.common.command;

public interface CommandChainReceiver extends Receiver<Object, Object> {

  <T, V> V invoke(Class<? extends Command<T, V>> commandClass, T parameter);

  <T, V> V invoke(Class<? extends Command<T, V>> commandClass, T parameter, String traceId);

  @Override
  default Object execute(Object parameter) throws IllegalStateException {
    return null;
  }
}
