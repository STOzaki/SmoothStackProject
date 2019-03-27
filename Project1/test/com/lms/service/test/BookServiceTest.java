package com.lms.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lms.dao.BookDaoImpl;
import com.lms.model.Book;
import com.lms.service.AuthorService;
import com.lms.service.BookService;
import com.lms.service.PublisherService;

public class BookServiceTest {
	
	private String title = "Tester T. Testing";
	private int authorId = -1;
	private int publisherId = -1;
	private BookDaoImpl bookDao;
	int newBookId;

	private String publisherName = "Tester T. Testing";
	private String publisherAddress = "123 E Franklin Ave, South Keep IL, 56498";
	private String publisherPhone = "1234567890";
	

	private String authorName = "Tester T. Testing";

	int[] listOfPrimaryIds;

	@BeforeEach
	void init() throws FileNotFoundException, IOException {
		authorId = AuthorService.saveAuthor(authorName);
		publisherId = PublisherService.savePublisher(publisherName, publisherAddress, publisherPhone);
		bookDao = new BookDaoImpl();
		Book newbook = new Book(0, title, authorId, publisherId);
		newBookId = bookDao.save(newbook);
		BookService.saveBook(title, authorId, publisherId);
	}
	
	@AfterEach
	void tearThis() throws FileNotFoundException, IOException {
		// WARNING maybe something that doesn't call the method we are trying to test
		bookDao.delete(newBookId);
		AuthorService.deleteAuthor(authorId);
		PublisherService.deletePublisher(publisherId);
	}

	
	@Test
	public void saveBookTest() throws FileNotFoundException, IOException {
		bookDao.delete(newBookId);
		
		int previousSize = bookDao.findAll().size();
		listOfPrimaryIds = BookService.saveBook(title, authorId, publisherId);
		newBookId = listOfPrimaryIds[0];
		int currentSize = bookDao.findAll().size();
		
		assertNotNull(AuthorService.findAuthor(authorId));
		assertNotNull(PublisherService.findPublisher(publisherId));
		assertEquals(listOfPrimaryIds[1], authorId);
		assertEquals(listOfPrimaryIds[2], publisherId);
		assertTrue(previousSize < currentSize);
		assertTrue(bookDao.find(newBookId).getTitle().equals(title));
	}

	@DisplayName("Book does not save if Author does not exist")
	@Test
	public void saveBookWithoutAuthor() throws FileNotFoundException, IOException {
		bookDao.delete(newBookId);

		int nonExistingAuthorId = Integer.MAX_VALUE;
		listOfPrimaryIds = BookService.saveBook(title, nonExistingAuthorId, publisherId);
		Book nonExistingBook = BookService.findBook(listOfPrimaryIds[0]);
		
		assertNull(nonExistingBook);
		assertEquals(listOfPrimaryIds[1], -1);
		assertEquals(listOfPrimaryIds[2], publisherId);
	}
	
	@DisplayName("Book does not save if Publisher does not exist")
	@Test
	public void saveBookWithoutPublisher() throws FileNotFoundException, IOException {
		bookDao.delete(newBookId);
		
		int nonExistingPublisherId = Integer.MAX_VALUE;
		listOfPrimaryIds = BookService.saveBook(title, authorId, nonExistingPublisherId);
		Book nonExistingBook = BookService.findBook(listOfPrimaryIds[0]);

		assertNull(nonExistingBook);
		assertEquals(listOfPrimaryIds[1], authorId);
		assertEquals(listOfPrimaryIds[2], -1);
		
	}

	
	@Test
	public void deleteBookTest() throws FileNotFoundException, IOException {
		int previousSize = bookDao.findAll().size();
		BookService.deleteBook(newBookId);
		int currentSize = bookDao.findAll().size();
		assertTrue(previousSize > currentSize);
		assertNull(bookDao.find(newBookId));
	}
	
	@DisplayName("Update correctly")
	@Test
	public void updateBookTest() throws FileNotFoundException, IOException {
		Book book = new Book(newBookId, "The Chronicles of Narnia", authorId, publisherId);
		boolean[] booleanList = BookService.updateBook(book);
		Book updatedBook = bookDao.find(newBookId);
		
		assertTrue(booleanList[0] && booleanList[1] && booleanList[2] && booleanList[3]);
		
		assertEquals(updatedBook.getId(), book.getId());
		assertTrue(updatedBook.getTitle().equals(book.getTitle()));
		assertEquals(updatedBook.getAuthorId(), book.getAuthorId());
		assertEquals(updatedBook.getPublisherId(), book.getPublisherId());
	}
	
	@DisplayName("Return false and update should fail without an Author")
	@Test
	public void updateBookWithNoAuthor() throws FileNotFoundException, IOException {
		Book book = new Book(newBookId, "The Chronicles of Narnia", Integer.MAX_VALUE, publisherId);
		boolean[] booleanList = BookService.updateBook(book);
		
		assertFalse(booleanList[0]);
		assertFalse(booleanList[1]);
		assertTrue(booleanList[2]);
		assertTrue(booleanList[3]);
	}
	
	@DisplayName("Return false and update should fail without an Publisher")
	@Test
	public void updateBookWithNoPublisher() throws FileNotFoundException, IOException {
		Book book = new Book(newBookId, "The Chronicles of Narnia", authorId, Integer.MAX_VALUE);
		boolean[] booleanList = BookService.updateBook(book);
		
		assertFalse(booleanList[0]);
		assertTrue(booleanList[1]);
		assertFalse(booleanList[2]);
		assertTrue(booleanList[3]);
	}
	
	@DisplayName("Returns all false if the id is incorrect!")
	@Test
	public void updateBookWithWrongBookId() throws FileNotFoundException, IOException {
		Book book = new Book(Integer.MAX_VALUE, "The Chronicles of Narnia", authorId, publisherId);
		boolean[] booleanList = BookService.updateBook(book);
		
		assertFalse(booleanList[0] || booleanList[1] || booleanList[2] || booleanList[3]);
	}

	@Test
	public void findBookTest() throws FileNotFoundException, IOException {
		Book foundBook = BookService.findBook(newBookId);
		assertTrue(title.equals(foundBook.getTitle()));
		assertEquals(authorId, foundBook.getAuthorId());
		assertEquals(publisherId, foundBook.getPublisherId());
	}
	
	@DisplayName("return null for book not found")
	@Test
	public void notFindBookTest() throws IOException {
		bookDao.delete(newBookId);
		assertNull(BookService.findBook(newBookId));
		newBookId = bookDao.findAll().size();
	}
	
	
}
