package adapter;

import businessLogic.BLFacadeImplementation;
import dataAccess.DataAccess;
import domain.Driver;
import domain.User;

public class Main {

	public static void main(String[]	args)	{
		//		the	BL	is	local


		/*
		boolean isLocal =	true;
		BLFacade	blFacade =	new BLFactory().getBusinessLogicFactory(isLocal);
		*/
		
		User u = new User("urtzi@gmail.com", "Urtzi", "123", "D");
		
		BLFacadeImplementation blFacade = new BLFacadeImplementation();
		DataAccess db = new DataAccess();
		Driver	d= blFacade. getDriver(u.getEmail());
				
		DriverTable	dt=new	DriverTable(d);
		dt.setVisible(true);
	}
}
