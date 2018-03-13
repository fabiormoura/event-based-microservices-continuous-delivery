package com.fabiormoura;

import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.util.StringUtils;

import java.util.Map;

import static org.springframework.messaging.MessageHeaders.CONTENT_TYPE;

public class AvroPayloadTransformer extends AbstractTransformer {
    private final MessageConverter messageConverter;
    private final Map<String, Class<?>> eventsMap;

    public AvroPayloadTransformer(MessageConverter messageConverter, Map<String, Class<?>> eventsMap) {
        this.messageConverter = messageConverter;
        this.eventsMap = eventsMap;
    }

    @Override
    protected Object doTransform(Message<?> message) throws Exception {
        String eventContentType = (String) message.getHeaders().get(CONTENT_TYPE);
        String eventType = eventContentType
                .replace("application/vnd.", "")
                .replaceAll("\\.v\\d\\+avro", "");
        if (StringUtils.isEmpty(eventType)) {
            throw new Exception(String.format("Event class not found for %s", eventContentType));
        }
        return messageConverter.fromMessage(message, eventsMap.get(eventType));
    }
}
