package com.lms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lms.dao.BookDaoImpl;
import com.lms.dao.PublisherDaoImpl;

import com.lms.model.Book;
import com.lms.model.Publisher;

public class PublisherService {
	
	public static void savePublisher(String publisherName, String publisherAddress, String publisherPhone) {
		PublisherDaoImpl publisherDao = new PublisherDaoImpl();
		List<Publisher> publisherList = new ArrayList<Publisher>();
		
		try {
			publisherList = publisherDao.findAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Publisher newAuthor = new Publisher(publisherList.size(), publisherName, publisherAddress, publisherPhone);
		
		try {
			publisherDao.save(newAuthor);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Publisher deletePublisher(int id) {

		PublisherDaoImpl publisherDao = new PublisherDaoImpl();
		BookDaoImpl bookDao = new BookDaoImpl();
		List<Book> bookList = new ArrayList<Book>();
		
		try {
			bookList = bookDao.findAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Integer> dependentBooksId = new ArrayList<Integer>();
		for(Book book : bookList) {
			if(book.getPublisherId() == id) {
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
		
		// now delete Publisher
		Publisher deletedPublisher= null;
		try {
			deletedPublisher = publisherDao.delete(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return deletedPublisher;
	}
	
	public static void updatePublisher(Publisher author) {
		try {
			new PublisherDaoImpl().update(author);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Publisher findPublisher(int id) {
		Publisher publisher = null;
		try {
			publisher = new PublisherDaoImpl().find(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return publisher;
	}
	
	public static List<Publisher> findAllPublisher() {
		List<Publisher> listPublisher = new ArrayList<Publisher>();
		try {
			listPublisher = new PublisherDaoImpl().findAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listPublisher;
	}
	
	
}
