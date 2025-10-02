package dataAccess;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import domain.User;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess  {
	private  EntityManager  db;
	private  EntityManagerFactory emf;

	ConfigXML c=ConfigXML.getInstance();

	private static final String CITY_DONOSTIA = "Donostia";

     public DataAccess()  {
		if (c.isDatabaseInitialized()) {
			String fileName=c.getDbFilename();

			File fileToDelete= new File(fileName);
			if(fileToDelete.delete()){
				File fileToDeleteTemp= new File(fileName+"$");
				fileToDeleteTemp.delete();

				  System.out.println("File deleted");
				} else {
				  System.out.println("Operation failed");
				}
		}
		open();
		if  (c.isDatabaseInitialized())initializeDB();
		
		System.out.println("DataAccess created => isDatabaseLocal: "+c.isDatabaseLocal()+" isDatabaseInitialized: "+c.isDatabaseInitialized());
		close();

	}
     
    public DataAccess(EntityManager db) {
    	this.db=db;
    }

	
	
	/**
	 * This is the data access method that initializes the database with some events and questions.
	 * This method is invoked by the business logic (constructor of BLFacadeImplementation) when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	public void initializeDB(){
		
		db.getTransaction().begin();

		try {

		   Calendar today = Calendar.getInstance();
		   
		   int month=today.get(Calendar.MONTH);
		   int year=today.get(Calendar.YEAR);
		   if (month==12) { month=1; year+=1;} 

		   
		   Ride ride1 = new Ride(CITY_DONOSTIA, "Madrid");
		   Ride ride12 = new Ride("Madrid", CITY_DONOSTIA);
		   ride1.addStop("Alava");
		   ride1.addStop("Logroño");
		   ride1.addStop("Soria");
		   ride12.addStop("Alava");
		   ride12.addStop("Logroño");
		   ride12.addStop("Soria");
		   ride1.setState("ini");
		   ride12.setState("ini");
		   
		   Ride ride2 = new Ride(CITY_DONOSTIA, "Barcelona");
		   Ride ride22 = new Ride("Barcelona", CITY_DONOSTIA);
		   ride2.addStop("Pamplona");
		   ride2.addStop("Zaragoza");
		   ride2.addStop("Tarragona");
		   ride22.addStop("Pamplona");
		   ride22.addStop("Zaragoza");
		   ride22.addStop("Tarragona");	
		   ride2.setState("ini");
		   ride22.setState("ini");
		   
		   Ride ride3 = new Ride("Barcelona", "Madrid");
		   Ride ride32 = new Ride("Madrid", "Barcelona");
		   ride3.addStop("Tarragona");
		   ride3.addStop("Teruel");
		   ride3.addStop("Guadalajara");
		   ride32.addStop("Tarragona");
		   ride32.addStop("Teruel");
		   ride32.addStop("Guadalajara");
		   ride3.setState("ini");
		   ride32.setState("ini");
		   
		   Ride ride4 = new Ride("Barcelona", "Valencia");
		   Ride ride42 = new Ride("Valencia", "Barcelona");
		   ride4.addStop("Tarragona");
		   ride4.addStop("Castellón");
		   ride42.addStop("Tarragona");
		   ride42.addStop("Castellón");  
		   ride4.setState("ini");
		   ride42.setState("ini");

		   Ride ride5 = new Ride("Valencia", "Madrid");
		   Ride ride52 = new Ride("Madrid", "Valencia");
		   ride5.addStop("Albacete");
		   ride5.addStop("Cuenca");
		   ride52.addStop("Albacete");
		   ride52.addStop("Cuenca");
		   ride5.setState("ini");
		   ride52.setState("ini");
		   
		   Ride ride6 = new Ride("Granada", "Madrid");
		   Ride ride62 = new Ride("Madrid", "Granada");
		   ride6.addStop("Jaén");
		   ride6.addStop("Toledo");
		   ride62.addStop("Jaén");
		   ride62.addStop("Toledo");
		   ride6.setState("ini");
		   ride62.setState("ini");

		   Ride ride7 = new Ride("Granada", CITY_DONOSTIA);
		   Ride ride72 = new Ride(CITY_DONOSTIA, "Granada");
		   ride7.addStop("Jaén");
		   ride7.addStop("Toledo");
		   ride7.addStop("Madrid");
		   ride7.addStop("Soria");
		   ride72.addStop("Jaén");
		   ride72.addStop("Toledo");
		   ride72.addStop("Madrid");
		   ride72.addStop("Soria");
		   ride7.setState("ini");
		   ride72.setState("ini");
		   
		   db.persist(ride1);
		   db.persist(ride12);
		   db.persist(ride2);
		   db.persist(ride22);
		   db.persist(ride3);
		   db.persist(ride32);
		   db.persist(ride4);
		   db.persist(ride42);
		   db.persist(ride5);
		   db.persist(ride52);
		   db.persist(ride6);
		   db.persist(ride62);
		   db.persist(ride7);
		   db.persist(ride72);
		   
		   
		   User userT = new User("emailT", "emailT","emailT", "T");
		   userT.getTraveler().setEmail("emailT");
		   
		   User userD = new User("emailD", "emailD","emailD", "D");
		   userD.getDriver().setEmail("emailD");
		   
		   List<String> rides = new ArrayList<String>();
		   rides.add("Pamplona");
		   userD.getDriver().addCreatedRide(new Ride("Barcelona", CITY_DONOSTIA, rides, newDate(2024, 4, 19), 2, (float) 22.19, userD.getDriver()));
		   
		   rides = new ArrayList<String>();
		   rides.add("Pamplona");
		   rides.add("Zaragoza");
		   userD.getDriver().addCreatedRide(new Ride("Barcelona", CITY_DONOSTIA, rides, newDate(2024, 4, 19), 3, (float) 35.13, userD.getDriver()));
		   
		   rides = new ArrayList<String>();
		   rides.add("Pamplona");
		   rides.add("Tarragona");
		   userD.getDriver().addCreatedRide(new Ride("Barcelona", CITY_DONOSTIA, rides, newDate(2024, 4, 19), 1, (float) 17.75, userD.getDriver()));
		   
		   rides = new ArrayList<String>();
		   userD.getDriver().addCreatedRide(new Ride("Barcelona", CITY_DONOSTIA, rides, newDate(2024, 4, 19), 4, (float) 48.22, userD.getDriver()));
		   
		   rides = new ArrayList<String>();
		   rides.add("Pamplona");
		   rides.add("Zaragona");
		   rides.add("Tarragona");
		   userD.getDriver().addCreatedRide(new Ride("Barcelona", CITY_DONOSTIA, rides, newDate(2024, 4, 19), 2, (float) 28.19, userD.getDriver()));
		   
		   rides = new ArrayList<String>();
		   userD.getDriver().addCreatedRide(new Ride("Barcelona", CITY_DONOSTIA, rides, newDate(2024, 4, 19), 2, (float) 20.19, userD.getDriver()));
		   
		   rides = new ArrayList<String>();
		   userD.getDriver().addCreatedRide(new Ride("Barcelona", CITY_DONOSTIA, rides, newDate(2024, 4, 19), 1, (float) 11.75, userD.getDriver()));
		   
		   rides = new ArrayList<String>();
		   rides.add("Tarragona");
		   userD.getDriver().addCreatedRide(new Ride("Barcelona", CITY_DONOSTIA, rides, newDate(2024, 4, 19), 1, (float) 13.95, userD.getDriver()));
		   
		   rides = new ArrayList<String>();
		   rides.add("Zaragoza");
		   userD.getDriver().addCreatedRide(new Ride("Barcelona", CITY_DONOSTIA, rides, newDate(2024, 4, 19), 5, (float) 60.42, userD.getDriver()));
		   

		   db.persist(userT);
		   db.persist(userD);
		   
		   db.getTransaction().commit();
		   System.out.println("Db initialized");
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
		   
	/**
	 * This method returns all the cities where rides depart 
	 * @return collection of cities
	 */
	public List<String> getDepartCities(){
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from", String.class);
		List<String> cities = query.getResultList();
		return cities;
	}
	/**
	 * This method returns all the arrival destinations, from all rides that depart from a given city  
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String from){
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to", String.class);
		query.setParameter(1, from);
		List<String> arrivingCities = query.getResultList(); 
		return arrivingCities;
	}
	
	public List<String> getThroughCities(String from ,String to){
		List<String> res = new ArrayList<String>();
		TypedQuery<Ride> query = db.createQuery("SELECT DISTINCT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.state=?3", Ride.class);
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, "ini");
		
		if(query.getResultList().size() > 0) {
			System.out.println(query.getResultList().get(0).getStops());
			res = query.getResultList().get(0).getStops();
		}
		return res;
	}

	
	public Ride addRide(Ride ride) {
		Driver drvr = db.find(Driver.class, ride.getDriverInfo());

		db.getTransaction().begin();
		drvr.addCreatedRide(ride);
		db.getTransaction().commit();
		
		return ride;
	}

	public Ride bookRide(Integer cd, String emailTraveler) {
		Ride ride = db.find(Ride.class, cd);
		Driver drvr = db.find(Driver.class, ride.getDriverInfo().getEmail());
		Traveler trvlr = db.find(Traveler.class, emailTraveler);

		db.getTransaction().begin();
		ride.setState("reservado");
		ride.setTravelerInfo(trvlr);
		
		DecimalFormat df = new DecimalFormat("0.00");
		if(trvlr.hasDiscountRide(ride.getFrom(), ride.getTo())) {
			ride.setDiscount();
			trvlr.subCash(Float.parseFloat(df.format((float) (ride.getPrice() - ride.getPrice() * 0.1))));
		}
		else
			trvlr.subCash(Float.parseFloat(df.format(ride.getPrice())));
		
		trvlr.bookRide(ride);
		drvr.bookRide(ride);
		db.getTransaction().commit();
	
		System.out.println("Viaje reservado: \n"+ride);
		return ride;
	}
	
	public Ride acceptRide(Integer cd) {
		Ride ride = db.find(Ride.class, cd);
		Driver drvr = db.find(Driver.class, ride.getDriverInfo().getEmail());
		Traveler trvlr = db.find(Traveler.class, ride.getTravelerInfo().getEmail());

		db.getTransaction().begin();
		ride.setState("confirmado");

		drvr.acceptRide(ride);
		trvlr.acceptRide(ride);
		trvlr.checkDiscount(ride.getFrom(), ride.getTo());
		db.getTransaction().commit();
		
		System.out.println("Viaje aceptado: \n"+ride);
		return ride;
	}
	
	public Ride cancelRide(Integer cod) {
		Ride ride = db.find(Ride.class, cod);
		Driver drvr = db.find(Driver.class, ride.getDriverInfo().getEmail());
		Traveler trvlr = db.find(Traveler.class, ride.getTravelerInfo().getEmail());
		
		db.getTransaction().begin();
		ride.setState("cancelado");
		
		drvr.cancelRide(ride);
		trvlr.cancelRide(ride);
		db.getTransaction().commit();
		
		System.out.println("Viaje cancelado: \n"+ride);
		return ride;
	}
	
	/**
	 * This method retrieves from the database the dates a month for which there are events
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride 
	 * @param date of the month for which days with rides want to be retrieved 
	 * @return collection of rides
	 */
	
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getEventsMonth");
		List<Date> res = new ArrayList<>();	
		
		Date firstDayMonthDate= UtilDate.firstDayMonth(date);
		Date lastDayMonthDate= UtilDate.lastDayMonth(date);
				
		TypedQuery<Date> query = db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4 AND r.state=?5",Date.class);   
		
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, firstDayMonthDate);
		query.setParameter(4, lastDayMonthDate);
		query.setParameter(5, "s");
		
		List<Date> dates = query.getResultList();
		for (Date d:dates)
			res.add(d);
		
	 	return res;
	}
	
	public List<Ride> getRides(String from, String to, Date date, String state) {
		
        TypedQuery<Ride> query = this.db.createQuery("SELECT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date=?3 AND r.state=?4", Ride.class);  
        query.setParameter(1, from);
        query.setParameter(2, to);
        query.setParameter(3, date);
        query.setParameter(4, state);
        
        System.out.println(query.getResultList());
        
		return query.getResultList();
	}
	
	public List<Ride> getAcceptedRides(String from, String to, Date date, String email) {
		
        TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date=?3 AND r.state=?4 AND r.travelerInfo.email=?5", Ride.class);  
        query.setParameter(1, from);
        query.setParameter(2, to);
        query.setParameter(3, date);
        query.setParameter(4, "confirmado");
        query.setParameter(5, email);
        
        System.out.println(query.getResultList());
        
		return query.getResultList();
	}
	
	public Driver getDriver(String email) {
		Driver d = db.find(Driver.class, email);
		System.out.println(d.getCreatedRides());
		System.out.println(d.getBookedRides());
		System.out.println(d.getAcceptedRides());
		return d;
	}
	
	public Traveler getTraveler(String email) {
		Traveler t = db.find(Traveler.class, email);
		System.out.println(t.getBookedRides());
		System.out.println(t.getAcceptedRides());
		return t;
	}
	
	public Ride getRideStopsByCod(String from, String to, Date date, String state, Integer cd) {
		Ride ride = this.db.find(Ride.class, cd);
	    if(ride != null) {
	    	List<Ride> rides = this.getRides(from, to, date, state);
	    	if(!rides.contains(ride))
	    		return null;
	    }
		return ride;
	}
	
	public Ride getRide(Integer cd) {
		Ride ride =	db.find(Ride.class, cd);
		return ride;
	}
	
	
	public User registerUser(User user) {
		User u = db.find(User.class, user.getEmail());
		if(u == null) {
			db.getTransaction().begin();
			if(user.getTraveler() != null) {
				user.getTraveler().setUserInfo(user);
				user.getTraveler().setEmail(user.getEmail());
			}
			else if (user.getDriver() != null) {
				user.getDriver().setUserInfo(user);
				user.getDriver().setEmail(user.getEmail());
			}
			db.persist(user);
			db.getTransaction().commit();
		}
		return u;
	}
	
	public User loginUser(String email, String passw) {
		User user = db.find(User.class, email);
		//Contraseña incorrecta
		if (user != null && !user.getPassword().equals(passw)){
				user.setPassword(null);
		}
		if(user != null) {
			if(user.getDriver() != null) {
				System.out.println(user.getDriver().getCreatedRides());
				System.out.println(user.getDriver().getBookedRides());
				System.out.println(user.getDriver().getAcceptedRides());
			}
			else if(user.getTraveler() != null)
				System.out.println(user.getTraveler().getAcceptedRides());
		}	
		return user;
	}
	
	private Date newDate(int year,int month,int day) {

	     Calendar calendar = Calendar.getInstance();
	     calendar.set(year, month, day,0,0,0);
	     calendar.set(Calendar.MILLISECOND, 0);

	     return calendar.getTime();
	}	

public void open(){
		
		String fileName=c.getDbFilename();
		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);
			  db = emf.createEntityManager();
    	   }
		System.out.println("DataAccess opened => isDatabaseLocal: "+c.isDatabaseLocal());

		
	}

	public void close(){
		db.close();
		System.out.println("DataAcess closed");
	}

}