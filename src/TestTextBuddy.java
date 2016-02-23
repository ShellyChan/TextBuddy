import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.soap.Text;

import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.INITIALIZE;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;

public class TestTextBuddy {
	TextBuddy textBuddy;

	@Before
	public void INITIALIZE(){
		textBuddy = new TextBuddy();
		TextBuddy.fileName = "list.txt";
		TextBuddy.createFile(TextBuddy.fileName);

	}
	@Test
	public void testAdd() throws IOException {
		textBuddy.executeCommand("add there is a cat");
		assertEquals("added to "+ TextBuddy.fileName +": \"there is a cat\"", TextBuddy.message );
	}

	@Test
	public void testClear() throws IOException{
		textBuddy.executeCommand("clear");
		assertTrue(TextBuddy.itemList.size() == 0);
	}
	
	@Test
	public void testDelete() throws IOException{
		textBuddy.executeCommand("add life is great");
		textBuddy.executeCommand("add doggy doggy dog");
		textBuddy.executeCommand("add Peter");
		textBuddy.executeCommand("Delete 3");
		assertArrayEquals(new String[]{"life is great","doggy doggy dog"}, 
				new String[]{TextBuddy.itemList.get(0), TextBuddy.itemList.get(1)});
	}
	
	@Test
	public void testSort() throws IOException{
		textBuddy.executeCommand("add kitty catty cat");
		textBuddy.executeCommand("add CAT2016");
		textBuddy.executeCommand("add 3215");
		textBuddy.executeCommand("add *&^^%$^&");
		textBuddy.executeCommand("sort");
		assertArrayEquals(new String[]{"*&^^%$^&", "3215", "CAT2016", "kitty catty cat"}, 
				new String[]{TextBuddy.itemList.get(0),TextBuddy.itemList.get(1),TextBuddy.itemList.get(2),
						TextBuddy.itemList.get(3)});
	}
	
	@Test
	public void testSearch() throws IOException{
		textBuddy.executeCommand("add kitty catty cat");
		textBuddy.executeCommand("add CAT2016");
		textBuddy.executeCommand("add _catWoman");
		textBuddy.executeCommand("add 3215");
		textBuddy.executeCommand("add *&^^%$^&");
		textBuddy.executeCommand("Search CAT");
		assertArrayEquals(new String[]{"kitty catty cat", "CAT2016","_catWoman"}, 
				new String[]{TextBuddy.itemList.get(0),TextBuddy.itemList.get(1),TextBuddy.itemList.get(2)});
	}

}
