import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
import testOperations.TestDataAccess;

public class GetRideStopsBDWhiteTest {


	static DataAccess sut=new DataAccess();

	static TestDataAccess testDA=new TestDataAccess();







	@Test
	//viaje no existe, debe devolver null, si no es asi o salta excepcion, falla el test
	public void test1() {

		String from = "A";
		String to = "B";
		Date date = new Date();
		String state = "pendiente";
		Integer cd = 50;

		sut.open();
		Ride result = sut.getRideStopsByCod(from, to, date, state, cd);
		sut.close();

		assertNull(result);
	} 

	@Test
	//viaje exite, paradas no, debe devolver el viaje, si salta excepcion o devuelve null, falla el test
	public void test2() {

		String from = "A";
		String to = "B";
		Date date = new Date();
		String state = "pendiente";
		Driver driver = new Driver();
		Ride ride = new Ride(from, to, null, date, 4, 10, driver);

		try {
			testDA.open();
			testDA.addDriver(driver);
			testDA.addRide(ride);


			sut.open();
			Ride result = sut.getRideStopsByCod(from, to, date, state, ride.getRideNumber());
			sut.close();

			assertNotNull(result);
			assertEquals(ride.getRideNumber(), result.getRideNumber());
			assertEquals(ride.getFrom(), result.getFrom());
			assertEquals(ride.getTo(), result.getTo());
			assertEquals(ride.getStops(), result.getStops());
			assertEquals(ride.getPrice(), result.getPrice(), 0.01f);

		} finally {
			testDA.removeRide(ride);
			testDA.removeDriver(driver);
			testDA.close();
		}
	} 




	@Test
	//viaje con paradas, debe devolver el viaje, si devuelve null o salta excepcion, falla el test
	public void test3() {


		String from = "A";
		String to = "B";
		Date date = new Date();
		String state = "pendiente";
		ArrayList<String> paradas = new ArrayList<String>();
		paradas.add("C");
		paradas.add("D");
		Driver driver = new Driver();
		Ride ride = new Ride(from, to, paradas, date, 4, 10, driver);

		try {
			testDA.open();
			testDA.addDriver(driver);
			testDA.addRide(ride);


			sut.open();
			Ride result = sut.getRideStopsByCod(from, to, date, state, ride.getRideNumber());
			sut.close();

			assertNotNull(result);
			assertEquals(ride.getRideNumber(), result.getRideNumber());
			assertEquals(ride.getFrom(), result.getFrom());
			assertEquals(ride.getTo(), result.getTo());
			assertEquals(ride.getStops(), result.getStops());
			assertEquals(ride.getPrice(), result.getPrice(), 0.01f);

		} finally {
			testDA.removeRide(ride);
			testDA.removeDriver(driver);
			testDA.close();
		}
	} 

}