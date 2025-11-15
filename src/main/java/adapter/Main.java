package adapter;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import businessLogic.BLFactory;
import dataAccess.DataAccess;
import domain.Driver;
import domain.User;

public class Main {

	public static void main(String[]	args)	{
		//		the	BL	is	local
		boolean isLocal =	true;
		BLFacade	blFacade =	new BLFactory().getBusinessLogic(isLocal);
		Driver	d= blFacade. getDriver("Urtzi");
		DriverTable	dt=new	DriverTable(d);
		dt.setVisible(true);
	}
}
