import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.Ride;
import domain.User;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import testOperations.TestDataAccess;
import domain.Driver;


public class RegisterUserBDWhiteTest {
	

	 //sut:system under test
	 static DataAccess sut=new DataAccess();
	 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();

	private Driver driver; 

	@Test
	//sut.registerUser:  The UserT(“uT@gmail.com”, “uT”, “uT”, “T”) IS on the DB. 
	//OK: no hace persist
	//FAIL: hace persist
	public void test1() {
		
		boolean existUser = false;
		User uT = new User("uT@gmail.com", "uT", "uT", "T");

		
		try {
			//configure the state of the system (create object in the database)
			testDA.open();
			existUser = testDA.existUser(uT);
			if (!existUser) testDA.createUser(uT);
			User userOriginal = testDA.findUser(uT);
			int countBefore = testDA.countUsersByEmail(uT.getEmail());
			testDA.close();		

			//invoke System Under Test (sut)  
			sut.open();
			User userDevuelto = sut.registerUser(uT);
			sut.close();
			
			

			testDA.open();
	        int countAfter = testDA.countUsersByEmail(uT.getEmail());
	        testDA.close();
	        
	        assertEquals(userOriginal.getEmail(), userDevuelto.getEmail());
	        assertEquals(countBefore, countAfter);

			
		} finally {
			//Remove the created objects in the database (cascade removing)   
			testDA.open();
			if (!existUser) testDA.removeUser(uT);
			testDA.close();
		}
		
	} 
	
	@Test
	//sut.registerUser: TRAVELER The UserT(“uT@gmail.com”, “uT”, “uT”, “T”) IS NOT on the DB. 
	// OK: persist con parametros “uT@gmail.com”, “uT”, “uT”, “T”
	// FAIL: no hace persist o hace persist con valores nulos?
	public void test2() {
		
		boolean existUser = false;
		User uT = new User("uT@gmail.com", "uT", "uT", "T");

		
		try {
			//configure the state of the system (create object in the database)
			testDA.open();
			existUser = testDA.existUser(uT);
			if (existUser) testDA.removeUser(uT);
			//no debería existir, tiene que ser null
			User userAntesNULL = testDA.findUser(uT);
			//DEBERÍA SER CERO
			int countBefore0 = testDA.countUsersByEmail(uT.getEmail());
			testDA.close();		
			
			assertNull(userAntesNULL);
			assertEquals(0, countBefore0);
			
			
			//invoke System Under Test (sut)  
			sut.open();
			User userDevuelto = sut.registerUser(uT);
			sut.close();
			
			

			testDA.open();
			//DEBERÍA SER 1
	        int countAfter1 = testDA.countUsersByEmail(uT.getEmail());
	        User userPersistido = testDA.findUser(uT);
	        testDA.close();
	        
	        
	        assertNotNull(userDevuelto);
	        assertNotNull(userPersistido);
	        assertEquals(uT.getEmail(), userDevuelto.getEmail());
	        
	        assertEquals(countBefore0 + 1, countAfter1);

			
		} finally {
			//Remove the created objects in the database (cascade removing)   
			testDA.open();
			if (!existUser) testDA.removeUser(uT);
			testDA.close();
		}
		
	}
	
	@Test
	//sut.registerUser: DRIVER The UserD(“uD@gmail.com”, “uD”, “uD”, “D”) IS NOT on the DB. 
	// OK: persist con parametros “uD@gmail.com”, “uD”, “uD”, “D”
	// FAIL: no hace persist o devuelve valores nulos?
	public void test3() {
		
		boolean existUser = false;
		User uD = new User("uD@gmail.com", "uD", "uD", "D");

		
		try {
			//configure the state of the system (create object in the database)
			testDA.open();
			existUser = testDA.existUser(uD);
			if (existUser) testDA.removeUser(uD);
			//no debería existir, tiene que ser null
			User userAntesNULL = testDA.findUser(uD);
			//DEBERÍA SER CERO
			int countBefore0 = testDA.countUsersByEmail(uD.getEmail());
			testDA.close();		
			
			assertNull(userAntesNULL);
			assertEquals(0, countBefore0);
			
			
			//invoke System Under Test (sut)  
			sut.open();
			User userDevuelto = sut.registerUser(uD);
			sut.close();
			
			

			testDA.open();
			//DEBERÍA SER 1
	        int countAfter1 = testDA.countUsersByEmail(uD.getEmail());
	        User userPersistido = testDA.findUser(uD);
	        testDA.close();
	        
	        
	        assertNotNull(userDevuelto);
	        assertNotNull(userPersistido);
	        assertEquals(uD.getEmail(), userDevuelto.getEmail());	   
	        assertEquals(countBefore0 + 1, countAfter1);

			
		} finally {
			//Remove the created objects in the database (cascade removing)   
			testDA.open();
			if (!existUser) testDA.removeUser(uD);
			testDA.close();
		}
		
	}
	
	@Test
	//sut.registerUser: The User(“u@gmail.com”, “u”, “u”, "" ) IS NOT on the DB. 
	// OK: persist con parametros “u@gmail.com”, “u”, “u”, “”
	// FAIL: no hace persist o devuelve valores nulos?
	public void test4() {
		
		boolean existUser = false;
		User u = new User("u@gmail.com", "u", "u", "");

		
		try {
			//configure the state of the system (create object in the database)
			testDA.open();
			existUser = testDA.existUser(u);
			if (existUser) testDA.removeUser(u);
			//no debería existir, tiene que ser null
			User userAntesNULL = testDA.findUser(u);
			//DEBERÍA SER CERO
			int countBefore0 = testDA.countUsersByEmail(u.getEmail());
			testDA.close();		
			
			assertNull(userAntesNULL);
			assertEquals(0, countBefore0);
			
			
			//invoke System Under Test (sut)  
			sut.open();
			User userDevuelto = sut.registerUser(u);
			sut.close();
			
			

			testDA.open();
			//DEBERÍA SER 1
	        int countAfter1 = testDA.countUsersByEmail(u.getEmail());
	        User userPersistido = testDA.findUser(u);
	        testDA.close();
	        
	        
	        assertNotNull(userDevuelto);
	        assertNotNull(userPersistido);
	        assertEquals(u.getEmail(), userDevuelto.getEmail());	   
	        assertEquals(countBefore0 + 1, countAfter1);

			
		} finally {
			//Remove the created objects in the database (cascade removing)   
			testDA.open();
			if (!existUser) testDA.removeUser(u);
			testDA.close();
		}
		
	}
	



}
