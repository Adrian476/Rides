package domain;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Ride implements Serializable {
	@XmlID
	@Id 
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer rideNumber;
	private String from;
	private String to;
	private List<String> stops;
	private Date date;
	private int seats;
	private String state;
	private float price;
	private boolean hasDiscount;
	
	private Driver driverInfo;
	private Traveler travelerInfo;

	
	public Ride(String from, String to, List<String> stops, Date date, int seats, float price, Driver drvr) {
		super();
		this.from = from;
		this.to = to;
		this.stops = stops;
		this.seats = seats;
		this.date = date;
		this.state = "pendiente";
		this.price = price;
		this.driverInfo = drvr;
		this.hasDiscount = false;
	}
	
	public Ride(String from, String to) {
		super();
		this.from = from;
		this.to = to;
		this.stops = new ArrayList<String>();
		this.price = 0;
	}

	public Integer getRideNumber() {
		return rideNumber;
	}
	
	public void setRideNumber(Integer rideNumber) {
		this.rideNumber = rideNumber;
	}

	public Driver getDriverInfo() {
		return driverInfo;
	}
	
	public void setDriverInfo(Driver drvr) {
		this.driverInfo = drvr;
	}
	
	public Traveler getTravelerInfo() {
		return travelerInfo;
	}
	
	public void setTravelerInfo(Traveler trvlr) {
		this.travelerInfo = trvlr;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public boolean hasDiscount() {
		return hasDiscount;
	}
	
	public void setDiscount() {
		hasDiscount = true;
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getTo() {
		return to;
	}
	
	public List<String> getStops() {
		return stops;
	}
	
	public void addStop(String stop){
		stops.add(stop);
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public float getnSeats() {
		return seats;
	}

	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}

	public String toString(){
		String s = "";
		s = "Origen: "+from;
		s += "\nDestino: "+to;
		s += "\nParadas: "+stops;
		s += "\nNº asientos: "+seats;
		s += "\nFecha: "+date;
		s += "\nPrecio: "+price;
		s += "\nEstado: "+state;
		
		return s;  
	}
	
	public String stopsToString() {
		String s = "";
		for(String st: stops) 
			s = s+st+"\n";
		return s;
	}
}
