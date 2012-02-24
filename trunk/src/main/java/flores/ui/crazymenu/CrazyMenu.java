package flores.ui.crazymenu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



public class CrazyMenu extends JComponent {

	public CrazyMenu(List<AbstractItem> items) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));		

		//add main menu
		add(newListFor(null, items));
	}

	private JList newListFor(JList superList, List<AbstractItem> items) {
		JList list = new JList(new Vector<AbstractItem>(items));
		list.setSelectedIndex(0);
//		list.setAutoscrolls(true);

		Dimension size = list.getPreferredSize();
		size.height = Short.MAX_VALUE;
		list.setMaximumSize(size);

		list.addListSelectionListener(this.listSelectionListener());

		list.registerKeyboardAction(performItemAction(), KeyStroke.getKeyStroke("released ENTER"), JComponent.WHEN_FOCUSED);
		list.registerKeyboardAction(enterToSubListAction(), KeyStroke.getKeyStroke("pressed RIGHT"), JComponent.WHEN_FOCUSED);
		if (superList != null) {
			list.registerKeyboardAction(returnToSuperListAction(superList), KeyStroke.getKeyStroke("pressed LEFT"), JComponent.WHEN_FOCUSED);
		}

		list.addFocusListener(removeSubListsListener());
		
		return list;
	}

	private FocusListener removeSubListsListener() {
		return new FocusListener() {

			public void focusGained(FocusEvent event) {
				Object focusedList = event.getSource();
				List<Component> lists = Arrays.asList(getComponents());
				int indexOfFocusedList = lists.indexOf(focusedList);
				
				for (Iterator iterator = lists.listIterator(indexOfFocusedList + 1); iterator.hasNext();) {
					Component listToRemove = (Component) iterator.next();
					listToRemove.setVisible(false);
					remove(listToRemove);
				}
				CrazyMenu.this.validate();
			}

			public void focusLost(FocusEvent event) {
			}
		};
	}

	private ListSelectionListener listSelectionListener() {
		return new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {
					selectionAction().actionPerformed(null);	
				}
			}
			
		};
	}

	private Action selectionAction() {
		return new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("select");
			}
			
		};
	}

	private Action enterToSubListAction() {
		return new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JList list = (JList) e.getSource();
				AbstractItem item = (AbstractItem) list.getSelectedValue();
				
				if (!item.getSubItems().isEmpty()) {
					JList subList = newListFor(list, item.getSubItems());
					add(subList);
					validate();
					subList.requestFocusInWindow();
				}

//				System.out.println("enter");
			}
			
		};
	}

	private Action performItemAction() {
		return new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JList list = (JList) e.getSource();
				AbstractItem item = (AbstractItem) list.getSelectedValue();

				item.getAction().actionPerformed(null);
//				System.out.println("perform");
			}
			
		};
	}

	private ActionListener returnToSuperListAction(final JList superList) {
		return new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				JList list = (JList) e.getSource();
//				list.setVisible(false);
//				remove(list);
//				validate();
			
				superList.requestFocusInWindow();
//				System.out.println("return from " + list);
			}
			
		};
	}

	
}
