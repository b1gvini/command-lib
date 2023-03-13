package br.com.b1g.command.common.command.impl;

import br.com.b1g.command.common.command.Receiver;

public abstract class AbstractReceiver<P, R> implements Receiver<P, R> {
  public R execute(P parameter) throws IllegalStateException {
    return doExecute(parameter);
  }

  protected abstract R doExecute(P parameter);
}
