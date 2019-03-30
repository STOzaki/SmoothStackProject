package com.lms.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface Dao <T> {
	// creates new entry using DataObject into file
	public abstract int save(T t) throws IOException;
	// deletes associated book entry using the bookId and returns the entry in an dataObject
	public abstract T delete(int id) throws FileNotFoundException, IOException;
	// Not sure about this (maybe pass id and object and compare the object with the entry and if anything is different then update
	public abstract void update(T t) throws FileNotFoundException, IOException;
	// returns DataObject of the requested Id (in the future it can be anything else like name)
	public abstract T find(int id) throws FileNotFoundException, IOException;
	// return DataObject array with all entries in the database
	public abstract List<T> findAll() throws FileNotFoundException, IOException;
	default public String configureString(String input) {
		String returnString = input.replace(",", "`");
		return returnString;
	}
	
	default public String deconfigureString(String input) {
		String returnString = input.replace("`", ",");
		return returnString;
	}

}
