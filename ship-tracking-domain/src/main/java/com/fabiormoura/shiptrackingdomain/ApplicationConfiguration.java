package com.fabiormoura.shiptrackingdomain;

import com.mongodb.MongoClient;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.spring.messaging.OutboundEventMessageChannelAdapter;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.MessageConverter;

@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableSchemaRegistryClient
public class ApplicationConfiguration {
    @Bean
    public EventStorageEngine eventStorageEngine(MongoProperties mongoProperties, final MongoClient mongoClient) {
        return new MongoEventStorageEngine(new DefaultMongoTemplate(mongoClient, mongoProperties.getDatabase()));
    }

    @Bean
    public IntegrationFlow eventbusOutboundGateway(AmqpTemplate amqpTemplate, MessageChannel eventBusOutboundChannel, @Qualifier("avroSchemaMessageConverter") MessageConverter messageConverter) {
        return IntegrationFlows
                .from(eventBusOutboundChannel)
                .transform(messageConverter, "toMessage")
                .handle(Amqp
                        .outboundAdapter(amqpTemplate)
                        .exchangeName("eventbus"))

                .get();
    }

    @Bean
    public MessageChannel eventBusOutboundChannel() {
        DirectChannel directChannel = new DirectChannel();
        return directChannel;
    }

    @Bean
    public OutboundEventMessageChannelAdapter outboundEventMessageChannelAdapter(EventBus eventBus, MessageChannel eventBusOutboundChannel) {
        return new OutboundEventMessageChannelAdapter(eventBus, eventBusOutboundChannel);
    }
}
