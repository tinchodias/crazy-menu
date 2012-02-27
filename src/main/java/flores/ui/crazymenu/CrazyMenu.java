package flores.ui.crazymenu;

import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import flores.ui.crazymenu.layout.CrazyCrazyLayout;



public class CrazyMenu extends JComponent {

	public CrazyMenu(List<AbstractItem> items) {
		setLayout(new CrazyCrazyLayout());
		
		//add main menu
		JComponent base = newListFor(null, items);
		add(base, new Integer(200));
	}

	private JComponent newListFor(JList superList, List<AbstractItem> items) {
		JList list = new JList(new Vector<AbstractItem>(items));
		list.setSelectedIndex(0);
//		list.setAutoscrolls(true);
		list.setVisibleRowCount(items.size());

		list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		list.addListSelectionListener(this.listSelectionListener());
		list.registerKeyboardAction(performItemAction(), KeyStroke.getKeyStroke("released ENTER"), JComponent.WHEN_FOCUSED);
		list.registerKeyboardAction(enterToSubListAction(), KeyStroke.getKeyStroke("pressed RIGHT"), JComponent.WHEN_FOCUSED);
		if (superList != null) {
			list.registerKeyboardAction(returnToSuperListAction(superList), KeyStroke.getKeyStroke("pressed LEFT"), JComponent.WHEN_FOCUSED);
		}
		list.addFocusListener(removeSubListsListener());

		JScrollPane pane = new JScrollPane(list);
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		return pane;
	}

	private FocusListener removeSubListsListener() {
		return new FocusListener() {

			public void focusGained(FocusEvent event) {
				Component focused = (Component) event.getSource();
				List<Component> components = Arrays.asList(getComponents());
				int indexOfFocused = components.indexOf(focused.getParent().getParent());
				
				if (indexOfFocused == -1) {
					throw new Error("asdasdasd");
				}
				
				for (Iterator<Component> iterator = components.listIterator(indexOfFocused + 1); iterator.hasNext();) {
					Component componentToRemove = iterator.next();
					componentToRemove.setVisible(false);
					CrazyMenu.this.remove(componentToRemove);
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
//				System.out.println("select");
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
					JComponent subList = newListFor(list, item.getSubItems());
					Rectangle cellBounds = list.getCellBounds(list.getSelectedIndex(), list.getSelectedIndex());
					//TODO issue resizing window
					Integer verticalGap = list.getParent().getParent().getY() + list.getY() + cellBounds.y + (cellBounds.height / 2);
					System.out.println("y: " + verticalGap);
					
					CrazyMenu.this.add(subList, verticalGap);

					CrazyMenu.this.validate();
					((Container) subList.getComponent(0)).getComponent(0).requestFocusInWindow();
				}
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
			}
			
		};
	}

	private ActionListener returnToSuperListAction(final JList superList) {
		return new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				superList.requestFocusInWindow();
			}
			
		};
	}

	
}
