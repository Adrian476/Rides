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
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class RegisterUserMockBlackTest {
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
		User userDevuelto = sut.registerUser(uT);
		sut.close();
		
		assertEquals(uT,userDevuelto);
		Mockito.verify(db, Mockito.times(0)).persist(uT);

	}
	
	@Test
	//sut.registerUser: The UserT(“uT@gmail.com”, “uT”, “uT”, “T”) IS NOT on the DB. 
	// OK: persist con parametros “uT@gmail.com”, “uT”, “uT”, “T”
	// FAIL: no hace persist o devuelve valores nulos?
	public void test2() {
		
		User userNuevo = new User("uT@gmail.com", "uT", "uT", "T");
		//configure the state through mocks 
		Mockito.when(db.find(User.class, userNuevo.getEmail())).thenReturn(null);	

		//invoke System Under Test (sut)  
		sut.open();
		User userDevuelto = sut.registerUser(userNuevo);
		sut.close();
		
		assertEquals(userNuevo,userDevuelto);
		Mockito.verify(db, Mockito.times(1)).persist(userNuevo);

	}
	
	@Test
	//sut.registerUser: DRIVER The UserD(“uD@gmail.com”, “uD”, “uD”, “D”) IS NOT on the DB. 
	// OK: persist con parametros “uD@gmail.com”, “uD”, “uD”, “D”
	// FAIL: no hace persist o devuelve valores nulos
	public void test3() {
		
		User userNuevo = new User("uT@gmail.com", "uT", "uT", "T");
		//configure the state through mocks 
		Mockito.when(db.find(User.class, userNuevo.getEmail())).thenReturn(null);	

		//invoke System Under Test (sut)  
		sut.open();
		User userDevuelto = sut.registerUser(userNuevo);
		sut.close();
		
		Mockito.verify(db, Mockito.times(1)).persist(userNuevo);
		assertEquals(userNuevo,userDevuelto);
	}
	
	@Test
	//sut.registerUser: User null. 
	// OK: lanza excepción
	// FAIL: ejecuta el método
	
	public void test4() {
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
