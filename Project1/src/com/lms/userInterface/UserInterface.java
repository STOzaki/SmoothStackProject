package com.lms.userInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.lms.model.Author;
import com.lms.model.Book;
import com.lms.model.Publisher;
import com.lms.service.AuthorService;
import com.lms.service.BookService;
import com.lms.service.PublisherService;

public class UserInterface {
	private static String[] dataBases = {"Author", "Book", "Publisher"};
	private static String[] authorOptions = {"Author Name"};
	private static String[] bookOptions = {"Title", "Author Id", "Publisher Id"};
	private static String[] publisherOptions = {"Publisher Name", "Publisher Address", "Publisher Phone Number"};
	private static String[] operationOptions = {"Save", "Delete", "Update", "Find", "Find All"};

	
	private static int whichOperation(Scanner userInput) {
		System.out.println("What would you like to do with the database?");
		iterateOptions(operationOptions);
		int choice = validId(userInput);
		return choice;
	}
	
	private static List<String> infoGathering(Scanner userInput, String[] list) {
		List<String> returnInfo = new ArrayList<>();
		for(int i = 0; i < list.length; i++) {
			System.out.println("What is the " + list[i] + "?");
			String response = userInput.nextLine();
			returnInfo.add(response);
		}
		return returnInfo;
	}
	
	private static void iterateOptions(String[] list) {
		for(int i = 0; i < list.length; i++) {
			System.out.println(i + 1 + ". " + list[i]);
		}
	}
	
	private static int promptForId(Scanner userInput, String method) {
		System.out.println("What is the Id of the entry you are trying to " + method + "?");
		int choice = validId(userInput);
		return choice;
	}
	
	private static List<String> updateInfo(Scanner userInput, String[] list) {
		List<String> returnInfo = new ArrayList<>();
		for(int i = 0; i < list.length; i++) {
			System.out.println("What would you like " + list[i] + " to be updated to? (hit enter to skip)");
			returnInfo.add(userInput.nextLine());
		}
		return returnInfo;
	}
	
	private static int validId(Scanner userInput) {
		boolean validId = false;
		int yourChoice = -1;
		while(!validId) {
			try {
				yourChoice = Integer.parseInt(userInput.nextLine());
				validId = true;
			} catch(NumberFormatException e) {
				System.out.println("That is not a number.");
			}
		}
		return yourChoice;
	}
	
	private static void displayBookAndAssociation(Book foundBook) {
		Author associatedAuthor = AuthorService.findAuthor(foundBook.getAuthorId());
		Publisher associatedPublisher = PublisherService.findPublisher(foundBook.getPublisherId());
		System.out.println("Book -> " + foundBook.toString());
		System.out.println("Associated Author -> " + associatedAuthor.toString());
		System.out.println("Associated Publisher -> " + associatedPublisher.toString());
	}
	
	private static void displayPublisherWithBooks(Publisher publisher) {
		System.out.println("Publisher -> " + publisher.toString());
		System.out.println();
		System.out.println("List of Books from this publisher:");
		List<Book> listOfBooks = BookService.findAllBooks();
		listOfBooks.stream()
		.filter(b -> b.getPublisherId() == publisher.getId())
		.forEach(b -> System.out.println(b.toString()));
		System.out.println("<----------------------------------------------------------------------->");
		System.out.println();
	}
	
