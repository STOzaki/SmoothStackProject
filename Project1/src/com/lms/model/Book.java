package com.lms.model;

public final class Book extends POJO {
	private String title;
	private Author author;
	private Publisher publisher;

	public Book(int id, String title, Author author, Publisher publisher) {
		super(id);
		this.title = title;
		this.author = author;
		this.publisher = publisher;
	}

	public String getTitle() {
		return title;
	}

	public Author getAuthor() {
		return author;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	

}
