import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class TextBuddy {
	
	private static File inputFile;
	
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use";
	private static final String MESSAGE_COMMAND = "command";
	private static final String MESSAGE_ADD = "added to %s: \"%s\"";
	private static final String MESSAGE_DELETE = "deleted from %s: \"%s\"";
	private static final String MESSAGE_CLEAR = "all content deleted from %s";
	private static final String ERROR_INVALID_FILE_COMMAND = "INVALID COMMAND. Please try with an input file";
	private static final String ERROR_CREATING_FILE = "Error occurs while creating file";
	private static final String ERROR_READING_FILE = "File not found while reading file";
	
	private static Scanner scanner = new Scanner(System.in);
	
	enum COMMAND{
		ADD, DISPLAY, DELETE, CLEAR, EXIT
	};
	
	
	public static void main(String[] args) {
		
		if(isFileDeclared(args)){
			String fileName = args[0];
			String welcomeMessage = getWelcomeMessage(fileName);
			showToUser(welcomeMessage);
			
			if(isFileAlreadyCreated(fileName)){
				//readFromExistingFile(inputFile);
			}else {
				createFile(inputFile);
			}
			
		}else{
			showToUser(ERROR_INVALID_FILE_COMMAND);
		}
			
	}
	
	private static void showToUser(String text) {
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
			showToUser(ERROR_CREATING_FILE);
		}
	}
	
	private static void readFromExistingFile(File file){
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
		} catch (FileNotFoundException e) {
			showToUser(ERROR_READING_FILE);
		}
		
	}
	
	private static String getWelcomeMessage(String fileName){
		return String.format(MESSAGE_WELCOME, fileName);
	}
	
	private static void getCommand(){
		
	}

}
