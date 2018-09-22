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
import javax.persistence.EntityNotFoundException;

import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.hibernate.PropertyValueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.EmptyResultDataAccessException;
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
        val allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Transactional
    public void shouldAddEntityToTheDatabaseCorrectly() {
        bookRepository.save(firstBook);

        var allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .hasSize(1)
                .contains(firstBook);

        bookRepository.saveAndFlush(secondBook);

        allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
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
    public void shouldThrowDataIntegrityViolationExceptionWhenAddIncorrectEntity() {
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

    @Test
    @Transactional
    public void shouldAddAllEntitiesToTheDatabaseCorrectly() {
        bookRepository.saveAll(books);

        var allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .hasSize(2)
                .contains(firstBook, secondBook);
    }

    @Test
    @Transactional
    public void shouldThrowInvalidDataAccessApiUsageExceptionWhenAddNullListOfEntities() {
        val exceptionMessage = "The given Iterable of entities not be null!; nested exception is " +
                "java.lang.IllegalArgumentException: The given Iterable of entities not be null!";

        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
                .isThrownBy(() -> bookRepository.saveAll(null))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @Transactional
    public void shouldThrowInvalidDataAccessApiUsageExceptionWhenAddListWithNullEntity() {
        val exceptionMessage = "Target object must not be null; nested exception is " +
                "java.lang.IllegalArgumentException: Target object must not be null";

        books.add(null);

        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
                .isThrownBy(() -> bookRepository.saveAll(books))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Transactional
    public void shouldThrowDataIntegrityViolationExceptionWhenAddListWithIncorrectEntity() {
        val exceptionMessage = "not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name; nested exception is " +
                "org.hibernate.PropertyValueException: not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name";

        books.get(0).setName(null);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> bookRepository.saveAll(books))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(PropertyValueException.class);
    }

    @Test
    @Transactional
    public void shouldGetByIdEntityFromTheDatabaseCorrectly() {
        bookRepository.save(firstBook);

        val book = bookRepository.getOne(1L);

        assertThat(book)
                .isNotNull()
                .isEqualTo(firstBook);
    }

    @Test
    @Transactional
    public void shouldThrowEntityNotFoundExceptionWhenGetByIdNotExistEntity() {
        val exceptionMessage = "Unable to find com.qthegamep.bookmanager3.entity.Book with id 2";

        bookRepository.save(firstBook);

        val book = bookRepository.getOne(2L);

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> System.out.println(book))
                .withMessage(exceptionMessage);
    }

    @Test
    @Transactional
    public void shouldGetByNameEntitiesFromTheDatabaseCorrectly() {
        bookRepository.saveAll(books);

        val booksFromTheDatabase = bookRepository.findBooksByName("test firstBook");

        assertThat(booksFromTheDatabase)
                .isNotNull()
                .hasSize(1)
                .contains(firstBook);
    }

    @Test
    @Transactional
    public void shouldGetByNameReturnEmptyEntitiesListCorrectly() {
        val booksFromTheDatabase = bookRepository.findBooksByName("nothing");

        assertThat(booksFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Transactional
    public void shouldGetByAuthorEntitiesFromTheDatabaseCorrectly() {
        bookRepository.saveAll(books);

        val booksFromTheDatabase = bookRepository.findBooksByAuthor("test firstAuthor");

        assertThat(booksFromTheDatabase)
                .isNotNull()
                .hasSize(1)
                .contains(firstBook);
    }

    @Test
    @Transactional
    public void shouldGetByAuthorReturnEmptyEntitiesListCorrectly() {
        val booksFromTheDatabase = bookRepository.findBooksByAuthor("nothing");

        assertThat(booksFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Transactional
    public void shouldGetByPrintYearEntitiesFromTheDatabaseCorrectly() {
        bookRepository.saveAll(books);

        val booksFromTheDatabase = bookRepository.findBooksByPrintYear(2000);

        assertThat(booksFromTheDatabase)
                .isNotNull()
                .hasSize(1)
                .contains(firstBook);
    }

    @Test
    @Transactional
    public void shouldGetByPrintYearReturnEmptyEntitiesListCorrectly() {
        val booksFromTheDatabase = bookRepository.findBooksByPrintYear(0);

        assertThat(booksFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Transactional
    public void shouldGetByIsReadEntitiesFromTheDatabaseCorrectly() {
        bookRepository.saveAll(books);

        val booksFromTheDatabase = bookRepository.findBooksByIsRead(false);

        assertThat(booksFromTheDatabase)
                .isNotNull()
                .hasSize(1)
                .contains(firstBook);
    }

    @Test
    @Transactional
    public void shouldGetByIsReadReturnEmptyEntitiesListCorrectly() {
        val booksFromTheDatabase = bookRepository.findBooksByIsRead(true);

        assertThat(booksFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Transactional
    public void shouldGetAllEntitiesFromTheDatabaseCorrectly() {
        bookRepository.saveAll(books);

        val allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .hasSize(2)
                .contains(firstBook, secondBook);
    }

    @Test
    @Transactional
    public void shouldGetAllReturnEmptyEntitiesListCorrectly() {
        val allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Transactional
    public void shouldUpdateEntityInTheDatabaseCorrectly() {
        bookRepository.saveAll(books);

        firstBook.setName("updated");

        bookRepository.save(firstBook);

        var allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .hasSize(2)
                .contains(firstBook, secondBook);

        secondBook.setName("updated");

        bookRepository.saveAndFlush(secondBook);

        allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .hasSize(2)
                .contains(firstBook, secondBook);
    }

    @Test
    @Transactional
    public void shouldUpdateAllEntitiesInTheDatabaseCorrectly() {
        bookRepository.saveAll(books);

        books.get(0).setName("updated");
        books.get(1).setName("updated");

        bookRepository.saveAll(books);

        val allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .hasSize(2)
                .contains(books.get(0), books.get(1));
    }

    @Test
    @Transactional
    public void shouldDeleteEntityFromTheDatabaseCorrectly() {
        bookRepository.saveAll(books);

        bookRepository.delete(firstBook);

        var allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .hasSize(1)
                .contains(secondBook);

        bookRepository.deleteById(2L);

        allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Transactional
    public void shouldNotDeleteEntityFromTheDatabaseWhenDatabaseIsEmpty() {
        bookRepository.delete(firstBook);

        val allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Transactional
    public void shouldThrowInvalidDataAccessApiUsageExceptionWhenDeleteNullEntity() {
        val exceptionMessage = "The entity must not be null!; nested exception is " +
                "java.lang.IllegalArgumentException: The entity must not be null!";

        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
                .isThrownBy(() -> bookRepository.delete(null))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Transactional
    public void shouldThrowDataIntegrityViolationExceptionWhenDeleteIncorrectEntity() {
        val exceptionMessage = "not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name; nested exception is " +
                "org.hibernate.PropertyValueException: not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name";

        firstBook.setName(null);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> bookRepository.delete(firstBook))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(PropertyValueException.class);
    }

    @Test
    @Transactional
    public void shouldThrowEmptyResultDataAccessExceptionWhenDeleteNotExistEntity() {
        val exceptionMessage = "No class com.qthegamep.bookmanager3.entity.Book entity with id 1 exists!";

        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> bookRepository.deleteById(1L))
                .withMessage(exceptionMessage);
    }

    @Test
    @Transactional
    public void shouldDeleteAllEntitiesFromTheDatabaseCorrectly() {
        bookRepository.saveAll(books);

        bookRepository.deleteAll(books);

        var allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .isEmpty();

        bookRepository.saveAll(books);

        bookRepository.deleteAll();

        allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Transactional
    public void shouldNotDeleteAllEntitiesFromTheDatabaseWhenDatabaseIsEmpty() {
        bookRepository.deleteAll();

        val allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Transactional
    public void shouldNotDeleteListOfAllEntitiesFromTheDatabaseWhenDatabaseIsEmpty() {
        bookRepository.deleteAll(books);

        val allBooksFromTheDatabase = bookRepository.findAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Transactional
    public void shouldThrowInvalidDataAccessApiUsageExceptionWhenDeleteNullListOfEntities() {
        val exceptionMessage = "The given Iterable of entities not be null!; nested exception is " +
                "java.lang.IllegalArgumentException: The given Iterable of entities not be null!";

        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
                .isThrownBy(() -> bookRepository.deleteAll(null))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Transactional
    public void shouldThrowInvalidDataAccessApiUsageExceptionWhenDeleteListWithNullEntity() {
        val exceptionMessage = "The entity must not be null!; nested exception is " +
                "java.lang.IllegalArgumentException: The entity must not be null!";

        books.add(null);

        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
                .isThrownBy(() -> bookRepository.deleteAll(books))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Transactional
    public void shouldThrowDataIntegrityViolationExceptionWhenDeleteListWithIncorrectEntity() {
        val exceptionMessage = "not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name; nested exception is " +
                "org.hibernate.PropertyValueException: not-null property references a null or transient " +
                "value : com.qthegamep.bookmanager3.entity.Book.name";

        books.get(0).setName(null);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> bookRepository.deleteAll(books))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(PropertyValueException.class);
    }
}
