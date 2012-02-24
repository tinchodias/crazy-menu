/**
 * 
 */
package flores.ui.crazymenu;

import java.util.List;

import javax.swing.Action;


public abstract class AbstractItem {

	private final String title;

	public AbstractItem(String title) {
		this.title = title;
	}

	public abstract List<AbstractItem> getSubItems();

	public abstract Action getAction();
	
	@Override
	public String toString() {
		return title;
	}
}