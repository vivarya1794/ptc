package com.ptc.commons.service.api;

import com.azure.messaging.eventhubs.EventData;

public interface EventConsumer {

    Object consumeEvent(EventData eventData);
}
