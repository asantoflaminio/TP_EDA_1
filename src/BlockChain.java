package AVL;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;


import javax.xml.bind.DatatypeConverter;

public class BlockChain<T> {

	private final int zeros;
	private Block<T> last;
	private AvlTree<T> avl;

	public BlockChain(final int zeros, Comparator<T> cmp) {
		this.last = null;
		this.zeros = zeros;
		this.avl = new AvlTree(cmp);
	}

	private class Block<T> {

		private int index;
		private long nonce;
		private String prevHash;
		private String hash;
		private String data;
		private boolean validate;
		private Block<T> prevBlock;

		public Block(String data, Block<T> prevBlock, boolean val) {
			this.data = data;
			this.prevBlock = prevBlock;
			if (prevBlock != null) {
				this.prevHash = prevBlock.hash;
				this.index = prevBlock.index + 1;
			} else {
				this.prevHash = "0000000000000000000000000000000000000000000000000000000000000000";
				this.index = 0;
			}
			this.validate = val;

		}

		
		public void printBlock() {
			System.out.println("Block index: " + this.index);
			System.out.println("Hash: " + this.hash);
			System.out.println("Previous hash: " + this.prevHash);
			System.out.println("Nonce: " + this.nonce);
			System.out.println("Operation: " + this.data);
			System.out.println(this.validate? "Successful operation" : "Failed operation");
			avl.printInfo();
			System.out.println("----------------------------------------------------------");
		}

		private void generateHash(int zeros) throws NoSuchAlgorithmException {

			String s = "";
			for (int i = 0; i < zeros; i++)
				s += '0';

			Boolean flag = false;
			long i = 0;
			String prueba2 = "";
			while (!flag) {
				/*
				 * Coloco el numero magico a utilizar delante del que seria el String
				 * representativo del arbol
				 */
				String prueba = new String(i + "-" + this.prevHash + "-" + this.index + '-' + this.data);
				/*
				 * Creo el Hash del
				 */
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				byte[] hash = digest.digest(prueba.getBytes(StandardCharsets.UTF_8));
				prueba2 = DatatypeConverter.printHexBinary(hash);
				;

				/*
				 * Verifico que los n primeros bytes sean 0
				 */
				if (prueba2.substring(0, zeros).equals(s)) {
					flag = true;
				}
				i++;
			}
			this.nonce = i;
			this.hash = prueba2;
		}

		public void setData(String data) {
			this.data = data;
		}

	}

	public void add(String command, T elem) throws NoSuchAlgorithmException {
		if (elem != null) {

			if (this.add(elem)) {
				this.last = new Block(command, this.last, true);
			} else {
				this.last = new Block(command, this.last, false);
			}

			this.last.generateHash(this.zeros);
			this.last.printBlock();
		} else {
			System.out.println("Invalid command");
		}

	
	}

	public void remove(String command, T elem) throws NoSuchAlgorithmException {
		if (elem != null) {
			if (this.remove(elem)) {
				this.last = new Block(command, this.last, true);
			} else {
				this.last = new Block(command, this.last, false);
			}

			this.last.generateHash(this.zeros);
			this.last.printBlock();
		} else {
			System.out.println("Invalid command");
		}

	}

	public void lookUp(String command, T elem) {
		if (elem != null) {
			List<Integer> ls = lookUp(elem);
			if (ls != null) {
				System.out.println("Indeces of modified blocks: ");
				for (Integer i : ls) {
					System.out.println("Block index:" + i);
				}
			} else {
				System.out.println("The element " + elem + " doesn't exist in the tree");
			}
		} else {
			System.out.println("Invalid command");
		}

	}

	public void validate() {
		String toCmp = "";
		for (int i = 0; i < zeros; i++)
			toCmp += '0';

		boolean valid = validate(this.last, toCmp);

		System.out.print("Blockchain status: ");
		System.out.println(valid ? "valid" : "invalid");
	}

	public void modify(String command) {

		boolean flag = true;
		int i = 7;
		int acu = 0;
		while (flag && command.charAt(i) != ' ') {
			if (command.charAt(i) < '0' || command.charAt(i) > '9') {
				flag = false;
			} else {
				acu *= 10;
				acu += command.charAt(i) - '0';
			}
			i++;
		}

		if (acu > this.last.index || !flag) {
			System.out.println("The data entered is not valid");
		} else {
			modify(acu, command.substring(i));
		}

	}

	private boolean add(T elem) {
		if (last == null)
			return this.avl.insert(elem, 0);
		else
			return this.avl.insert(elem, last.index + 1);
	}

	private boolean remove(T elem) {
		if (last == null)
			return this.avl.remove(elem, 0);
		else
			return this.avl.remove(elem, last.index + 1);
	}

	private List<Integer> lookUp(T elem) {
		List<Integer> list = this.avl.contains(elem);
		return list;
	}

	private boolean validate(Block<T> current, String s) {
		if (current == null)
			return true;
		if (!current.hash.substring(0, this.zeros).equals(s))
			return false;
		return validate(current.prevBlock, s);
	}

	private void modify(int n, String filePath) {

		// Saco los corchetes
		filePath = filePath.substring(2, filePath.length() - 1);

		System.out.println("Taking data from: " + filePath);
		try {
			String op = new String(Files.readAllBytes(Paths.get(filePath)));
			System.out.println("The new block operation " + n + " is: " + op);
			/*
			 * NO SE si hay que chequear si esa operacion es valida o no yo creo que no
			 * porque da igual y la blockchain se rompe pongas lo q pongas pueden ponerte
			 * cualquier cosa y se tiene q romper entiendo yo
			 */
			Block<T> current = this.last;
			int flag = 0;
			while (current != null && flag == 0) {
				if (current.index == n) {
					flag = 1;
				}
			}

			current.setData(op);
			// Modifico la operacion guardada

			// A PARTIR DE ACA VENDRIA LO DEL HASH y VALIDACION etc

		} catch (IOException ex) {
			System.out.println("Could not find file");
			return;
		}

	}

}