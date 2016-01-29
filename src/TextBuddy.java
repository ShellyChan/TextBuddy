import java.awt.Stroke;
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
/**
 * 
 * @author Shelly
 *	
 * Name : Ingine Hmwe
 * Matric No. : A0112835L
 * Team : T16-1J
 *
 */
public class TextBuddy {

	private static File inputFile;
	private static File tempFile;

	private static String fileName;
	private static ArrayList<String> dataListFromFile;

	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use\n";
	private static final String MESSAGE_COMMAND = "command: ";
	private static final String MESSAGE_ADD = "added to %1$s: \"%2$s\"\n";
	private static final String MESSAGE_DELETE = "deleted from %s: \"%s\"\n";
	private static final String MESSAGE_CLEAR = "all content deleted from %s\n";
	private static final String MESSAGE_DISPLAY_EMPTY = "%s is empty";

	private static final String ERROR_INVALID_USER_COMMAND = "INVALID COMMAND INPUT";
	private static final String ERROR_INVALID_FILE_COMMAND = "INVALID COMMAND. Please try with an input file";
	private static final String ERROR_CREATING_FILE = "Error occurs while creating file";
	private static final String ERROR_FILE_NOT_FOUND = "File not found while reading file";
	private static final String ERROR_WRITING_FILE = "Error occurs while writing to file";
	private static final String ERROR_READING_FILE = "Error occurs while reading to file";
	private static final String ERROR_CLEARING_FILE = "Error occurs while clearing data from the file";

	private static Scanner scanner = new Scanner(System.in);

	enum COMMAND_TYPE{
		ADD, DISPLAY, DELETE, CLEAR, EXIT, INVALID
	};


	public static void main(String[] args) throws IOException {

		if (isFileDeclared(args)){
			fileName = args[0];
			String welcomeMessage = getWelcomeMessage(fileName);
			showLineToUser(welcomeMessage);

			if (!isFileAlreadyCreated(fileName)){	
				createFile(inputFile);
			}

			while (true){
				showLineToUser(MESSAGE_COMMAND);
				String userCommand = getCommand();
				executeCommand(userCommand);
			}

		}else{
			showLineToUser(ERROR_INVALID_FILE_COMMAND);
		}

	}

	public static void executeCommand(String userCommand) throws IOException{
		String commandTypeString = getFirstWord(userCommand);
		COMMAND_TYPE commandType = getCommandType(commandTypeString);
		String message;

		switch (commandType){
		case ADD :
			String userInputLine = removeFirstWord(userCommand);
			executeAdd(userInputLine);
			message = getAddMessage(fileName, userInputLine);
			showLineToUser(message);
			break;
		case DISPLAY :
			executeDisplay();
			break;
		case DELETE :
			int lineNumber = Integer.parseInt(removeFirstWord(userCommand));
			executeDelete(lineNumber);
			break;
		case CLEAR :
			executeClear();
			message = getClearMessage(fileName);
			showLineToUser(message);
			break;
		case EXIT :
			System.exit(0);
		case INVALID :
			showLineToUser(ERROR_INVALID_USER_COMMAND);
			break;
		}

	}

	private static void executeAdd(String description){
		saveToExistingFile(description);
	}

	private static void executeDisplay() throws IOException{
		ArrayList<String> dataFromFile = fetchDataFromFile();
		if (dataFromFile.isEmpty()){
			String emptyMessage = getDisplayEmptyMessage(fileName);
			showLineToUser(emptyMessage);
		}else{
			String textToDisplay;
			int lineNumber;
			for (int i=0; i<dataFromFile.size(); i++){
				lineNumber = i + 1;
				textToDisplay = lineNumber + "." + dataFromFile.get(i);
				showLineToUser(textToDisplay);
			}
			showLineToUser(" ");
		}
	}

	private static void executeClear(){
		inputFile.delete();
		createFile(inputFile);
	}

	private static void executeDelete(int lineNumber){
		String lineToRemove = null;
		try{

			dataListFromFile = fetchDataFromFile();

			lineToRemove = dataListFromFile.get(lineNumber-1);
			dataListFromFile.remove(lineNumber-1);
			String currentLine; 
			executeClear();

			for (int i=0; i<dataListFromFile.size(); i++){
				executeAdd(dataListFromFile.get(i));
			}

			String message = getDeleteMessage(fileName, lineToRemove );
			showLineToUser(message);

		}catch (IndexOutOfBoundsException e){
			String message = getDisplayEmptyMessage(fileName);
			showLineToUser(message);
		}

	}

	private static void showLineToUser(String text) {
		System.out.println(text);
	}

	private static boolean isFileDeclared(String[] file){
		return file.length > 0;
	}

	private static boolean isFileAlreadyCreated(String fileName){
		inputFile = new File(fileName);
		return (inputFile.exists() ? true : false);
	}

	private static void createFile(File file){
		try {
			file.createNewFile();
		} catch (IOException e) {
			showLineToUser(ERROR_CREATING_FILE);
		}
	}

	private static void saveToExistingFile(String content){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(inputFile, true));
			bw.write(content);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			showLineToUser(ERROR_WRITING_FILE);
		}
	}

	private static String getFirstWord(String userCommand){
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	private static String removeFirstWord(String userCommand){
		String description = userCommand.replace(getFirstWord(userCommand),"").trim();
		return description;
	}

	private static String getWelcomeMessage(String fileName){
		return String.format(MESSAGE_WELCOME, fileName);
	}

	private static String getAddMessage(String fileName, String message){
		return String.format(MESSAGE_ADD, fileName, message);
	}

	private static String getDeleteMessage(String fileName, String message){
		return String.format(MESSAGE_DELETE, fileName, message);
	}

	private static String getClearMessage(String fileName) {
		return String.format(MESSAGE_CLEAR, fileName);
	}

	private static String getDisplayEmptyMessage(String fileName){
		return String.format(MESSAGE_DISPLAY_EMPTY, fileName);
	}
	private static String getCommand(){
		String command = scanner.nextLine();
		return command;
	}

	private static COMMAND_TYPE getCommandType(String commandTypeString){

		if (commandTypeString == null){
			throw new Error("commandTypeString cannot be null");
		}

		if (commandTypeString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("clear")) {
			return COMMAND_TYPE.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("delete")){
			return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("exit")){
			return COMMAND_TYPE.EXIT;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}

	private static ArrayList<String> fetchDataFromFile(){
		ArrayList<String> content = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			content = new ArrayList<String>();
			String line;

			while ((line = br.readLine()) != null){
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
