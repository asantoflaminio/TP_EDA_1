
public class Data<T> {

	private Operation<T> operation;
	private AvlTree<T> avl;

	public Data(Operation<T> operation, AvlTree<T> avl) {
		this.operation = operation;
		this.avl = avl;
	}

	public Operation<T> getOperation() {
		return operation;
	}

	public AvlTree<T> getAvl() {
		return avl;
	}

}
