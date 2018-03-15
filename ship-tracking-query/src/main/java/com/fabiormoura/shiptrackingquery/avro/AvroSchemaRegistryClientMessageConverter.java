package com.fabiormoura.shiptrackingquery.avro;

import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.core.io.Resource;
import org.springframework.messaging.converter.MessageConversionException;

import java.io.IOException;
import java.util.Map;

public class AvroSchemaRegistryClientMessageConverter extends org.springframework.cloud.stream.schema.avro.AvroSchemaRegistryClientMessageConverter {
    private final Map<Class<?>, Resource> consumerEventsSchemaMap;

    /**
     * Creates a new instance, configuring it with {@link SchemaRegistryClient} and
     * {@link CacheManager}.
     *
     * @param schemaRegistryClient    the {@link SchemaRegistryClient} used to interact with
     *                                the schema registry server.
     * @param cacheManager            instance of {@link CacheManager} to cache parsed schemas. If
     *                                caching is not required use {@link NoOpCacheManager}
     * @param consumerEventsSchemaMap
     */
    public AvroSchemaRegistryClientMessageConverter(SchemaRegistryClient schemaRegistryClient,
                                                    CacheManager cacheManager, Map<Class<?>, Resource> consumerEventsSchemaMap) {
        super(schemaRegistryClient, cacheManager);
        this.consumerEventsSchemaMap = consumerEventsSchemaMap;
    }

    @Override
    protected DatumReader<Object> getDatumReader(Class<Object> type, Schema schema, Schema writerSchema) {
        try {
            Schema readerSchema = parseSchema(consumerEventsSchemaMap.get(type));
            DatumReader<Object> reader = new ReflectDatumReader<>(writerSchema, readerSchema, new ReflectData(type.getClassLoader()));
            if (reader == null) {
                throw new MessageConversionException(
                        "No schema can be inferred from type " + type
                                .getName() + " and no schema has been explicitly configured.");
            }
            return reader;
        } catch (IOException e) {
            throw new MessageConversionException(String.format("Unable to parse schema for %s", type.getSimpleName()), e);
        }
    }

    @Override
    protected Schema resolveReaderSchemaForDeserialization(Class<?> targetClass) {
        try {
            return parseSchema(consumerEventsSchemaMap.get(targetClass));
        } catch (IOException e) {
            throw new MessageConversionException(String.format("Not able to resolver reader schema for %s", targetClass.getSimpleName()));
        }
    }
}
