package net.miginfocom.examples;

import net.miginfocom.swt.MigLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Mikael Grev, MiG InfoCom AB
 *         Date: Apr 12, 2008
 *         Time: 8:19:01 AM
 */
public class SwtTest
{
   public static void main(String[] args)
   {
      Display display = new Display();
      Shell shell = new Shell();
      Composite parent = new Composite(shell, SWT.DOUBLE_BUFFERED);

      shell.setLayout(new FillLayout());
//      parent.setLayoutData("wmin 100");

      MigLayout layout = new MigLayout("debug,wrap 2");
      parent.setLayout(layout);

      Label label1 = new Label(parent, SWT.WRAP);
      label1.setText("This is an even longer label that just goes on and on...");
      label1.setLayoutData("wmin 50");
      Label label = new Label(parent, SWT.NONE);
      label.setText("Label 2");
      label = new Label(parent, SWT.NONE);
      label.setText("Label 3");
      label = new Label(parent, SWT.NONE);
      label.setText("Label 4");

//      layout.addLayoutCallback(new LayoutCallback()
//      {
//         public void correctBounds(ComponentWrapper comp)
//         {
//            if (comp.getComponent() instanceof Control)
//            {
//               if ((((Control) comp.getComponent()).getStyle() & SWT.WRAP) == SWT.WRAP)
//               {
//                  Control c = (Control) comp.getComponent();
//                  Rectangle rect = c.getBounds();
//                  Point newSize = c.computeSize(rect.width, SWT.DEFAULT, false);
//                  c.setBounds(rect.x, rect.y, newSize.x, newSize.y);
//               }
//            }
//         }
//      });

      shell.setSize(300, 300);
      shell.open();
      shell.layout();

      while (!shell.isDisposed())
      {
         if (!display.readAndDispatch())
         {
            display.sleep();
         }
      }
   }
}
//public class SwtTest
//{
//	public static void main(String[] args)
//	{
//		Display display = new Display();
//		Shell shell = new Shell(display);
//		shell.setMaximized(true);
//
//		MigLayout migLayout = new MigLayout("fill", "[left]");
//		shell.setLayout(migLayout);
//
//		List list = new List(shell, SWT.V_SCROLL);
//		list.setLayoutData("grow");
//
//		for (int i = 0; i < 128; i++) {
//			list.add("Item " + i);
//		}
//		shell.pack();
//		shell.open();
//
//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch())
//				display.sleep();
//		}
//		display.dispose();
//	}
//}
