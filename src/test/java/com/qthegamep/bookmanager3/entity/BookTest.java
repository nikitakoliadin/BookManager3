package com.qthegamep.bookmanager3.entity;

import com.qthegamep.bookmanager3.testhelper.rule.Rules;
import com.qthegamep.bookmanager3.testhelper.util.TestDataUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.val;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;

import static org.assertj.core.api.Assertions.*;

public class BookTest {

    @ClassRule
    public static ExternalResource summaryRule = Rules.SUMMARY_RULE;

    @Rule
    public Stopwatch stopwatchRule = Rules.STOPWATCH_RULE;

    private Book firstBook;
    private Book firstBookCopy;

    @Before
    public void setUp() {
        firstBook = TestDataUtil.createFirstBook();
        firstBookCopy = TestDataUtil.createFirstBook();
    }

    @Test
    public void shouldCreateObjectWithNoArgsConstructor() {
        assertThat(firstBook).isNotNull();
    }

    @Test
    public void shouldGetAndSetId() {
        val id = 2L;

        firstBook.setId(id);

        assertThat(firstBook.getId()).isEqualTo(id);
    }

    @Test
    public void shouldGetAndSetName() {
        val name = "testBook";

        firstBook.setName(name);

        assertThat(firstBook.getName()).isEqualTo(name);
    }

    @Test
    public void shouldGetAndSetAuthor() {
        val author = "testAuthor";

        firstBook.setAuthor(author);

        assertThat(firstBook.getAuthor()).isEqualTo(author);
    }

    @Test
    public void shouldGetAndSetPrintYear() {
        val printYear = 2010;

        firstBook.setPrintYear(printYear);

        assertThat(firstBook.getPrintYear()).isEqualTo(printYear);
    }

    @Test
    public void shouldGetAndSetIsRead() {
        val isRead = true;

        firstBook.setRead(isRead);

        assertThat(firstBook.isRead()).isEqualTo(isRead);
    }

    @Test
    public void shouldBeEquals() {
        assertThat(firstBook).isEqualTo(firstBookCopy);
    }

    @Test
    public void shouldBeEqualsWithNullId() {
        firstBook.setId(null);
        firstBookCopy.setId(null);

        assertThat(firstBook).isEqualTo(firstBookCopy);
    }

    @Test
    public void shouldBeEqualsWithNullName() {
        firstBook.setName(null);
        firstBookCopy.setName(null);

        assertThat(firstBook).isEqualTo(firstBookCopy);
    }

    @Test
    public void shouldBeEqualsWithNullAuthor() {
        firstBook.setAuthor(null);
        firstBookCopy.setAuthor(null);

        assertThat(firstBook).isEqualTo(firstBookCopy);
    }

    @Test
    public void shouldBeEqualsOfCopyObject() {
        firstBookCopy = firstBook;

        assertThat(firstBook).isEqualTo(firstBookCopy);
    }

    @Test
    public void shouldBeNotEqualsIfIdIsNotEquals() {
        firstBookCopy.setId(2L);

        assertThat(firstBook).isNotEqualTo(firstBookCopy);
    }

    @Test
    public void shouldBeNotEqualsIfIdOfFirstObjectIsNull() {
        firstBook.setId(null);

        assertThat(firstBook).isNotEqualTo(firstBookCopy);
    }

    @Test
    public void shouldBeNotEqualsIfNameOfFirstObjectIsNull() {
        firstBook.setName(null);

        assertThat(firstBook).isNotEqualTo(firstBookCopy);
    }

    @Test
    public void shouldBeNotEqualsIfNameIsNotEquals() {
        firstBookCopy.setName("firstBookCopy");

        assertThat(firstBook).isNotEqualTo(firstBookCopy);
    }

    @Test
    public void shouldBeNotEqualsIfAuthorOfFirstObjectIsNull() {
        firstBook.setAuthor(null);

        assertThat(firstBook).isNotEqualTo(firstBookCopy);
    }

    @Test
    public void shouldBeNotEqualsIfAuthorIsNotEquals() {
        firstBookCopy.setAuthor("newAuthor");

        assertThat(firstBook).isNotEqualTo(firstBookCopy);
    }

