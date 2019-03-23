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

import com.lms.dao.BookDaoImpl;
import com.lms.model.Book;

public class BookDaoTest {

	private Book book;
	private BookDaoImpl bookDaoIml;
	private static int ID = 10000;
	private static int AUTHORID = 111111;
	private static int PUBLISHERID = 222222;
	
	@BeforeEach
	void init() {
		book = new Book(ID, "publisher name", AUTHORID, PUBLISHERID);
		bookDaoIml = new BookDaoImpl();
	}
	
	@AfterEach
	void tearThis() throws FileNotFoundException, IOException {
		// WARNING maybe something that doesnt call the method we are trying to test
		bookDaoIml.delete(ID);
	}
	
	@Test
	public void saveBookTest() throws FileNotFoundException, IOException {
		int previousCount;
		previousCount = bookDaoIml.findAll().size();
		bookDaoIml.save(book);
		int currentCount = bookDaoIml.findAll().size();
		assertTrue(previousCount < currentCount);
	}
	
	@Test
	public void deleteBookTest() throws FileNotFoundException, IOException {
		bookDaoIml.save(book);
		int previousCount = bookDaoIml.findAll().size();
		bookDaoIml.delete(ID);
		int currentCount = bookDaoIml.findAll().size();
		assertTrue(previousCount > currentCount);
	}
	
	@Test
	public void findBookTest() throws FileNotFoundException, IOException {
		bookDaoIml.save(book);
		assertEquals(book.getId(), bookDaoIml.find(ID).getId());
	}
	
	@DisplayName("return null for book not found")
	@Test
	public void notFindBookTest() throws IOException {
		assertNull(bookDaoIml.find(ID));
	}
	
	@Test
	public void updateBookTest() throws FileNotFoundException, IOException {
		bookDaoIml.save(book);
		Book newUpdate = new Book(ID, "publisher name2", 123451, 1455123);
		bookDaoIml.update(newUpdate);
		Book newBook = bookDaoIml.find(ID);
		assertEquals(newBook.getId(), newUpdate.getId());
		assertTrue(newBook.getTitle().equals(newUpdate.getTitle()));
		assertEquals(newBook.getAuthorId(), (newUpdate.getAuthorId()));
		assertEquals(newBook.getPublisherId(), newBook.getPublisherId());
	}
}
