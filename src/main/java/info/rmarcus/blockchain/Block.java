package info.rmarcus.blockchain;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.hash.Hashing;

public class Block implements Serializable {

	private static final long serialVersionUID = 1L;

	private long blockID;
	private String previousHash;
	private ImmutableMap<String, String> data;

	/**
	 * Construct a new genises block
	 */
	public Block() {
		blockID = 0;
		setPreviousHash("");
		data = null;
	}

	public Block(Block previous, Map<String, String> data) {
		this.blockID = previous.blockID + 1;
		
		if (data != null) 
			this.data = ImmutableMap.copyOf(data);
		
		this.setPreviousHash(previous.getHash());
	}


	public String getHash() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(this);

			return Hashing.sha512().hashBytes(bos.toByteArray()).toString();
		} catch (Exception e) {
			System.err.println("Error hashing block: " + e);
			return null;
		}
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	public ImmutableMap<String, String> getData() {
		return data;
	}

	public boolean isGenises() {
		return this.blockID == 0; 
	}
	
	@Override
	public String toString() {
		return blockID + ":" + getHash().substring(getHash().length() - 10);
	}

	public long getBlockID() {
		return blockID;
	}


}
