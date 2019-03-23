package com.lms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lms.dao.AuthorDaoImpl;
import com.lms.dao.BookDaoImpl;

import com.lms.model.Author;
import com.lms.model.Book;

public class AuthorService {
	public static void saveAuthor(String authorName) {
		AuthorDaoImpl authorDao = new AuthorDaoImpl();
		List<Author> authorList = new ArrayList<Author>();
		
		try {
			authorList = authorDao.findAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Author newAuthor = new Author(authorList.size(), authorName);
		
		try {
			authorDao.save(newAuthor);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	public static void updateAuthor(Author author) {
		try {
			new AuthorDaoImpl().update(author);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
