package logInUser;
import dataAccess.DataAccess;
import domain.User;
import testOperations.TestDataAccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.*;
public class LogInUserBDBlackTest {

	 //sut:system under test
	 static DataAccess sut =new DataAccess();;
	 static TestDataAccess testDA = new TestDataAccess();
	 
	 String email, password;
	 User expected, result;
	 
	 /*
	 @Before
	 public void setUp() {
	     sut = new DataAccess();
	     testDA = new TestDataAccess();
	 }
	
	 @After
	 public void tearDown() {
	     if (sut != null) {
	         try {
	             sut.close();
	         } catch (Exception e) {
	             // Ignorar si ya está cerrado
	         }
	     }
	 }
 */
	//sut.login:  The UserT("existe@mail.com", “uT”, “cualquiera”, “T”) exists in the DB. 
	//OK: returns null
	//FAIL: returns a user
	 @Test
	 public void test1() {
		 System.out.println("║║║ BD BLACK TEST ║║║");

		 System.out.println("----------------------------------------------------");
		 System.out.println("TEST Nº1");

		 User expected = new User("existe@mail.com", "uT", "cualquiera", "T");
		 System.out.println("Expected user : ");
		 System.out.println(expected.toString());
		//2.	invoke	the	method	and	get	the	result	
		 testDA.open();
		 testDA.createUser(expected);
		 testDA.close();
		 
		sut.open();
		result = sut.loginUser("existe@mail.com", "cualquiera");
		if (result == null) fail(" X Retrieved value was null");
		
		System.out.println("Retrieved value:");
		System.out.print(result.toString());

		 sut.close();
		//3.	check
		 assertEquals(result.getEmail(), expected.getEmail());
		 assertEquals(result.getPassword(), expected.getPassword());
		 
		 testDA.open();
		 testDA.removeUser(expected);
		 testDA.close();
	 }
	 
	 
	//sut.login:  The User(“u2@gmail.com”, “u”, password, "T" ) IS in the DB
	//			  and the password is incorrect
	//OK: returns the user with email, u and empty password
	//FAIL: returns null or the user with the incorrect password
	 @Test
	 public void test2() {
		
		 System.out.println("----------------------------------------------------");
		 System.out.println("TEST Nº2");
		//1.	expected	value	
		User u2 = new User("u2@gmail.com", "u2", "correcta", "T");
		System.out.println("Expected user : ");
		System.out.println(u2.toString());
		testDA.open();
		testDA.removeUser(u2);
		testDA.createUser(u2);
		testDA.close();
		u2.setPassword(null);
		expected = u2;
		//2.	invoke	the	method	and	get	the	result	
		sut.open();
		result = sut.loginUser(u2.getEmail(), "incorrecta");
		sut.close(); 
		if(result ==  null) {
			fail("The retrieved value was null, it was expected to get the user without the password");
		}	
		//3.	check
		assertEquals(u2.getEmail(), result.getEmail());
		assertNull(result.getPassword());
		
		//Reestablish the state before the test
		testDA.open();
		testDA.removeUser(u2);
		testDA.close();
	 }
	 
	 /*
	  * sut.login:  The User(“u1@gmail.com", “uT”, “correcta”, "T") does not exist on the DB.
	  * OK : Returns null
	  * FAIL : Returns something but null
	  */
	 @Test
	 public void test3() {
		System.out.println("----------------------------------------------------");
		System.out.println("TEST Nº3");
		//1.	expected	value
		User u3 = new User("noexisto@ehu.eus", "uB", "bidaiaria","T");
		System.out.println("Expected user : NULL");
		//2.	invoke	the	method	and	get	the	result	
		sut.open();
		result = sut.loginUser(u3.getEmail(), "bidaiaria");
		sut.close(); 
		//3.	check
		assertNull(result);
	 }
	 /*
	  * sut.login:  The User(null, “uT”, “correcta”, "T") cannot be accepted
	  * OK : raises an exception
	  * FAIL : does not raise a NullPointerException
	  */
	 @Test
	 public void test4() {
		System.out.println("----------------------------------------------------");
		System.out.println("TEST Nº4");
		System.out.println("Expected to raise an exception");
		 try {
			 //2.	invoke	the	method	and	get	the	result	
		sut.open();
		result = sut.loginUser(null, "driver");
		sut.close(); 
		fail();
		 }catch(IllegalArgumentException e) {
			System.out.println("Exception raised");
			 assertTrue(true);
		 }
	 }
	 
	 /*
	  * sut.login:  The User("erabiltzaile@ehu.eus", "uT", null, "T") cannot be accepted
	  * OK : the retrieved value is null
	  * FAIL : do not retrieve a null
	  */
	 @Test
	 public void test5() {
		System.out.println("----------------------------------------------------");
		System.out.println("TEST Nº5");
		User u5 = new User ("erabiltzaile@ehu.eus", "uT", "correcta", "T");
		testDA.open();
		testDA.createUser(u5);
		testDA.close();
		//2.	invoke	the	method	and	get	the	result	
		sut.open();
		result = sut.loginUser("erabiltzaile@ehu.eus", null);
		sut.close(); 
		if (result == null) {
			fail("Retrieved value is null");
		}
		assertEquals(result.getEmail(), u5.getEmail());
		assertNull(result.getPassword());
	 }
}
