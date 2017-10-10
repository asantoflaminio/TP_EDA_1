package AVL;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class List<T> implements Iterable<T> {

	private Node<T> first;
	private Node<T> last;

	public void add(T elem) {
		if (first == null) {
			first = new Node<T>(elem);
			last = first;
			return;
		}

		if (last.value == elem)
			return;
		last.next = new Node<T>(elem);
		last = last.next;

	}

	public void showElements() {
		Node<T> aux = first;
		System.out.print("The blocks that modified this node were: ");
		while (aux != null) {
			System.out.print(aux.value + " ");
			aux = aux.next;
		}
		System.out.println();

	}
	

	@Override
	public Iterator<T> iterator() {
		return new MyListIterator<T>(first);
	}

	private class MyListIterator<T> implements Iterator<T> {
		
		private Node<T> next;

		public MyListIterator(Node<T> first) {
			next = first;
		}

		public boolean hasNext() {
			return next != null;
		}

		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();

			T value = next.value;
			next = next.next;
			return value;

		}

	}

	private static class Node<T> {

		private Node<T> next;
		private T value;

		public Node(T value) {
			this.value = value;
		}

		public Node<T> getNext() {
			return next;
		}

		public T getValue() {
			return value;
		}

	}

}
