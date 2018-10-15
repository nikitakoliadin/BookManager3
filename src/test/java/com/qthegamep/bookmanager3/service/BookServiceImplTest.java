package com.qthegamep.bookmanager3.service;

import com.qthegamep.bookmanager3.entity.Book;
import com.qthegamep.bookmanager3.exception.EntityAlreadyExistsException;
import com.qthegamep.bookmanager3.repository.BookRepository;
import com.qthegamep.bookmanager3.testhelper.rule.Rules;
import com.qthegamep.bookmanager3.testhelper.util.TestDataUtil;

import lombok.val;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.hibernate.PropertyValueException;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@WebAppConfiguration
@ContextConfiguration("classpath:testApplicationContext.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
public class BookServiceImplTest {

    @ClassRule
    public static ExternalResource summaryRule = Rules.SUMMARY_RULE;

    @Rule
    public Stopwatch stopwatchRule = Rules.STOPWATCH_RULE;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Autowired
    private BookService bookService;
    private BookService bookServiceMock;

    @Mock
    private BookRepository bookRepositoryMock;

    private Book firstBook;
    private Book secondBook;

    private List<Book> books;

    @Before
    public void setUp() {
        bookServiceMock = new BookServiceImpl(bookRepositoryMock);

        firstBook = TestDataUtil.createFirstBook();
        secondBook = TestDataUtil.createSecondBook();

        books = TestDataUtil.createBooks();
    }

    @Test
    public void shouldAutowiredBookService() {
        assertThat(bookService).isNotNull();
        assertThat(bookServiceMock).isNotNull();
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenCreateBookServiceWithNullRepository() {
        val exceptionMessage = "bookRepository is marked @NonNull but is null";

        assertThatNullPointerException()
                .isThrownBy(() -> new BookServiceImpl(null))
                .withMessage(exceptionMessage);
    }

    @Test
    public void shouldCreateBookRepository() {
        assertThat(bookRepositoryMock).isNotNull();
    }

    @Test
    public void shouldImplementsBookServiceInterface() {
        assertThat(bookService).isInstanceOf(BookService.class);
        assertThat(bookServiceMock).isInstanceOf(BookService.class);
    }

    @Test
    public void shouldBeEmptyDatabaseBeforeEachTest() {
        val allBooksFromTheDatabase = bookService.getAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void shouldAddBookCorrectly() {
        val addedBook = bookService.add(firstBook);

        assertThat(addedBook)
                .isNotNull()
                .isEqualTo(firstBook);
    }

    @Test
    public void shouldCallAddMethodCorrectly() {
        bookServiceMock.add(firstBook);

        verify(bookRepositoryMock, times(1)).existsById(firstBook.getId());
        verify(bookRepositoryMock, times(1)).save(firstBook);

        verifyNoMoreInteractions(bookRepositoryMock);
    }

    @Test
    public void shouldAddBookWithNullIdCorrectly() {
        firstBook.setId(null);

        val addedBook = bookService.add(firstBook);

        assertThat(addedBook)
                .isNotNull()
                .isEqualTo(firstBook);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenAddNullBook() {
        val exceptionMessage = "book is marked @NonNull but is null";

        assertThatNullPointerException()
                .isThrownBy(() -> bookService.add(null))
                .withMessage(exceptionMessage);
    }

    @Test
    public void shouldThrowEntityAlreadyExistsExceptionWhenAddAlreadyExistedBook() {
        val exceptionMessage = "Entity: Book(id=1, name=test firstBook, author=test firstAuthor, printYear=2000, " +
                "read=false) already exists. You should update this entity or add new one";

        bookService.add(firstBook);

        assertThatExceptionOfType(EntityAlreadyExistsException.class)
                .isThrownBy(() -> bookService.add(firstBook))
                .withMessage(exceptionMessage);
    }

    @Test
    public void shouldThrowDataIntegrityViolationExceptionWhenAddIncorrectEntity() {
        val exceptionMessage = "not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name; nested exception is " +
                "org.hibernate.PropertyValueException: not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name";

        firstBook.setName(null);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> bookService.add(firstBook))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(PropertyValueException.class);
    }

    @Test
    public void shouldAddAllBooksCorrectly() {
        val addedBooks = bookService.addAll(books);

        assertThat(addedBooks)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(books)
                .contains(firstBook, secondBook);
    }

    @Test
    public void shouldCallAddAllMethodCorrectly() {
        bookServiceMock.addAll(books);

        verify(bookRepositoryMock, times(1)).existsById(firstBook.getId());
        verify(bookRepositoryMock, times(1)).existsById(secondBook.getId());
        verify(bookRepositoryMock, times(1)).saveAll(books);

        verifyNoMoreInteractions(bookRepositoryMock);
    }

    @Test
    public void shouldAddAllBooksWithNullIdCorrectly() {
        books.get(0).setId(null);
        books.get(1).setId(null);

        val addedBooks = bookService.addAll(this.books);

        assertThat(addedBooks)
                .isNotNull()
                .isNotEmpty()
                .contains(firstBook, secondBook);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenAddAllNullListOfBooks() {
        val exceptionMessage = "books is marked @NonNull but is null";

        assertThatNullPointerException()
                .isThrownBy(() -> bookService.addAll(null))
                .withMessage(exceptionMessage);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenAddAllListWithNullBook() {
        val exceptionMessage = "book is marked @NonNull but is null";

        books.add(null);

        assertThatNullPointerException()
                .isThrownBy(() -> bookService.addAll(books))
                .withMessage(exceptionMessage);
    }

    @Test
    public void shouldThrowEntityAlreadyExistsExceptionWhenAddAllListOfAlreadyExistedBooks() {
        val exceptionMessage = "Entity: Book(id=1, name=test firstBook, author=test firstAuthor, printYear=2000, " +
                "read=false) already exists. You should update this entity or add new one";

        bookService.addAll(books);

        assertThatExceptionOfType(EntityAlreadyExistsException.class)
                .isThrownBy(() -> bookService.addAll(books))
                .withMessage(exceptionMessage);
    }

    @Test
    public void shouldThrowDataIntegrityViolationExceptionWhenAddAllListWithIncorrectEntity() {
        val exceptionMessage = "not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name; nested exception is " +
                "org.hibernate.PropertyValueException: not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name";

        books.get(0).setName(null);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> bookService.addAll(books))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(PropertyValueException.class);
    }

    @Test
    public void shouldGetByIdBookCorrectly() {
        bookService.add(firstBook);

        val book = bookService.getById(1L);

        assertThat(book)
                .isNotNull()
                .isEqualTo(firstBook);
    }

    @Test
    public void shouldCallGetByIdMethodCorrectly() {
        when(bookRepositoryMock.findById(1L)).thenReturn(Optional.of(firstBook));

        bookServiceMock.getById(1L);

        verify(bookRepositoryMock, times(1)).findById(1L);

        verifyNoMoreInteractions(bookRepositoryMock);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenGetByNullId() {
        val exceptionMessage = "id is marked @NonNull but is null";

        assertThatNullPointerException()
                .isThrownBy(() -> bookService.getById(null))
                .withMessage(exceptionMessage);
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenGetByIdNotExistEntity() {
        val exceptionMessage = "Unable to find com.qthegamep.bookmanager3.entity.Book with id 2";

        bookService.add(firstBook);

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> bookService.getById(2L))
                .withMessage(exceptionMessage);
    }

    @Test
    public void shouldGetByNameBooksCorrectly() {
        bookService.addAll(books);

        val booksFromTheDatabase = bookService.getByName("test firstBook");

        assertThat(booksFromTheDatabase)
                .isNotNull()
                .isNotEmpty()
                .contains(firstBook);
    }

    @Test
    public void shouldCallGetByNameMethodCorrectly() {
        bookServiceMock.getByName("test firstBook");

        verify(bookRepositoryMock, times(1)).findBooksByName("test firstBook");

        verifyNoMoreInteractions(bookRepositoryMock);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenGetByNullName() {
        val exceptionMessage = "name is marked @NonNull but is null";

        assertThatNullPointerException()
                .isThrownBy(() -> bookService.getByName(null))
                .withMessage(exceptionMessage);
    }

    @Test
    public void shouldGetByAuthorBooksCorrectly() {
        bookService.addAll(books);

        val booksFromTheDatabase = bookService.getByAuthor("test firstAuthor");

        assertThat(booksFromTheDatabase)
                .isNotNull()
                .isNotEmpty()
                .contains(firstBook);
    }

    @Test
    public void shouldCallGetByAuthorMethodCorrectly() {
        bookServiceMock.getByAuthor("test firstAuthor");

        verify(bookRepositoryMock, times(1)).findBooksByAuthor("test firstAuthor");

        verifyNoMoreInteractions(bookRepositoryMock);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenGetByNullAuthor() {
        val exceptionMessage = "author is marked @NonNull but is null";

        assertThatNullPointerException()
                .isThrownBy(() -> bookService.getByAuthor(null))
                .withMessage(exceptionMessage);
    }

    @Test
    public void shouldGetByPrintYearBooksCorrectly() {
        bookService.addAll(books);

        val booksFromTheDatabase = bookService.getByPrintYear(2000);

        assertThat(booksFromTheDatabase)
                .isNotNull()
                .isNotEmpty()
                .contains(firstBook);
    }

    @Test
    public void shouldCallGetByPrintYearMethodCorrectly() {
        bookServiceMock.getByPrintYear(2000);

        verify(bookRepositoryMock, times(1)).findBooksByPrintYear(2000);

        verifyNoMoreInteractions(bookRepositoryMock);
    }

    @Test
    public void shouldGetByReadBooksCorrectly() {
        bookService.addAll(books);

        val booksFromTheDatabase = bookService.getByRead(false);

        assertThat(booksFromTheDatabase)
                .isNotNull()
                .isNotEmpty()
                .contains(firstBook);
    }

    @Test
    public void shouldCallGetByReadMethodCorrectly() {
        bookServiceMock.getByRead(false);

        verify(bookRepositoryMock, times(1)).findBooksByRead(false);

        verifyNoMoreInteractions(bookRepositoryMock);
    }

    @Test
    public void shouldGetAllBooksCorrectly() {
        bookService.addAll(books);

        val allBooksFromTheDatabase = bookService.getAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(books)
                .contains(firstBook, secondBook);
    }

    @Test
    public void shouldCallGetAllMethodCorrectly() {
        bookServiceMock.getAll();

        verify(bookRepositoryMock, times(1)).findAll();

        verifyNoMoreInteractions(bookRepositoryMock);
    }

    @Test
    public void shouldUpdateBookCorrectly() {
        bookService.add(firstBook);

        firstBook.setPrintYear(9999);

        val updatedBook = bookService.update(firstBook);

        assertThat(updatedBook)
                .isNotNull()
                .isEqualTo(firstBook);
    }

    @Test
    public void shouldCallUpdateMethodCorrectly() {
        bookServiceMock.update(firstBook);

        verify(bookRepositoryMock, times(1)).save(firstBook);

        verifyNoMoreInteractions(bookRepositoryMock);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenUpdateNullBook() {
        val exceptionMessage = "book is marked @NonNull but is null";

        assertThatNullPointerException()
                .isThrownBy(() -> bookService.update(null))
                .withMessage(exceptionMessage);
    }

    @Test
    public void shouldThrowDataIntegrityViolationExceptionWhenUpdateIncorrectEntity() {
        val exceptionMessage = "not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name; nested exception is " +
                "org.hibernate.PropertyValueException: not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name";

        firstBook.setName(null);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> bookService.update(firstBook))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(PropertyValueException.class);
    }

    @Test
    public void shouldUpdateAllBooksCorrectly() {
        bookService.addAll(books);

        books.get(0).setPrintYear(9999);
        books.get(1).setPrintYear(8888);

        val updatedBooks = bookService.updateAll(books);

        assertThat(updatedBooks)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(books);
    }

    @Test
    public void shouldCallUpdateAllMethodCorrectly() {
        bookServiceMock.updateAll(books);

        verify(bookRepositoryMock, times(1)).saveAll(books);

        verifyNoMoreInteractions(bookRepositoryMock);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenUpdateAllNullListOfBooks() {
        val exceptionMessage = "books is marked @NonNull but is null";

        assertThatNullPointerException()
                .isThrownBy(() -> bookService.updateAll(null))
                .withMessage(exceptionMessage);
    }

    @Test
    public void shouldThrowDataIntegrityViolationExceptionWhenUpdateAllListWithIncorrectEntity() {
        val exceptionMessage = "not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name; nested exception is " +
                "org.hibernate.PropertyValueException: not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name";

        books.get(0).setName(null);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> bookService.updateAll(books))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(PropertyValueException.class);
    }

    @Test
    public void shouldRemoveBookCorrectly() {
        bookService.add(firstBook);

        bookService.remove(firstBook);

        val allBooksFromTheDatabase = bookService.getAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void shouldCallRemoveMethodCorrectly() {
        bookServiceMock.remove(firstBook);

        verify(bookRepositoryMock, times(1)).delete(firstBook);

        verifyNoMoreInteractions(bookRepositoryMock);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenRemoveNullBook() {
        val exceptionMessage = "book is marked @NonNull but is null";

        assertThatNullPointerException()
                .isThrownBy(() -> bookService.remove(null))
                .withMessage(exceptionMessage);
    }

    @Test
    public void shouldThrowDataIntegrityViolationExceptionWhenRemoveIncorrectEntity() {
        val exceptionMessage = "not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name; nested exception is " +
                "org.hibernate.PropertyValueException: not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name";

        firstBook.setName(null);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> bookService.remove(firstBook))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(PropertyValueException.class);
    }

    @Test
    public void shouldRemoveAllListOfBooksBooksCorrectly() {
        bookService.addAll(books);

        bookService.removeAll(books);

        val allBooksFromTheDatabase = bookService.getAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void shouldCallRemoveAllListOfBooksMethodCorrectly() {
        bookServiceMock.removeAll(books);

        verify(bookRepositoryMock, times(1)).deleteAll(books);

        verifyNoMoreInteractions(bookRepositoryMock);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenRemoveAllNullListOfBooks() {
        val exceptionMessage = "books is marked @NonNull but is null";

        assertThatNullPointerException()
                .isThrownBy(() -> bookService.removeAll(null))
                .withMessage(exceptionMessage);
    }

    @Test
    public void shouldThrowDataIntegrityViolationExceptionWhenRemoveAllListWithIncorrectEntity() {
        val exceptionMessage = "not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name; nested exception is " +
                "org.hibernate.PropertyValueException: not-null property references a null or transient value : " +
                "com.qthegamep.bookmanager3.entity.Book.name";

        books.get(0).setName(null);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> bookService.removeAll(books))
                .withMessage(exceptionMessage)
                .withCauseExactlyInstanceOf(PropertyValueException.class);
    }

    @Test
    public void shouldRemoveAllBooksCorrectly() {
        bookService.addAll(books);

        bookService.removeAll();

        val allBooksFromTheDatabase = bookService.getAll();

        assertThat(allBooksFromTheDatabase)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void shouldCallRemoveAllMethodCorrectly() {
        bookServiceMock.removeAll();

        verify(bookRepositoryMock, times(1)).deleteAll();

        verifyNoMoreInteractions(bookRepositoryMock);
    }
}
