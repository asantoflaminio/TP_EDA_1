

public class Node<T> {

		T value;
		Node<T> leftTree;
		Node<T> rightTree;
		int height;
		int xpos;  //posiciones X e Y del nodo en el arbol
		int ypos;  //solo se usan en la funcion draw

		public Node(T value) {
			this.value = value;
			this.height = 0;
			this.leftTree = null;
			this.rightTree = null;
		}
}