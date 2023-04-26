package com.example.library2boot.services;

import com.example.library2boot.models.Book;
import com.example.library2boot.models.Person;
import com.example.library2boot.repositories.PeopleRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PeopleService {
    private final PeopleRepository peopleRepository;
    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }
    public List<Person> findAll() {
        return peopleRepository.findAll();
    }
    public Person findOne(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElse(null);
    }
    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }
    public void save(Person person) {
        peopleRepository.save(person);
    }
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }
    public List<Book> getBooksByPersonId(int id) {
        Person person = peopleRepository.findById(id).get();
        Hibernate.initialize(person.getBooks());
        if(person.getBooks().isEmpty())
            return Collections.emptyList();
        else {
            List<Book> books = person.getBooks();
            Date currentDate = new Date();
            books.forEach(book -> {
                book.setOverDue(((currentDate.getTime() - book.getTakenAt().getTime()) /
                        (1000 * 60 * 60 * 24)) % 365 > 10);
            });
            return books;
        }

    }
}
