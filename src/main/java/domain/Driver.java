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
	
	public String getStopsFromDate(String from, String to, Integer code, Date date, String state) {
		List<Ride> ridesDate = new ArrayList<Ride>();
		
		if(state.equals("p"))
			ridesDate = this.getCreatedRidesFromDate(from, to, date);
		else if(state.equals("b"))
			ridesDate = this.getBookedRidesFromDate(from, to, date);
		else if(state.equals("a"))
			ridesDate = this.getAcceptedRidesFromDate(from, to, date);
		
		boolean isCode = false;
		for(Ride r: ridesDate) {
			if(r.getRideNumber().compareTo(code) == 0) {
				isCode = true;
				if(r.getStops().size() > 0)
					return r.stopsToString();
			}
		}
		if(isCode == false) return "No se encuentra el viaje";
		else return "Sin paradas";
	}
}
