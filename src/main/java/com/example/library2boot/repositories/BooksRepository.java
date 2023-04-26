package com.example.library2boot.repositories;

import com.example.library2boot.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findBooksByTitleStartingWith(String title);
}
