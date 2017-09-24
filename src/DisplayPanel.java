package AVL;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DisplayPanel<T> extends JPanel {
	AvlTree avl;
	int xs;
	int ys;

	public DisplayPanel(AvlTree avl) {
		this.avl = avl;
		setBackground(Color.white);
		setForeground(Color.black);
	}

	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(getForeground());
		Font MyFont = new Font("SansSerif", Font.PLAIN, 10);
		g.setFont(MyFont);
		xs = 10; // donde empieza a imprimir el panel
		ys = 20;
		g.drawString("Binary Search tree for the input string:\n", xs, ys);
		ys = ys + 10;
		;
		int start = 0;
		// 150 chars por linea
		// si el string/valor del nodo es muy largo (mas de 23 lineas no imprime)
		if (avl.getRoot().value.toString().length() < 23 * 150) {
			while ((avl.getRoot().value.toString().length() - start) > 150) {
				g.drawString(avl.getRoot().value.toString().substring(start, start + 150), xs, ys);
				start += 151;
				ys += 15;
			}
			g.drawString(avl.getRoot().value.toString().substring(start, avl.getRoot().value.toString().length()), xs,
					ys);
		}
		MyFont = new Font("SansSerif", Font.BOLD, 20); // fuente para el arbol
		g.setFont(MyFont);
		this.drawTree(g, avl.getRoot()); // dibujar el arbol
		revalidate(); // actualizar panel
	}

	public void drawTree(Graphics g, Node<T> root) {// Dibuja el arbol
		int dx, dy, dx2, dy2;
		int SCREEN_WIDTH = 1000; // screen size for panel
		int SCREEN_HEIGHT = 1000;
		int XSCALE, YSCALE;
		XSCALE = SCREEN_WIDTH / avl.getTotalNodes(); // scale x by total nodes in tree
		YSCALE = (SCREEN_HEIGHT - ys) / (avl.getMaxHeight() + 1); // scale y by tree height

		if (root != null) { // inorder transversal para dibujar cada nodo
			drawTree(g, root.leftTree); // lado izquierdo inorder transversal
			dx = root.xpos * XSCALE; // conseguir coordenadas y escalarlas
			dy = root.ypos * YSCALE + ys;
			String s = (String) root.value.toString(); // string del nodo
			g.drawString(s, dx, dy); // dibuja la palabra/valor
			// dibuja lineas del padre al hijo
			if (root.leftTree != null) { // dibujar linea a hijo izquierdo
				dx2 = root.leftTree.xpos * XSCALE;
				dy2 = root.leftTree.ypos * YSCALE + ys;
				g.drawLine(dx, dy, dx2, dy2);
			}
			if (root.rightTree != null) { // dibujar linea a hijo derecho
				dx2 = root.rightTree.xpos * XSCALE;// conseguir hijo derecho
				dy2 = root.rightTree.ypos * YSCALE + ys;
				g.drawLine(dx, dy, dx2, dy2);
			}
			drawTree(g, root.rightTree); // inorder transversal lado derecho
		}
	}
}
