package AVL;

import java.util.Comparator;

public class Zeros<T> {
	private final int zeros;

	public Zeros(int zeros) {
		this.zeros = zeros;
	}

	public BlockChain createBlockchain(Comparator cmp) {
		return new BlockChain<T>(this.zeros, cmp);
	}
}