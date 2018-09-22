package com.qthegamep.bookmanager3.repository;

import com.qthegamep.bookmanager3.entity.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This interface is a repository. It has all the standard JPA Repository operations realized by spring
 * and some custom methods.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * This repository method should return list of books entities objects from the database by name.
     *
     * @param name is the parameter by which the list of entities objects will be returned.
     * @return list of books entities objects.
     */
    List<Book> findBooksByName(String name);

    /**
     * This repository method should return list of books entities objects from the database by author.
     *
     * @param author is the parameter by which the list of entities objects will be returned.
     * @return list of books entities objects.
     */
    List<Book> findBooksByAuthor(String author);

    /**
     * This repository method should return list of books entities objects from the database by print year.
     *
     * @param printYear is the parameter by which the list of entities objects will be returned.
     * @return list of books entities objects.
     */
    List<Book> findBooksByPrintYear(int printYear);

    /**
     * This repository method should return list of books entities objects from the database by is read.
     *
     * @param isRead is the parameter by which the list of entities objects will be returned.
     * @return list of books entities objects.
     */
    List<Book> findBooksByIsRead(boolean isRead);
}
