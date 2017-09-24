package AVL;

import java.util.Comparator;
import java.util.Random;

public class BlockChain<T> {

	private final int zeros;
	private Block<T> last;
	private Comparator<T> cmp;

	public BlockChain(final int zeros, Comparator<T> cmp) {
		this.last = null;
		this.zeros = zeros;
		this.cmp = cmp;
	}

	public Block<T> getLast() {
		return last;
	}

	public int getZeros() {
		return zeros;
	}

	public BlockChain<T> add(AvlOperation<T> operation) {
		AvlTree<T> avl;
		Data<T> data;

		if (last != null)
			avl = last.getData().getAvl();
		else
			avl = new AvlTree<T>(cmp);

		data = new Data<T>(operation, operation.apply(avl), avl);
		last = addBlock(data);

		last.print();
		// No esta guardando en la blockchain el avl de forma correcta
		// siempre guarda el ultimo avl

		return this;
	}

	private Block<T> addBlock(Data<T> data) {
		return new Block<T>(new Random().nextInt(10000), data, last);
	}

	private static class Block<T> {

		private static int lastIndex;
		private static int lastPrevHash;
		private int index;
		private int nonce;
		private int prevHash;
		private int hash;
		private Data<T> data;
		private Block<T> prevBlock;

		public Block(int nonce, Data<T> data, Block<T> prevBlock) {
			this.index = ++lastIndex;
			this.nonce = nonce;
			this.prevHash = lastPrevHash;
			this.hash = generateHash(index, nonce, prevHash, data);
			this.data = data;
			this.prevBlock = prevBlock;
			Block.lastPrevHash = hash;
		}

		public int getIndex() {
			return index;
		}

		public int getNonce() {
			return nonce;
		}

		public Data<T> getData() {
			return data;
		}

		public int getHash() {
			return hash;
		}

		public int getPrevHash() {
			return prevHash;
		}

		public Block<T> getPrevBlock() {
			return prevBlock;
		}

		public int generateHash(int index, int nonce, int prevHash, Data<T> data) {
			return 1; // SHA256
		}

		// Para probar, despues se cambia
		public void print() {
			System.out.println("Block index " + this.index);
			System.out.println("Avl Tree:");
			this.data.getAvl().print(); // Si quieren probar el draw de Ale poner .draw() pero abre una ventana por cada operacion
			System.out.println(data);
			System.out.println("--------------------");
		}

	}

}
