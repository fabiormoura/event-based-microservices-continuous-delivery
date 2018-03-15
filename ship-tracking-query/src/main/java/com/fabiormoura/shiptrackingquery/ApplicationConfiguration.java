package com.fabiormoura.shiptrackingquery;

import com.fabiormoura.shiptrackingquery.avro.AvroConfiguration;
import com.fabiormoura.shiptrackingquery.avro.AvroPayloadTransformer;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.spring.messaging.InboundEventMessageChannelAdapter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
@EnableAutoConfiguration
@EnableSchemaRegistryClient
@Import({
        AvroConfiguration.class
})
public class ApplicationConfiguration {
    @Bean
    public IntegrationFlow eventBusInboundIntegrationFlow(ConnectionFactory connectionFactory, InboundEventMessageChannelAdapter inboundEventMessageChannelAdapter, AvroPayloadTransformer avroPayloadTransformer) {
        return IntegrationFlows
                .from(Amqp.inboundAdapter(connectionFactory, "ship-tracking-queue"))
                .transform(avroPayloadTransformer)
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
