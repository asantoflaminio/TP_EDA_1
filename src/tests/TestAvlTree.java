package tests;

import static org.junit.Assert.*;


import org.junit.Test;

import avl.AvlTree;

import java.util.Comparator;
import org.junit.Before;

public class TestAvlTree {
	private AvlTree<Integer> tree;

	@Before
	public void setUp() throws Exception {
		Comparator<Integer> cmp = new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}

		};
		tree = new AvlTree<Integer>(cmp);

	}

	/*
	 * Chequeo el metodo isAVL
	 */
	@Test(timeout = 100)
	public void testIsAVL() {
		tree.insert(4, 0);
		tree.insert(1, 1);
		tree.insert(2, 2);
		assertEquals(true, tree.isAVL());
	}

	/*
	 * Chequeo si remueve correctamente
	 */
	@Test(timeout = 100)
	public void testRemove() {
		tree.insert(4, 0);
		tree.insert(1, 1);
		tree.insert(2, 2);
		tree.remove(4, 3);
		assertEquals(1, tree.getHeight());
		tree.remove(2, 4);
		assertEquals(0, tree.getHeight());
	}

	/*
	 * Chequeo si la cantidad de nodos que debe devolver la lista al llamar a
	 * contains es correcta
	 */
	@Test(timeout = 100)
	public void testContains() {

		tree.insert(8, 0); // Modifica a 8: SI *se crea*
		tree.insert(6, 1); // Modifica a 8: SI *se agrega como hijo izq*
		tree.insert(9, 2); // Modifica a 8: SI *se agrega como hijo der*
		tree.insert(5, 3); // Modifica a 8: SI *rotacion*
		tree.insert(4, 4); //// Modifica a 8: NO.
		assertEquals(4, tree.contains(8).length());
	}

}