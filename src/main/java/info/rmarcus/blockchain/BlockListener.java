package info.rmarcus.blockchain;

public interface BlockListener {
	public boolean processBlock(LinkedBlock b);
}
