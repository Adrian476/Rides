package logInUser;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.User;

public class LogInUserMockWhiteTest {
	//system under test
	static DataAccess sut;
	
	String email;
	String password;
	protected MockedStatic <Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
	

	@Before
    public  void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut=new DataAccess(db);
    }
	
	@After
    public  void tearDown() {
		persistenceMock.close();	
    }
	
	@Test
	//sut.login:  The UserT("noexiste@mail.com", “uT”, “cualquiera”, “T”) does not exists in the DB. 
	//OK: returns null
	//FAIL: returns a user
	public void test1() {
		 System.out.println("║║║ MOCK WHITE TEST ║║║");

		email ="noexiste@mail.com";
		password="cualquiera";
		User ut = new User(email, "uT", password, "T");
		//configure the state through mocks 
		Mockito.when(db.find(User.class, ut.getEmail())).thenReturn(null);	
		//invoke System Under Test (sut)  
		sut.open();
		User u = sut.loginUser(email, password);
		sut.close();

		assertNull(u);
	//	Mockito.verify(db, Mockito.times(0)).persist(u);
	}
	
	//sut.login:  The User(“email”, “u”, password, "T" ) IS in the DB
	//			  and the password is incorrect
	//OK: returns the user with email, u and empty password
	//FAIL: returns null or the user with the incorrect password
	@Test
	public void test2() {
		String email ="u1@gmail.com";
		String password="incorrecta";
		User ut = new User(email, "uT", "CORRECTA", "T");
		//configure the state through mocks 
		Mockito.when(db.find(User.class, ut.getEmail())).thenReturn(ut);	

		try {
			sut.open();
			User u = sut.loginUser(email, password);
			sut.close();
			assertNull(u.getPassword());
		}catch(NullPointerException e){
			fail("retrieved value was null");
		}
		//invoke System Under Test (sut)  
		
		
	}
	@Test
	/*
	 * sut.login:  The User(“u1@gmail.com", “uT”, “correcta”, "T") IS on the DB.
	 * OK : Returns the traveller object and prints the info
	 */
	public void test3() {
		// Redirige System.out a un stream temporal
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outContent));
		    
		String email1 ="traveller@gmail.com";
		String password1="traveler";
		User uT = new User(email1, "uT", password1,"T");
		//configure the state through mocks 
		Mockito.when(db.find(User.class, uT.getEmail())).thenReturn(uT);	


		//invoke System Under Test (sut)  
		sut.open();
		User u = sut.loginUser(email1, password1); 
		sut.close();
		
		System.setOut(originalOut);
		String salida = outContent.toString();
		//texto esperado
	    assertTrue(salida.contains(u.getTraveler().getAcceptedRides().toString()));
	    assertNotNull(u.getTraveler());


	}
	@Test
	//sut.login:  The User(“u1@gmail.com", “uT”, “correcta”, "T") IS on the DB.
	 // OK : Returns the traveller object and prints the info
		public void test4() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outContent));
		    
		String email ="driver@gmail.com";
		String password="driver";
		User uD = new User(email, "uD", password,"D");
		//configure the state through mocks 
		Mockito.when(db.find(User.class, uD.getEmail())).thenReturn(uD);	


		//invoke System Under Test (sut)  
		sut.open();
		User u = sut.loginUser(email, password); 
		sut.close();
		
		System.setOut(originalOut);
		String salida = outContent.toString();
		//texto esperado
	    assertTrue(salida.contains(u.getDriver().getCreatedRides().toString()));
	    assertTrue(salida.contains(u.getDriver().getAcceptedRides().toString()));
	    assertTrue(salida.contains(u.getDriver().getBookedRides().toString()));
	    assertNotNull(u.getDriver());
	}

	

}
