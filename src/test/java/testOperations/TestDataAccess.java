package testOperations;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import configuration.ConfigXML;
import domain.Driver;
import domain.Ride;
import domain.User;


public class TestDataAccess {
	protected  EntityManager  db;
	protected  EntityManagerFactory emf;

	ConfigXML  c=ConfigXML.getInstance();


	public TestDataAccess()  {
		
		System.out.println("TestDataAccess created");

		//open();
		
	}

	
	public void open(){
		

		String fileName=c.getDbFilename();
		
		if (c.isDatabaseLocal()) {
			  emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			  db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);

			  db = emf.createEntityManager();
    	   }
		System.out.println("TestDataAccess opened");

		
	}
	public void close(){
		db.close();
		System.out.println("TestDataAccess closed");
	}
	
	public boolean existUser(User user) {
		 return  db.find(User.class, user.getEmail())!=null;
		 

	}
	
	public void createUser(User user) {
		System.out.println(">> TestDataAccess: createUser");
			db.getTransaction().begin();
			try {
				db.persist(user);
				db.getTransaction().commit();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			
    }

	public boolean removeUser(User user) {
		System.out.println(">> TestDataAccess: removeUser");
		User u = db.find(User.class, user.getEmail());
		if (u!=null) {
			db.getTransaction().begin();
			db.remove(u);
			db.getTransaction().commit();
			return true;
		} else 
		return false;
    }
	
	public User findUser(User user) {
		System.out.println(">> TestDataAccess: findUser"); 	
		return db.find(User.class, user.getEmail());
    }
		
	public void addRide(Ride ride) {
        db.getTransaction().begin();
        db.persist(ride);
        db.getTransaction().commit();
    }
	
	public void removeRide(Ride ride) {
		db.getTransaction().begin();
		Ride r = db.find(Ride.class, ride.getRideNumber());
        if (r != null) {
            db.remove(r);
        }
        db.getTransaction().commit();
	}







}
