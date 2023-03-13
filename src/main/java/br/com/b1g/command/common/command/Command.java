package br.com.b1g.command.common.command;

public interface Command<P, R> {
  void setExecutionId(String executionId);

  String getExecutionId();

  void setTraceId(String traceId);

  String getTraceId();

  R getResult() throws IllegalStateException;

  void setParameter(P parameter) throws IllegalArgumentException;

  P getParameter();

  void setReceiver(Receiver<P, R> receiver) throws IllegalArgumentException;

  void execute() throws IllegalStateException;
}
