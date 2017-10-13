package avl;

import java.util.Comparator;
import java.util.LinkedList;

public class AvlTree<T> {

	private Node<T> root;
	private Comparator<T> cmp;

	public AvlTree(Comparator<T> cmp) {
		this.cmp = cmp;
	}

	private static class Node<T> {

		T value;
		Node<T> leftTree;
		Node<T> rightTree;
		List<Integer> blocksModified;
		int height;

		public Node(T value) {
			this.value = value;
			this.height = 0;
			this.leftTree = null;
			this.rightTree = null;
			this.blocksModified = new List<Integer>();
		}

	}

	/**
	 * 
	 * @param elem
	 *            Elemento a insertar
	 * @param blockIndex
	 *            Indice del bloque que va a modificar el arbol
	 * @return Devuelve true si se pudo agregar el elemento
	 * @see add
	 * 
	 *      Es la funcion wrapper que se encarga de agregar un elemento al arbol
	 */

	public boolean insert(T elem, int blockIndex) {
		Check c = new Check(true);
		this.root = add(this.root, elem, blockIndex, c);
		return c.getValue(); // SACAR
	}

	/**
	 * 
	 * @param current
	 *            Nodo en cuestion de la funcion recursiva
	 * @param elem
	 *            Elemento a insertar
	 * @param blockIndex
	 *            Indice del bloque que va a modificar el arbol
	 * @return Retorna el nodo en cuestion see getHeight, balanceTree
	 */

	private Node<T> add(Node<T> current, T elem, int blockIndex, Check c) {
		if (current == null) {
			Node<T> node = new Node<T>(elem);
			node.blocksModified.add(blockIndex);
			return node;
		}
		Node<T> auxLeft = current.leftTree;
		Node<T> auxRight = current.rightTree;
		if (cmp.compare(current.value, elem) == 0) {
			c.setValue(false);
			return current;
		}
		if (cmp.compare(current.value, elem) > 0)
			current.leftTree = add(current.leftTree, elem, blockIndex, c);
		else
			current.rightTree = add(current.rightTree, elem, blockIndex, c);
		current.height = getHeight(current);
		current = balanceTree(current, blockIndex);
		if (!sameSons(auxLeft, auxRight, current))
			current.blocksModified.add(blockIndex);
		return current;
	}

	/**
	 * 
	 * @param left
	 *            Hijo izquierdo a comparar
	 * @param right
	 *            Hijo derecho a comparar
	 * @param current
	 *            Nodo en cuestion al cual compararle los hijos
	 * @return Retorna un booleano que dice si los hijos eran iguales o alguno
	 *         cambio
	 */

	private boolean sameSons(Node<T> left, Node<T> right, Node<T> current) {
		if (left == current.leftTree && right == current.rightTree)
			return true;
		return false;
	}

	/**
	 * 
	 * @param elem
	 *            Elemento en cuestion a eliminar
	 * @param blockIndex
	 *            Indice del bloque que va a modificar el arbol
	 * @return Retorna un booleano para indicar si pudo realizar la operacion o no
	 * @see delete Es la funcion wrapper de la funcion recursiva delete
	 */

	public boolean remove(T elem, int blockIndex) {
		Check flag = new Check(true);
		root = delete(this.root, elem, blockIndex, flag);
		return flag.getValue();
	}

	/**
	 * 
	 * @param current
	 *            Nodo en cuestion de la funcion recursiva
	 * @param elem
	 *            Elemento a eliminar del arbol
	 * @param blockIndex
	 *            Indice del bloque que va a modificar el arbol
	 * @param flag
	 *            Indica si la operacion se realizo o no
	 * @return Retorna el nodo en cuestion
	 */

	private Node<T> delete(Node<T> current, T elem, int blockIndex, Check flag) {
		if (current == null) {
			flag.setValue(false);
			return null;
		}
		Node<T> auxLeft = current.leftTree;
		Node<T> auxRight = current.rightTree;
		if (cmp.compare(current.value, elem) == 0) {
			flag.setValue(true);
			if (noSons(current)) {
				return null;
			} else if (oneSon(current)) {
				if (current.leftTree != null)
					return current.leftTree;
				else
					return current.rightTree;
			} else {
				Node<T> result = new Node<T>(current.value);
				current.leftTree = getMaxLeaf(current.leftTree,result,blockIndex,flag);
				if (!sameSons(auxLeft, auxRight, result)) {
					result.blocksModified.add(blockIndex);
				}
				result.leftTree = current.leftTree;
				result.rightTree = current.rightTree;
				result.height = getHeight(result);
				result = balanceTree(result, blockIndex);
				return result;
			}
		} else {
			if (cmp.compare(current.value, elem) > 0)
				current.leftTree = delete(current.leftTree, elem, blockIndex, flag);
			else
				current.rightTree = delete(current.rightTree, elem, blockIndex, flag);
		}

		current.height = getHeight(current);
		current = balanceTree(current, blockIndex);
		
		if (!sameSons(auxLeft, auxRight, current)) {
			current.blocksModified.add(blockIndex);
		}

		return current;

	}
	
