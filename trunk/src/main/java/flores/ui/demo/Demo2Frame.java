package flores.ui.demo;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import flores.ui.crazymenu.AbstractItem;
import flores.ui.crazymenu.ActionItem;
import flores.ui.crazymenu.CompositeItem;
import flores.ui.crazymenu.CrazyMenu;

public class Demo2Frame extends JFrame {

	public static void main(String[] args) {
	    System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

		new Demo2Frame().setVisible(true);
	}

	public Demo2Frame() {
		setSize(400, 400);
		setLocationRelativeTo(null);
		initComponents();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initTimingFramework();
	}

	
	private void initTimingFramework() {
		final SwingTimerTimingSource animationTimer = new SwingTimerTimingSource();
		animationTimer.init();
	    this.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosed(WindowEvent e) {
	          super.windowClosed(e);
	          animationTimer.dispose();
	        }
	      });
	    Animator.setDefaultTimingSource(animationTimer);
	}

	private void initComponents() {
		add(new CrazyMenu(storeItems()));
	}

	private ArrayList<AbstractItem> storeItems() {
		ArrayList<AbstractItem> items = new ArrayList<AbstractItem>();
		items.add(new CompositeItem("Personas", randomPersonItems()));
		items.add(nullItem("Stock"));
		items.add(nullItem("Caja"));
		items.add(nullItem("Informes"));
		items.add(nullItem("Sesión"));

		return items;
	}

	private ArrayList<AbstractItem> randomPersonItems() {
		Random random = new Random();
		ArrayList<AbstractItem> items = new ArrayList<AbstractItem>();

		for (int i = 0; i < 20; i++) {
			String[] names = new String[] {"Elvira", "Cuca", "Marcelo", "Pepe", "Cacho"};
			String randomName = names[random.nextInt(names.length)] + random.nextInt(20);

			items.add(personItem(randomName));
		}

//		items.add(nullItem("< Nuevo >"));

		return items;
	}
	
	private AbstractItem personItem(String name) {
		ArrayList<AbstractItem> items = new ArrayList<AbstractItem>();
		items.add(ventasItem());
		items.add(nullItem("Detalles"));
		
		return new CompositeItem(name, items);
	}



	private AbstractItem ventasItem() {
		Random random = new Random();
		ArrayList<AbstractItem> items = new ArrayList<AbstractItem>();

		for (int i = 0; i < 30; i++) {
			String text = DateFormat.getDateInstance().format(new Date(random.nextInt())) + " $" + random.nextInt(200000) / 100.0;
			items.add(detalleVentaItem(text));
		}

//		items.add(nullItem("< Nuevo >"));

		return new CompositeItem("Ventas", items);
	}

	private AbstractItem detalleVentaItem(String text) {
		Random random = new Random();
		ArrayList<AbstractItem> items = new ArrayList<AbstractItem>();
		items.add(productosVentaItem("Articulos"));
		items.add(nullItem("Total: $" + random.nextInt(20000) / 100.0));
		items.add(nullItem("Pago: $" + random.nextInt(20000) / 100.0));

		
		return new CompositeItem(text, items);
	}

	private AbstractItem productosVentaItem(String text) {
		Random random = new Random();
		ArrayList<AbstractItem> items = new ArrayList<AbstractItem>();

		for (int i = 0; i < 10; i++) {
			String[] products = new String[] {"Rosa", "Clavel", "Helecho", "Statis", "Lisiantus"};
			String randomProduct = products[random.nextInt(products.length)];
			String randomDescription = (1 + random.nextInt(10)) + " " + randomProduct  + " $" + random.nextInt(20000) / 100.0;
			items.add(nullItem(randomDescription));
		}

		return new CompositeItem(text, items);
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
