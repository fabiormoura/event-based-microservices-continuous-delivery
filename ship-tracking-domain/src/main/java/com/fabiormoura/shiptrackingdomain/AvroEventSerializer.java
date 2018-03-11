package com.fabiormoura.shiptrackingdomain;

import org.axonframework.serialization.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;

import java.util.HashMap;

public class AvroEventSerializer implements Serializer {
    private final Serializer delegateSerializer;
    private final MessageConverter messageConverter;

    public AvroEventSerializer(Serializer delegateSerializer, MessageConverter messageConverter) {
        this.delegateSerializer = delegateSerializer;
        this.messageConverter = messageConverter;
    }

    @Override
    public <T> SerializedObject<T> serialize(Object object, Class<T> expectedRepresentation) {
        Message<?> message = messageConverter.toMessage(object, new MessageHeaders(new HashMap<>()));
        return new SimpleSerializedObject(message.getPayload(), expectedRepresentation, object.getClass().getName(), null);
    }

    @Override
    public <T> boolean canSerializeTo(Class<T> expectedRepresentation) {
        return delegateSerializer.canSerializeTo(expectedRepresentation);
    }

    @Override
    public <S, T> T deserialize(SerializedObject<S> serializedObject) {
        return delegateSerializer.deserialize(serializedObject);
    }

    @Override
    public Class classForType(SerializedType type) throws UnknownSerializedTypeException {
        return delegateSerializer.classForType(type);
    }

    @Override
    public SerializedType typeForClass(Class type) {
        return delegateSerializer.typeForClass(type);
    }

    @Override
    public Converter getConverter() {
        return delegateSerializer.getConverter();
    }
}
