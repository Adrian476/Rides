import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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

public class LogInUserMockBlackTest {
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
	//sut.login:  The User("existe@mail.com", “uT”, “cualquiera”, “T”) does exists in the DB. 
	//OK: returns user
	//FAIL: returns a user
	public void test1() {
		email ="existe@mail.com";
		password="cualquiera";
		User ut = new User(email, "uT", password, "T");
		//configure the state through mocks 
		Mockito.when(db.find(User.class, ut.getEmail())).thenReturn(ut);	
		//invoke System Under Test (sut)  
		sut.open();
		User u = sut.loginUser(email, password);
		sut.close();

		assertEquals(u.getEmail(), ut.getEmail());
		assertEquals(u.getPassword(),ut.getPassword());
	//	Mockito.verify(db, Mockito.times(0)).persist(u);
	}
	
	/*
	 * public void test1_2() {
		email ="existe@mail.com";
		password="cualquiera";
		User ut = new User(email, "uT", password, "D");
		//configure the state through mocks 
		Mockito.when(db.find(User.class, ut.getEmail())).thenReturn(ut);	
		//invoke System Under Test (sut)  
		sut.open();
		User u = sut.loginUser(email, password);
		sut.close();

		assertEquals(u.getEmail(), ut.getEmail());
		assertEquals(u.getPassword(),ut.getPassword());
	//	Mockito.verify(db, Mockito.times(0)).persist(u);
	}
	 */
	
	
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


		//invoke System Under Test (sut)  
		sut.open();
		User u = sut.loginUser(email, password);
		sut.close();
		assertEquals(u.getEmail(), ut.getEmail());
		assertNull(u.getPassword());
	}
	@Test
	/*
	 * sut.login:  The User(“u1@gmail.com", “uT”, “correcta”, "T") IS on the DB.
	 * OK : Returns the traveller object and prints the info
	 */
	public void test3() {
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


	}
	@Test
	//sut.login(null, "idk")
	 // OK : returns null
	 // FAIL : does not raise an exception
		public void test4() {
		
		email = null;
		password = "cualquiera";
		User ut = new User(email, "uT", password, "T");
		Mockito.when(db.find(User.class, ut.getEmail())).thenThrow(new NullPointerException());
		try {
			sut.open();
			sut.loginUser(email, password);
			sut.close();
			fail();
		}catch(NullPointerException e) {
			assertTrue(true);
		}
		
		
	}

	@Test
	//sut.login:  The User(“u1@gmail.com", “uT”, “correcta”, "T") IS on the DB.
	 // OK : Returns the traveller object and prints the info
		public void test5() {
		email = "user@ehu.eus";
		password = null;
		User ut = new User(email, "uT", password, "T");
		Mockito.when(db.find(User.class, ut.getEmail())).thenReturn(ut);
		try {
			sut.open();
			sut.loginUser(email, password);
			sut.close();
			fail();
		}catch(NullPointerException e) {
			assertTrue(true);
		}
	}

}