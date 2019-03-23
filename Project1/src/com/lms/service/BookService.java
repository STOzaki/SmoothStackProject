package com.lms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lms.dao.AuthorDaoImpl;
import com.lms.dao.BookDaoImpl;
import com.lms.dao.PublisherDaoImpl;
import com.lms.model.Author;
import com.lms.model.Book;
import com.lms.model.Publisher;

public class BookService {
	
	public static void saveBook(String title, int authorId, int publisherId) {
		PublisherDaoImpl publisherDao = new PublisherDaoImpl();
		AuthorDaoImpl authorDao = new AuthorDaoImpl();
		Publisher publisher = null;
		Author author = null;
		try {
			publisher = publisherDao.find(publisherId);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			author = authorDao.find(authorId);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		if(publisher != null && author != null) {
			BookDaoImpl bookDao = new BookDaoImpl();
			Book book = null;
			try {
				book = new Book(bookDao.findAll().size(), title, authorId, publisherId);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bookDao.save(book);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Book deleteBook(int id) {
		Book deletedBook = null;
		try {
			deletedBook = new BookDaoImpl().delete(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return deletedBook;
	}
	
	public static boolean updateBook(Book newBook) {
		BookDaoImpl bookDao = new BookDaoImpl();
		Book oldBook = null;
		try {
			oldBook = bookDao.find(newBook.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			if(oldBook.getAuthorId() != newBook.getAuthorId()) {
				Author newAuthor = new AuthorDaoImpl().find(newBook.getAuthorId());
				if(newAuthor == null) {
					return false;
				}
			}
			
			if(oldBook.getPublisherId() != newBook.getPublisherId()) {
				Publisher newPublisher = new PublisherDaoImpl().find(newBook.getPublisherId());
				if(newPublisher == null) {
					return false;
				}
			}
			bookDao.update(newBook);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static Book findBook(int id) {
		Book book = null;
		try {
			book = new BookDaoImpl().find(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return book;
	}
	
	public static List<Book> findAllBooks() {
		List<Book> listBooks = new ArrayList<Book>();
		try {
			listBooks = new BookDaoImpl().findAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listBooks;
	}

}
