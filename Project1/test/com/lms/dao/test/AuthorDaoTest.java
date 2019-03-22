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

import com.lms.dao.AuthorDaoImpl;

import com.lms.model.Author;

public class AuthorDaoTest {
	
	private Author author;
	private AuthorDaoImpl authorDao;
	private static int ID = 10000;
	
	@BeforeEach
	void init() {
		author = new Author(ID, "test 1");
		authorDao = new AuthorDaoImpl();
	}
	
	@AfterEach
	void tearThis() throws FileNotFoundException, IOException {
		// WARNING maybe something that doesnt call the method we are trying to test
		authorDao.delete(ID);
	}

	@Test
	public void saveTest() throws FileNotFoundException, IOException {
		int previousCount;
		previousCount = authorDao.findAll().size();
		authorDao.save(author);
		int currentCount = authorDao.findAll().size();
		assertTrue(previousCount < currentCount);
	}
	
	@Test
	public void deleteTest() throws FileNotFoundException, IOException {
		authorDao.save(author);
		int previousCount = authorDao.findAll().size();
		authorDao.delete(ID);
		int currentCount = authorDao.findAll().size();
		assertTrue(previousCount > currentCount);
	}
	
	@Test
	public void findAuthorTest() throws FileNotFoundException, IOException {
		authorDao.save(author);
		assertEquals(author.getId(), authorDao.find(ID).getId());
	}
	
	@DisplayName("return null for author not found")
	@Test
	public void notFindAuthorTest() throws IOException {
		assertNull(authorDao.find(ID));
	}
	
	@Test
	public void updateAuthorTest() throws FileNotFoundException, IOException {
		authorDao.save(author);
		Author newUpdate = new Author(ID, "test 2");
		authorDao.update(newUpdate);
		assertTrue(author.getAuthorName() != newUpdate.getAuthorName());
	}
}
