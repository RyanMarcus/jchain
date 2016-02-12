package info.rmarcus.blockchain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

public abstract class BlockEmitter {

	private Set<BlockListener> l;
	private LinkedBlock genesis;
	private Map<String, LinkedBlock> blocks;
	private LinkedBlock head;
	
	public BlockEmitter() {
		l = Collections.newSetFromMap(Maps.newConcurrentMap());
		genesis = null;
		blocks = new HashMap<String, LinkedBlock>();
	}
	
	public synchronized void addListener(BlockListener bl) {
		l.add(bl);
	}
	
	public synchronized void deleteListener(BlockListener bl) {
		l.remove(bl);
	}
	
	protected synchronized void emitBlock(LinkedBlock b) {
		

		
		blocks.put(b.getB().getHash(), b);
		
		if (b.getB().getBlockID() == 0)
			genesis = b;
		
		Iterator<BlockListener> it = l.iterator();
		while (it.hasNext()) {
			BlockListener bl = it.next();
			if (!bl.processBlock(b))
				it.remove();
			
			
			//System.out.println("gave " + b + " to " + bl);
		}
		
		// TODO make this not require scanning all blocks...
		head = blocks.values()
				.stream()
				.max((b1, b2) -> b1.getLength() - b2.getLength()).get();
		
//		synchronized (System.out) {
//			System.out.println("----- " + b.getB().getBlockID());
//			blocks.values().forEach(System.out::println);
//			System.out.println("still waiting:");
//			l.forEach(System.out::println);
//			
//			System.out.println("-----");
//		}
	}
	
	public synchronized LinkedBlock getBlockByHash(String hash) {
		return blocks.get(hash);
	}
	
	
	public synchronized LinkedBlock getGenesis() {
		return genesis;
	}
	
	public synchronized LinkedBlock getHeadBlock() {
		return head;
	}

}
