package com.ptc.commons.config;

import com.azure.messaging.eventhubs.*;
import com.azure.messaging.eventhubs.checkpointstore.blob.BlobCheckpointStore;
import com.azure.messaging.eventhubs.models.ErrorContext;
import com.azure.messaging.eventhubs.models.EventContext;
import com.azure.messaging.eventhubs.models.PartitionContext;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.ptc.commons.service.api.EventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class AppConfig {

    //Can be fetched from app property

//    private static final String PRODUCER_CONNECTIONSTRING = "Endpoint=sb://az-eastus-it-ofe-dev.servicebus.windows.net/;SharedAccessKeyName=devaccess;SharedAccessKey=5VLqrYjMb+SwOgZVoPYWiFp16e0uJr73wSgP43hfcjA=;EntityPath=ptcevent";
    private static final String PRODUCER_CONNECTIONSTRING = "Endpoint=sb://sanda71.servicebus.windows.net/;SharedAccessKeyName=avalara-test;SharedAccessKey=M7A1uc2ffpEG5gKn46ftRRxe6F3YgA3vax+WG0LSe2E=;EntityPath=sandbox-test";
    private static final String CONSUMER_CONNECTIONSTRING = "Endpoint=sb://az-eastus-it-ofe-dev.servicebus.windows.net/;SharedAccessKeyName=devaccess;SharedAccessKey=5VLqrYjMb+SwOgZVoPYWiFp16e0uJr73wSgP43hfcjA=;EntityPath=ptcevent";
    private static final String STORAGE_CONNECTIONSTRING = "BlobEndpoint=https://itofeaccountdev.blob.core.windows.net;SharedAccessSignature=sp=racwdl&st=2021-12-16T14:28:47Z&se=2022-02-01T22:28:47Z&spr=https&sv=2020-08-04&sr=c&sig=jRgGhrZF52tSWDSr1kpeKp1BfTZ2iu5ta52C799RWxo%3D";
    private static final String STORAGE_CONTAINERNAME = "orderfullfilment";
    private static final String EVENT_HUBNAME = "ptcevent";
    private static final String SAS_TOKEN_STRING = "sp=racwdl&st=2021-12-16T14:28:47Z&se=2022-02-01T22:28:47Z&spr=https&sv=2020-08-04&sr=c&sig=jRgGhrZF52tSWDSr1kpeKp1BfTZ2iu5ta52C799RWxo%3D";

    //@Autowired
    //EventConsumer eventConsumer;

    @Bean
    public EventHubProducerClient eventHubProducerClient() {

        return new EventHubClientBuilder()
                .connectionString(PRODUCER_CONNECTIONSTRING)
                .buildProducerClient();
    }


    @Bean
    public EventProcessorClient eventProcessorClient() {

        // Create a blob container client that you use later to build an event processor client to receive and process events
        BlobContainerAsyncClient blobContainerAsyncClient = new BlobContainerClientBuilder()
                .connectionString(STORAGE_CONNECTIONSTRING)
                .containerName(STORAGE_CONTAINERNAME)
                .sasToken(SAS_TOKEN_STRING)
                .buildAsyncClient();
        return new EventProcessorClientBuilder()
                .connectionString(CONSUMER_CONNECTIONSTRING, EVENT_HUBNAME)
                .consumerGroup("test")
                .processEvent(PARTITION_PROCESSOR)
                .processError(ERROR_HANDLER)
                .checkpointStore(new BlobCheckpointStore(blobContainerAsyncClient)).buildEventProcessorClient();
    }

    public final Consumer<EventContext> PARTITION_PROCESSOR = eventContext -> {
        PartitionContext partitionContext = eventContext.getPartitionContext();
        EventData eventData = eventContext.getEventData();
        log.info("Processing event from partition {} with sequence number {} with body: {}",
                partitionContext.getPartitionId(), eventData.getSequenceNumber(), eventData.getBody());
        // eventConsumer.consumeEvent(eventData);
        // Every 10 events received, it will update the checkpoint stored in Azure Blob Storage.
        if (eventData.getSequenceNumber() % 10 == 0) {
            eventContext.updateCheckpoint();
        }
    };

    public final Consumer<ErrorContext> ERROR_HANDLER = errorContext -> {
        log.error("Error occurred in partition processor for partition {}, {}",
                errorContext.getPartitionContext().getPartitionId(),
                errorContext.getThrowable());
    };

}
