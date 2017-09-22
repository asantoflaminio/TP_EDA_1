
public abstract class AvlOperation<T> extends Operation<T> {

	private T elem;

	public AvlOperation(String op, T elem) {
		super(op);
		this.elem = elem;
	}

	public T getElem() {
		return elem;
	}

	public abstract AvlTree<T> apply(AvlTree<T> avl);
}
