package com.qthegamep.bookmanager3.repository;

import com.qthegamep.bookmanager3.entity.Book;
import com.qthegamep.bookmanager3.testhelper.rule.Rules;
import com.qthegamep.bookmanager3.testhelper.util.TestDataUtil;

import lombok.val;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import org.hibernate.PropertyValueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@WebAppConfiguration
@ContextConfiguration("classpath:/applicationContext.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/initDB.sql")
@RunWith(SpringJUnit4ClassRunner.class)
public class BookRepositoryTest {

    @ClassRule
    public static ExternalResource summaryRule = Rules.SUMMARY_RULE;

    @Rule
    public Stopwatch stopwatchRule = Rules.STOPWATCH_RULE;

    @Resource
    private BookRepository bookRepository;

    private Book firstBook;
    private Book secondBook;

    private List<Book> books;

    @Before
    public void setUp() {
        firstBook = TestDataUtil.createFirstBook();
        secondBook = TestDataUtil.createSecondBook();

        books = TestDataUtil.createBooks();
    }

    @Test
    public void shouldBeNotNullBookRepository() {
        assertThat(bookRepository).isNotNull();
    }

    @Test
    public void shouldImplementsJpaRepositoryInterface() {
        assertThat(bookRepository).isInstanceOf(JpaRepository.class);
    }

    @Test
    @Transactional
    public void shouldBeEmptyDatabaseBeforeEachTest() {
        val allEntitiesFromTheDatabase = bookRepository.findAll();

        assertThat(allEntitiesFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Transactional
    public void shouldAddEntityToTheDatabaseCorrectly() {
        bookRepository.save(firstBook);

        var allEntitiesFromTheDatabase = bookRepository.findAll();

        assertThat(allEntitiesFromTheDatabase)
                .isNotNull()
                .hasSize(1)
                .contains(firstBook);

        bookRepository.saveAndFlush(secondBook);

        allEntitiesFromTheDatabase = bookRepository.findAll();

        assertThat(allEntitiesFromTheDatabase)
                .isNotNull()
                .hasSize(2)
                .contains(firstBook, secondBook);
    }

    @Test
    @Transactional
    public void shouldThrowInvalidDataAccessApiUsageExceptionWhenAddNullEntity() {
        val exceptionMessage = "Target object must not be null; nested exception is " +
                "java.lang.IllegalArgumentException: Target object must not be null";

        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
                .isThrownBy(() -> bookRepository.save(null))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(IllegalArgumentException.class);

        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
                .isThrownBy(() -> bookRepository.saveAndFlush(null))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Transactional
    public void shouldThrowDataIntegrityViolationExceptionWhenEntityIsIncorrect() {
        val exceptionMessage = "not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name; nested exception is " +
                "org.hibernate.PropertyValueException: not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name";

        firstBook.setName(null);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> bookRepository.save(firstBook))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(PropertyValueException.class);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> bookRepository.saveAndFlush(firstBook))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(PropertyValueException.class);
    }
}
