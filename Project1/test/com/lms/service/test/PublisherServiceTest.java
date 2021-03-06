package com.lms.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lms.model.Publisher;

import com.lms.dao.PublisherDaoImpl;
import com.lms.service.AuthorService;
import com.lms.service.BookService;
import com.lms.service.PublisherService;

public class PublisherServiceTest {

	private String publisherName = "Tester T. Testing";
	private String publisherAddress = "123 E Franklin Ave, South Keep IL, 56498";
	private String publisherPhone = "1234567890";
	
	private String authorName = "Tester T. Testing";

	private static String title = "Test Title";
	
	private PublisherDaoImpl publisherDao;
	int newPublisherId;
	int newAuthorId;
	int newBookId;
	
	@BeforeEach
	void init() throws FileNotFoundException, IOException {
		publisherDao = new PublisherDaoImpl();
		Publisher newpublisher = new Publisher(0, publisherName, publisherAddress, publisherPhone);
		newPublisherId = publisherDao.save(newpublisher);
	}
	
	@AfterEach
	void tearThis() throws FileNotFoundException, IOException {
		// WARNING maybe something that doesn't call the method we are trying to test
		publisherDao.delete(newPublisherId);
	}
	
	@Test
	public void savePublisherTest() throws FileNotFoundException, IOException {
		publisherDao.delete(newPublisherId);
		
		int previousSize = publisherDao.findAll().size();
		newPublisherId = PublisherService.savePublisher(publisherName, publisherAddress, publisherPhone);
		// newPublisherID can also be thought as previousSize
		int currentSize = publisherDao.findAll().size();
		assertTrue(previousSize < currentSize);
		assertTrue(publisherDao.find(newPublisherId).getPublisherName().equals(publisherName));
		assertTrue(publisherDao.find(newPublisherId).getPublisherAddress().equals(publisherAddress));
		assertTrue(publisherDao.find(newPublisherId).getPublisherPhone().equals(publisherPhone));
	}
	
	@DisplayName("Deleting an Publisher will also delete the associated book(s)")
	@Test
	public void deletePublisherTest() throws FileNotFoundException, IOException {
		newAuthorId = AuthorService.saveAuthor(authorName);
		int[] primaryIds = BookService.saveBook(title, newAuthorId, newPublisherId);
		
		int previousBookSize = BookService.findAllBooks().size();
		int previousPublisherSize = publisherDao.findAll().size();
		
		PublisherService.deletePublisher(newPublisherId);
		
		int currentBookSize = BookService.findAllBooks().size();
		int currentPublisherSize = publisherDao.findAll().size();
		assertTrue(previousBookSize > currentBookSize);
		assertTrue(previousPublisherSize > currentPublisherSize);
		assertNull(publisherDao.find(newPublisherId));
		
		// clean up
		AuthorService.deleteAuthor(primaryIds[1]);
		BookService.deleteBook(primaryIds[0]);
		newPublisherId = primaryIds[2];
	}
	
	@DisplayName("Will update correct")
	@Test
	public void updatePublisherTest() throws FileNotFoundException, IOException {
		Publisher publisher = new Publisher(newPublisherId, "publisher the 2nd", "456 S Washington ct, Valley Way, IL 12345", "9876543210");
		boolean updateCompleted = PublisherService.updatePublisher(publisher);
		Publisher updatedPublisher = publisherDao.find(newPublisherId);
		
		assertTrue(updateCompleted);
		assertEquals(updatedPublisher.getId(), publisher.getId());
		assertEquals(updatedPublisher.getPublisherName(), publisher.getPublisherName());
		assertEquals(updatedPublisher.getPublisherAddress(), publisher.getPublisherAddress());
		assertEquals(updatedPublisher.getPublisherPhone(), publisher.getPublisherPhone());
	}
	
	@DisplayName("Does not update correctly because there is no such Author Id")
	@Test
	public void updateWithNoPublisherIdTest() throws FileNotFoundException, IOException {
		int nonExistingPublisherId = Integer.MAX_VALUE;
		
		Publisher publisher = new Publisher(nonExistingPublisherId, "publisher the 2nd", "456 S Washington ct, Valley Way, IL 12345", "9876543210");
		boolean updateCompleted = PublisherService.updatePublisher(publisher);
		Publisher updatedPublisher = publisherDao.find(nonExistingPublisherId);

		assertFalse(updateCompleted);
		assertNull(updatedPublisher);
	}
	
	@Test
	public void findPublisherTest() throws FileNotFoundException, IOException {
		Publisher foundPublisher = PublisherService.findPublisher(newPublisherId);
		assertTrue(publisherName.equals(foundPublisher.getPublisherName()));
		assertTrue(publisherAddress.equals(foundPublisher.getPublisherAddress()));
		assertTrue(publisherPhone.equals(foundPublisher.getPublisherPhone()));
	}
	
	@DisplayName("return null for publisher not found")
	@Test
	public void notFindPublisherTest() throws IOException {
		publisherDao.delete(newPublisherId);
		assertNull(PublisherService.findPublisher(newPublisherId));
		newPublisherId = publisherDao.findAll().size();
	}
	
	
}
