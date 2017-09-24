package AVL;

public abstract class Operation<T> {

	private String op;

	public Operation(String op) {
		this.op = op;
	}

	public String getOperation() {
		return op;
	}
	
	public abstract BlockChain<T> apply(BlockChain<T> blockChain);
}
