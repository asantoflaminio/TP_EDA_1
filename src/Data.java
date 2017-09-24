package AVL;

public class Data<T> {

	private Operation<T> operation;
	private boolean operationState;
	private AvlTree<T> avl;

	public Data(Operation<T> operation, boolean operationState, AvlTree<T> avl) {
		this.operation = operation;
		this.operationState = operationState;
		this.avl = avl;
	}

	public Operation<T> getOperation() {
		return operation;
	}

	public AvlTree<T> getAvl() {
		return avl;
	}

	public boolean getOperationState() {
		return operationState;
	}

	public String toString() {
		String s = "Operation: " + operation.getOperation() + "\n";
		s += operationState ? "Operación exitosa" : "Operación fallida";
		return s;
	}

}
