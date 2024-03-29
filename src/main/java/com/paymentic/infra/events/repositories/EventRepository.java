package com.paymentic.infra.events.repositories;

import com.paymentic.infra.events.Event;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EventRepository implements PanacheRepository<Event> {
  private static final Logger LOGGER = Logger.getLogger(EventRepository.class);
  private static final String ERROR = "Error to persist Event Id %s ";
  @Transactional
  public boolean shouldHandle(Event event) {
    try {
      persistAndFlush(event);
      return true;
    } catch (Exception exception) {
      LOGGER.error(String.format(ERROR, event.getId().toString()));
      return false;
    }
  }

}