    @Test
    public void shouldBeNotEqualsIfPrintYearIsNotEquals() {
        firstBookCopy.setPrintYear(2011);

        assertThat(firstBook).isNotEqualTo(firstBookCopy);
    }

    @Test
    public void shouldBeNotEqualsIfIsReadIsNotEquals() {
        firstBookCopy.setRead(true);

        assertThat(firstBook).isNotEqualTo(firstBookCopy);
    }

    @Test
    public void shouldBeNotEqualsToNullObject() {
        firstBookCopy = null;

        assertThat(firstBook).isNotEqualTo(firstBookCopy);
    }

    @Test
    public void shouldBeNotEqualsIfSubClassHasNewContract() {

        @Data
        @EqualsAndHashCode(callSuper = true)
        class BookWithDescription extends Book {

            private String description;
        }

        val bookWithDescription = new BookWithDescription();

        bookWithDescription.setId(1L);
        bookWithDescription.setName("testBook");
        bookWithDescription.setAuthor("testAuthor");
        bookWithDescription.setDescription("testDescription");
        bookWithDescription.setPrintYear(2010);
        bookWithDescription.setRead(false);

        assertThat(firstBook).isNotEqualTo(bookWithDescription);
    }

    @Test
    public void shouldBeEqualsHashCode() {
        assertThat(firstBook.hashCode()).isEqualTo(firstBookCopy.hashCode());
    }

    @Test
    public void shouldBeEqualsHashCodeOfCopyObject() {
        firstBookCopy = firstBook;

        assertThat(firstBook.hashCode()).isEqualTo(firstBookCopy.hashCode());
    }

    @Test
    public void shouldWorkHashCodeCorrectly() {
        val actual = firstBook.hashCode();

        var expected = 1;
        val id = firstBook.getId();
        expected = expected * 59 + (id == null ? 43 : id.hashCode());
        val name = firstBook.getName();
        expected = expected * 59 + (name == null ? 43 : name.hashCode());
        val author = firstBook.getAuthor();
        expected = expected * 59 + (author == null ? 43 : author.hashCode());
        expected = expected * 59 + firstBook.getPrintYear();
        expected = expected * 59 + (firstBook.isRead() ? 79 : 97);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldBeNotEqualsHashCodeIfIdIsNotEquals() {
        firstBookCopy.setId(2L);

        assertThat(firstBook.hashCode()).isNotEqualTo(firstBookCopy.hashCode());
    }

    @Test
    public void shouldBeNotEqualsHashCodeIfIdOfFirstObjectIsNull() {
        firstBook.setId(null);

        assertThat(firstBook.hashCode()).isNotEqualTo(firstBookCopy.hashCode());
    }

    @Test
    public void shouldBeNotEqualsHashCodeIfNameOfFirstObjectIsNull() {
        firstBook.setName(null);

        assertThat(firstBook.hashCode()).isNotEqualTo(firstBookCopy.hashCode());
    }

    @Test
    public void shouldBeNotEqualsHashCodeIfAuthorOfFirstObjectIsNull() {
        firstBook.setAuthor(null);

        assertThat(firstBook.hashCode()).isNotEqualTo(firstBookCopy.hashCode());
    }

    @Test
    public void shouldBeNotEqualsHashCodeIfPrintYearIsNotEquals() {
        firstBookCopy.setPrintYear(2011);

        assertThat(firstBook.hashCode()).isNotEqualTo(firstBookCopy.hashCode());
    }

    @Test
    public void shouldBeNotEqualsHashCodeIfIsReadIsNotEquals() {
        firstBookCopy.setRead(true);

        assertThat(firstBook.hashCode()).isNotEqualTo(firstBookCopy.hashCode());
    }

    @Test
    public void shouldBeNotEqualsHashCodeToNullObject() {
        firstBookCopy = null;

        assertThat(firstBook.hashCode()).isNotEqualTo(firstBookCopy);
    }

    @Test
    public void shouldWorkToStringCorrectly() {
        val expected = "Book(id=1, name=test firstBook, author=test firstAuthor, printYear=2000, read=false)";

        assertThat(firstBook.toString()).isEqualTo(expected);
    }
}
