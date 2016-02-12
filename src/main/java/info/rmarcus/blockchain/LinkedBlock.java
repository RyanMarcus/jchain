package info.rmarcus.blockchain;

public class LinkedBlock implements BlockListener {
	
	private Block b;
	private LinkedBlock previous;
	private BlockEmitter bo;
	private int confirmations = 0;
	
	public LinkedBlock(Block b, BlockEmitter bo) {
		this.b = b;
		this.bo = bo;
		
		LinkedBlock p = bo.getBlockByHash(b.getPreviousHash());
		
		
		if (p == null) {
			bo.addListener(this);
		} else {
			setPrevious(p);
		}
	}

	public LinkedBlock getPrevious() {
		return previous;
	}

	public void setPrevious(LinkedBlock previous) {
		this.previous = previous;
		
		LinkedBlock c = previous;
		
		int curr = getConfirmations();
		while (c != null) {
			curr++;
			c.confirmations = curr;
			c = c.getPrevious();
		}
		
		
		stopObservingIfComplete();
	}
	
	
	public int getConfirmations() {
		return confirmations;
	}
	
	public int getLength() {
		
		// TODO cache the value if we get all the way
		// back to the genesis block
		
		LinkedBlock c = this;
		int toR = 0;
		
		while (c != null) {
			toR++;
			c = c.getPrevious();
		}
		
		return toR;
	}
	

	
	public Block getB() {
		return b;
	}
	
	private boolean stopObservingIfComplete() {		
		if (previous == null && !b.isGenises())
			return false;
		
		bo.deleteListener(this);
		return true;
	}
	
	public String getMissingHashFromChain() {
		LinkedBlock curr = this;
		
		while (curr.getPrevious() != null) {
			curr = curr.getPrevious();
		}
		
		if (curr.getB().getPreviousHash().length() == 0)
			return null;
		
		return curr.getB().getPreviousHash();
	}




	@Override
	public boolean processBlock(LinkedBlock b) {
		if (previous == null) {
			if (getB().getPreviousHash().equals(b.getB().getHash())) {
				// we found the previous block!
				setPrevious(b);
				return false;
			}
		}
		
		return true;
				
	}
	
	@Override
	public String toString() {
		String toR = (getPrevious() != null ? getPrevious().getB() : "null") +
				" <- " + getB() + " (confirmations: " + confirmations + ")";
		
		if (getPrevious() == null && !getB().isGenises()) {
			toR += " expecting: " + getB().getPreviousHash().substring(getB().getPreviousHash().length() - 10);
		}
		
		return toR;
	}
	
	
	
	
}
