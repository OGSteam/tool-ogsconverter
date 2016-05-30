package ogsconverter;

import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;


public class SplashWindow extends JWindow { 
    private static long timeEnd = 0;
    private static SplashWindow instance = null;

    private SplashWindow(URL filename, long intMinTime) {
        super();
        
        //initialise la valeur a laquelle le splash screen doit etre fermé
        timeEnd = System.currentTimeMillis() + intMinTime;
        
        //ajoute la progress bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        getContentPane().add(progressBar, BorderLayout.SOUTH);
        
        // cree un label avec notre image
        ImageIcon img = new ImageIcon(filename);
        JLabel image = new JLabel(img);
        // ajoute le label au panel
        getContentPane().add(image, BorderLayout.CENTER);

        pack();
        // centre le splash screen
        setLocationRelativeTo(null);
        
        // affiche le splash screen
        setVisible(true);
    }
    
    public static void startSplashWindow(URL imagePath, int minimumTime) {
    	final URL imgPath = imagePath;
    	final int minTime = minimumTime;

    	new Thread() {
			public void run() {
				instance = new SplashWindow(imgPath, minTime);
			}
    	}.start();
    }
    
    public static void stopSplashWindow() {
    	
    	if(instance==null)
    		return;
    	
    	while(System.currentTimeMillis() < timeEnd) {
    		try {
				Thread.sleep(100);
			} catch (InterruptedException e) { }
    	}
    	
    	instance.setVisible(false);
    	instance.dispose();
    	instance = null;
    }
}
