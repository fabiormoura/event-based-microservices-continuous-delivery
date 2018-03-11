package com.fabiormoura.shiptrackingdomain;

import com.mongodb.MongoClient;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.serialization.*;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Qualifier("eventSerializer")
    @Bean
    public Serializer eventSerializer(@Qualifier("avroSchemaMessageConverter") MessageConverter messageConverter) {
        return new AvroEventSerializer(new JacksonSerializer(), messageConverter);
    }
}
