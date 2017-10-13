package blockchain;


import avl.List;
import avl.AvlTree;

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
		this.avl = new AvlTree<T>(cmp);
	}

	private class Block<T> {

		private int index;
		private long nonce;
		private String prevHash;
		private String hash;
		private String data;
		private boolean validate;
		private Block<T> prevBlock;
		private int hashTree;

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

		public void setData(String data) {
			this.data = data;
		}

		public void printBlock() {
			System.out.println("Block index: " + this.index);
			System.out.println("Hash: " + this.hash);
			System.out.println("Previous hash: " + this.prevHash);
			System.out.println("Nonce: " + this.nonce);
			System.out.println("Operation: " + this.data);
			System.out.println(this.validate ? "Successful operation" : "Failed operation");
			System.out.println("Hash tree: " + this.hashTree);
			avl.printByLevels();
			System.out.println("----------------------------------------------------------");
		}
		
		/*
		 * @param zeros cantidad de ceros que debe tener el elemento al principio del hash
		 */
		
		private void generateHash(int zeros) throws NoSuchAlgorithmException {

			String s = "";
			for (int i = 0; i < zeros; i++)
				s += '0';

			Boolean flag = false;
			long i = 0;
			String str = "";
			while (!flag) {
				/*
				 * Poner el nonce al principio del String de prueba
				 */
				String aux = new String(i + "-" + this.prevHash + "-" + this.index + "-" + this.data);
				/*
				 * Hasheo el String de prueba
				 */
				str = getHash(aux);

				/*
				 * Verifico que los primeros "zeros" caracteres sean 0
				 */
				if (str.substring(0, zeros).equals(s)) {
					flag = true;
				}
				i++;
			}
			this.nonce = i;
			this.hash = str;
		}
		
		
		private void reHash() throws NoSuchAlgorithmException {
			String aux = new String(this.nonce + "-" + this.prevHash + "-" + this.index + "-" + this.data);
			this.hash = getHash(aux);
		}

		private String getHash(String str) throws NoSuchAlgorithmException {

			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
			str = DatatypeConverter.printHexBinary(hash);

			return str;
		}

	}
	
	/**
	 * @param command 
	 * 		El string que dice add
	 * @param elem
	 * 			Valor del nodo con el que se opera. 
	 * 
	 * Agrega al árbol un nodo con el elemento y crea un nuevo bloque.
	 */
	public void add(String command, T elem) throws NoSuchAlgorithmException {
		if (this.isValid()) {
			if (elem != null) {

				if (this.add(elem)) {
					this.last = new Block<T>(command + " " + elem.toString(), this.last, true);
				} else {
					this.last = new Block<T>(command + " " + elem.toString(), this.last, false);
				}

				this.last.generateHash(this.zeros);
				this.last.hashTree = this.avl.getHashCode();	
				this.last.printBlock();	
				
			} else {
				System.out.println("Invalid command");
			}
		} else {
			System.out.println("Invalid BlockChain. You can not apply any operation on it");
		}

	}

	private boolean add(T elem) {
		if (last == null)
			return this.avl.insert(elem, 0);
		else
			return this.avl.insert(elem, last.index + 1);
	}
	
	
	/**
	 * @param command 
	 * 		El string que dice remove
	 * @param elem
	 * 			Valor del nodo con el que se opera. 
	 * 
	 * Saca del arbol un elemento y crea un nuevo bloque. 
	 */
	public void remove(String command, T elem) throws NoSuchAlgorithmException {
		if (this.isValid()) {
			if (elem != null) {
				if (this.remove(elem)) {
					this.last = new Block<T>(command + " " + elem.toString(), this.last, true);
				} else {
					this.last = new Block<T>(command + " " + elem.toString(), this.last, false);
				}

				this.last.generateHash(this.zeros);
				this.last.hashTree = this.avl.getHashCode();
				this.last.printBlock();
			} else {
				System.out.println("Invalid command");
			}
		} else {
			System.out.println("Invalid BlockChain. You can not apply any operation on it");
		}
	}

	private boolean remove(T elem) {
		if (last == null)
			return this.avl.remove(elem, 0);
		else
			return this.avl.remove(elem, last.index + 1);
	}
	
	/**
	 * @param command 
	 * 		El string que dice lookUp
	 * @param elem
	 * 			Valor del nodo con el que se opera. 
	 * 
	 * Imprime todos los indices de bloques que modificaron al elemento elem. 
	 * Los bloques que modificaron a tal elemento serían cuando el mismo se agrega, cuando se 
	 * agrega o modifica algún hijo y cuando el elemento participa en alguna rotacion.
	 */
	public void lookUp(String command, T elem) {
		if (this.isValid()) {
			if (elem != null) {
				List<Integer> ls = lookUp(elem);
				if (ls != null) {
					System.out.println("Indexes of modified blocks: ");
					for (Integer i : ls) {
						System.out.println("Block index:" + i);
					}
				} else {
					System.out.println("The element " + elem + " doesn't exist in the tree");
				}
			} else {
				System.out.println("Invalid command");
			}
		} else {
			System.out.println("Invalid BlockChain. You can not apply any operation on it");
		}
	}

	private List<Integer> lookUp(T elem) {
		List<Integer> list = this.avl.contains(elem);
		return list;
	}


	private boolean isValid() {
		String toCmp = "";
		for (int i = 0; i < zeros; i++)
			toCmp += '0';

		boolean valid = validate(this.last, toCmp);

		return valid ? true : false;

	}
	
	/**
	 * Devuelve si la BlockChain sigue siendo valida, es decir comprueba
	 * si la cantidad de ceros iniciales en los hashes es la correcta.
	 */
	public void validate() {
		String toCmp = "";
		for (int i = 0; i < zeros; i++)
			toCmp += '0';

		boolean valid = validate(this.last, toCmp);

		System.out.print("Blockchain status: ");
		System.out.println(valid ? "valid" : "invalid");

	}

	private boolean validate(Block<T> current, String s) {
		if (current == null)
			return true;
		if (!current.hash.substring(0, this.zeros).equals(s))
			return false;
		return validate(current.prevBlock, s);
	}
	
	/**
	 * @param command 
	 * 		El string que dice modify
	 * 
	 *  Chequea que el comando sea valido.
	 */
	public void modify(String command) throws NoSuchAlgorithmException {
		if (this.isValid()) {
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
		} else {
			System.out.println("Invalid BlockChain. You can not apply any operation on it");
		}
	}
	
	/**
	 * @param n 
	 * 		El indice del bloque a modificar.
	 * @param filePath
	 * 			Lugar donde se encuentra el archivo con la nueva operacion. 
	 * 
	 * Modifica el contenido de un bloque y luego su hash para romper la cadena. Para eso llama
	 * luego a la funcion modifyRecursive.
	 */
	private void modify(int n, String filePath) throws NoSuchAlgorithmException {
		
		if(filePath.charAt(1)!= '[' && filePath.charAt(filePath.length()-1) != ']') {
			System.out.println("Syntax error");
			return;
		}
		// Saco los corchetes
		filePath = filePath.substring(2, filePath.length() - 1);
		
		System.out.println("Taking data from: " + filePath);
		try {
			String op = new String(Files.readAllBytes(Paths.get(filePath)));
			System.out.println("The new operation in block " + n + " is: " + op);
			Block<T> current = this.last;
			int flag = 0;
			while (current != null && flag == 0) {
				if (current.index == n) {
					flag = 1;
				} else {
					current = current.prevBlock;
				}
			}

			current.setData(op);
			modifyRecursive(n, last);

		} catch (IOException ex) {
			System.out.println("Could not find file");
			return;
		}

		printBlockChain();
	}
	
	/**
	 * @param n
	 * 		Indice del bloque a cambiarle el hash.
	 * @param elem
	 * 			Valor del nodo con el que se opera. 
	 * 
	 * Funcion recursiva. Modifica el hash del bloque y de todos los que siguen. 
	 */
	private String modifyRecursive(int n, Block<T> current) throws NoSuchAlgorithmException {
		if (current.index == n) {
			current.reHash();
			return current.hash;
		}
		current.prevHash = modifyRecursive(n, current.prevBlock);
		current.reHash();
		return current.hash;
	}

	private void printBlockChain() {
		System.out.println("The blockchain after being modified: \n");
		Block<T> block = this.last;

		while (block != null) {
			block.printBlock();
			block = block.prevBlock;
		}

	}

}
