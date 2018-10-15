package com.qthegamep.bookmanager3.service;

import com.qthegamep.bookmanager3.entity.Book;
import com.qthegamep.bookmanager3.exception.EntityAlreadyExistsException;

import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * This interface is a service. It contains all the services that this application can do with the database.
 */
public interface BookService {

    /**
     * This service method should add book entity to the database.
     * If book entity is already exists in the database then would be thrown
     * {@link com.qthegamep.bookmanager3.exception.EntityAlreadyExistsException}.
     * If book entity is incorrect then would be thrown
     * {@link org.springframework.dao.DataIntegrityViolationException}.
     *
     * @param book is the entity that will be added to the database.
     * @return book entity.
     * @throws EntityAlreadyExistsException    when trying to add book entity and this entity already exists.
     * @throws DataIntegrityViolationException when trying to add book entity and this entity is incorrect.
     */
    Book add(Book book) throws EntityAlreadyExistsException, DataIntegrityViolationException;

    /**
     * This service method should add list of book entities to the database.
     * If book entity is already exists in the database then would be thrown
     * {@link com.qthegamep.bookmanager3.exception.EntityAlreadyExistsException}.
     * If book entity is incorrect then would be thrown
     * {@link org.springframework.dao.DataIntegrityViolationException}.
     *
     * @param books is the list of entities that will be added to the database.
     * @return list of book entities.
     * @throws EntityAlreadyExistsException    when trying to add book entity and this entity already exists.
     * @throws DataIntegrityViolationException when trying to add book entity and this entity is incorrect.
     */
    List<Book> addAll(List<Book> books) throws EntityAlreadyExistsException, DataIntegrityViolationException;

    /**
     * This service method should return book entity from the database by id.
     * If book entity is not exists in the database then would be thrown
     * {@link javax.persistence.EntityNotFoundException}.
     *
     * @param id is the parameter by which the entity will be returned.
     * @return book entity.
     * @throws EntityNotFoundException when trying to get entity by id and this entity does not exists.
     */
    Book getById(Long id) throws EntityNotFoundException;

    /**
     * This service method should return list of book entities from the database by name.
     *
     * @param name is the parameter by which the list of entities will be returned.
     * @return list of book entities.
     */
    List<Book> getByName(String name);

    /**
     * This service method should return list of book entities from the database by author.
     *
     * @param author is the parameter by which the list of entities will be returned.
     * @return list of book entities.
     */
    List<Book> getByAuthor(String author);

    /**
     * This service method should return list of book entities from the database by print year.
     *
     * @param printYear is the parameter by which the list of entities will be returned.
     * @return list of book entities.
     */
    List<Book> getByPrintYear(int printYear);

    /**
     * This service method should return list of book entities from the database by read.
     *
     * @param read is the parameter by which the list of entities will be returned.
     * @return list of book entities.
     */
    List<Book> getByRead(boolean read);

    /**
     * This service method should return list of all book entities from the database.
     *
     * @return list of book entities.
     */
    List<Book> getAll();

    /**
     * This service method should update book entity in the database.
     * If book entity is incorrect then would be thrown
     * {@link org.springframework.dao.DataIntegrityViolationException}.
     *
     * @param book is the entity that will be updated in the database.
     * @return book entity.
     * @throws DataIntegrityViolationException when trying to update book entity and this entity is incorrect.
     */
    Book update(Book book) throws DataIntegrityViolationException;

    /**
     * This service method should update list of book entities in the database.
     * If book entity is incorrect then would be thrown
     * {@link org.springframework.dao.DataIntegrityViolationException}.
     *
     * @param books is the list of entities that will be updated in the database.
     * @return list of book entities.
     * @throws DataIntegrityViolationException when trying to update book entity and this entity is incorrect.
     */
    List<Book> updateAll(List<Book> books) throws DataIntegrityViolationException;

    /**
     * This service method should delete book entity from the database.
     * If book entity is incorrect then would be thrown
     * {@link org.springframework.dao.DataIntegrityViolationException}.
     *
     * @param book is the entity that will be deleted from the database.
     * @throws DataIntegrityViolationException when trying to remove book entity and this entity is incorrect.
     */
    void remove(Book book) throws DataIntegrityViolationException;

    /**
     * This service method should delete list of book entities from the database.
     * If book entity is incorrect then would be thrown
     * {@link org.springframework.dao.DataIntegrityViolationException}.
     *
     * @param books is the list of entities that will be deleted from the database.
     * @throws DataIntegrityViolationException when trying to remove book entity and this entity is incorrect.
     */
    void removeAll(List<? extends Book> books) throws DataIntegrityViolationException;

    /**
     * This service method should delete all book entities from the database.
     */
    void removeAll();
}
