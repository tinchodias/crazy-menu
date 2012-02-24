package flores.ui.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import flores.ui.crazymenu.AbstractItem;
import flores.ui.crazymenu.ActionItem;
import flores.ui.crazymenu.CompositeItem;
import flores.ui.crazymenu.CrazyMenu;

public class Demo1Frame extends JFrame {

	public static void main(String[] args) {
		new Demo1Frame().setVisible(true);
	}

	public Demo1Frame() {
		setSize(800, 600);
		setLocationRelativeTo(null);
		initComponents();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		add(new CrazyMenu(randomCompositeItems()), BorderLayout.CENTER);
	}

	private ArrayList<AbstractItem> randomCompositeItems() {
		ArrayList<AbstractItem> items = new ArrayList<AbstractItem>();
		items.add(new CompositeItem("Root", randomActionItems("Person", 5)));

		return items;
	}

	private ArrayList<AbstractItem> randomActionItems(String prefix, Integer numberOfCompositeItems) {
		Random random = new Random();
		ArrayList<AbstractItem> items = new ArrayList<AbstractItem>();

		for (int i = 0; i < 10; i++) {
			final String randomName = prefix + random.nextInt();

			items.add(actionItem(randomName));
		}

		for (int i = 0; i < numberOfCompositeItems; i++) {
			items.add(random.nextInt(items.size() - 1), compositeItem("More", numberOfCompositeItems - 1));
		}
		
		return items;
	}

	private AbstractItem compositeItem(String randomName, Integer numberOfCompositeItems) {
		return new CompositeItem("More", randomActionItems("Person", numberOfCompositeItems));
	}

	private ActionItem actionItem(final String randomName) {
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
