package net.miginfocom.examples;

import net.miginfocom.swt.MigLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

public class ExampleGood
{
	protected void buildControls(Composite parent)
	{
		parent.setLayout(new MigLayout("inset 0", "[fill, grow]", "[fill, grow]"));

		Table table = new Table(parent, SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL);
		table.setLayoutData("id table, hmin 100, wmin 300");
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		Label statusLabel = new Label(parent, SWT.BORDER);
		statusLabel.setText("Label Text");
		statusLabel.moveAbove(null);
		statusLabel.setLayoutData("pos 0 0");

		for (int i = 0; i < 10; i++)
		{
			TableItem ti = new TableItem(table, SWT.NONE);
			ti.setText("item #" + i);
		}
	}

	public static void main(String[] args)
	{
		final Display display = new Display();
		final Shell shell = new Shell(display);
		new ExampleGood().buildControls(shell);
		shell.open();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
