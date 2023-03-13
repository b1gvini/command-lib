package br.com.b1g.command.common.exception;

public final class IllegalArgumentExceptionThrowHelper {

  private IllegalArgumentExceptionThrowHelper() {}

  public static void throwIfMissingRequiredArgument(String argumentname, Object argumentValue) {
    if (argumentValue == null) {
      throwMissingRequiredIllegalArgumentException(argumentname);
    }
  }

  private static void throwMissingRequiredIllegalArgumentException(String argumentName) {
    throw new IllegalArgumentException(
        String.format("The required argument '%s' was not provided.", argumentName));
  }
}
