
public class Add<T> extends AvlOperation<T> {

	public Add(String op, T elem) {
		super(op, elem);
	}

	@Override
	public BlockChain<T> apply(BlockChain<T> blockChain) {
		return blockChain.add(this);
	}

	public AvlTree<T> apply(AvlTree<T> avl) {
		return avl.insert(getElem());
	}

}