	private static void displayAuthorWithBooks(Author author) {
		System.out.println("Author -> " + author.toString());
		System.out.println();
		System.out.println("List of Books from this author:");
		List<Book> listOfBooks = BookService.findAllBooks();
		listOfBooks.stream()
		.filter(b -> b.getAuthorId() == author.getId())
		.forEach(b -> System.out.println(b.toString()));
		System.out.println("<----------------------------------------------------------------------->");
		System.out.println();
	}
	
	
	private static void authorMenu(Scanner userInput) {

		List<String> info = new ArrayList<>();
		int operation = -1;
		// Author database
		operation = whichOperation(userInput);
		switch (operation) {
		case 1:
			// Author save
			info = infoGathering(userInput, authorOptions);
			System.out.println("This is the AuthorID associated with your entry: " + AuthorService.saveAuthor(info.get(0)));
			break;
		case 2:
			// Author delete
			Author deletedEntry = AuthorService.deleteAuthor(promptForId(userInput, "delete"));
			if(deletedEntry == null) {
				System.out.println("Unfortunatly, we failed to delete that entry");
			} else {
				System.out.println("Successfully deleted entry: (Info about deleted entry)");
				System.out.println(deletedEntry.toString());
			}
			break;
		case 3:
			// Author update
			System.out.println("What is the Author Id that you would like to update?");
			int authorId = validId(userInput);
			List<String> updateList = updateInfo(userInput, authorOptions);
			Author updatedAuthor = new Author(authorId, updateList.get(0));
			boolean authorUpdateCompleted = AuthorService.updateAuthor(updatedAuthor);
			if(authorUpdateCompleted) {
				System.out.println("Completed Author Update Successfully");
			} else {
				System.out.println("ERROR: Either the Author Id was incorrect or an Error occurred");
			}
			break;
		case 4:
			// Author find
			Author foundAuthor = AuthorService.findAuthor(promptForId(userInput, "find"));
			if(foundAuthor == null) {
				System.out.println("Could not locate that entry");
			} else {
				displayAuthorWithBooks(foundAuthor);
			}
			break;
		case 5:
			// Author findAll
			List<Author> allAuthors = AuthorService.findAllAuthors();
			allAuthors.stream().forEach(a -> displayAuthorWithBooks(a));
			break;
		default:
			System.out.println("No such operation.");
		}
	}
	
	private static void bookMenu(Scanner userInput) {

		List<String> info = new ArrayList<>();
		int operation = -1;
		// Book database
		operation = whichOperation(userInput);
		switch (operation) {
		case 1:
			// Book save
			info = infoGathering(userInput, bookOptions);
			boolean validInputSave = false;
			int newAuthorId = -1;
			int newPublisherId = -1;
			while(!validInputSave) {
				try {
					newAuthorId = Integer.parseInt(info.get(1));
					newPublisherId = Integer.parseInt(info.get(2));
					validInputSave = true;
				} catch (NumberFormatException e) {
					newAuthorId = -1;
					newPublisherId = -1;
					System.out.println("I am sorry but AuthorId and/or the PublisherId is not a number");
					info = infoGathering(userInput, bookOptions);
				}
			}
			int[] ids = BookService.saveBook(info.get(0), newAuthorId, newPublisherId);
			
			if(ids[0] == -1) {
				System.out.println("Error: could not save your book.");
			}
			if(ids[1] == -1) {
				System.out.println("I am sorry but that Author Id does not exist.");
			}
			
			if(ids[2] == -1) {
				System.out.println("I am sorry but that Publisher Id does not exist.");
			}
			
			if(ids[0] != -1 && ids[1] != -1 && ids[2] != -1) {
				System.out.println("This is the BookID associated with your entry: " + ids[0]);
				System.out.println("AuthorId: " + ids[1]);
				System.out.println("PublisherId: " + ids[2]);
			}
			break;
		case 2:
			// Book delete
			Book deletedEntry = BookService.deleteBook(promptForId(userInput, "delete"));
			if(deletedEntry == null) {
				System.out.println("Unfortunatly, we failed to delete that entry");
			} else {
				System.out.println("Successfully deleted entry: (Info about deleted entry)");
				System.out.println(deletedEntry.toString());
			}
			break;
		case 3:
			// Book update
			System.out.println("What is the Book Id that you would like to update?");
			int bookId = validId(userInput);

			boolean validInputUpdateAuthor = false;
			boolean validInputUpdatePublisher = false;
			int authorId = -1;
			int publisherId = -1;
			List<String> updateList = null;
			
			while(!validInputUpdateAuthor && !validInputUpdatePublisher) {
				updateList = updateInfo(userInput, bookOptions);
				if(!updateList.get(1).isEmpty()) {
					try {
						authorId = Integer.parseInt(updateList.get(1));
						validInputUpdateAuthor = true;
					} catch (NumberFormatException e) {
						System.out.println("I am sorry that AuthorId is not a number.");
					}
				}
				
				if(!updateList.get(2).isEmpty()) {
					try {
						publisherId = Integer.parseInt(updateList.get(2));
						validInputUpdatePublisher = true;
					} catch (NumberFormatException e) {
						System.out.println("I am sorry that PublisherId is not a number.");
					}
				}
			}
			
			Book updatedAuthor = new Book(bookId, updateList.get(0), authorId, publisherId);
			boolean[] results = BookService.updateBook(updatedAuthor);
			if(!results[3]) {
				System.out.println("BookId does not exist!");
			} else {
				if(!results[0]) {
					System.out.println("Book failed to update.");
				}
				
				if(!results[1]) {
					System.out.println("The provided Author Id does not exist.");
				}
				
				if(!results[2]) {
					System.out.println("The provided Publisher Id does not exist");
				}
				
				if(results[0] && results[1] && results[2] && results[3]) {
					System.out.println("Book updated successfully");
				}
			}
			break;
		case 4:
			// Book find
			Book foundBook = BookService.findBook(promptForId(userInput, "find"));
			if(foundBook == null) {
				System.out.println("Could not locate that entry");
			} else {
				displayBookAndAssociation(foundBook);
			}
			break;
		case 5:
			// Book findAll
			List<Book> allBooks = BookService.findAllBooks();
			allBooks.stream().forEach(b -> {
				displayBookAndAssociation(b);
				System.out.println();
				System.out.println("<----------------------------------------------------------------------->");
				System.out.println();
			});
			break;
		default:
			System.out.println("No such operation.");
		}
	}
	
