
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

	public BlockChain<T> add(AvlOperation<T> operation) {
		AvlTree<T> avl;
		Data<T> data;

		if (last != null)
			avl = last.getData().getAvl();
		else
			avl = new AvlTree<T>(cmp);

		data = new Data<T>(operation, operation.apply(avl));
		last = addIterative(data);
		avl.print(); // Sacar
		return this;
	}

	public Block<T> addIterative(Data<T> data) {
		return new Block<T>(new Random().nextInt(10000), data, last);
	}

	private static class Block<T> {

		private static int lastIndex;
		private static int lastPrevHash;
		private int index;
		private int nonce;
		private Data<T> data;
		private int hash;
		private int prevHash;
		private Block<T> prevBlock;

		public Block(int nonce, Data<T> data, Block<T> prevBlock) {
			this.index = ++lastIndex;
			this.nonce = nonce;
			this.data = data;
			this.hash = hash();
			this.prevHash = lastPrevHash;
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

		public int hash() {
			return 1; // SHA256
		}
	}

}

