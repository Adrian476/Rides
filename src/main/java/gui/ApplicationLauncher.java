//prueba push

package gui;

import java.awt.Color;
import java.util.Locale;

import javax.swing.UIManager;

import configuration.ConfigXML;
import businessLogic.BLFacade;
import businessLogic.BLFactory; 

public class ApplicationLauncher { 
   
    public static void main(String[] args) {
        
        ConfigXML c = ConfigXML.getInstance();
    
        System.out.println(c.getLocale());
        
        Locale.setDefault(new Locale(c.getLocale()));
        
        System.out.println("Locale: "+Locale.getDefault());
        
     //   Driver driver=new Driver("driver3@gmail.com","Test Driver");

        
        MainGUI a = new MainGUI();
        a.setLocationRelativeTo(null);
        a.setVisible(true);


        try {
            BLFacade appFacadeInterface;
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            boolean isLocal = c.isBusinessLogicLocal();
            appFacadeInterface = new BLFactory().getBusinessLogic(isLocal);
            
            MainGUI.setBussinessLogic(appFacadeInterface);  // Corrige typo a "BusinessLogic" si puedes
        }catch (Exception e) {
            a.jLabelSelectOption.setText("Error: "+e.toString());
            a.jLabelSelectOption.setForeground(Color.RED);	
            
            System.out.println("Error in ApplicationLauncher: "+e.toString());
        }
        //a.pack();


    }

}