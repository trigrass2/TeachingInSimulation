package com.cas.sim.tis.test.office;

public class DisplayThread extends Thread {  
//    private Display display;  
//    Object sem = new Object();  
//  
//    public void run() {  
//        synchronized (sem) {  
//            display = Display.getDefault();  
//            sem.notifyAll();  
//        }  
//        swtEventLoop();  
//    }  
//  
//    private void swtEventLoop() {  
//        while (true) {  
//            if (!display.readAndDispatch()) {  
//                display.sleep();  
//            }  
//            System.out.println("DisplayThread.swtEventLoop()");
//        }  
//    }  
//  
//    public Display getDisplay() {  
//        try {  
//            synchronized (sem) {  
//                while (display == null)  
//                    sem.wait();  
//                return display;  
//            }  
//        } catch (Exception e) {  
//            return null;  
//        }  
//    }  
  
}  