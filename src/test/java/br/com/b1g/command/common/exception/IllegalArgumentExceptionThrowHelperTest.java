package br.com.b1g.command.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class IllegalArgumentExceptionThrowHelperTest {

  @Test
  void shouldFailWhenArgumentIsNotProvided() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Object nullArgument = null;
          IllegalArgumentExceptionThrowHelper.throwIfMissingRequiredArgument("teste", nullArgument);
        });
  }
}