	/**
	 * 
	 * @param current
	 *            Nodo en cuestion de la funcion recursiva
	 * @return Retorna el nodo current La funcion sirve para buscar el nodo que
	 *         tenga el valor maximo del subArbol izquierdo del nodo que llamo a la
	 *         funcion y eliminarlo llevando consigo una copia y borrando el encontrado
	 */

	private Node<T> getMaxLeaf(Node<T> current, Node<T> result, int blockIndex, Check flag) {
		Node<T> auxLeft = current.leftTree;
		Node<T> auxRight = current.rightTree;
		if (current.rightTree != null) {
			current.rightTree = getMaxLeaf(current.rightTree,result,blockIndex,flag);
			current.height = getHeight(current);
			current = balanceTree(current, blockIndex);	
			if (!sameSons(auxLeft, auxRight, current)) {
				current.blocksModified.add(blockIndex);
			}
		}
		else {
			result.value = current.value;
			result.blocksModified = current.blocksModified;
			current = delete(current,current.value,blockIndex, flag);
		}
		return current;
	}

	/**
	 * 
	 * @param current
	 *            Nodo en cuestion el cual la funcion chequea si debe balancear
	 * @param blockIndex
	 *            Indice del bloque que va a modificar el arbol
	 * @return Retorna el nodo balanceado
	 * @see getBalance La funcion balanceTree revisa los posibles casos de las
	 *      rotaciones y decide que rotacion debe hacerse para balancear el arbol
	 */

	private Node<T> balanceTree(Node<T> current, int blockIndex) {
		if (current.leftTree != null && current.rightTree != null) {
			if (((current.leftTree.height + 1) - (current.rightTree.height + 1)) < -1) {
				if (getBalance(current.rightTree) > 0)
					return rotateLeftRight(current, blockIndex);
				return rotateLeft(current, blockIndex);
			} else if (((current.leftTree.height + 1) - (current.rightTree.height + 1)) > 1) {
				if (getBalance(current.leftTree) < 0)
					return rotateRightLeft(current, blockIndex);
				return rotateRight(current, blockIndex);
			}
		} else if (current.leftTree != null) {
			if (current.leftTree.height + 1 > 1) {
				if (getBalance(current.leftTree) < 0)
					return rotateRightLeft(current, blockIndex);
				return rotateRight(current, blockIndex);
			}
		} else if (current.rightTree != null) {
			if (current.rightTree.height + 1 > 1) {
				if (getBalance(current.rightTree) > 0)
					return rotateLeftRight(current, blockIndex);
				return rotateLeft(current, blockIndex);
			}
		}
		return current;
	}

	/**
	 * 
	 * @param current
	 *            Nodo en cuestion de la funcion
	 * @return Retorna el factor de balance del nodo current
	 */

	private int getBalance(Node<T> current) {
		if (noSons(current))
			return 0;
		if (current.leftTree == null)
			return -current.rightTree.height - 1;
		if (current.rightTree == null)
			return current.leftTree.height + 1;
		return (current.leftTree.height + 1) - (current.rightTree.height + 1);
	}

	/**
	 * 
	 * @param current
	 *            Nodo en cuestion de la funcion
	 * @return Retorna un numero con la altura actual del nodo current Se utiliza
	 *         principalmente para actualizar la altura del nodo luego de haberse
	 *         hecho agregados, borrados o rotaciones
	 */

	private int getHeight(Node<T> current) {
		if (current.leftTree != null && current.rightTree != null)
			return Math.max(current.leftTree.height, current.rightTree.height) + 1;
		else if (current.leftTree != null)
			return current.leftTree.height + 1;
		else if (current.rightTree != null)
			return current.rightTree.height + 1;
		else
			return 0;
	}

	/**
	 * 
	 * @param current
	 *            Nodo en cuestion de la funcion
	 * @return Retorna un boolean para reconocer si el nodo current tiene almenos un
	 *         hijo
	 */

	private boolean noSons(Node<T> current) {
		if (current.leftTree == null && current.rightTree == null)
			return true;
		return false;
	}

	/**
	 * 
	 * @param current
	 *            Nodo en cuestion de la funcion
	 * @return Retorna un boolean para reconocer si el nodo current tiene
	 *         exactamente un hijo
	 */

	private boolean oneSon(Node<T> current) {
		if (current.leftTree != null && current.rightTree == null)
			return true;
		if (current.leftTree == null && current.rightTree != null)
			return true;
		return false;
	}


	/**
	 * 
	 * @param big
	 *            Representa al nodo desbalanceado que llamo a la rotacion
	 * @param blockIndex
	 *            Indice del bloque que va a modificar el arbol
	 * @return Retorna el nodo que ocupara el lugar del nodo Big luego del balanceo
	 * @see rotateRightLeft, rotateLeft, rotateRight, rotateLeftRight Para todas las
	 *      funciones nombradas en @see sirve lo argumentado en este comentario
	 */

