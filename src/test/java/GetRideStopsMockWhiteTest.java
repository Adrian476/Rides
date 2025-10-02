import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

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
import org.mockito.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.User;
import domain.Driver;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class GetRideStopsMockWhiteTest {
	
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
	//intentando saber porque da IllegalStateException
	//viaje no existe, debe devolver null, si salta excepcion o no devuelve null, falla el test
	public void test1() {
		try {
			String from = "A";
			String to = "B";
			Date date = new Date();
			String state = "reservado";
			Integer cd = 50;
			
			when(db.find(Ride.class, cd)).thenReturn(null); //posible excepcion aqui
			Ride result = sut.getRideStopsByCod(from, to, date, state, cd);
			
			assertNull(result);
		} catch (Exception e) {
			fail();
		}
	} 
	
	@Test
	//aviso TypeQuery
	//viaje exite, paradas no, debe devolver null, si salta excepcion o no devuelve null, falla el test
	public void test2() {
		
	}
	
	@Test
	//aviso TypeQuery
	//viaje con paradas, no debe devolver null, si devuelve null o salta excepcion, falla el test
	public void test3() {
		
	}
	
	
	
}