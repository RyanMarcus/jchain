package info.rmarcus.blockchain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LinkedBlockTest {

	
	private class MockBlockEmitter extends BlockEmitter {
		
		public void mockEmit(LinkedBlock lb) {
			this.emitBlock(lb);
		}
		
	}
	
	
	@Test
	public void idealLinkingTest() {
		Block b = new Block();
		Block b1 = new Block(b, null);
		Block b2 = new Block(b1, null);
		Block b3 = new Block(b2, null);

		
		MockBlockEmitter mock = new MockBlockEmitter();
		
		LinkedBlock lb = new LinkedBlock(b, mock);
		mock.mockEmit(lb);
		
		LinkedBlock lb1 = new LinkedBlock(b1, mock);
		mock.mockEmit(lb1);

		
		LinkedBlock lb2 = new LinkedBlock(b2, mock);
		mock.mockEmit(lb2);
		
		LinkedBlock lb3 = new LinkedBlock(b3, mock);
		mock.mockEmit(lb3);

		
		assertTrue("Genises block prev should be null", lb.getPrevious() == null);
		assertEquals("Blocks should be properly chained", lb1.getPrevious(), lb);
		assertEquals("Blocks should be properly chained", lb2.getPrevious(), lb1);
		assertEquals("Blocks should be properly chained", lb3.getPrevious(), lb2);

		
		assertEquals("Should have proper length", 1, lb.getLength());
		assertEquals("Should have proper length", 2, lb1.getLength());
		assertEquals("Should have proper length", 3, lb2.getLength());
		assertEquals("Should have proper length", 4, lb3.getLength());
		assertEquals("Should have proper confirmations", 3, lb.getConfirmations());
		assertEquals("Should have proper confirmations", 2, lb1.getConfirmations());
		assertEquals("Should have proper confirmations", 1, lb2.getConfirmations());
		assertEquals("Should have proper confirmations", 0, lb3.getConfirmations());


	}
	
	
	@Test
	public void middleFlipLinkingTest() {
		Block b = new Block();
		Block b1 = new Block(b, null);
		Block b2 = new Block(b1, null);
		Block b3 = new Block(b2, null);

		
		MockBlockEmitter mock = new MockBlockEmitter();
		
		LinkedBlock lb = new LinkedBlock(b, mock);
		mock.mockEmit(lb);

		LinkedBlock lb2 = new LinkedBlock(b2, mock);
		mock.mockEmit(lb2);
		
		LinkedBlock lb1 = new LinkedBlock(b1, mock);
		mock.mockEmit(lb1);

		LinkedBlock lb3 = new LinkedBlock(b3, mock);
		mock.mockEmit(lb3);
		
		assertTrue("Genises block prev should be null", lb.getPrevious() == null);
		assertEquals("Blocks should be properly chained", lb1.getPrevious(), lb);
		assertEquals("Blocks should be properly chained", lb2.getPrevious(), lb1);
		assertEquals("Blocks should be properly chained", lb3.getPrevious(), lb2);

		

		assertEquals("Should have proper length", 1, lb.getLength());
		assertEquals("Should have proper length", 2, lb1.getLength());
		assertEquals("Should have proper length", 3, lb2.getLength());
		assertEquals("Should have proper length", 4, lb3.getLength());
		assertEquals("Should have proper confirmations", 3, lb.getConfirmations());
		assertEquals("Should have proper confirmations", 2, lb1.getConfirmations());
		assertEquals("Should have proper confirmations", 1, lb2.getConfirmations());
		assertEquals("Should have proper confirmations", 0, lb3.getConfirmations());

	}
	
	@Test
	public void outerFlipLinkingTest() {
		Block b = new Block();
		Block b1 = new Block(b, null);
		Block b2 = new Block(b1, null);
		Block b3 = new Block(b2, null);

		
		MockBlockEmitter mock = new MockBlockEmitter();
		
		LinkedBlock lb = new LinkedBlock(b, mock);
		mock.mockEmit(lb);

		LinkedBlock lb3 = new LinkedBlock(b3, mock);
		mock.mockEmit(lb3);
		
		LinkedBlock lb1 = new LinkedBlock(b1, mock);
		mock.mockEmit(lb1);

		
		LinkedBlock lb2 = new LinkedBlock(b2, mock);
		mock.mockEmit(lb2);
		


		
		assertTrue("Genises block prev should be null", lb.getPrevious() == null);
		assertEquals("Blocks should be properly chained", lb1.getPrevious(), lb);
		assertEquals("Blocks should be properly chained", lb2.getPrevious(), lb1);
		assertEquals("Blocks should be properly chained", lb3.getPrevious(), lb2);

		

		assertEquals("Should have proper length", 1, lb.getLength());
		assertEquals("Should have proper length", 2, lb1.getLength());
		assertEquals("Should have proper length", 3, lb2.getLength());
		assertEquals("Should have proper length", 4, lb3.getLength());
		assertEquals("Should have proper confirmations", 3, lb.getConfirmations());
		assertEquals("Should have proper confirmations", 2, lb1.getConfirmations());
		assertEquals("Should have proper confirmations", 1, lb2.getConfirmations());
		assertEquals("Should have proper confirmations", 0, lb3.getConfirmations());

	}
	
	@Test
	public void reverseLinkingTest() {
		Block b = new Block();
		Block b1 = new Block(b, null);
		Block b2 = new Block(b1, null);
		Block b3 = new Block(b2, null);

		
		MockBlockEmitter mock = new MockBlockEmitter();
		

		LinkedBlock lb3 = new LinkedBlock(b3, mock);
		mock.mockEmit(lb3);
		
		LinkedBlock lb2 = new LinkedBlock(b2, mock);
		mock.mockEmit(lb2);

		LinkedBlock lb1 = new LinkedBlock(b1, mock);
		mock.mockEmit(lb1);

		LinkedBlock lb = new LinkedBlock(b, mock);
		mock.mockEmit(lb);


		assertTrue("Genises block prev should be null", lb.getPrevious() == null);
		assertEquals("Blocks should be properly chained", lb1.getPrevious(), lb);
		assertEquals("Blocks should be properly chained", lb2.getPrevious(), lb1);
		assertEquals("Blocks should be properly chained", lb3.getPrevious(), lb2);


		assertEquals("Should have proper length", 1, lb.getLength());
		assertEquals("Should have proper length", 2, lb1.getLength());
		assertEquals("Should have proper length", 3, lb2.getLength());
		assertEquals("Should have proper length", 4, lb3.getLength());
		assertEquals("Should have proper confirmations", 3, lb.getConfirmations());
		assertEquals("Should have proper confirmations", 2, lb1.getConfirmations());
		assertEquals("Should have proper confirmations", 1, lb2.getConfirmations());
		assertEquals("Should have proper confirmations", 0, lb3.getConfirmations());

	}
	
	@Test
	public void impossibleLinkingTest() {
		Block b = new Block();
		Block b1 = new Block(b, null);
		Block b2 = new Block(b1, null);
		Block b3 = new Block(b2, null);

		
		MockBlockEmitter mock = new MockBlockEmitter();
		

		LinkedBlock lb3 = new LinkedBlock(b3, mock);
		mock.mockEmit(lb3);
		
		LinkedBlock lb2 = new LinkedBlock(b2, mock);
		mock.mockEmit(lb2);

		//LinkedBlock lb1 = new LinkedBlock(b1, mock);
		//mock.mockEmit(lb1);

		LinkedBlock lb = new LinkedBlock(b, mock);
		mock.mockEmit(lb);





		
		assertTrue("Genises block prev should be null", lb.getPrevious() == null);
		assertEquals("Blocks should be properly chained", lb3.getPrevious(), lb2);
		assertEquals("Blocks should be properly chained", lb2.getPrevious(), null);
		

		assertEquals("Should have proper length", 1, lb.getLength());
		assertEquals("Should have proper length", 1, lb2.getLength());
		assertEquals("Should have proper length", 2, lb3.getLength());


	}
}
