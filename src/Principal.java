package AVL;

import java.util.Comparator;
import java.util.Scanner;

public class Principal {
	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);
		BlockChain<Integer> blockChain = null;

		// Hay que diferenciar las operaciones de 1(validate), 2(zeros, add, remove,
		// lookup) y 3 (modify) parametros
	
		while (scan.hasNext()) {
			String op = scan.next();
			if (op.equals("exit")) // Escribir exit para salir
				break;
	
			String elem = scan.next();
			Operation<Integer> operation = getOperation(op, Integer.parseInt(elem), blockChain);
	
			if (operation != null)
				blockChain = operation.apply(blockChain);
	
			}
		
		scan.close();

	}

	public static Operation<Integer> getOperation(String op, Integer elem, BlockChain<Integer> blockChain) {

		switch (op) {
		case "zeros":
			Comparator<Integer> cmp = new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			};
			if (blockChain == null)
				return new Zeros<Integer>(op, elem, cmp);
			
			
		case "add":
			if (blockChain != null)
				return new Add<Integer>(op, elem);

		case "remove":
			if (blockChain != null)
				return new Remove<Integer>(op, elem);

		case "lookup":
			if (blockChain != null)
				return new LookUp<Integer>(op, elem);
		
		default:
			return null;
		}
	}

}
