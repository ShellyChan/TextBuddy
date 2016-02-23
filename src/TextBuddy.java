import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

/**
 * 
 * @author Shelly
 * 
 *         Name : Ingine Hmwe 
 *         Tut : W05
 *         Team : T16-1J
 *
 */


/**
 * Precondition: The program requires the user to provide the name of the text file at the start. 
 * 				If the provided text file does not exist, it will be created.
 * 
 * This is the program where users can add items to the text file ,or display and delete items
 * from the text file or clear the entire items. In addition, it provides functionality to sort the items
 * in the list alphabetically and  to search for words ,returning the lines one by one if there is a match. 
 *
 */
public class TextBuddy {

	// to store user input text in the file
	public static String fileName;
	public static String message;
	public static ArrayList<String> itemList;

	private static File inputFile;

	//Message to feedback to user
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use";
	private static final String MESSAGE_COMMAND = "command: ";
	private static final String MESSAGE_ADD = "added to %1$s: \"%2$s\"";
	private static final String MESSAGE_DELETE = "deleted from %s: \"%s\"";
	private static final String MESSAGE_CLEAR = "all content deleted from %s";
	private static final String MESSAGE_DISPLAY_EMPTY = "%s is empty";

	private static final String ERROR_INVALID_USER_COMMAND = "INVALID COMMAND INPUT";
	private static final String ERROR_INVALID_FILE_COMMAND = "INVALID COMMAND. Please try with an input file";
	private static final String ERROR_CREATING_FILE = "Error occurs while creating file";
	private static final String ERROR_FILE_NOT_FOUND = "File not found while reading file";
	private static final String ERROR_WRITING_FILE = "Error occurs while writing to file";
	private static final String ERROR_READING_FILE = "Error occurs while reading to file";
	private static final String ERROR_SEARCH_NOT_FOUND = "Search not found";
	private static final String ERROR_SEARCH_WORD_EMPTY = "Search word is missing";
	private static Scanner scanner = new Scanner(System.in);

	// possible command types
	enum COMMAND_TYPE {
		ADD, DISPLAY, DELETE, CLEAR, EXIT, INVALID, SEARCH, SORT
	};

	public static void main(String[] args) throws IOException {
		
		if (isFileDeclared(args)) {
			fileName = args[0];
			itemList = new ArrayList<>();
			createFile(fileName);

			showLineToUser(String.format(MESSAGE_WELCOME, fileName));
			
		} else {
			showLineToUser(ERROR_INVALID_FILE_COMMAND);
		}
		
		showLineToUser(MESSAGE_COMMAND);	
		while (scanner.hasNextLine()) {
			executeCommand(getCommand());
			saveToFile(itemList);
			showLineToUser(MESSAGE_COMMAND);	

		}
		scanner.close();
	}

	public static void executeCommand(String userCommand) throws IOException {
		String commandTypeString = getFirstWord(userCommand);
		COMMAND_TYPE commandType = getCommandType(commandTypeString);
		

		switch (commandType) {
		case ADD:
			String userInputLine = removeFirstWord(userCommand);
			executeAdd(userInputLine);		
			break;
		case DISPLAY:
			executeDisplay();
			break;
		case DELETE:
			int lineNumber = Integer.parseInt(removeFirstWord(userCommand));
			executeDelete(lineNumber);
			break;
		case CLEAR:
			executeClear();
			break;
		case SORT:
			executeSort(itemList);
			break;
		case SEARCH:
			String searchWord = removeFirstWord(userCommand);
			executeSearch(itemList, searchWord);
			break;
		case EXIT:
			System.exit(0);
		case INVALID:
			message = ERROR_INVALID_USER_COMMAND;
			showLineToUser(message);
			break;
		}

	}

	public static void createFile(String fileName) {
		if(!isFileAlreadyCreated(fileName)){
			try {
				inputFile.createNewFile();
			} catch (IOException e) {
				showLineToUser(ERROR_CREATING_FILE);
			}
		} else{
			itemList = fetchDataFromFile();
		}
	}
	
