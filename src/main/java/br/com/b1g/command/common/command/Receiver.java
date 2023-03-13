package br.com.b1g.command.common.command;

public interface Receiver<P, R> {
  R execute(P parameter) throws IllegalStateException;
}
