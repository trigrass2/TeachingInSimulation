package com.cas.sim.tis.test.office;  
  
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;  
  
public class SWTPanel extends JPanel {  
    /** 
     *  
     */  
    private static final long serialVersionUID = -1089789032409665511L;  
    DisplayThread displayThread;  
    private Canvas canvas;  
  
    public SWTPanel() {  
        displayThread = new DisplayThread();  
        displayThread.start();  
        canvas = new Canvas();  
        setLayout(new BorderLayout());  
        add(canvas, BorderLayout.CENTER);  
    }  
  
    public void addNotify() {  
        super.addNotify();  
        Display dis = displayThread.getDisplay();  
        dis.syncExec(new Runnable() {  
            public void run() {  
                Shell shell = SWT_AWT.new_Shell(displayThread.getDisplay(),  
                        canvas);  
                shell.setLayout(new FillLayout());  
                new WordView(shell, SWT.EMBEDDED);  
                System.out.println("SWTPanel.addNotify().new Runnable() {...}.run()");
            }  
        });  
    }  
    
    public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setContentPane(new SWTPanel());
		frame.setSize(new Dimension(1366, 768));
		frame.setVisible(true);
	}
    
}  