	private Node<T> rotateRight(Node<T> big, int blockIndex) {
		Node<T> aux = big.leftTree;
		big.leftTree = aux.rightTree;
		aux.rightTree = big;
		big.height = aux.height - 1;
		big.height = getHeight(big);
		aux.height = getHeight(aux);
		big.blocksModified.add(blockIndex);
		aux.blocksModified.add(blockIndex);
		return aux;
	}

	private Node<T> rotateRightLeft(Node<T> big, int blockIndex) {
		big.leftTree.blocksModified.add(blockIndex);
		big.leftTree.rightTree.height++;
		big.leftTree = rotateLeft(big.leftTree, blockIndex);
		big = rotateRight(big, blockIndex);
		big.height = getHeight(big);
		return big;
	}

	private Node<T> rotateLeft(Node<T> big, int blockIndex) {
		Node<T> aux = big.rightTree;
		big.rightTree = aux.leftTree;
		aux.leftTree = big;
		big.height = aux.height - 1;
		big.blocksModified.add(blockIndex);
		aux.blocksModified.add(blockIndex);
		big.height = getHeight(big);
		aux.height = getHeight(aux);
		return aux;
	}

	private Node<T> rotateLeftRight(Node<T> big, int blockIndex) {
		big.rightTree.blocksModified.add(blockIndex);
		big.rightTree.leftTree.height++;
		big.rightTree = rotateRight(big.rightTree, blockIndex);
		big = rotateLeft(big, blockIndex);
		big.height = getHeight(big);
		return big;
	}
	
	/**
	 * Busca el elemento pedido en el arbol
	 * 
	 * @param elem
	 *            Elemento a buscar
	 * @return Retorna una lista con los bloques que modificaron al nodo que posee
	 *         el elemento pedido Sino retorna null, en caso de no ser encontrado
	 */

	public List<Integer> contains(T elem) {
		Node<T> searched = search(this.root, elem);
		if (searched != null)
			return searched.blocksModified;
		else
			return null;
	}

	/**
	 * 
	 * @param current
	 *            Nodo en cuestion de la recursion
	 * @param elem
	 *            Elemento a buscar
	 * @return Devuelve el nodo que contiene al elemento
	 */
	private Node<T> search(Node<T> current, T elem) {
		if (current == null)
			return null;
		if (this.cmp.compare(current.value, elem) == 0)
			return current;

		if (this.cmp.compare(current.value, elem) < 0)
			return search(current.rightTree, elem);
		else
			return search(current.leftTree, elem);

	}

	public int getHeight() {
		int result = -1 + calculateHeight(root);
		return result;
	}

	private int calculateHeight(Node<T> node) {
		if (node == null)
			return 0;
		return 1 + Math.max(calculateHeight(node.leftTree), calculateHeight(node.rightTree));
	}

	public void printByLevels() {
		for (int i = 0; i <= this.getHeight(); i++) {
			System.out.print("Nodes at level " + i + ": ");
			printLevel(0, i, root);
			System.out.println();
		}
	}

	private void printLevel(int currLevel, int index, Node<T> node) {
		if (node == null)
			return;
		if (currLevel != index) {
			printLevel(currLevel + 1, index, node.leftTree);
			printLevel(currLevel + 1, index, node.rightTree);
		} else {
			System.out.print(node.value + " ");
		}
		return;
	}

	public boolean isBTS() {
		LinkedList<T> list = new LinkedList<T>();
		if (!isBTS(root, list))
			return false;
		for (int i = 0; i < list.size() - 1; i++) {
			if (cmp.compare(list.get(i), list.get(i + 1)) > 0)
				return false;
		}
		return true;
	}

	private boolean isBTS(Node<T> node, LinkedList<T> list) {
		if (node == null)
			return true;
		boolean left = true;
		boolean right = true;
		if (node.leftTree != null) {
			if (cmp.compare(node.value, node.leftTree.value) < 0)
				return false;
			left = isBTS(node.leftTree, list);
		}
		list.add(node.value);
		if (node.rightTree != null) {
			if (cmp.compare(node.value, node.rightTree.value) > 0)
				return false;
			right = isBTS(node.rightTree, list);
		}
		return left && right;
	}

	public boolean isAVL() {
		return isAVL(root);
	}

	private boolean isAVL(Node<T> current) {
		if (current == null)
			return true;
		if(getBalance(current) < -1 || getBalance(current) > 1) {
			System.out.println("Returns false by the node: " + current.value);
			return false;
		}
		return isAVL(current.leftTree) && isAVL(current.rightTree);

	}



	public void showModifications() {
		showModificationsOnNodes(this.root);
	}

	private void showModificationsOnNodes(Node<T> current) {
		if (current == null)
			return;
		System.out.println("Node: " + current.value);
		current.blocksModified.showElements();
		showModificationsOnNodes(current.leftTree);
		showModificationsOnNodes(current.rightTree);

	}
	
	public int getHashCode() {
		return getHashCode(this.root).hashCode();
	}
	
	public String getHashCode(Node<T> current) {
		if(current == null)
			return "";
		String s = current.value.toString();
		s += "-";
		s += getHashCode(current.leftTree);
		s += getHashCode(current.rightTree);
		return s;
	}
	
}
