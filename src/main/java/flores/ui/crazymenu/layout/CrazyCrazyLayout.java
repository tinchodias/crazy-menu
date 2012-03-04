package flores.ui.crazymenu.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CrazyCrazyLayout implements LayoutManager2 {

	private Map<Component, Integer> components;

	public CrazyCrazyLayout() {
		components = new HashMap<Component, Integer>();
	}
	
	@Override
	public Dimension maximumLayoutSize(Container arg0) {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public Dimension minimumLayoutSize(Container arg0) {
		return new Dimension(5, 5); 
	}
	
	@Override
	public Dimension preferredLayoutSize(Container arg0) {
		return new Dimension(Integer.MAX_VALUE, 5);
	}
	
	@Override
	public void layoutContainer(Container pane) {
		List<Component> components = Arrays.asList(pane.getComponents());
		Point anchor = new Point(0, 0);
		
		System.out.println(pane.getBounds());
		
		for (Iterator<Component> iterator = components.iterator(); iterator.hasNext();) {
			Component component = iterator.next();
			anchor.y = this.components.get(component);
			layout(pane, component, anchor);
			anchor.x += component.getWidth();
		}

	}

	
	private void layout(Component pane, Component component, Point anchor) {
		Dimension size = component.getPreferredSize();
		Rectangle bounds = new Rectangle(anchor.x, anchor.y - (size.height / 2), size.width, size.height);

		if (pane.getSize().height < size.height) {
			bounds.y = 0;
			bounds.height = pane.getSize().height;
		} else if (bounds.getMaxY() > pane.getBounds().getMaxY()) {
			bounds.y += pane.getBounds().getMaxY() - bounds.getMaxY();
		} else if (bounds.getMinY() < pane.getBounds().getMinY()) {
			bounds.y += pane.getBounds().getMinY() - bounds.getMinY();
		}
		component.setBounds(bounds);
	}

	
	
	@Override
	public void addLayoutComponent(Component component, Object param) {
		components.put(component, (Integer) param);
	}
	
	@Override
	public float getLayoutAlignmentX(Container arg0) {
		return 0.5f;
	}
	
	@Override
	public float getLayoutAlignmentY(Container arg0) {
		return 0.5f;
	}
	
	@Override
	public void invalidateLayout(Container arg0) {
	}
	
	@Override
	public void addLayoutComponent(String arg0, Component arg1) {
		System.out.println("?");
	}

	@Override
	public void removeLayoutComponent(Component component) {
		components.remove(component);
	}

}
