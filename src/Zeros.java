package AVL;

import java.util.Comparator;

public class Zeros<T> extends Operation<T> {

	private Comparator<T> cmp;
	private int elem;

	public Zeros(String op, int elem, Comparator<T> cmp) {
		super(op);
		this.elem = elem;
		this.cmp = cmp;
	}

	@Override
	public BlockChain<T> apply(BlockChain<T> blockChain) {
		return new BlockChain<T>(elem, cmp);
	}

}
