package br.com.b1g.command.common.command.impl;

import br.com.b1g.command.common.command.exception.ReceiverNotSetException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class AbstractCommandTest {

  @Test
  void shouldFailWhenReceiverIsNotSet() {
    AbstractCommand<String, String> command =
        mock(AbstractCommand.class, Mockito.CALLS_REAL_METHODS);
    assertThrows(ReceiverNotSetException.class, command::execute);
  }
}
