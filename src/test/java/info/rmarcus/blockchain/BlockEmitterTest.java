package info.rmarcus.blockchain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BlockEmitterTest {

private class MockBlockEmitter extends BlockEmitter {
		
		public void mockEmit(LinkedBlock lb) {
			this.emitBlock(lb);
		}
		
	}
	
	
	@Test
	public void headTest() {
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

		
		assertEquals("incorrect genesis block", lb, mock.getGenesis());
		assertEquals("incorrect head", lb3, mock.getHeadBlock());


	}
	
	@Test
	public void forkHeadTest() {
		Block b = new Block();
		Block b1 = new Block(b, null);
		Block b2 = new Block(b1, null);
		Block b3 = new Block(b1, null);
		Block b4 = new Block(b3, null);


		
		MockBlockEmitter mock = new MockBlockEmitter();
		
		LinkedBlock lb = new LinkedBlock(b, mock);
		mock.mockEmit(lb);
		
		LinkedBlock lb1 = new LinkedBlock(b1, mock);
		mock.mockEmit(lb1);

		
		LinkedBlock lb2 = new LinkedBlock(b2, mock);
		mock.mockEmit(lb2);
		
		LinkedBlock lb3 = new LinkedBlock(b3, mock);
		mock.mockEmit(lb3);
		
		LinkedBlock lb4 = new LinkedBlock(b4, mock);
		mock.mockEmit(lb4);

		
		assertEquals("incorrect genesis block", lb, mock.getGenesis());
		assertEquals("incorrect head", lb4, mock.getHeadBlock());


	}
	
	@Test
	public void forkHeadFlippedTest() {
		Block b = new Block();
		Block b1 = new Block(b, null);
		Block b2 = new Block(b1, null);
		Block b3 = new Block(b1, null);
		Block b4 = new Block(b3, null);


		
		MockBlockEmitter mock = new MockBlockEmitter();
		
		LinkedBlock lb = new LinkedBlock(b, mock);
		mock.mockEmit(lb);
		
		LinkedBlock lb1 = new LinkedBlock(b1, mock);
		mock.mockEmit(lb1);

		
		LinkedBlock lb2 = new LinkedBlock(b2, mock);
		mock.mockEmit(lb2);
		
		LinkedBlock lb4 = new LinkedBlock(b4, mock);
		mock.mockEmit(lb4);
		
		LinkedBlock lb3 = new LinkedBlock(b3, mock);
		mock.mockEmit(lb3);
		


		
		assertEquals("incorrect genesis block", lb, mock.getGenesis());
		assertEquals("incorrect head", lb4, mock.getHeadBlock());


	}
	

}
