//prueba push

package gui;

import java.awt.Color;
import java.util.Locale;

import javax.swing.UIManager;

import configuration.ConfigXML;
import iterator.ExtendedIterator;
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
            
            MainGUI.setBussinessLogic(appFacadeInterface);  

            ExtendedIterator<String>	i	=	appFacadeInterface.getDepartingCitiesIterator();	
            String	city;	
            System.out.println("_____________________");	
            System.out.println("FROM	LAST	TO	FIRST");	
            i.goLast();	//	Go	to	last	element	
            while	(i.hasPrevious())	{	
            city	=	(String) i.previous();	
            System.out.println(city);	
            }	
            System.out.println();	
            System.out.println("_____________________");	
            System.out.println("FROM	FIRST	TO	LAST");	
            i.goFirst();	//	Go	to	first	element	
            while	(i.hasNext())	{	
            city	=	(String) i.next();	
            System.out.println(city);	
            }	
            
        }catch (Exception e) {
            a.jLabelSelectOption.setText("Error: "+e.toString());
            a.jLabelSelectOption.setForeground(Color.RED);	
            
            System.out.println("Error in ApplicationLauncher: "+e.toString());
        }
        //a.pack();


    }

}