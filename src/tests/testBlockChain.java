package tests;


import org.junit.Test;

import blockchain.BlockChain;


/*
 * El objetivo es testear cuanto tiempo toma completar operaciones luego
 * de haber indicado la funcion zeros con diferentes numeros.
 * 
 */

import java.security.NoSuchAlgorithmException;
import java.util.Comparator;

import org.junit.Before;

public class TestBlockChain {
	BlockChain<Integer> chain;
	Comparator<Integer> cmp = new Comparator<Integer>() {

		@Override
		public int compare(Integer o1, Integer o2) {
			return o1 - o2;
		}
	};

	@Before
	public void setUp() throws Exception {

		//

	}

	/*
	 * Para cinco ceros y cuatro operaciones el tiempo limite es 10s. 
	 */
	@Test(timeout = 10000)
	public void testSpeed5() throws NoSuchAlgorithmException {
		chain = new BlockChain<Integer>(5, cmp);
		chain.add("add", 5);
		chain.add("add", 6);
		chain.add("add", 8);
		chain.remove("remove", 5);

	}

	/*
	 * Para tres ceros y cuatro opearaciones el tiempo limite es 3s. 
	 */
	@Test(timeout = 3000)
	public void testSpeed3() throws NoSuchAlgorithmException {
		chain = new BlockChain<Integer>(3, cmp);
		chain.add("add", 5);
		chain.add("add", 6);
		chain.add("add", 8);
		chain.remove("remove", 5);

	}

	/*	
	 * Para un cero y cuatro operaciones el tiempo limite es 0.1s.
	 */
	@Test(timeout = 100)
	public void testSpeed1() throws NoSuchAlgorithmException {
		chain = new BlockChain<Integer>(1, cmp);
		chain.add("add", 5);
		chain.add("add", 6);
		chain.add("add", 8);
		chain.remove("remove", 5);

	}

	/*
	 * 
	 * Para seis ceros y una operacion el tiempo limite es 10s.
	 */
	@Test(timeout = 10000)
	public void testSpeed6() throws NoSuchAlgorithmException {
		chain = new BlockChain<Integer>(6, cmp);
		chain.add("add", 1);


	}

	
}