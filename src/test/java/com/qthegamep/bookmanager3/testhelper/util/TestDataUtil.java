package com.qthegamep.bookmanager3.testhelper.util;

import com.qthegamep.bookmanager3.entity.Book;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is an utility helper class that is responsible for creating test entities.
 */
@UtilityClass
public class TestDataUtil {

    /**
     * This method create new {@link com.qthegamep.bookmanager3.entity.Book} entity.
     *
     * @return new entity.
     */
    public Book createFirstBook() {
        val book = new Book();

        book.setId(1L);
        book.setName("test firstBook");
        book.setAuthor("test firstAuthor");
        book.setPrintYear(2000);
        book.setRead(false);

        return book;
    }

    /**
     * This method create new {@link com.qthegamep.bookmanager3.entity.Book} entity.
     *
     * @return new entity.
     */
    public Book createSecondBook() {
        val book = new Book();

        book.setId(2L);
        book.setName("test secondBook");
        book.setAuthor("test secondAuthor");
        book.setPrintYear(2010);
        book.setRead(true);

        return book;
    }

    /**
     * This method create new list of two {@link com.qthegamep.bookmanager3.entity.Book} entities.
     *
     * @return new list of two entities.
     */
    @NotNull
    @Contract(" -> new")
    public List<Book> createBooks() {
        return new ArrayList<>(Arrays.asList(createFirstBook(), createSecondBook()));
    }
}
