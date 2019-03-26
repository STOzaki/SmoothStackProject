package com.lms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lms.dao.BookDaoImpl;
import com.lms.model.Author;
import com.lms.model.Book;
import com.lms.model.Publisher;

public class BookService {
	private static final String emptyCell = "N/A";
	
	public static int[] saveBook(String title, int authorId, int publisherId) {
		Publisher publisher = null;
		Author author = null;
		publisher = PublisherService.findPublisher(publisherId);
		author = AuthorService.findAuthor(authorId);
		
		int[] primaryIds = new int[3];
		for(int i = 0; i < primaryIds.length;i++)
			primaryIds[i] = -1;
		
		BookDaoImpl bookDao = new BookDaoImpl();
		Book book = null;
		
		if(publisher == null) {
			System.out.println("WARNING: that publisher Id does not exist, so an empty one was created");
			publisherId = PublisherService.savePublisher(emptyCell, emptyCell, emptyCell);
		}
		
		if(author == null) {
			System.out.println("WARNING: that author Id does not exist, so an empty one was created");
			authorId = AuthorService.saveAuthor(emptyCell);
		}

		book = new Book(0, title, authorId, publisherId);
		try {
			primaryIds[0] = bookDao.save(book);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		primaryIds[1] = authorId;
		primaryIds[2] = publisherId;
		return primaryIds;
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
	
	// in int[] [0] is whether it updated with no errors, [1] is whether AuthorId exists, [2] is whether 
	// publisherId exists, [3] is if the bookId does not exit
	public static boolean[] updateBook(Book newBook) {
		boolean returnValues[] = new boolean[4];
		
		for(int i = 0; i < returnValues.length; i++)
			returnValues[i] = true;
		
		BookDaoImpl bookDao = new BookDaoImpl();
		Book oldBook = null;
		oldBook = BookService.findBook(newBook.getId());
		
		if(oldBook != null) {
			if(oldBook.getAuthorId() != newBook.getAuthorId()) {
				Author newAuthor = AuthorService.findAuthor(newBook.getAuthorId());
				if(newAuthor == null) {
					returnValues[1] = false;
				}
			}
			
			if(oldBook.getPublisherId() != newBook.getPublisherId()) {
				Publisher newPublisher = PublisherService.findPublisher(newBook.getPublisherId());
				if(newPublisher == null) {
					returnValues[2] = false;
				}
			}
			
			if(returnValues[1] && returnValues[2]) {
				try {
					bookDao.update(newBook);
					returnValues[0] = true;
				} catch (IOException e) {
					// WARNING: it might silently die! because it does not return anything
					e.printStackTrace();
					returnValues[0] = false;
				}
			} else {
				returnValues[0] = false;
			}
			
		} else {
			returnValues[0] = false;
			returnValues[1] = false;
			returnValues[2] = false;
			returnValues[3] = false;
		}
		return returnValues;
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
