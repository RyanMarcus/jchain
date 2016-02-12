package info.rmarcus.blockchain.networking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.tempusfugit.concurrency.IntermittentTestRunner;
import com.google.code.tempusfugit.concurrency.annotations.Intermittent;

import info.rmarcus.blockchain.Block;
import info.rmarcus.blockchain.BlockEmitter;

@RunWith(IntermittentTestRunner.class)
@Intermittent(repetition = 10)
public class BlockServerTest {

	@Before
	public void delay() throws InterruptedException {
		Thread.sleep(100);
	}	
	
	private boolean hasBlocks(BlockEmitter be, Block... b) {
		return Arrays.stream(b)
		.allMatch(block -> be.getBlockByHash(block.getHash()) != null);
	}

	@Test
	public void twoServerTest() throws IOException, InterruptedException {
		try (
				BlockServer bs1 = new BlockServer(9091);
				BlockServer bs2 = new BlockServer(9092);
				) {
			Block b = new Block();
			Block b1 = new Block(b, null);
			Block b2 = new Block(b1, null);
			Block b3 = new Block(b2, null);
			
			bs1.sendBlock(b);
			bs1.sendBlock(b1);
			bs1.sendBlock(b2);
			bs1.sendBlock(b3);

			bs2.addClient("localhost", 9091);

			int maxTry = 20;
			while (!hasBlocks(bs2, b, b1, b2, b3)) {
				Thread.sleep(10);
				maxTry--;
				
				if (maxTry <= 0) {
					fail("Timed out waiting for blocks"); 
				}
			}
			

			assertTrue("remote client should have genesis block", bs2.getGenesis() != null);
			assertEquals("remote client should have head block", bs2.getBlockByHash(b3.getHash()).getB().getHash(), b3.getHash());
			assertEquals("remote client should have chained block", bs2.getBlockByHash(b3.getHash()).getPrevious().getB().getHash(), b2.getHash());
			assertEquals("remote client should have chained block", bs2.getBlockByHash(b3.getHash()).getPrevious().getPrevious().getB().getHash(), b1.getHash());
			assertEquals("remote client should have chained block", bs2.getBlockByHash(b3.getHash()).getPrevious().getPrevious().getPrevious().getB().getHash(), b.getHash());

			
		}

	}

	@Test
	public void twoServerInterleaveTest() throws IOException, InterruptedException {

		try (
				BlockServer bs1 = new BlockServer(9091);
				BlockServer bs2 = new BlockServer(9092);
				) {


			Thread.sleep(100);

			bs2.addClient("localhost", 9091);

			Block b = new Block();
			Block b1 = new Block(b, null);
			Block b2 = new Block(b1, null);
			Block b3 = new Block(b2, null);

			bs1.sendBlock(b);
			bs2.sendBlock(b1);
			bs1.sendBlock(b2);
			bs2.sendBlock(b3);

			int maxTry = 20;
			while (!hasBlocks(bs2, b, b1, b2, b3) || !hasBlocks(bs1, b, b1, b2, b3)) {
				Thread.sleep(100);
				maxTry--;
				
				if (maxTry <= 0) {
					fail("Timed out waiting for blocks"); 
				}
			}

			assertTrue("remote client should have genesis block", bs2.getGenesis() != null);
			assertEquals("remote client should have head block", bs2.getBlockByHash(b3.getHash()).getB().getHash(), b3.getHash());
			assertEquals("remote client should have chained block", bs2.getBlockByHash(b3.getHash()).getPrevious().getB().getHash(), b2.getHash());
			assertEquals("remote client should have chained block", bs2.getBlockByHash(b3.getHash()).getPrevious().getPrevious().getB().getHash(), b1.getHash());
			assertEquals("remote client should have chained block", bs2.getBlockByHash(b3.getHash()).getPrevious().getPrevious().getPrevious().getB().getHash(), b.getHash());


			assertTrue("local client should have genesis block", bs1.getGenesis() != null);
			assertEquals("local client should have head block", bs1.getBlockByHash(b3.getHash()).getB().getHash(), b3.getHash());
			assertEquals("local client should have chained block", bs1.getBlockByHash(b3.getHash()).getPrevious().getB().getHash(), b2.getHash());
			assertEquals("local client should have chained block", bs1.getBlockByHash(b3.getHash()).getPrevious().getPrevious().getB().getHash(), b1.getHash());
			assertEquals("local client should have chained block", bs1.getBlockByHash(b3.getHash()).getPrevious().getPrevious().getPrevious().getB().getHash(), b.getHash());


		}
	}

