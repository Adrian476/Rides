package businessLogic;

import java.util.Date;
import java.util.List;

import domain.Driver;
//import domain.Booking;
import domain.Ride;
import domain.Traveler;
import domain.User;

import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

import javax.jws.WebMethod;
import javax.jws.WebService;
 
/**
 * Interface that specifies the business logic.
 */
@WebService
public interface BLFacade  {
	  
	/**
	 * This method returns all the cities where rides depart 
	 * @return collection of cities
	 */
	@WebMethod public List<String> getDepartCities();
	
	/**
	 * This method returns all the arrival destinations, from all rides that depart from a given city  
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	@WebMethod public List<String> getDestinationCities(String from);

	public List<String> getThroughCities(String from, String to);

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @param nPlaces available seats
	 * @param driver to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today 
 	 * @throws RideAlreadyExistException if the same ride already exists for the driver
	 */
   @WebMethod
   	public Ride createRide(String from, String to, List<String> stops, Date date, Integer seats, float price, Driver drvr);
      
   	public Ride bookRide(String from, String to, Date date, Integer cd,  Traveler t);
   	
   	public Ride acceptRide(String from, String to, Date date, Integer cd, String emailTraveler);
   	
   	public Ride cancelRide(String from, String to, Date date, Integer cd, String emailTraveler);
   	
   	public Driver getDriver(String email);
   	
   	public Traveler getTraveler(String email);
   	
   	public String getRideStopsByCod(String from, String to, Date date, String state, Integer cd);
   	   
   	public Ride getRide(Integer cd);
   	
   	public List<Ride> getRides(String from, String to, Date date, String state);
   	
	public List<Ride> getAcceptedRides(String from, String to, Date date, String email);
			
	/**
	 * This method retrieves from the database the dates a month for which there are events
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride 
	 * @param date of the month for which days with rides want to be retrieved 
	 * @return collection of rides
	 */
	@WebMethod public List<Date> getThisMonthDatesWithRides(String from, String to, Date date);
		
	/**
	 * This method calls the data access to initialize the database with some events and questions.
	 * It is invoked only when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	@WebMethod public void initializeBD();
	
	public User registerUser(String email, String name, String passw, String type);
	
	public User loginUser(String user, String passw);

	
}