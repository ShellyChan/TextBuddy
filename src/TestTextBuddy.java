import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.soap.Text;

import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.INITIALIZE;

public class TestTextBuddy {
	TextBuddy textBuddy;

	@Before
	public void INITIALIZE(){
		textBuddy = new TextBuddy();
		TextBuddy.fileName = "inputTextFile.txt";
		TextBuddy.createFile(TextBuddy.fileName);

	}
	@Test
	public void testAdd() throws IOException {

		textBuddy.executeCommand("add Peter");

		assertEquals("added to inputTextFile.txt: \"Peter\"", TextBuddy.message );
	}

	@Test
	public void testClear() throws IOException{
		textBuddy.executeCommand("testing");
		assertEquals("INVALID COMMAND INPUT", TextBuddy.message );
	}
	
	@Test
	public void testDelete() throws IOException{
		textBuddy.executeCommand("Delete Peter");
		///assertEquals("INVALID COMMAND INPUT", TextBuddy.message );
	}

}
