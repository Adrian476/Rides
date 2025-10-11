package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Driver implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@XmlID
	@Id 
	private String email;
	@OneToOne(cascade=CascadeType.PERSIST)
	private User userInfo;
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private ArrayList<Ride> createdRides = new ArrayList<>();
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Ride> bookedRides = new Vector<Ride>();
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Ride> acceptedRides = new Vector<Ride>();

	
	public Driver() {
		super();
	}
	
	public User getUserInfo() {
		return userInfo;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}
	
	public void addCreatedRide(Ride ride)  {
		createdRides.add(ride);
	}
	
	public void bookRide(Ride ride) {
		createdRides.remove(ride);
		bookedRides.add(ride);
	}
	
	public void acceptRide(Ride ride){
		bookedRides.remove(ride);
		acceptedRides.add(ride);
	}
	
	public void cancelRide(Ride ride) {
		acceptedRides.remove(ride);
	}
	
	public List<Ride> getCreatedRides(){
		return createdRides;
	}
	
	public List<Ride> getBookedRides(){
		return bookedRides;
	}
	
	public List<Ride> getAcceptedRides(){
		return acceptedRides;
	}
	
	private List<Ride> getRidesFromDate(List<Ride> rides, String from, String to, Date date) {
		List<Ride> res = new ArrayList<>();
		for (Ride r : rides) {
			if (r.getDate().getTime() == date.getTime() && r.getFrom().equals(from) && r.getTo().equals(to)) {
				res.add(r);
			}
		}
		System.out.println(res);
		return res;
	}
	
	public List<Ride> getCreatedRidesFromDate(String from, String to, Date date){
		return getRidesFromDate(createdRides, from, to, date);
	}
	
	public List<Ride> getBookedRidesFromDate(String from, String to, Date date){
		return getRidesFromDate(bookedRides, from, to, date);
	}
	
	public List<Ride> getAcceptedRidesFromDate(String from, String to, Date date){
		return getRidesFromDate(acceptedRides, from, to, date);
	}
	
	public String getStopsFromDate(String from, String to, Date date, Integer code) {
	    List<Ride> ridesDate = new ArrayList<>();
	    
	    // Combinar las listas de rides de diferentes estados
	    ridesDate.addAll(getCreatedRidesFromDate(from, to, date));
	    ridesDate.addAll(getBookedRidesFromDate(from, to, date));
	    ridesDate.addAll(getAcceptedRidesFromDate(from, to, date));
	    
	    // Buscar el ride por código
	    Ride ride = findRideByCode(ridesDate, code);
	    if (ride == null) {
	        return "No se encuentra el viaje";
	    }
	    return ride.getStops().isEmpty() ? "Sin paradas" : ride.stopsToString();
	}

	// Método auxiliar para buscar un ride por su código
	private Ride findRideByCode(List<Ride> rides, Integer code) {
	    for (Ride r : rides) {
	        if (r.getRideNumber().compareTo(code) == 0) {
	            return r;
	        }
	    }
	    return null;
	}

	
}
