package com.paymentic.domain.repositories;

import com.paymentic.domain.Book;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {

  public Book findById(UUID id){
    return null;
  }

}