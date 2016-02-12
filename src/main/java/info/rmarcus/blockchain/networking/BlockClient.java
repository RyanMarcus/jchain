package info.rmarcus.blockchain.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import info.rmarcus.blockchain.BlockListener;
import info.rmarcus.blockchain.LinkedBlock;

public class BlockClient implements BlockListener {

	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private BlockServer bl;

	private Set<String> sentBlocks;

	public String prefix = "";

	public BlockClient(Socket s, BlockServer bl) throws IOException {
		this.s = s;
		sentBlocks = new HashSet<String>();
		this.bl = bl;
		oos = new ObjectOutputStream(s.getOutputStream());		
		new Thread(() -> clientLoop()).start();
	}

	private void clientLoop() {

		try {
			ois = new ObjectInputStream(s.getInputStream());
			if (bl.getHeadBlock() != null) {
				// make sure they have the head block
				//System.out.println(prefix + ": sending head block " + bl.getHeadBlock().getB());
				sendObject(new NetworkMessage(bl.getHeadBlock().getB()));
			}
		} catch (IOException e1) {
			// failed in client loop!
			System.err.println(bl.getServerID() + ": " + e1);
			return;
		}



		while (s.isConnected()) {
			try {
				//System.out.println(prefix + ": waiting for message");

				Object o = ois.readObject();
				if (!(o instanceof NetworkMessage)) {
					// reject any stream that sends a non-block
					s.shutdownInput();
					s.close();
					break;
				}

				
				NetworkMessage nm = (NetworkMessage) o;
				
				//System.out.println(prefix + ": got " + nm);
				switch (nm.type) {
				case BLOCK:
					// don't send blocks to this client that we got from this client...
					sentBlocks.add(nm.block.getHash());

					bl.processRawBlock(nm.block);

					// check to see if we have all the previous block...
					LinkedBlock previous = bl.getBlockByHash(nm.block.getPreviousHash());
					if (previous == null && nm.block.getPreviousHash().length() != 0) {
						// we are missing the immed. preceeding block
						NetworkMessage toSend = new NetworkMessage(nm.block.getPreviousHash());
						sendObject(toSend);
						break;
					}


					// we have the immed. preceding block, but do we have all of 
					// the prec. blocks?
					if (previous != null) {
						String nextMissing = previous.getMissingHashFromChain();
						if (nextMissing != null) {
							// send a block request for the previous block..
							NetworkMessage toSend = new NetworkMessage(nextMissing);
							sendObject(toSend);
						}
					}
					

					break;
				case BLOCK_REQUEST:
					// check to see if we have the block...
					LinkedBlock toSend = bl.getBlockByHash(nm.blockRequest);

					
					
					if (toSend == null) {
						//System.out.println(prefix + ": Got a request for block " + nm.blockRequest + " but I don't have it!");
						break;
					}

					// if we have it, send it
					sendObject(new NetworkMessage(toSend.getB()));
					break;
				}

			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void sendObject(NetworkMessage o) throws IOException {
		//System.out.println(prefix + ": sending " + o);

		
		
		if (o.type == MessageType.BLOCK)
			bl.incrementBlocksSent();

		synchronized(oos) {
			oos.writeObject(o);
			oos.flush();
		}
		
		//System.out.println(prefix + ": sent " + o);

	}

	@Override
	public synchronized boolean processBlock(LinkedBlock b) {
		//System.out.println(prefix + ": got block to process " + b);
		
		// don't send a block we've already sent...
		if (sentBlocks.contains(b.getB().getHash())) {
			return true;
		}

		// send the new block over the wire..
		try {
			sendObject(new NetworkMessage(b.getB()));
			sentBlocks.add(b.getB().getHash());
		} catch (Exception e) {
			// couldn't send the block. abandon the connection
			// and unregister
			try {
				s.close();
			} catch (IOException e1) {  }
			return true;
		}

		return true;
	}
}
