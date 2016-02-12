package info.rmarcus.blockchain.networking;

import java.io.Serializable;

import info.rmarcus.blockchain.Block;

public class NetworkMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	public NetworkMessage(Block b) {
		type = MessageType.BLOCK;
		block = b;
	}

	public NetworkMessage(String s) {
		type = MessageType.BLOCK_REQUEST;
		blockRequest = s;
	}

	public MessageType type;
	public Block block;
	public String blockRequest;

	@Override
	public String toString() {
		switch (type) {
		case BLOCK:
			return "block " + block;
		case BLOCK_REQUEST:
			return "request for block " + blockRequest.substring(blockRequest.length() - 10);
		}
		
		return "unknown message";
	}

}
