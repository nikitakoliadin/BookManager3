package com.qthegamep.bookmanager3.service;

import com.qthegamep.bookmanager3.entity.Book;
import com.qthegamep.bookmanager3.exception.EntityAlreadyExistsException;
import com.qthegamep.bookmanager3.repository.BookRepository;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

/**
 * This class is book service implementation.
 */
@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    /**
     * This is constructor that inject bean dependency driven by
     * {@link org.springframework.beans.factory.annotation.Autowired} annotation.
     *
     * @param bookRepository bean, that will be given from spring context.
     *                       Should not be null.
     */
    @Autowired
    public BookServiceImpl(@NonNull BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * This service method implements adding book entity to the database.
     * If Book entity is already exists in the database then would be thrown
     * {@link com.qthegamep.bookmanager3.exception.EntityAlreadyExistsException}.
     * If book entity is incorrect then would be thrown
     * {@link org.springframework.dao.DataIntegrityViolationException}.
     *
     * @param book is the entity that will be added to the database.
     *             Should not be null.
     * @return book entity.
     * @throws EntityAlreadyExistsException    when trying to add book entity and this entity already exists.
     * @throws DataIntegrityViolationException when trying to add book entity and this entity is incorrect.
     */
    @Override
    public Book add(@NonNull Book book) throws EntityAlreadyExistsException, DataIntegrityViolationException {
        log.info("Preparing to add entity: {}", book);

        checkIfBookExists(book);

        val savedBook = bookRepository.save(book);

        log.info("Entity: {} was added to the database", savedBook);

        return savedBook;
    }

    /**
     * This service method implements adding list of book entities to the database.
     * If one of book entities is already exists in the database then would be thrown
     * {@link com.qthegamep.bookmanager3.exception.EntityAlreadyExistsException}.
     * If book entity is incorrect then would be thrown
     * {@link org.springframework.dao.DataIntegrityViolationException}.
     *
     * @param books is the list of entities that will be added to the database.
     *              Should not be null.
     * @return list of book entities.
     * @throws EntityAlreadyExistsException    when trying to add book entity and this entity already exists.
     * @throws DataIntegrityViolationException when trying to add book entity and this entity is incorrect.
     */
    @Override
    public List<Book> addAll(@NonNull List<Book> books)
            throws EntityAlreadyExistsException, DataIntegrityViolationException {
        log.info("Preparing to add all entities: {}", books);

        books.forEach(this::checkIfBookExists);

        val savedBooks = bookRepository.saveAll(books);

        log.info("Entities: {} was added to the database", savedBooks);

        return savedBooks;
    }

    /**
     * This service method implements returning book entity from the database by id.
     * If book entity is not exists in the database then would be thrown
     * {@link javax.persistence.EntityNotFoundException}.
     *
     * @param id is the parameter by which the entity will be returned.
     *           Should not be null.
     * @return book entity.
     * @throws EntityNotFoundException when trying to get entity by id and this entity does not exists.
     */
    @Override
    public Book getById(@NonNull Long id) throws EntityNotFoundException {
        log.info("Preparing to get book entity by id: {}", id);

        if (!bookRepository.existsById(id)) {
            log.info("Unable to find com.qthegamep.bookmanager3.entity.Book with id {}", id);

            throw new EntityNotFoundException("Unable to find com.qthegamep.bookmanager3.entity.Book with id " + id);
        }

        val book = bookRepository.getOne(id);

        log.info("Entity: {} was gotten from the database", book);

        return book;
    }

    /**
     * This service method implements returning list of book entities from the database by name.
     *
     * @param name is the parameter by which the list of entities will be returned.
     *             Should not be null.
     * @return list of book entities.
     */
    @Override
    public List<Book> getByName(@NonNull String name) {
        log.info("Preparing to get book entities by name: {}", name);

        val books = bookRepository.findBooksByName(name);

        log.info("Entities: {} was gotten from the database", books);

        return books;
    }

    /**
     * This service method implements returning list of book entities from the database by author.
     *
     * @param author is the parameter by which the list of entities will be returned.
     *               Should not be null.
     * @return list of book entities.
     */
    @Override
    public List<Book> getByAuthor(@NonNull String author) {
        log.info("Preparing to get book entities by author: {}", author);

        val books = bookRepository.findBooksByAuthor(author);

        log.info("Entities: {} was gotten from the database", books);

        return books;
    }

    /**
     * This service method implements returning list of book entities from the database by print year.
     *
     * @param printYear is the parameter by which the list of entities will be returned.
     * @return list of book entities.
     */
    @Override
    public List<Book> getByPrintYear(int printYear) {
        log.info("Preparing to get book entities by print year: {}", printYear);

        val books = bookRepository.findBooksByPrintYear(printYear);

        log.info("Entities: {} was gotten from the database", books);

        return books;
    }

    /**
     * This service method implements returning list of book entities from the database by read.
     *
     * @param read is the parameter by which the list of entities will be returned.
     * @return list of book entities.
     */
    @Override
    public List<Book> getByRead(boolean read) {
        log.info("Preparing to get book entities by read: {}", read);

        val books = bookRepository.findBooksByRead(read);

        log.info("Entities: {} was gotten from the database", books);

        return books;
    }

    /**
     * This service method implements returning list of all book entities from the database.
     *
     * @return list of book entities.
     */
    @Override
    public List<Book> getAll() {
        log.info("Preparing to get all book entities");

        val books = bookRepository.findAll();

        log.info("Entities: {} was gotten from the database", books);

        return books;
    }

    /**
     * This service method implements updating book entity in the database.
     * If book entity is incorrect then would be thrown
     * {@link org.springframework.dao.DataIntegrityViolationException}.
     *
     * @param book is the entity that will be updated in the database.
     *             Should not be null.
     * @return book entity.
     * @throws DataIntegrityViolationException when trying to update book entity and this entity is incorrect.
     */
    @Override
    public Book update(@NonNull Book book) throws DataIntegrityViolationException {
        log.info("Preparing to update entity: {}", book);

        val updatedBook = bookRepository.save(book);

        log.info("Entity: {} was updated in the database", updatedBook);

        return updatedBook;
    }

    /**
     * This service method implements updating list of book entities in the database.
     * If book entity is incorrect then would be thrown
     * {@link org.springframework.dao.DataIntegrityViolationException}.
     *
     * @param books is the list of entities that will be updated in the database.
     *              Should not be null.
     * @return list of book entities.
     * @throws DataIntegrityViolationException when trying to update book entity and this entity is incorrect.
     */
    @Override
    public List<Book> updateAll(@NonNull List<Book> books) throws DataIntegrityViolationException {
        log.info("Preparing to update all entities: {}", books);

        val updatedBooks = bookRepository.saveAll(books);

        log.info("Entities: {} was updated in the database", updatedBooks);

        return updatedBooks;
    }

    /**
     * This service method implements deleting book entity from the database.
     * If book entity is incorrect then would be thrown
     * {@link org.springframework.dao.DataIntegrityViolationException}.
     *
     * @param book is the entity that will be deleted from the database.
     *             Should not be null.
     * @throws DataIntegrityViolationException when trying to remove book entity and this entity is incorrect.
     */
    @Override
    public void remove(@NonNull Book book) throws DataIntegrityViolationException {
        log.info("Preparing to remove entity: {}", book);

        bookRepository.delete(book);

        log.info("Entity: {} was removed from the database", book);
    }

    /**
     * This service method implements deleting list of book entities from the database.
     * If book entity is incorrect then would be thrown
     * {@link org.springframework.dao.DataIntegrityViolationException}.
     *
     * @param books is the list of entities that will be deleted from the database.
     *              Should not be null.
     * @throws DataIntegrityViolationException when trying to remove book entity and this entity is incorrect.
     */
    @Override
    public void removeAll(@NonNull List<? extends Book> books) throws DataIntegrityViolationException {
        log.info("Preparing to remove all entities: {}", books);

        bookRepository.deleteAll(books);

        log.info("Entities: {} was removed from the database", books);
    }

    /**
     * This service method implements deleting all entities from the database.
     */
    @Override
    public void removeAll() {
        log.info("Preparing to remove all book entities");

        bookRepository.deleteAll();

        log.info("All book entities was removed from the database");
    }

    private void checkIfBookExists(@NonNull Book book) throws EntityAlreadyExistsException {
        var exists = false;

        val id = book.getId();

        if (Objects.nonNull(id)) {
            exists = bookRepository.existsById(id);
        }

        log.info("Is entity: {} exists: {}", book, exists);

        if (exists) {
            log.info("Entity: {} is exists! You should update this entity or add new one", book);

            throw new EntityAlreadyExistsException("Entity: " + book + " already exists. " +
                    "You should update this entity or add new one"
            );
        }
    }
}
