package com.lms.model;

public final class Book extends POJO {
	private String title;
	private int authorId;
	private int publisherId;

	public Book(int id, String title, int authorId, int publisherId) {
		super(id);
		this.title = title;
		this.authorId = authorId;
		this.publisherId = publisherId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(int publisherId) {
		this.publisherId = publisherId;
	}

	@Override
	public String toString() {
		return "id: " + this.getId() + ", Book Name: " + this.title + ", AuthorId: " + this.authorId + ", PublisherId: " + this.publisherId;
	}

}
