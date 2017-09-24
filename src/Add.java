package AVL;

public class Add<T> extends AvlOperation<T> {
	
	public Add(String op, T elem) {
		super(op, elem);
	}

	public boolean apply(AvlTree<T> avl) {
		return avl.insert(getElem());
	}
	


}
