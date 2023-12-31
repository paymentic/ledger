package com.paymentic.adapter.kafka;

import com.paymentic.domain.events.TransactionRegistered;
import com.paymentic.infra.events.repositories.EventRepository;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.ce.IncomingCloudEventMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TransactionRegisteredProcessor {
  private static final String TRANSACTION_REGISTERED_EVENT_TYPE = "paymentic.payments-gateway.v1.transaction-registered";
  private static final Logger LOGGER = Logger.getLogger(TransactionRegisteredProcessor.class);
  private static final String ERROR = "Event %s already handled!!!";
  private final Event<TransactionRegistered> trigger;
  private final EventRepository eventRepository;
  public TransactionRegisteredProcessor(Event<TransactionRegistered> trigger,
      EventRepository eventRepository) {
    this.trigger = trigger;
    this.eventRepository = eventRepository;
  }
  @Blocking
  @Incoming("transaction-registered")
  public CompletionStage<Void> process(Message<TransactionRegistered> message) {
    var event = message.getMetadata(IncomingCloudEventMetadata.class).orElseThrow(() -> new IllegalArgumentException("Expected a Cloud Event"));
    var handle = eventRepository.shouldHandle(new com.paymentic.infra.events.Event(UUID.fromString(event.getId())));
    if (handle){
      if (TRANSACTION_REGISTERED_EVENT_TYPE.equals(event.getType())){
        LOGGER.info("Receiving transaction registered event. Start processing....");
        var transaction = message.getPayload();
        this.trigger.fire(transaction);
        LOGGER.info("Transaction registered event processed!");
      }
    }else {
      LOGGER.error(String.format(ERROR, event.getId()));
    }
    return message.ack();
  }

}
