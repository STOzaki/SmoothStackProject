package com.lms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lms.dao.AuthorDaoImpl;
import com.lms.dao.BookDaoImpl;

import com.lms.model.Author;
import com.lms.model.Book;

public class AuthorService {
	// if something goes wrong, it will return -1
	public static int saveAuthor(String authorName) {
		AuthorDaoImpl authorDaoImpl = new AuthorDaoImpl();
		Author newAuthor = new Author(0, authorName);
		int primary = -1;
		
		try {
			primary = authorDaoImpl.save(newAuthor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return primary;
	}
	
	public static Author deleteAuthor(int id) {
		AuthorDaoImpl authorDao = new AuthorDaoImpl();
		BookDaoImpl bookDao = new BookDaoImpl();
		List<Book> bookList = new ArrayList<Book>();
		
		try {
			bookList = bookDao.findAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Integer> dependentBooksId = new ArrayList<Integer>();
		for(Book book : bookList) {
			if(book.getAuthorId() == id) {
				dependentBooksId.add(book.getId());
			}
		}
		
		// deletes dependent books first
		for(int bookId : dependentBooksId) {
			try {
				bookDao.delete(bookId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// now delete Author
		Author deletedAuthor= null;
		try {
			deletedAuthor = authorDao.delete(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return deletedAuthor;
	}
	
	public static boolean updateAuthor(Author author) {
		boolean updateComplete = false;
		
		Author oldAuthor = AuthorService.findAuthor(author.getId());
		if(oldAuthor != null) {
			try {
				new AuthorDaoImpl().update(author);
				updateComplete = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return updateComplete;
	}
	
	public static Author findAuthor(int id) {
		Author author = null;
		try {
			author = new AuthorDaoImpl().find(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return author;
	}
	
	public static List<Author> findAllAuthors() {
		List<Author> authorList = new ArrayList<Author>();
		try {
			authorList = new AuthorDaoImpl().findAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return authorList;
	}
}
