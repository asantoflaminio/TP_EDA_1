package AVL;

import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Scanner;

public class Principal {
	public static void main(String[] args) throws NoSuchAlgorithmException {

		Scanner scan = new Scanner(System.in);
		BlockChain<Integer> blockChain = null;
		Comparator<Integer> cmp = new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}

		};

		FromString<Integer> fs = new FromString<Integer>() {

			@Override
			public Integer convert(String s) {
				return Integer.parseInt(s);
			}

		};

		while (scan.hasNext()) {
			String command = scan.nextLine();

			if (command.equals("exit")) // Escribir exit para salir
				break;

			if (blockChain != null) {
				blockChain.operate(command);
			} else if (command.substring(0, 6).toLowerCase().equals("zeros ")) {
				Zeros<Integer> zeros = new Zeros<Integer>(Integer.parseInt(command.substring(6)));
				blockChain = zeros.createBlockchain(cmp, fs);
			}

		}

		scan.close();

	}

}
