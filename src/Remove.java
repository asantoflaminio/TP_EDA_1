
public class Remove<T> extends AvlOperation<T> {

	public Remove(String op, T elem) {
		super(op, elem);
	}

	@Override
	public BlockChain<T> apply(BlockChain<T> blockChain) {
		return blockChain.add(this);
	}

	public AvlTree<T> apply(AvlTree<T> avl) {
		return avl.remove(getElem());
	}
}

