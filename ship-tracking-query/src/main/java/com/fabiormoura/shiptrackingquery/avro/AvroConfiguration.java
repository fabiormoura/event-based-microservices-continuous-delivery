package com.fabiormoura.shiptrackingquery.avro;


import com.fabiormoura.shiptrackingquery.event.ShipCreatedEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.stream.schema.avro.AvroMessageConverterProperties;
import org.springframework.cloud.stream.schema.avro.DefaultSubjectNamingStrategy;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.messaging.converter.MessageConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class AvroConfiguration {
    @Bean
    public AvroSchemaRegistryClientMessageConverter avroSchemaMessageConverter(SchemaRegistryClient schemaRegistryClient,
                                                                               CacheManager cacheManager,
                                                                               AvroMessageConverterProperties avroMessageConverterProperties,
                                                                               Map<Class<?>, Resource> consumerEventsSchemaMap) {
        AvroSchemaRegistryClientMessageConverter avroSchemaRegistryClientMessageConverter = new AvroSchemaRegistryClientMessageConverter(
                schemaRegistryClient, cacheManager, consumerEventsSchemaMap);

        avroSchemaRegistryClientMessageConverter.setPrefix(avroMessageConverterProperties.getPrefix());

        avroSchemaRegistryClientMessageConverter.setSubjectNamingStrategy(new DefaultSubjectNamingStrategy());
        return avroSchemaRegistryClientMessageConverter;
    }

    @Bean
    public Map<String, Class<?>> eventContentTypeMap() {
        Map<String, Class<?>> eventsMap = new HashMap<>();
        eventsMap.put("shipcreatedevent", ShipCreatedEvent.class);
        return eventsMap;
    }

    @Bean
    public Map<Class<?>, Resource> consumerEventsSchemaMap(@Value("${consumerSchemasPath}") String consumerSchemasPath, ResourceLoader resourceLoader) throws IOException {
        List<Class<ShipCreatedEvent>> events = asList(ShipCreatedEvent.class);
        List<Resource> schemaFiles = asList(ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(consumerSchemasPath));
        return events
                .stream()
                .collect(Collectors.toMap(eventClass -> eventClass, eventClass -> findSchema(eventClass, schemaFiles)));
    }

    private Resource findSchema(Class<ShipCreatedEvent> eventClass, List<Resource> schemaFiles) {
        return schemaFiles
                .stream()
                .filter(schemaFile -> eventClass.getSimpleName().equals(schemaFile.getFilename().replaceAll(".avsc", "")))
                .findFirst()
                .get();
    }

    @Bean
    public AvroPayloadTransformer avroPayloadTransformer(@Qualifier("avroSchemaMessageConverter") MessageConverter messageConverter, Map<String, Class<?>> eventContentTypeMap) {
        return new AvroPayloadTransformer(messageConverter, eventContentTypeMap);
    }
}
