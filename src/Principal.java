
import java.util.Comparator;
import java.util.Scanner;

public class Principal {
	public static void main(String[] args) {
		/*Comparator<Integer> cmp = new Comparator<Integer>(){
			public int compare(Integer i1, Integer i2){
				return i1.compareTo(i2);
			}
		};
		AvlTree<Integer> avl = new AvlTree<>(cmp);
		avl.insert(5);
		avl.insert(4);
		avl.insert(3);
		avl.insert(2);
		avl.insert(9);
		avl.insert(10);
		avl.insert(11);
		avl.insert(12);
		avl.insert(67);
		avl.insert(112);
		avl.insert(154);
		avl.print();
		avl.draw();*/
		//LO DE ARRIBA ES POR SI QUIEREN PROBAR DRAW. 
		
		
		Scanner scan = new Scanner(System.in);
		BlockChain<Integer> blockChain = null;

		while (scan.hasNextLine()) {
			String op = scan.next();
			if (op.equals("exit")) // Escribir exit para salir
				break;

			String elem = scan.next();
			Operation<Integer> operation = getOperation(op, Integer.parseInt(elem), blockChain);

			if (operation != null) {
				blockChain = operation.apply(blockChain);
				System.out.println("Operacion realizada");
			} else
				System.out.println("Error");
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
			if (blockChain != null)
				return null;
			return new Zeros<Integer>(op, elem, cmp);

		case "add":
			if (blockChain != null)
				return new Add<Integer>(op, elem);

		case "remove":
			if (blockChain != null)
				return new Remove<Integer>(op, elem);

		default:
			return null;
		}
	}

}