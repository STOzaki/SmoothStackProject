package com.lms.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lms.dao.AuthorDaoImpl;
import com.lms.model.Author;

import com.lms.service.AuthorService;
import com.lms.service.PublisherService;
import com.lms.service.BookService;;

public class AuthorServiceTest {
	private String authorName = "Tester T. Testing";
	
	private static String publisherName = "Tester T. Testing";
	private static String publisherAddress = "123 E Franklin Ave, South Keep IL, 56498";
	private static String publisherPhone = "1234567890";
	
	private static String title = "Test Title";
	
	private AuthorDaoImpl authorDao;
	int newAuthorId;
	int newPublisherId;
	int newBookId;
	
	@BeforeEach
	void init() throws FileNotFoundException, IOException {
		authorDao = new AuthorDaoImpl();
		Author initAuthor = new Author(0, authorName);
		newAuthorId = authorDao.save(initAuthor);
	}
	
	@AfterEach
	void tearThis() throws FileNotFoundException, IOException {
		// WARNING maybe something that doesn't call the method we are trying to test
		authorDao.delete(newAuthorId);
	}
	
	@Test
	public void saveAuthorTest() throws FileNotFoundException, IOException {
		authorDao.delete(newAuthorId);
		
		int previousSize = authorDao.findAll().size();
		newAuthorId = AuthorService.saveAuthor(authorName);
		int currentSize = authorDao.findAll().size();
		assertTrue(previousSize < currentSize);
		assertTrue(authorDao.find(newAuthorId).getAuthorName().equals(authorName));
	}
	
	@DisplayName("Deleting an Author will also delete the associated book(s)")
	@Test
	public void deleteAuthorTest() throws FileNotFoundException, IOException {
		newPublisherId = PublisherService.savePublisher(publisherName, publisherAddress, publisherPhone);
		int[] primaryIds = BookService.saveBook(title, newAuthorId, newPublisherId);
		
		int previousBookSize = BookService.findAllBooks().size();
		int previousAuthorSize = authorDao.findAll().size();
		
		AuthorService.deleteAuthor(newAuthorId);
		
		int currentBookSize = BookService.findAllBooks().size();
		int currentAuthorSize = authorDao.findAll().size();
		assertTrue(previousAuthorSize > currentAuthorSize);
		assertTrue(previousBookSize > currentBookSize);
		assertNull(authorDao.find(newAuthorId));
		
		// clean up
		PublisherService.deletePublisher(primaryIds[2]);
		BookService.deleteBook(primaryIds[0]);
		newAuthorId = primaryIds[1];
	}
	
	@DisplayName("Will update correct")
	@Test
	public void updateAuthorTest() throws FileNotFoundException, IOException {
		Author author = new Author(newAuthorId, "test 2");
		AuthorService.updateAuthor(author);
		Author updatedAuthor = authorDao.find(newAuthorId);
		assertEquals(updatedAuthor.getId(), author.getId());
		assertTrue(updatedAuthor.getAuthorName().equals(author.getAuthorName()));
	}
	
	@Test
	public void findAuthorTest() throws FileNotFoundException, IOException {
		Author foundAuthor = AuthorService.findAuthor(newAuthorId);
		assertTrue(authorName.equals(foundAuthor.getAuthorName()));
	}
	
	@DisplayName("return null for author not found")
	@Test
	public void notFindAuthorTest() throws IOException {
		authorDao.delete(newAuthorId);
		assertNull(AuthorService.findAuthor(newAuthorId));
		newAuthorId = authorDao.findAll().size();
	}
}
