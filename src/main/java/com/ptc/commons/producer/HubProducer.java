package com.ptc.commons.producer;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class HubProducer {

    private final EventHubProducerClient eventHubProducerClient;

    @Autowired
    private HubProducer(EventHubProducerClient eventHubProducerClient) {
        this.eventHubProducerClient = eventHubProducerClient;
    }

    public void publishMessage(Object payload) throws IOException {

        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        try (ObjectOutputStream ois = new ObjectOutputStream(boas)) {
            ois.writeObject(payload);
            byte[] bytePayload=boas.toByteArray();
            List<EventData> allEvents = Arrays.asList(new EventData(SerializationUtils.serialize(bytePayload)));
            eventHubProducerClient.send(allEvents);
        } catch (IOException ioe) {
            log.error(" error in hub producer ", ioe);
        }

        //EventDataBatch eventDataBatch = eventHubProducerClient.createBatch();
        /*for (EventData eventData : allEvents) {
            if (!eventDataBatch.tryAdd(eventData)) {
                eventHubProducerClient.send(eventDataBatch);
                eventDataBatch = eventHubProducerClient.createBatch();

                // Try to add that event that couldn't fit before.
                if (!eventDataBatch.tryAdd(eventData)) {
                    throw new IllegalArgumentException("Event is too large for an empty batch. Max size: "
                            + eventDataBatch.getMaxSizeInBytes());
                }
            }
        }
        // send the last batch of remaining events
        if (eventDataBatch.getCount() > 0) {
            eventHubProducerClient.send(eventDataBatch);
        }*/
    }

}
