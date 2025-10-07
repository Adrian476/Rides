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
	 static DataAccess sut;
	 static TestDataAccess testDA;
	 
	 String email, password;
	 User expected, result;
	 
	 /*
	 @Before
	 public void setUp() {
	     sut = new DataAccess();
	     testDA = new TestDataAccess();
	 }
	 */
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

	//sut.login:  The UserT("existe@mail.com", “uT”, “cualquiera”, “T”) does not exists in the DB. 
	//OK: returns null
	//FAIL: returns a user
	 @Test
	 public void test1() {
		 User expected = new User("existe@mail.com", "uT", "cualquiera", "T");
		//2.	invoke	the	method	and	get	the	result	
		 testDA.open();
		 testDA.createUser(expected);
		 testDA.close();
		 
		 sut.open();
		 result = sut.loginUser("existe@mail.com", "cualquiera");
		 sut.close();
		//3.	check
		 assertEquals(result.getEmail(), expected.getEmail());
		 assertEquals(result.getPassword(), expected.getPassword());
		 
		 testDA.open();
		 testDA.removeUser(expected);
		 testDA.close();
	 }
	 
	 
	//sut.login:  The User(“email”, “u”, password, "T" ) IS in the DB
	//			  and the password is incorrect
	//OK: returns the user with email, u and empty password
	//FAIL: returns null or the user with the incorrect password
	 @Test
	 public void test2() {
		//1.	expected	value	
		User u1 = new User("email@gmail.com", "u", "correcta", "T");
		testDA.open();
		testDA.createUser(u1);
		testDA.close();
		u1.setPassword(null);
		expected = u1;
		//2.	invoke	the	method	and	get	the	result	
		sut.open();
		result = sut.loginUser(u1.getEmail(), "incorrecta");
		sut.close(); 
		//3.	check
		assertNull(result.getPassword());
		assertEquals(expected.getEmail(), result.getEmail());
		//Reestablish the state before the test
		testDA.open();
		testDA.removeUser(u1);
		testDA.close();
	 }
	 
	 /*
	  * sut.login:  The User(“u1@gmail.com", “uT”, “correcta”, "T") does not exist on the DB.
	  * OK : Returns null
	  * FAIL : Returns something but null
	  */
	 @Test
	 public void test3() {
		//1.	expected	value
		User u3 = new User("noexisto@ehu.eus", "uB", "bidaiaria","T");
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
		 try {
			 //2.	invoke	the	method	and	get	the	result	
		sut.open();
		result = sut.loginUser(null, "driver");
		sut.close(); 
		fail();
		 }catch(IllegalArgumentException e) {
			 assertTrue(true);
		 }
	 }
	 
	 /*
	  * sut.login:  The User("erabiltzaile@ehu.eus", "uT", "correcta", "T") cannot be accepted
	  * OK : raises an exception
	  * FAIL : does not raise a NullPointerException
	  */
	 @Test
	 public void test5() {
		User u5 = new User ("erabiltzaile@ehu.eus", "uT", "correcta", "T");
		testDA.open();
		testDA.createUser(u5);
		testDA.close();
		//2.	invoke	the	method	and	get	the	result	
		sut.open();
		result = sut.loginUser("erabiltzaile@ehu.eus", null);
		sut.close(); 
		assertEquals(result.getEmail(), u5.getEmail());
		assertNull(result.getPassword());
	 }
}
