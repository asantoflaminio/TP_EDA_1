package AVL;

public class Remove<T> extends AvlOperation<T> {

	public Remove(String op, T elem) {
		super(op, elem);
	}

	public boolean apply(AvlTree<T> avl) {
		return avl.remove(getElem());
	}
}
