package AVL;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

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

		// chain = new BlockChain<Integer>(2,cmp);

	}

	/*
	 * Quiero testear cuanto toma realizar operaciones cuando la cantidad de ceros
	 * es alta. En este caso probaremos con 5 ceros y un tiempo mmaximo de 10
	 * segundos para realizar 4 operaciones.
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
	 * Para tres ceros y cuatro operaciones el tiempo es maximo 3 segundos.
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
	 * Para 1 cero y cuatro operaciones el tiempo maximo es 0.1 segundos.
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
	 * Para 7 ceros y dos operaciones el tiempo maximo es 15 minutos.
	 */
	@Test(timeout = 900000)
	public void testSpeed7() throws NoSuchAlgorithmException {
		chain = new BlockChain<Integer>(7, cmp);
		chain.add("add", 9);
		chain.remove("remove", 9);

	}

	/*
	 * public void testLookUp() throws NoSuchAlgorithmException{ chain = new
	 * BlockChain<Integer>(3,cmp); chain.add("add", 5); chain.add("remove", 5);
	 * chain.add("add", 4); chain.lookUp("lookUp", 5); }
	 * 
	 * @Test public void testModify() throws NoSuchAlgorithmException{ chain = new
	 * BlockChain<Integer>(3,cmp); chain.add("add", 5); chain.add("remove", 5);
	 * chain.add("add", 4); chain.modify("modify"); assertEquals(false,
	 * chain.validate()); }
	 */

}