package br.com.b1g.command.common.command.exception;

public class CommandNotSetException extends IllegalStateException {
  public CommandNotSetException(String s) {
    super(s);
  }
}
