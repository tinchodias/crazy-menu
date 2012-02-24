/**
 * 
 */
package flores.ui.crazymenu;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

public class CompositeItem extends AbstractItem {

	private final List<AbstractItem> subItems;

	public CompositeItem(String title, List<AbstractItem> subItems) {
		super(title);
		this.subItems = subItems;

	}

	@Override
	public List<AbstractItem> getSubItems() {
		return subItems;
	}

	@Override
	public Action getAction() {
		return new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//null action
			}
			
		};
	}

}