import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;

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


	Driver driver;




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
}