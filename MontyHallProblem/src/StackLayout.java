import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * Class that represents a Layout Manager. Components in the manager are add one
 * beneath the other and the width of every component is scaled on the width of
 * the widest component.
 * 
 * @author Azra Demirovic
 * 
 */
public class StackLayout implements LayoutManager {

	/**
	 * {@inheritDoc} Method that positions every added component downwards
	 * according to the adding schedule and scales each component so that all
	 * can fit in the window.
	 */
	@Override
	public void layoutContainer(Container parent) {

		Component[] components = parent.getComponents();
		int numOfComponents = parent.getComponentCount();
		Dimension windowDimension = parent.getSize();
		Dimension preferredDimension = preferredLayoutSize(parent);
		// coefficients with which the preferred height and width of components
		// are multiplied for the purpose of scaling each component so that they 
		// all fit in the window
		double coeffHeight = windowDimension.getHeight()
				/ preferredDimension.getHeight();
		double coeffWidth = windowDimension.getWidth()
				/ preferredDimension.getWidth();
		double x = parent.getInsets().left;
		double y = parent.getInsets().top;
		double widthOfComponent = preferredDimension.getWidth() * coeffWidth;

		// positioning of components, one beneath the other and scaling of
		// height and width
		for (int i = 0; i < numOfComponents; i++) {
			Dimension componentDimension = components[i].getPreferredSize();
			double componentHeight = componentDimension.getHeight()
					* coeffHeight;
			components[i].setSize((int) widthOfComponent, (int) componentHeight);
			components[i].setLocation((int) x, (int) y);
			y = y + componentHeight;
		}
	}

	/**
	 * {@inheritDoc} Method that on basis of minimal dimension of each component
	 * calculates the minimum window dimension.
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {

		Component[] components = parent.getComponents();
		int numOfComponents = parent.getComponentCount();
		double totalHeight = 0;
		double seekedWidth = 0;

		// calculating the total sum of minimum heights of components and
		// minimum width of the widest component
		for (int i = 0; i < numOfComponents; i++) {
			Dimension minimumDimension = components[i].getMinimumSize();
			double componentWidth = minimumDimension.getWidth();
			double componentHeight = minimumDimension.getHeight();

			if (componentWidth > seekedWidth) {
				seekedWidth = componentWidth;
			}

			totalHeight += componentHeight;
		}

		Dimension minimumWindowDimension = new Dimension();
		minimumWindowDimension.setSize(seekedWidth, totalHeight);
		return minimumWindowDimension;
	}

	/**
	 * {@inheritDoc} Method that on the basis of preferred dimensions of each
	 * component calculates and returns the preferred window dimension.
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {

		Component[] components = parent.getComponents();
		int numOfComponents = parent.getComponentCount();
		double totalHeight = 0;
		double seekedWidth = 0;

		// calculating the total sum of preferred component heights and the
		// preferred width of the widest component
		for (int i = 0; i < numOfComponents; i++) {
			Dimension preferredDimension = components[i].getPreferredSize();
			double componentWidth = preferredDimension.getWidth();
			double componentHeight = preferredDimension.getHeight();

			if (componentWidth > seekedWidth) {
				seekedWidth = componentWidth;
			}

			totalHeight += componentHeight;
		}

		Dimension windowDimension = new Dimension();
		windowDimension.setSize(seekedWidth, totalHeight);

		return windowDimension;
	}

	@Override
	public void addLayoutComponent(String arg0, Component arg1) {
		// interface demanded method
	}

	@Override
	public void removeLayoutComponent(Component arg0) {
		// interface demanded method
	}

}
