package gt.dvdyzag;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestBubble {

	@Test
	public void testBubbleCount() {
		Bubble.addBubble();
		Bubble.addBubble();
		Bubble.addBubble();
		Bubble.addBubble();
		
		int actual = Bubble.getBubbleCount();
		
		int expected = 4 ;
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testBubbleColor(){
		Bubble b = new Bubble(Bubble.AZUL);
		String actual = b.toString();
		String expected = "Azul" ;
		assertEquals(expected, actual);
	}

}
