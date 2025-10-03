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
	//sut.registerUser:  The User1(“u1@gmail.com”, “u1”, “u1”, “T”) IS on the DB. 
	//OK: no hace persist
	//FAIL: hace persist
	public void test1() {
		
		User u1 = new User("u1@gmail.com", "u1", "u1", "T");
		u1.getTraveler().setEmail("u1@gmail.com");
		//configure the state through mocks 
		Mockito.when(db.find(User.class, u1.getEmail())).thenReturn(u1);	


		//invoke System Under Test (sut)  
		sut.open();
		User u = sut.registerUser(u1);
		sut.close();
		Mockito.verify(db, Mockito.times(0)).persist(u);



	}
	@Test
	//sut.registerUser:  The User1(“u1@gmail.com”, “u1”, “u1”, “T”) IS NOT on the DB. 
	// OK: persist con parametros “u1@gmail.com”, “u1”, “u1”, “T”
	// FAIL: no hace persist o hace persist con valores nulos?
	public void test2() {
		
		User u1 = new User("u1@gmail.com", "u1", "u1", "T");
		u1.getTraveler().setEmail("u1@gmail.com");
		//configure the state through mocks 
		Mockito.when(db.find(User.class, u1.getEmail())).thenReturn(null);	
		
		//invoke System Under Test (sut)  
		sut.open();
		User u = sut.registerUser(u1);
		sut.close();
		//define	Argument	captors
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		//verify	call	numbers	and	capture	parameters
		Mockito.verify(db,Mockito.times(1)).persist(userCaptor.capture());
		//verify	parameter	values	as	usual	using	JUnit	asserts
	


	}
	
	
	
	
}