	private static void publisherMenu(Scanner userInput) {

		List<String> info = new ArrayList<>();
		int operation = -1;
		// Publisher database
		operation = whichOperation(userInput);
		switch (operation) {
		case 1:
			// Publisher save
			info = infoGathering(userInput, publisherOptions);
			System.out.println("This is the PublisherID associated with your entry: " + 
					PublisherService.savePublisher(info.get(0), info.get(1), info.get(2)));
			break;
		case 2:
			// Publisher delete
			Publisher deletedEntry = PublisherService.deletePublisher(promptForId(userInput, "delete"));
			if(deletedEntry == null) {
				System.out.println("Unfortunatly, we failed to delete that entry");
			} else {
				System.out.println("Successfully deleted entry: (Info about deleted entry)");
				System.out.println(deletedEntry.toString());
			}
			break;
		case 3:
			// Publisher update
			System.out.println("What is the Publisher Id that you would like to update?");
			int publisherId = validId(userInput);
			userInput.nextLine();
			List<String> updateList = updateInfo(userInput, publisherOptions);
			Publisher updatedPublisher = new Publisher(publisherId, updateList.get(0), updateList.get(1), updateList.get(2));
			boolean publisherUpdateCompleted = PublisherService.updatePublisher(updatedPublisher);
			if(publisherUpdateCompleted) {
				System.out.println("Completed Publisher Update Successfully");
			} else {
				System.out.println("ERROR: Either the Publisher Id was incorrect or an Error occurred");
			}
			break;
		case 4:
			// Publisher find
			Publisher publisher = PublisherService.findPublisher(promptForId(userInput, "find"));
			if(publisher == null) {
				System.out.println("Could not locate that entry");
			} else {
				displayPublisherWithBooks(publisher);
			}
			break;
		case 5:
			// Publisher findAll
			List<Publisher> allPublishers = PublisherService.findAllPublishers();
			allPublishers.stream().forEach(p -> displayPublisherWithBooks(p));
			break;
		default:
			System.out.println("No such operation.");
		}
	}
	
	public static void main(String[] args) {
		Scanner userInput = new Scanner(System.in);
		System.out.println("Which database would you like to use?");
		iterateOptions(dataBases);

		int dataChoice = validId(userInput);
		switch (dataChoice) {
		case 1:
			authorMenu(userInput);
			break;
		case 2:
			bookMenu(userInput);
			break;
		case 3:
			publisherMenu(userInput);
			break;
		default:
			// No database
			System.out.println("I am sorry that is not a database.");
			break;
		}
		
		
		userInput.close();
			
	}

}
