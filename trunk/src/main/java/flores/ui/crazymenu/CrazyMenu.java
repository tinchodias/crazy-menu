package flores.ui.crazymenu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.interpolators.AccelerationInterpolator;

import flores.ui.crazymenu.layout.CrazyCrazyLayout;



public class CrazyMenu extends JComponent {

	private JPanel panel;
	private JViewport view;
	private Animator animator;
	
	public CrazyMenu(List<AbstractItem> items) {
		setLayout(new BorderLayout());

		panel = new JPanel();
		panel.setLayout(new CrazyCrazyLayout());
		view = new JViewport();
		view.add(panel);
		
		view.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent e) {
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				adjustView();
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}

		});
		
		add(view, BorderLayout.CENTER);
		
		//add main menu
		JComponent base = newListFor(null, items);
		panel.add(base, new Integer(200));
	}

	private void adjustView() {
		int lastRight = (int) panel.getComponent(panel.getComponentCount() - 1).getBounds().getMaxX();
		int newX = Math.max(0, lastRight - this.getWidth());
		Point newLocation = new Point(newX, 0);
		
		if (animator != null) {
			animator.cancel();
			animator.clearTargets();
		} else {
			animator = new Animator.Builder().setDuration(250, TimeUnit.MILLISECONDS).build();
		}

		animator.addTarget(
				PropertySetter.getTargetTo(
						view, 
						"viewPosition", 
						new AccelerationInterpolator(0.5, 0.5), 
						newLocation ));
		
		animator.start();
	}
	
	private JComponent newListFor(JList superList, List<AbstractItem> items) {
		JList list = new JList(new Vector<AbstractItem>(items));
		list.setSelectedIndex(0);
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
				List<Component> components = Arrays.asList(panel.getComponents());
				int indexOfFocused = components.indexOf(focused.getParent().getParent());
				
				if (indexOfFocused == -1) {
					throw new Error("asdasdasd");
				}
				
				for (Iterator<Component> iterator = components.listIterator(indexOfFocused + 1); iterator.hasNext();) {
					Component componentToRemove = iterator.next();
					componentToRemove.setVisible(false);
					panel.remove(componentToRemove);
				}
				panel.validate();
				adjustView();
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
					
					panel.add(subList, verticalGap);

					panel.validate();
					adjustView(); 
					
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
