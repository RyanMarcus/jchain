package info.rmarcus.blockchain.networking;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import javax.net.ServerSocketFactory;

import info.rmarcus.blockchain.Block;
import info.rmarcus.blockchain.BlockEmitter;
import info.rmarcus.blockchain.LinkedBlock;

public class BlockServer extends BlockEmitter implements Closeable {
	private ServerSocket ss;
	
	private volatile boolean closed;
	private long serverID;
	private long blocksRecv = 0;
	private long blocksSent = 0;
	private long clientCount = 0;
	
	
	public BlockServer(int port) throws IOException {
		super();
		ss = ServerSocketFactory.getDefault().createServerSocket(port);
		closed = false;
		serverID = (new Random()).nextLong();
		new Thread(() -> listen()).start();
	}
	
	public void addClient(String ip, int port) throws UnknownHostException, IOException {
		addClient(ip, port, "");
	}
	
	public void addClient(String ip, int port, String prefix) throws UnknownHostException, IOException {
		Socket s = new Socket(ip, port);

		BlockClient bc = new BlockClient(s, this);
		bc.prefix = "C" + prefix;
		
		addListener(bc);
	}
	
	public void incrementBlocksSent() {
		blocksSent++;
	}
	
	public long getBlocksSent() {
		return blocksSent;
	}
	
	public void sendBlock(Block b) {
		sendBlock(new LinkedBlock(b, this));
	}
	
	public void sendBlock(LinkedBlock b) {
		emitBlock(b);
	}

	private void listen() {
		while (!isClosed()) {
			try {
				Socket newClient = ss.accept();
				BlockClient bc = new BlockClient(newClient, this);
				bc.prefix = "S" + clientCount;
				clientCount++;
				addListener(bc);
				
			} catch (IOException e) {
				if (!closed)
					e.printStackTrace();
				
			}
		}
	}
	
	@Override
	public synchronized void close() throws IOException {
		closed = true;
		ss.close();
	}
	
	public synchronized boolean isClosed() {
		return closed;
	}

	
	public void processRawBlock(Block b) {
		blocksRecv++;
		LinkedBlock lb = new LinkedBlock(b, this);
		this.emitBlock(lb);
	}
	
	public long getServerID() {
		return serverID;
	}
	
	public long getBlocksRecv() {
		return blocksRecv;
	}
	
	
}
