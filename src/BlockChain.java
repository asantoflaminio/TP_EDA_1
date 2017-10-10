package AVL;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

public class BlockChain<T> {

	private FromString<T> fromString;
	private final int zeros;
	private Block<T> last;
	private AvlTree<T> avl;

	public BlockChain(final int zeros, Comparator<T> cmp, FromString<T> fs) {
		this.last = null;
		this.zeros = zeros;
		this.avl = new AvlTree(cmp);
		this.fromString = fs;
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

		// Para probar, despues se cambia
		public void print() {
			System.out.println("Block index: " + this.index);
			System.out.println("Hash: " + this.hash);
			System.out.println("Done: " + this.validate);
			System.out.println("Avl tree: ");
			avl.printInfo();
			System.out.println("--------------------");
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

	}

	// Se llama a esta funcion luego de insertar un comando, si el comando es valido
	// se llama a las funciones, sino se tira excepecion o mensaje de error
	// Faltan los casos que la operacion no se puede realizar sobre el avl pero si
	// genero un bloque
	// Faltan los chequeos sobre el comando recibido

	public void operate(String command) throws NoSuchAlgorithmException {

		if (command.substring(0, 4).toLowerCase().equals("add ")) {
			T value = fromString.convert(command.substring(4));
			if (value != null) {

				if (this.add(value)) {
					this.last = new Block(command, this.last, true);
				} else {
					this.last = new Block(command, this.last, false);
				}
				this.last.generateHash(this.zeros);

			} else {
				System.out.println("INVALID COMMAND");
			}
		} else if (command.substring(0, 7).toLowerCase().equals("remove ")) {
			T value = fromString.convert(command.substring(7));
			if (value != null) {
				if (this.remove(value)) {
					this.last = new Block(command, this.last, true);
				} else {
					this.last = new Block(command, this.last, false);
				}
				this.last.generateHash(this.zeros);
			} else {
				System.out.println("INVALID COMMAND");
			}
		} else if (command.substring(0, 7).toLowerCase().equals("lookup ")) {
			T value = fromString.convert(command.substring(7));
			if (value != null) {
				List<Integer> ls = lookUp(value);

				if (ls != null) {
					for (Integer i : ls) {
						System.out.println("Block:" + i);
					}
				}
			} else {
				System.out.println("INVALID COMMAND");
			}
		} else if (command.toLowerCase().equals("validate")) {
			System.out.println(validate());
		} else if (command.substring(0, 7).toLowerCase().equals("modify ")) {
			
			boolean flag = true;
			int i = 7;
			int acu = 0;
			while(flag && command.charAt(i) != ' ') {
				if(command.charAt(i) < '0' || command.charAt(i) > '9') {
					flag = false;
				}
				else {
					acu*= 10;
					acu += command.charAt(i) - '0';
				}
				i++;
			}
			
			if(acu > this.last.index || !flag) {
				System.out.println("Los datos ingresados no son validos");
			}
			else {
				modify(acu, command.substring(i));
			}
			
		} else {
			System.out.println("NOT VALID COMMAND");
		}
		this.last.print(); // Tener en cuenta que esta imprimiendo el ultimo bloque siempre aunque no se
							// agrege nada, hay q cambiar esto

	}

	// Necesito que insert me devuelva boolean
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
		if (list == null)
			System.out.println("El elemento " + elem + " no existe en el arbol");
		return list;
	}

	private boolean validate() {
		String toCmp = "";
		for (int i = 0; i < zeros; i++)
			toCmp += '0';
		return validate(this.last, toCmp);
	}

	private boolean validate(Block<T> current, String s) {
		if (current == null)
			return true;
		if (!current.hash.substring(0, this.zeros).equals(s))
			return false;
		return validate(current.prevBlock, s);
	}

	private void modify(int n, String file) {
		System.out.println(n + " " + file);
	}

}