	@Test
	public void twoServerOrderTest() throws IOException, InterruptedException {
		try (
				BlockServer bs1 = new BlockServer(9091);
				BlockServer bs2 = new BlockServer(9092);
				) {


			Thread.sleep(100);

			bs2.addClient("localhost", 9091);

			Block b = new Block();
			Block b1 = new Block(b, null);
			Block b2 = new Block(b1, null);
			Block b3 = new Block(b2, null);

			bs1.sendBlock(b);
			bs1.sendBlock(b3);
			bs1.sendBlock(b1);
			bs1.sendBlock(b2);

			int maxTry = 20;
			while (!hasBlocks(bs2, b, b1, b2, b3) || !hasBlocks(bs1, b, b1, b2, b3)) {
				Thread.sleep(100);
				maxTry--;
				
				if (maxTry <= 0) {
					System.out.println("err!");
					fail("Timed out waiting for blocks"); 
				}
			}

			assertTrue("remote client should have genesis block", bs2.getGenesis() != null);
			assertEquals("remote client should have head block", bs2.getBlockByHash(b3.getHash()).getB().getHash(), b3.getHash());
			assertEquals("remote client should have chained block", bs2.getBlockByHash(b3.getHash()).getPrevious().getB().getHash(), b2.getHash());
			assertEquals("remote client should have chained block", bs2.getBlockByHash(b3.getHash()).getPrevious().getPrevious().getB().getHash(), b1.getHash());
			assertEquals("remote client should have chained block", bs2.getBlockByHash(b3.getHash()).getPrevious().getPrevious().getPrevious().getB().getHash(), b.getHash());


		}
	}

	@Test
	public void threeServerTest() throws IOException, InterruptedException {
		try (
				BlockServer bs1 = new BlockServer(9091);
				BlockServer bs2 = new BlockServer(9092);
				BlockServer bs3 = new BlockServer(9093);

				) {



			Thread.sleep(100);

			bs2.addClient("localhost", 9091, "0");
			bs3.addClient("localhost", 9091, "1");

			Block b = new Block();
			Block b1 = new Block(b, null);
			Block b2 = new Block(b1, null);
			Block b3 = new Block(b2, null);

			bs1.sendBlock(b);
			bs1.sendBlock(b1);
			bs1.sendBlock(b2);
			bs1.sendBlock(b3);

			int maxTry = 20;
			while (!hasBlocks(bs2, b, b1, b2, b3) || !hasBlocks(bs3, b, b1, b2, b3)) {
				Thread.sleep(100);
				maxTry--;
				
				if (maxTry <= 0) {
					fail("Timed out waiting for blocks"); 
				}
			}


			assertTrue("remote client should have genesis block", bs2.getGenesis() != null);
			assertEquals("remote client should have head block", bs2.getBlockByHash(b3.getHash()).getB().getHash(), b3.getHash());
			assertEquals("remote client should have chained block", bs2.getBlockByHash(b3.getHash()).getPrevious().getB().getHash(), b2.getHash());
			assertEquals("remote client should have chained block", bs2.getBlockByHash(b3.getHash()).getPrevious().getPrevious().getB().getHash(), b1.getHash());
			assertEquals("remote client should have chained block", bs2.getBlockByHash(b3.getHash()).getPrevious().getPrevious().getPrevious().getB().getHash(), b.getHash());


			assertTrue("remote client should have genesis block", bs3.getGenesis() != null);
			assertEquals("remote client should have head block", bs3.getBlockByHash(b3.getHash()).getB().getHash(), b3.getHash());
			assertEquals("remote client should have chained block", bs3.getBlockByHash(b3.getHash()).getPrevious().getB().getHash(), b2.getHash());
			assertEquals("remote client should have chained block", bs3.getBlockByHash(b3.getHash()).getPrevious().getPrevious().getB().getHash(), b1.getHash());
			assertEquals("remote client should have chained block", bs3.getBlockByHash(b3.getHash()).getPrevious().getPrevious().getPrevious().getB().getHash(), b.getHash());

		}
	}


}
