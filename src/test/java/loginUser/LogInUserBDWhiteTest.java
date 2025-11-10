package loginUser;
import dataAccess.DataAccess;
import domain.User;
import testOperations.TestDataAccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.*;
public class LogInUserBDWhiteTest {

	 //sut:system under test
	 static DataAccess sut=new DataAccess();
	 static TestDataAccess testDA=new TestDataAccess();
	 
	 String email, password;
	 User expected, result;
	 User uT=  new User("traveler@ehu.eus", "uT", "traveler","T");
	 User uD;
	/*
	 
	 @Before
	 public void initialize() {
		uD = new User("driver@ehu.eus", "uD", "driver","D");
		uT = new User("traveler@ehu.eus", "uT", "traveler","T");
		testDA.open();
		testDA.createUser(uD);testDA.createUser(uT);
		testDA.close();
	 }
	 
	 @After
	 public void erase() {
		 testDA.open();
		 testDA.removeUser(uD);
		 testDA.removeUser(uT);
		 testDA.close();
		 
	 }
	 
	
	//sut.login:  The UserT("noexiste@mail.com", “uT”, “cualquiera”, “T”) does not exists in the DB. 
	//OK: returns null
	//FAIL: returns a user
	 @Test
	 public void test1() {
		 System.out.println("║║║ BD WHITE TEST ║║║");

		//2.	invoke	the	method	and	get	the	result	
		 sut.open();
		 result = sut.loginUser("noexistee@mail.com", "cualquiera");
		 sut.close();
		//3.	check
		 assertNull(result);
	 }
	  */
	 /*
	//sut.login:  The User(“email”, “u”, password, "T" ) IS in the DB
	//			  and the password is incorrect
	//OK: returns the user with email, u and empty password
	//FAIL: returns null or the user with the incorrect password
	 @Test
	 public void test2() {
		//1.	expected	value	
		testDA.open();
		testDA.createUser(uT);
		testDA.close();
		uT.setPassword(null);
		expected = uT;
		//2.	invoke	the	method	and	get	the	result	
		try {
			sut.open();
			result = sut.loginUser(uT.getEmail(), "cualquiera");
			sut.close(); 
			//3.	check
			assertNull(result.getPassword());
			assertEquals(expected.getEmail(), result.getEmail());
		} catch(NullPointerException e) {
			fail("X Null value retrieved");
		}
		
		//Reestablish the state before the test
		testDA.open();
		testDA.removeUser(uT);
		testDA.close();
	 }
	 
	 
	  * sut.login:  The User(“u1@gmail.com", “uT”, “correcta”, "T") IS on the DB.
	  * OK : Returns the traveler object and prints the info
	 
	 @Test
	 public void test3() {
		//1.	expected	value
		User u3 = new User("bidaiaria@ehu.eus", "uB", "bidaiaria","T");
			testDA.open();
			testDA.createUser(u3);
			testDA.close();
			expected = u3 ;
			//2.	invoke	the	method	and	get	the	result	
			sut.open();
			result = sut.loginUser(u3.getEmail(), "bidaiaria");
			sut.close(); 
			if (result == null) {
				fail(" X retrieved value was null");
			}
			//3.	check
			assertEquals(expected.getEmail(), result.getEmail());
			assertEquals(expected.getPassword(), result.getPassword());
			testDA.open();
			testDA.removeUser(u3);
			testDA.close();
	 }
	 
	 
	 @Test
	 public void test4() {
		//1.	expected	value
		uD = new User("driver@ehu.eus", "uD", "driver","D");
		testDA.open();
		testDA.createUser(uD);
		testDA.close();
		expected = uD ;
		//2.	invoke	the	method	and	get	the	result	
		sut.open();
		result = sut.loginUser(uD.getEmail(), "driver");
		sut.close(); 
		//3.	check
		assertEquals(expected.getEmail(), result.getEmail());
		assertEquals(expected.getPassword(), result.getPassword());
		testDA.open();
		testDA.removeUser(uD);
		testDA.close();
	 }
	  */
}
