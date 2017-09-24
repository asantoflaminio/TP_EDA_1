package AVL;

public abstract class AvlOperation<T> extends Operation<T> {

	private T elem;

	public AvlOperation(String op, T elem) {
		super(op);
		this.elem = elem;
	}

	public T getElem() {
		return elem;
	}
	
	public BlockChain<T> apply(BlockChain<T> blockChain) {
		return blockChain.add(this);
	}
	
	public abstract boolean apply(AvlTree<T> avl);
}
