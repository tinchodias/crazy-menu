/**
 * 
 */
package flores.ui.crazymenu;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;


public class ActionItem extends AbstractItem {

	private final Action action;

	public ActionItem(String title, Action action) {
		super(title);
		this.action = action;
	}

	@Override
	public Action getAction() {
		return action;
	}

	@Override
	public List<AbstractItem> getSubItems() {
		return new ArrayList<AbstractItem>();
	}

}