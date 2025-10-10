package dataAccess;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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


//comentario para probar el build
//funciona?
/**
 * It implements the data access to the objectDb database
 */
public class DataAccess  {
	private  EntityManager  db;
	private  EntityManagerFactory emf;

	ConfigXML c=ConfigXML.getInstance();

	private static final String CITY_DONOSTIA = "Donostia";
	private static final String CITY_MADRID = "Madrid";
	private static final String CITY_BARCELONA = "Barcelona";
	private static final String CITY_GRANADA = "Granada";
	private static final String CITY_VALENCIA = "Valencia";




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
	        createBaseRides();
	        createUsersAndCustomRides();
	        db.getTransaction().commit();
	        System.out.println("Db initialized");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void createBaseRides() {
		String[][] routes = {
		        {CITY_DONOSTIA, CITY_MADRID, "Alava", "Logroño", "Soria"},
		        {CITY_DONOSTIA, CITY_BARCELONA, "Pamplona", "Zaragoza", "Tarragona"},
		        {CITY_BARCELONA, CITY_MADRID, "Tarragona", "Teruel", "Guadalajara"},
		        {CITY_BARCELONA, CITY_VALENCIA, "Tarragona", "Castellón"},
		        {CITY_VALENCIA, CITY_MADRID, "Albacete", "Cuenca"},
		        {CITY_GRANADA, CITY_MADRID, "Jaén", "Toledo"},
		        {CITY_GRANADA, CITY_DONOSTIA, "Jaén", "Toledo", CITY_MADRID, "Soria"}
		};

		    for (String[] r : routes) {
		        persistRoundTrip(r[0], r[1], Arrays.copyOfRange(r, 2, r.length));
		    }
	}
		   
	private void persistRoundTrip(String from, String to, String... stops) {
	    Ride a = new Ride(from, to);
	    Ride b = new Ride(to, from);
	    for (String s : stops) { a.addStop(s); b.addStop(s); }
	    a.setState("ini"); b.setState("ini");
	    db.persist(a); db.persist(b);
	}
	
	private void createUsersAndCustomRides() {
		User userT = createUser("emailT", "T");
	    User userD = createUser("emailD", "D");

	    float[] prices = {22.19f, 35.13f, 17.75f, 48.22f, 28.19f, 20.19f, 11.75f, 13.95f, 60.42f};
	    int[] seats = {2, 3, 1, 4, 2, 2, 1, 1, 5};
	    String[][] stops = {
	        {"Pamplona"}, {"Pamplona", "Zaragoza"}, {"Pamplona", "Tarragona"}, {},
	        {"Pamplona", "Zaragona", "Tarragona"}, {}, {}, {"Tarragona"}, {"Zaragoza"}
	    };

	    for (int i = 0; i < prices.length; i++)
	        addRide(userD, stops[i], seats[i], prices[i]);

	    db.persist(userT);
	    db.persist(userD);
	}
	
	private User createUser(String email, String role) {
	    User u = new User(email, email, email, role);
	    if (role.equals("T")) u.getTraveler().setEmail(email);
	    else u.getDriver().setEmail(email);
	    return u;
	}

	private void addRide(User userD, String[] stops, int seats, float price) {
	    userD.getDriver().addCreatedRide(
	        new Ride("Barcelona", CITY_DONOSTIA, Arrays.asList(stops), newDate(2024, 4, 19), seats, price, userD.getDriver())
	    );
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
		processRideBooking(ride, drvr, trvlr);
		db.getTransaction().commit();
	
		System.out.println("Viaje reservado: \n"+ride);
		return ride;
	}

	private void processRideBooking(Ride ride, Driver drvr, Traveler trvlr) {
		ride.setState("reservado");
		ride.setTravelerInfo(trvlr);
		
		applyPayment(ride, trvlr);
		
		trvlr.bookRide(ride);
		drvr.bookRide(ride);
	}

	private void applyPayment(Ride ride, Traveler trvlr) {
		DecimalFormat df = new DecimalFormat("0.00");
		float price = ride.getPrice();
		
		if(trvlr.hasDiscountRide(ride.getFrom(), ride.getTo())) {
			ride.setDiscount();
			price = (float) (price - price * 0.1);
		}
		trvlr.subCash(Float.parseFloat(df.format(price)));
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
	
	public Ride getRideStopsByCod(String state, Ride r) {
		Ride ride = this.db.find(Ride.class, r.getRideNumber());
	    if(ride != null) {
	    	List<Ride> rides = this.getRides(r.getFrom(), r.getTo(), r.getDate(), state);
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
		else if(user != null && user.getPassword().equals(passw)) {
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