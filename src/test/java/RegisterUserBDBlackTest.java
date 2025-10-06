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



public class RegisterUserBDBlackTest {
	 //sut:system under test
	 static DataAccess sut=new DataAccess();
	 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();
	 
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
			 
			 assertNotNull(userDevuelto);
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
	 //sut.registerUser: CUALQUIERA The UserT(“uT@gmail.com”, “uT”, “uT”, “T”) IS NOT on the DB. 
	 // OK: persist con parametros “uT@gmail.com”, “uT”, “uT”, “T”
	 // FAIL: no hace persist o devuelve valores nulos?
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

			 assertEquals(countBefore0 + 1, countAfter1);
			 assertNotNull(userDevuelto);
			 assertNotNull(userPersistido);
			 assertEquals(uT.getEmail(), userPersistido.getEmail());
			 assertEquals(uT.getEmail(), userDevuelto.getEmail());

			 


		 } finally {
			 //Remove the created objects in the database (cascade removing)   
			 testDA.open();
			 if (!existUser) testDA.removeUser(uT);
			 testDA.close();
		 }

	 }
	 
	 @Test
	 //sut.registerUser: User null. 
	 // OK: lanza excepción
	 // FAIL: ejecuta el método

	 public void test3() {
		 try {
			 //invoke System Under Test (sut)  
			 sut.open();
			 User userDevuelto = sut.registerUser(null);
			 fail();

		 } catch (NullPointerException e) {
			 assertTrue(true);
		 } finally {
			 sut.close();
		 }


	 }
	 




}
