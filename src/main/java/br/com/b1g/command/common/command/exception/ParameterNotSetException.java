package br.com.b1g.command.common.command.exception;

public class ParameterNotSetException extends IllegalStateException {
  public ParameterNotSetException(String s) {
    super(s);
  }
}
