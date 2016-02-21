import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

/**
 * 
 * @author Shelly
 * 
 *         Name : Ingine Hmwe 
 *         Tut : W05
 *         Matric No. : A0112835L 
 *         Team : T16-1J
 *
 */
public class TextBuddy {

	// to store user input text in the file
	public static String fileName;
	public static String message;
	private static File inputFile;

	// to store text from file in the list for display purpose
	private static ArrayList<String> dataListFromFile;

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
	private static final String ERROR_CLEARING_FILE = "Error occurs while clearing data from the file";

	private static Scanner scanner = new Scanner(System.in);

	// possible command types
	enum COMMAND_TYPE {
		ADD, DISPLAY, DELETE, CLEAR, EXIT, INVALID
	};

	public static void main(String[] args) throws IOException {
		
		if (isFileDeclared(args)) {
			fileName = args[0];
			showLineToUser(String.format(MESSAGE_WELCOME, fileName));
			createFile(fileName);
			
			while (true) {
				showLineToUser(MESSAGE_COMMAND);	
				executeCommand(getCommand());
			}

		} else {
			showLineToUser(ERROR_INVALID_FILE_COMMAND);
		}

	}

	public static void executeCommand(String userCommand) throws IOException {
		String commandTypeString = getFirstWord(userCommand);
		COMMAND_TYPE commandType = getCommandType(commandTypeString);
		

		switch (commandType) {
		case ADD:
			String userInputLine = removeFirstWord(userCommand);
			executeAdd(userInputLine);
			message = String.format(MESSAGE_ADD, fileName, userInputLine);
			showLineToUser(message);
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
			message = String.format(MESSAGE_CLEAR, fileName);
			showLineToUser(message);
			break;
		case EXIT:
			System.exit(0);
		case INVALID:
			message = ERROR_INVALID_USER_COMMAND;
			showLineToUser(message);
			break;
		}

	}

	private static void executeAdd(String description) {
		saveToExistingFile(description);
	}

	private static void executeDisplay() throws IOException {
		ArrayList<String> dataFromFile = fetchDataFromFile();
		if (dataFromFile.isEmpty()) {
			String emptyMessage = String.format(MESSAGE_DISPLAY_EMPTY, fileName);
			showLineToUser(emptyMessage);
		} else {
			String textToDisplay;
			int lineNumber;
			for (int i = 0; i < dataFromFile.size(); i++) {
				lineNumber = i + 1;
				textToDisplay = lineNumber + "." + dataFromFile.get(i);
				showLineToUser(textToDisplay);
			}
		}
	}

	public static void executeClear() {
		inputFile.delete();
		createFile(fileName);
	}

	public static void executeDelete(int lineNumber) {
		String lineToRemove = null;
		try {

			dataListFromFile = fetchDataFromFile();

			lineToRemove = dataListFromFile.get(lineNumber - 1);
			dataListFromFile.remove(lineNumber - 1);
			String currentLine;
			executeClear();

			for (int i = 0; i < dataListFromFile.size(); i++) {
				executeAdd(dataListFromFile.get(i));
			}

			String message = String.format(MESSAGE_DELETE, fileName, lineToRemove);
			showLineToUser(message);

		} catch (IndexOutOfBoundsException e) {
			String message = String.format(MESSAGE_DISPLAY_EMPTY, fileName);
			showLineToUser(message);
		}

	}

	private static void showLineToUser(String text) {
		System.out.println(text);
	}

	private static boolean isFileDeclared(String[] file) {
		return file.length > 0;
	}

	private static boolean isFileAlreadyCreated(String fileName) {
		inputFile = new File(fileName);
		return (inputFile.exists() ? true : false);
	}

	public static void createFile(String fileName) {
		if(!isFileAlreadyCreated(fileName)){
			try {
				inputFile.createNewFile();
			} catch (IOException e) {
				showLineToUser(ERROR_CREATING_FILE);
			}
		}
		
	}

	private static void saveToExistingFile(String content) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(inputFile, true));
			bw.write(content);
			bw.newLine();
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


