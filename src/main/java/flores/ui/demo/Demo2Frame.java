package flores.ui.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import flores.ui.crazymenu.AbstractItem;
import flores.ui.crazymenu.ActionItem;
import flores.ui.crazymenu.CompositeItem;
import flores.ui.crazymenu.CrazyMenu;

public class Demo2Frame extends JFrame {

	public static void main(String[] args) {
		new Demo2Frame().setVisible(true);
	}

	public Demo2Frame() {
		setSize(800, 600);
		setLocationRelativeTo(null);
		initComponents();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		add(new CrazyMenu(storeItems()), BorderLayout.CENTER);
	}

	private ArrayList<AbstractItem> storeItems() {
		ArrayList<AbstractItem> items = new ArrayList<AbstractItem>();
		items.add(nullItem("Sesión"));
		items.add(new CompositeItem("Personas", randomPersonItems()));
		items.add(nullItem("Stock"));
		items.add(nullItem("Caja"));
		items.add(nullItem("Informes"));

		return items;
	}

	private ArrayList<AbstractItem> randomPersonItems() {
		Random random = new Random();
		ArrayList<AbstractItem> items = new ArrayList<AbstractItem>();

		for (int i = 0; i < 20; i++) {
			String[] names = new String[] {"Elvira", "Cuca", "Marcelo", "Pepe", "Cacho"};
			String randomName = names[random.nextInt(names.length)] + random.nextLong();

			items.add(personItem(randomName));
		}

		return items;
	}
	
	private AbstractItem personItem(String name) {
		ArrayList<AbstractItem> items = new ArrayList<AbstractItem>();
		items.add(nullItem("Nueva Venta"));
		items.add(nullItem("Detalles"));
		items.add(nullItem("Ventas"));
		items.add(nullItem("Modificar"));
		
		return new CompositeItem(name, items);
	}



	private ActionItem nullItem(final String randomName) {
		Action action = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(randomName);
			}

		};
		ActionItem item = new ActionItem(randomName, action);
		return item;
	}

}
