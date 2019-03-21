package dataAccessObjects;

import java.util.List;

public interface DataAccessObject< T > {
	// creates new entry using DataObject into file
	public abstract void save(T t);
	// deletes associated book entry using the bookId and returns the entry in an dataObject
	public abstract T delete(long id);
	// Not sure about this (maybe pass id and object and compare the object with the entry and if anything is different then update
	public abstract void update(T t);
	// returns DataObject of the requested Id (in the future it can be anything else like name)
	public abstract T find();
	// return DataObject array with all entries in the database
	public abstract List<T> findAll();
}
