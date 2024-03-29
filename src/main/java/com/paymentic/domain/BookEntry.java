package com.paymentic.domain;

import com.paymentic.domain.ids.JournalEntryId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "book_entry")
public class BookEntry {
  @Id
  @Column(name = "book_entry_id")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;
  @ManyToOne
  private Book book;
  private LocalDateTime at;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name="id",column=@Column(name="journal_entry_id")),
  })
  private JournalEntryId journalEntry;
  @Column(name = "amount")
  private BigDecimal amount;
  private String currency;
  @Column(name = "operation_type")
  @Enumerated(value = EnumType.STRING)
  private OperationType operationType;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name="fromAmount",column=@Column(name="from_amount")),
      @AttributeOverride(name="toAmount",column=@Column(name="to_amount")),
  })
  private AmountSnapshot snapshot;

  public Integer version;
  public BookEntry() {
  }
  public BookEntry(LocalDateTime at, JournalEntryId journalEntry,
      BigDecimal amount, String currency, OperationType operationType,Book book,Integer version) {
    this.at = at;
    this.journalEntry = journalEntry;
    this.amount = amount;
    this.operationType = operationType;
    this.currency = currency;
    this.book = book;
    this.version = version;
  }
  public static BookEntry paymentEntry(JournalEntryId journalEntry,
      BigDecimal amount,String currency,Book book,Integer version){
    return new BookEntry(LocalDateTime.now(),journalEntry,amount,currency,OperationType.CREDIT,book,version);
  }
  public static BookEntry pendingEntry(JournalEntryId journalEntry,
      BigDecimal amount,String currency,Book book,Integer version){
    return new BookEntry(LocalDateTime.now(),journalEntry,amount,currency,OperationType.DEBIT,book,version);
  }
  public static BookEntry payoutEntry(JournalEntryId journalEntry,
      BigDecimal amount,String currency,Book book,Integer version){
    return new BookEntry(LocalDateTime.now(),journalEntry,amount,currency,OperationType.DEBIT,book,version);
  }
  public UUID getId() {
    return id;
  }
  public LocalDateTime getAt() {
    return at;
  }
  public JournalEntryId getJournalEntry() {
    return journalEntry;
  }
  public BigDecimal getAmount() {
    return amount;
  }
  public OperationType getOperationType() {
    return operationType;
  }
  public Book getBook() {
    return book;
  }
  public String getCurrency() {
    return currency;
  }
  public Integer getVersion() {
    return version;
  }
  public AmountSnapshot getSnapshot() {
    return snapshot;
  }
  public BookEntry holdBalance(AmountSnapshot bookBalance){
    this.snapshot = bookBalance;
    return this;
  }
}
