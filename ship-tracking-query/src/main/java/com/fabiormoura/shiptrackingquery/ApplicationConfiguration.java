package com.fabiormoura.shiptrackingquery;

import com.fabiormoura.AvroPayloadTransformer;
import com.fabiormoura.shiptrackingquery.event.ShipCreatedEvent;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.spring.messaging.InboundEventMessageChannelAdapter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableAutoConfiguration
@EnableSchemaRegistryClient
public class ApplicationConfiguration {
    @Bean
    public Map<String, Class<?>> eventsMap() {
        Map<String, Class<?>> eventsMap = new HashMap<>();
        eventsMap.put("application/vnd.shipcreatedevent.v1+avro", ShipCreatedEvent.class);
        return eventsMap;
    }

    @Bean
    public IntegrationFlow eventBusInboundIntegrationFlow(ConnectionFactory connectionFactory, InboundEventMessageChannelAdapter inboundEventMessageChannelAdapter, @Qualifier("avroSchemaMessageConverter") MessageConverter messageConverter, Map<String, Class<?>> eventsMap) {
        return IntegrationFlows
                .from(Amqp.inboundAdapter(connectionFactory, "ship-tracking-queue"))
                .transform(new AvroPayloadTransformer(messageConverter, eventsMap))
                .handle(inboundEventMessageChannelAdapter)
                .get();
    }

    @Bean
    public InboundEventMessageChannelAdapter inboundEventMessageChannelAdapter(EventBus eventBus) {
        return new InboundEventMessageChannelAdapter(eventBus);
    }

    @Autowired
    public void configure(EventHandlingConfiguration eventHandlingConfiguration, EventBus eventBus) {
        eventHandlingConfiguration.registerSubscribingEventProcessor("eventBus", c -> eventBus);
        eventHandlingConfiguration.byDefaultAssignTo("eventBus");
    }
}
