import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.User;
import domain.Driver;

public class RegisterUserMockWhiteTest {
	
	static DataAccess sut;
	
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
	
	
	Driver driver;

	@Test
	//sut.registerUser:  The UserT(“uT@gmail.com”, “uT”, “uT”, “T”) IS on the DB. 
	//OK: no hace persist
	//FAIL: hace persist
	public void test1() {
		
		User uT = new User("uT@gmail.com", "uT", "uT", "T");
		//configure the state through mocks 
		Mockito.when(db.find(User.class, uT.getEmail())).thenReturn(uT);	


		//invoke System Under Test (sut)  
		sut.open();
		User u = sut.registerUser(uT);
		sut.close();
		
		Mockito.verify(db, Mockito.times(0)).persist(uT);
		assertEquals(uT,u);



	}
	@Test
	//sut.registerUser: TRAVELER The UserT(“uT@gmail.com”, “uT”, “uT”, “T”) IS NOT on the DB. 
	// OK: persist con parametros “uT@gmail.com”, “uT”, “uT”, “T”
	// FAIL: no hace persist o devuelve valores nulos?
	public void test2() {
		
		User uT = new User("uT@gmail.com", "uT", "uT", "T");
		//configure the state through mocks 
		Mockito.when(db.find(User.class, uT.getEmail())).thenReturn(null);	
		
		
		//invoke System Under Test (sut)  
		sut.open();
		User u = sut.registerUser(uT);
		sut.close();
		
		
		Mockito.verify(db, Mockito.times(1)).persist(uT);
		assertEquals(uT,u);
	
	}
	
	@Test
	//sut.registerUser: TRAVELER The The UserT(“uT@gmail.com”, “uT”, “uT”, “T”) IS NOT on the DB. 
	// OK: persist con parametros “uT@gmail.com”, “uT”, “uT”, “T”
	// FAIL: no hace persist o devuelve valores nulos?
	public void test2PERSIST() {
		
		User uT = new User("uT@gmail.com", "uT", "uT", "T");
		//configure the state through mocks 
		Mockito.when(db.find(User.class, uT.getEmail())).thenReturn(null);	
		
		
		//invoke System Under Test (sut)  
		sut.open();
		sut.registerUser(uT);
		sut.close();
		
		//System.out.println(u);
		Mockito.verify(db, Mockito.times(1)).persist(uT);
		
	}
	
	@Test
	//sut.registerUser: DRIVER The UserD(“uD@gmail.com”, “uD”, “uD”, “D”) IS NOT on the DB. 
	// OK: persist con parametros “uD@gmail.com”, “uD”, “uD”, “D”
	// FAIL: no hace persist o devuelve valores nulos?
	public void test3() {
		
		User uD = new User("uD@gmail.com", "uD", "uD", "D");
	
		//configure the state through mocks 
		Mockito.when(db.find(User.class, uD.getEmail())).thenReturn(null);	
		
		
		//invoke System Under Test (sut)  
		sut.open();
		User u = sut.registerUser(uD);
		sut.close();
		
		Mockito.verify(db, Mockito.times(1)).persist(uD);
		assertEquals(uD,u);

		
	}
	@Test
	//sut.registerUser: The User(“u@gmail.com”, “u”, “u”, "" ) IS NOT on the DB. 
	// OK: persist con parametros “u@gmail.com”, “u”, “u”, “”
	// FAIL: no hace persist o devuelve valores nulos?
	public void test4() {
		
		User u = new User("u@gmail.com", "u", "u", "");
		
	
		//configure the state through mocks 
		Mockito.when(db.find(User.class, u.getEmail())).thenReturn(null);	
		
		
		//invoke System Under Test (sut)  
		sut.open();
		User userDevuelto = sut.registerUser(u);
		sut.close();
		
		Mockito.verify(db, Mockito.times(1)).persist(u);
		assertEquals(userDevuelto,u);

		
	}
		
	@Test
	//sut.registerUser: The User(“u@gmail.com”, “u”, “u”, "" ) IS on the DB. 
	// OK: no hace persist 
	// FAIL: hace persist
	public void test5() {
		
		User u = new User("u@gmail.com", "u", "u", "");
		
	
		//configure the state through mocks 
		Mockito.when(db.find(User.class, u.getEmail())).thenReturn(u);	
		
		
		//invoke System Under Test (sut)  
		sut.open();
		sut.registerUser(u);
		sut.close();
		
		Mockito.verify(db, Mockito.times(0)).persist(u);

		
	}


	
	
	
	
	
}