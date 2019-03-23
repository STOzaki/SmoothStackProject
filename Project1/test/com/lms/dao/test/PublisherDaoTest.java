package com.lms.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lms.dao.PublisherDaoImpl;
import com.lms.model.Publisher;


public class PublisherDaoTest {

	private Publisher publisher;
	private PublisherDaoImpl publisherDaoIml;
	private static int ID = 10000;
	
	@BeforeEach
	void init() {
		publisher = new Publisher(ID, "publisher name", "publisher address", "1234567890");
		publisherDaoIml = new PublisherDaoImpl();
	}
	
	@AfterEach
	void tearThis() throws FileNotFoundException, IOException {
		// WARNING maybe something that doesnt call the method we are trying to test
		publisherDaoIml.delete(ID);
	}

	@Test
	public void saveTest() throws FileNotFoundException, IOException {
		int previousCount;
		previousCount = publisherDaoIml.findAll().size();
		publisherDaoIml.save(publisher);
		int currentCount = publisherDaoIml.findAll().size();
		assertTrue(previousCount < currentCount);
	}
	
	@Test
	public void deleteTest() throws FileNotFoundException, IOException {
		publisherDaoIml.save(publisher);
		int previousCount = publisherDaoIml.findAll().size();
		publisherDaoIml.delete(ID);
		int currentCount = publisherDaoIml.findAll().size();
		assertTrue(previousCount > currentCount);
	}
	
	@Test
	public void findPublisherTest() throws FileNotFoundException, IOException {
		publisherDaoIml.save(publisher);
		assertEquals(publisher.getId(), publisherDaoIml.find(ID).getId());
	}
	
	@DisplayName("return null for publisher not found")
	@Test
	public void notFindPublisherTest() throws IOException {
		assertNull(publisherDaoIml.find(ID));
	}
	
	@DisplayName("Will update correct")
	@Test
	public void updatePublisherTest() throws FileNotFoundException, IOException {
		publisherDaoIml.save(publisher);
		Publisher newUpdate = new Publisher(ID, "publisher name2", "publisher address2", "0987654321");
		publisherDaoIml.update(newUpdate);
		Publisher newPublisher = publisherDaoIml.find(ID);
		assertEquals(newPublisher.getId(), newUpdate.getId());
		assertTrue(newPublisher.getPublisherName().equals(newUpdate.getPublisherName()));
		assertTrue(newPublisher.getPublisherAddress().equals(newUpdate.getPublisherAddress()));
		assertTrue(newPublisher.getPublisherPhone().equals(newUpdate.getPublisherPhone()));
	}
}
