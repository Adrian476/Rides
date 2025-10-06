import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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

public class GetRideStopsMockBlackTest {
	
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
	public void test1() {
		
		String from = null;
		String to = "B";
		Date date = new Date();
		String state = "pendiente";
		Integer cd = 50;
		Driver driver = new Driver();


		Ride ride = new Ride(from, to, null, date, 4, 10, driver);

		Mockito.when(db.find(eq(Ride.class), eq(cd))).thenReturn(ride);
		
		TypedQuery<Ride> mockTypedQuery = mock(TypedQuery.class);
		Mockito.when(db.createQuery(anyString(), eq(Ride.class))).thenReturn(mockTypedQuery);
		Mockito.when(mockTypedQuery.setParameter(anyInt(), any())).thenReturn(mockTypedQuery);
		Mockito.when(mockTypedQuery.getResultList()).thenReturn(Collections.emptyList());
		
		Ride rides = sut.getRideStopsByCod(from, to, date, state, cd);
		
		assertNull(rides);
	}
	
	
	
	@Test
	public void test2() {
		
		String from = "A";
		String to = null;
		Date date = new Date();
		String state = "pendiente";
		Integer cd = 50;
		Driver driver = new Driver();


		Ride ride = new Ride(from, to, null, date, 4, 10, driver);

		Mockito.when(db.find(eq(Ride.class), eq(cd))).thenReturn(ride);

		TypedQuery<Ride> mockTypedQuery = mock(TypedQuery.class);
		Mockito.when(db.createQuery(anyString(), eq(Ride.class))).thenReturn(mockTypedQuery);
		Mockito.when(mockTypedQuery.setParameter(anyInt(), any())).thenReturn(mockTypedQuery);
		Mockito.when(mockTypedQuery.getResultList()).thenReturn(Collections.emptyList());
		
		Ride rides = sut.getRideStopsByCod(from, to, date, state, cd);
		
		assertNull(rides);
	}
	
	@Test
	public void test3() {
		
		String from = "A";
		String to = "B";
		Date date = null;
		String state = "pendiente";
		Integer cd = 50;
		Driver driver = new Driver();


		Ride ride = new Ride(from, to, null, date, 4, 10, driver);

		Mockito.when(db.find(eq(Ride.class), eq(cd))).thenReturn(ride);

		TypedQuery<Ride> mockTypedQuery = mock(TypedQuery.class);
		Mockito.when(db.createQuery(anyString(), eq(Ride.class))).thenReturn(mockTypedQuery);
		Mockito.when(mockTypedQuery.setParameter(anyInt(), any())).thenReturn(mockTypedQuery);
		Mockito.when(mockTypedQuery.getResultList()).thenReturn(Collections.emptyList());
		
		Ride rides = sut.getRideStopsByCod(from, to, date, state, cd);
		
		assertNull(rides);
	}
	
	
	@Test
	public void test4() {
		
		String from = "A";
		String to = "B";
		Date date = new Date();
		String state = null;
		Integer cd = 50;
		Driver driver = new Driver();


		Ride ride = new Ride(from, to, null, date, 4, 10, driver);

		Mockito.when(db.find(eq(Ride.class), eq(cd))).thenReturn(ride);

		TypedQuery<Ride> mockTypedQuery = mock(TypedQuery.class);
		Mockito.when(db.createQuery(anyString(), eq(Ride.class))).thenReturn(mockTypedQuery);
		Mockito.when(mockTypedQuery.setParameter(anyInt(), any())).thenReturn(mockTypedQuery);
		Mockito.when(mockTypedQuery.getResultList()).thenReturn(Collections.emptyList());
		
		Ride rides = sut.getRideStopsByCod(from, to, date, state, cd);
		
		assertNull(rides);
	}
	
	
	
	@Test
	//viaje no existe, debe devolver null, si salta excepcion o no devuelve null, falla el test
	public void test5() {

		String from = "A";
		String to = "B";
		Date date = new Date();
		String state = "pendiente";
		Integer cd = 50;

		Mockito.when(db.find(eq(Ride.class), eq(cd))).thenReturn(null); 
		Ride result = sut.getRideStopsByCod(from, to, date, state, cd);

		assertNull(result);

	} 

	
	@Test
	//viaje exite, paradas no, debe devolver una lista vacia, si salta excepcion o no devuelve null, falla el test
	public void test6() {
		String from = "A";
		String to = "B";
		Date date = new Date();
		String state = "pendiente";
		Integer cd = 50;
		Driver driver = new Driver();


		Ride ride = new Ride(from, to, null, date, 4, 10, driver);

		Mockito.when(db.find(eq(Ride.class), eq(cd))).thenReturn(ride);

		TypedQuery<Ride> mockTypedQuery = mock(TypedQuery.class);
		Mockito.when(db.createQuery(anyString(), eq(Ride.class))).thenReturn(mockTypedQuery);
		Mockito.when(mockTypedQuery.setParameter(anyInt(), any())).thenReturn(mockTypedQuery);
		Mockito.when(mockTypedQuery.getResultList()).thenReturn(Collections.singletonList(ride));

		Ride result = sut.getRideStopsByCod(from, to, date, state, cd);

		assertNotNull(result);
		assertEquals(ride, result);

		verify(db, times(1)).find(eq(Ride.class), eq(cd));
		verify(db, times(1)).createQuery(anyString(), eq(Ride.class));
		verify(mockTypedQuery, times(4)).setParameter(anyInt(), any());
		verify(mockTypedQuery, times(2)).getResultList();
	}

	@Test
	//viaje con paradas, no debe devolver null, si devuelve null o salta excepcion, falla el test
	public void test7() {
		String from = "A";
		String to = "B";
		Date date = new Date();
		String state = "pendiente";
		Integer cd = 50;
		Driver driver = new Driver();
		ArrayList<String> paradas = new ArrayList<String>();
		paradas.add("C");
		paradas.add("D");


		Ride ride = new Ride(from, to, paradas, date, 4, 10, driver);

		Mockito.when(db.find(eq(Ride.class), eq(cd))).thenReturn(ride);
		
		TypedQuery<Ride> mockTypedQuery = mock(TypedQuery.class);
        Mockito.when(db.createQuery(anyString(), eq(Ride.class))).thenReturn(mockTypedQuery);
        Mockito.when(mockTypedQuery.setParameter(anyInt(), any())).thenReturn(mockTypedQuery);
        Mockito.when(mockTypedQuery.setParameter(anyString(), any())).thenReturn(mockTypedQuery);
        
        Mockito.when(mockTypedQuery.getResultList()).thenReturn(Collections.singletonList(ride));
        
		Ride result = sut.getRideStopsByCod(from, to, date, state, cd);

		assertNotNull(result);
		assertEquals(ride, result);

		verify(db, times(1)).find(eq(Ride.class), eq(cd));
		verify(db, times(1)).createQuery(anyString(), eq(Ride.class));
		verify(mockTypedQuery, times(4)).setParameter(anyInt(), any());
		verify(mockTypedQuery, times(2)).getResultList();
	}
	
	
}