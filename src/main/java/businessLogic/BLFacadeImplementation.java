package businessLogic;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.jws.WebMethod;
import javax.jws.WebService;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Ride;
import domain.Traveler;
import domain.User;
import domain.Driver;
//import domain.PaymentInfo;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation  implements BLFacade {
	DataAccess dbManager;

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		
		
		    dbManager=new DataAccess();
		    
		//dbManager.close();

		
	}
	
    public BLFacadeImplementation(DataAccess da)  {
		
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		ConfigXML c=ConfigXML.getInstance();
		
		dbManager=da;		
	}
    
    
    /**
     * {@inheritDoc}
     */
    @WebMethod public List<String> getDepartCities(){
    	dbManager.open();	
		
		 List<String> departLocations=dbManager.getDepartCities();		

		dbManager.close();
		
		return departLocations;
    	
    }
    /**
     * {@inheritDoc}
     */
	@WebMethod public List<String> getDestinationCities(String from){
		dbManager.open();	
		
		 List<String> targetCities=dbManager.getArrivalCities(from);		

		dbManager.close();
		
		return targetCities;
	}
	
	public List<String> getThroughCities(String from ,String to){
		dbManager.open();	
		List<String> throughCities = dbManager.getThroughCities(from, to);
		dbManager.close();
		return throughCities;
	}

	/**
	 * {@inheritDoc}
	 */
   @WebMethod
   public Ride createRide(String from, String to, List<String> stops, Date date, Integer seats, float price, Driver drvr){   
		Ride ride = new Ride(from, to, stops, date, seats, price, drvr);
		dbManager.open();
		Ride r = dbManager.addRide(ride);	
		dbManager.close();
		return r;
   };
	
	public Ride bookRide(String from, String to, Date date, Integer cd, Traveler t) {
		dbManager.open();
		Ride ride = dbManager.getRide(cd);

		if(ride != null) {
			if(t.getCash() > ride.getPrice()) {
				List<Ride> rides = dbManager.getRides(from, to, date, "pendiente");
				if(rides.contains(ride))
					ride = dbManager.bookRide(cd, t.getEmail());
				else
					ride = null;
			}
			else ride.setPrice(-1);
		}
		dbManager.close();
		return ride;
	}
	
	public Ride acceptRide(String from, String to, Date date, Integer cd, String emailDriver) {
		dbManager.open();
		Ride ride = dbManager.getRide(cd);

		if(ride != null) {
			List<Ride> rides = dbManager.getRides(from, to, date, "reservado");
			if(rides.contains(ride)) 
				ride = dbManager.acceptRide(cd);
			else
				ride = null;
		}
		
		dbManager.close();
		return ride;
	}
		
	public Ride cancelRide(String from, String to, Date date, Integer cd, String emailTraveler) {
		dbManager.open();
		Ride ride = dbManager.getRide(cd);

		if(ride != null) {
			List<Ride> rides = dbManager.getRides(from, to, date, "confirmado");
			if(rides.contains(ride)) 
				ride = dbManager.cancelRide(cd);
			else
				ride = null;
		}
		dbManager.close();
		return ride;
	}

	public Driver getDriver(String email) {
		dbManager.open();
		Driver drvr = dbManager.getDriver(email);
		dbManager.close();
		return drvr;
	}
	
	public Traveler getTraveler(String email) {
		dbManager.open();
		Traveler trvlr = dbManager.getTraveler(email);
		dbManager.close();
		return trvlr;
	}
	
	public Ride getRide(Integer cd) {
		dbManager.open();
		Ride ride = dbManager.getRide(cd);
		dbManager.close();
		return ride;
	}
	
	public List<Ride> getRides(String from, String to, Date date, String state){
		dbManager.open();
		List<Ride> rides = dbManager.getRides(from, to, date, state);
		dbManager.close();
		return rides;
	}
	
	public List<Ride> getAcceptedRides(String from, String to, Date date, String email){
		dbManager.open();
		List<Ride> rides = dbManager.getAcceptedRides(from, to, date, email);
		dbManager.close();
		return rides;
	}
	
	public String getRideStopsByCod(String from, String to, Date date, String state, Integer cd) {
		dbManager.open();
		Ride ride = dbManager.getRideStopsByCod(from, to, date, state, cd);
		dbManager.close();

		if(ride == null)
			return "No se encuentra el viaje";
		else if(ride.getStops().size() == 0) {
			return "Sin paradas";
		}
		return ride.stopsToString();
	}
	
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		dbManager.open();
		List<Date> dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		dbManager.close();
		return dates;
	}
	
	
	public void close() {
		DataAccess dB4oManager=new DataAccess();

		dB4oManager.close();

	}

	/**
	 * {@inheritDoc}
	 */
    @WebMethod	
	 public void initializeBD(){
    	dbManager.open();
		dbManager.initializeDB();
		dbManager.close();
	}
    

    public User registerUser(String email, String name, String passw, String type) {
    	User u = new User(email, name, passw, type);
    	dbManager.open();
    	User user = dbManager.registerUser(u);
    	dbManager.close();
    	return user;
    }
    
    public User loginUser(String name, String passw) {
    	dbManager.open();
    	User userAux = dbManager.loginUser(name, passw);
    	dbManager.close();
    	return userAux;
    }

}