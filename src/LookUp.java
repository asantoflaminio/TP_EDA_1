package AVL;

public class LookUp<T> extends AvlOperation<T> {

	public LookUp(String op, T elem) {
		super(op, elem);
	}

	public boolean apply(AvlTree<T> avl) {
		return avl.contains(getElem());
	}

	//Falta retornar todos los índices de bloques que modificaron el nodo

}
