package AVL;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class DrawnTree extends JFrame {
	JScrollPane scrollpane;
	DisplayPanel panel;

	public DrawnTree(AvlTree t) {
		panel = new DisplayPanel(t);
		panel.setPreferredSize(new Dimension(1000, 1000));
		panel.setAlignmentX(CENTER_ALIGNMENT);
		panel.setAlignmentY(CENTER_ALIGNMENT);
		scrollpane = new JScrollPane(panel);
		getContentPane().add(scrollpane, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack(); // limpia el panel
	}
}
