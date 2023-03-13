package br.com.b1g.command.common.gateway.annotation;

import br.com.b1g.command.common.command.Command;
import br.com.b1g.command.common.command.CommandListener;
import br.com.b1g.command.common.gateway.CommandFactory;
import br.com.b1g.command.common.gateway.CommandGateway;
import br.com.b1g.command.common.gateway.impl.DelegateCommandFactory;
import br.com.b1g.command.common.gateway.impl.DelegateCommandListener;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("java:S3740")
public class DelegateCommandListernerRegistrar implements ImportBeanDefinitionRegistrar {

  private static final String COMMAND_GATEWAY_BEAN_POST_PROCESSOR_BEAN =
      "commandGatewayBeanPostProcessor";

  @Override
  public void registerBeanDefinitions(
      AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
    AnnotationAttributes attrs =
        AnnotationAttributes.fromMap(
            metadata.getAnnotationAttributes(EnableCommandListener.class.getName(), false));
    String commandGatewayBean = Objects.requireNonNull(attrs).getString("commandGatewayBean");
    registry.registerBeanDefinition(
        COMMAND_GATEWAY_BEAN_POST_PROCESSOR_BEAN,
        BeanDefinitionBuilder.rootBeanDefinition(
                CommandGatewayBeanPostProcessor.class,
                () -> new CommandGatewayBeanPostProcessor(commandGatewayBean))
            .getBeanDefinition());
    ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(metadata, registry);
  }

  public class CommandGatewayBeanPostProcessor
      implements BeanPostProcessor, ApplicationContextAware {

    private final String commandGatewayBeanName;
    private ApplicationContext applicationContext;

    public CommandGatewayBeanPostProcessor(String commandGatewayBeanName) {
      this.commandGatewayBeanName = commandGatewayBeanName;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
        throws BeansException {
      if (beanName.equals(commandGatewayBeanName)) {
        CommandGateway commandGateway = (CommandGateway) bean;
        commandGateway.setCommandListener(configureDelegateCommandListener());
        commandGateway.setCommandFactory(configureDelegateCommandFactory());
        return commandGateway;
      } else {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
      }
    }

    private DelegateCommandListener configureDelegateCommandListener() {
      Map<String, CommandListener> commandListeners =
          applicationContext.getBeansOfType(CommandListener.class);
      return new DelegateCommandListener(commandListeners.values());
    }

    private DelegateCommandFactory configureDelegateCommandFactory() {
      Map<Class<? extends Command>, CommandFactory> commandFactoryMap =
          applicationContext.getBeansOfType(CommandFactory.class).values().stream()
              .collect(Collectors.toMap(CommandFactory::getType, Function.identity()));
      return new DelegateCommandFactory(commandFactoryMap);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.applicationContext = applicationContext;
    }
  }
}
