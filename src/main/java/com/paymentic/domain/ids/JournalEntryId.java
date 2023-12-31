package com.paymentic.domain.ids;

import jakarta.persistence.Embeddable;

@Embeddable
public class JournalEntryId {
  private String id;
  public JournalEntryId(){}

  public JournalEntryId(String id) {
    this.id = id;
  }
  public String getId() {
    return id;
  }

}
