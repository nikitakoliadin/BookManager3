package com.qthegamep.bookmanager3.service;

import com.qthegamep.bookmanager3.entity.Book;

import java.util.List;

/**
 * This interface is a service. It contains all the services that this application can do with the database.
 */
public interface BookService {

    /**
     * This service method should add book entity to the database.
     *
     * @param book is the entity that will be added to the database.
     * @return book entity.
     */
    Book add(Book book);

    /**
     * This service method should add list of books entities to the database.
     *
     * @param books is the list of entities that will be added to the database.
     * @return list of books entities.
     */
    List<Book> addAll(List<Book> books);

    /**
     * This service method should return book entity from the database by id.
     *
     * @param id is the parameter by which the entity will be returned.
     * @return book entity.
     */
    Book getById(Long id);

    /**
     * This service method should return list of books entities from the database by name.
     *
     * @param name is the parameter by which the list of entities will be returned.
     * @return list of books entities.
     */
    List<Book> getByName(String name);

    /**
     * This service method should return list of books entities from the database by author.
     *
     * @param author is the parameter by which the list of entities will be returned.
     * @return list of books entities.
     */
    List<Book> getByAuthor(String author);

    /**
     * This service method should return list of books entities from the database by print year.
     *
     * @param printYear is the parameter by which the list of entities will be returned.
     * @return list of books entities.
     */
    List<Book> getByPrintYear(int printYear);

    /**
     * This service method should return list of books entities from the database by read.
     *
     * @param read is the parameter by which the list of entities will be returned.
     * @return list of books entities.
     */
    List<Book> getByRead(boolean read);

    /**
     * This service method should return list of all books entities from the database.
     *
     * @return list of books entities.
     */
    List<Book> getAll();

    /**
     * This service method should update book entity in the database.
     *
     * @param book is the entity that will be updated in the database.
     * @return book entity.
     */
    Book update(Book book);

    /**
     * This service method should update list of books entities in the database.
     *
     * @param books is the list of entities that will be updated in the database.
     * @return list of books entities.
     */
    List<Book> updateAll(List<Book> books);

    /**
     * This service method should delete book entity from the database.
     *
     * @param book is the entity that will be deleted from the database.
     */
    void remove(Book book);

    /**
     * This service method should delete list of books entities from the database.
     *
     * @param books is the list of entities that will be deleted from the database.
     */
    void removeAll(List<? extends Book> books);

    /**
     * This service method should delete all entities from the database.
     */
    void removeAll();
}
