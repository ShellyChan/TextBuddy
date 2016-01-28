import java.util.Scanner;


public class TextBuddy {
	
	private static final String WELCOME_MSG = "Welcome to TextBuddy. mytextfile.txt is ready for use.";

	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		showToUser(WELCOME_MSG);
			
	}
	
	private static void showToUser(String text) {
		System.out.println(text);
	}

}
