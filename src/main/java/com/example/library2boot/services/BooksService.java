package com.example.library2boot.services;

import com.example.library2boot.models.Book;
import com.example.library2boot.models.Person;
import com.example.library2boot.repositories.BooksRepository;
import com.example.library2boot.repositories.PeopleRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;
    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if(sortByYear)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll();
    }
    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if(sortByYear)
            return booksRepository.findAll(PageRequest.of(page,booksPerPage,Sort.by("year"))).getContent();
        else
            return booksRepository.findAll(PageRequest.of(page,booksPerPage)).getContent();
    }
    public Book findOne(int id) {
        Optional<Book> person = booksRepository.findById(id);
        return person.orElse(null);
    }
    public void save(Book book) {
        booksRepository.save(book);
    }
    public void update(int id, Book updatedBook) {
        Book book = booksRepository.findById(id).get();
        updatedBook.setId(id);
        updatedBook.setPerson(book.getPerson());
        booksRepository.save(book);
    }
    public void delete(int id) {
        booksRepository.deleteById(id);
    }
    public Optional<Person> getBookOwner(int id) {
        Book book = booksRepository.findById(id).get();
        return Optional.ofNullable(book.getPerson());
    }
    public void assign(int bookId, Person person) {
        booksRepository.findById(bookId).ifPresent(book -> {
            book.setPerson(person);
            book.setTakenAt(new Date());
        });
    }
    public void release(int bookId) {
        booksRepository.findById(bookId).ifPresent(book -> {
            book.setPerson(null);
            book.setTakenAt(null);
        });
    }
    public List<Book> findByTitle(String title) {
        List<Book> books = booksRepository.findBooksByTitleStartingWith(title);
        books.forEach(book -> Hibernate.initialize(book.getPerson()));
        return books;
    }
}