	private static void executeAdd(String description) {
		
		itemList.add(description);
		message = String.format(MESSAGE_ADD, fileName, description);
		showLineToUser(message);
	}

	private static void executeDisplay() throws IOException {
		if (itemList.isEmpty()) {
			message = String.format(MESSAGE_DISPLAY_EMPTY, fileName);
			showLineToUser(message);
		} else {
			display();
		}
	}
	
	private static void display(){
		String textToDisplay;
		int lineNumber;
		for (int i = 0; i < itemList.size(); i++) {
			lineNumber = i + 1;
			textToDisplay = lineNumber + "." + itemList.get(i);
			showLineToUser(textToDisplay);
		}
	}

	private static void executeClear() {
		itemList.clear();
		message = String.format(MESSAGE_CLEAR, fileName);
		showLineToUser(message);
	}

	private static void executeDelete(int lineNumber) {
		String lineToRemove = null;
		try {
			lineToRemove = itemList.get(lineNumber - 1);
			itemList.remove(lineNumber - 1);
		
			String message = String.format(MESSAGE_DELETE, fileName, lineToRemove);
			showLineToUser(message);

		} catch (IndexOutOfBoundsException e) {
			String message = String.format(MESSAGE_DISPLAY_EMPTY, fileName);
			showLineToUser(message);
		}

	}
	
	//sort the items in the list alphabetically 
	private static void executeSort(ArrayList<String> itemList){
		Collections.sort(itemList);
	}

	public static void executeSearch(ArrayList<String> itemList, String searchWord){
		boolean isFound = false;
		if(searchWord.isEmpty()){
			showLineToUser(ERROR_SEARCH_WORD_EMPTY);
			return;
		}
		
		for(int i=0; i < itemList.size(); i++){
			if(itemList.get(i).toLowerCase().contains(searchWord.toLowerCase())){
				showLineToUser(itemList.get(i));
				isFound = true;
			}
		}
		if(!isFound){
			showLineToUser(ERROR_SEARCH_NOT_FOUND);
		}
	}
	
	private static void showLineToUser(String text) {
		System.out.println(text);
	}

	private static boolean isFileDeclared(String[] file) {
		return (file.length > 0);
	}

	private static boolean isFileAlreadyCreated(String fileName) {
		inputFile = new File(fileName);
		return (inputFile.exists() ? true : false);
	}

	private static void saveToFile(ArrayList<String> itemList) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(inputFile));
			for(int i =0; i < itemList.size(); i++){
				bw.write(itemList.get(i));
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			showLineToUser(ERROR_WRITING_FILE);
		}
	}

	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	private static String removeFirstWord(String userCommand) {
		String description = userCommand.replace(getFirstWord(userCommand), "").trim();
		return description;
	}


	private static String getCommand() {
		String command = scanner.nextLine();
		return command;
	}

	private static COMMAND_TYPE getCommandType(String commandTypeString) {

		if (commandTypeString == null) {
			throw new Error("commandTypeString cannot be null");
		}

		if (commandTypeString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("clear")) {
			return COMMAND_TYPE.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
			return COMMAND_TYPE.EXIT;
		} else if (commandTypeString.equalsIgnoreCase("sort")){
			return COMMAND_TYPE.SORT;	
		} else if (commandTypeString.equalsIgnoreCase("search")){
			return COMMAND_TYPE.SEARCH;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}

	private static ArrayList<String> fetchDataFromFile() {
		ArrayList<String> content = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			content = new ArrayList<String>();
			String line;

			while ((line = br.readLine()) != null) {
				content.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			showLineToUser(ERROR_FILE_NOT_FOUND);
		} catch (IOException e) {
			showLineToUser(ERROR_READING_FILE);
		}
		return content;

	}
	
}


