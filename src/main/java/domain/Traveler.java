package domain;


import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity
public class Traveler implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	private String email;
	@OneToOne(cascade=CascadeType.PERSIST)
	private User userInfo;
	private float cash;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<DiscountInfo> listDiscounts;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Ride> bookedRides;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Ride> acceptedRides;
	
	
	public Traveler(float cash) {
		bookedRides = new ArrayList<Ride>();
		acceptedRides = new ArrayList<Ride>();
		listDiscounts = new ArrayList<DiscountInfo>();
		this.cash = cash;
	}
	
	public User getUserInfo() {
		return userInfo;
	}
	
	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}
	
	public void setEmail(String email) {
		this.email=email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void bookRide(Ride ride) {
		bookedRides.add(ride);
	}
	
	public void acceptRide(Ride ride) {
		bookedRides.remove(ride);
		acceptedRides.add(ride);
	}
	
	public boolean hasDiscountRide(String origin, String destiny) {
		for(DiscountInfo di: listDiscounts) {
			if(di.getFrom().equals(origin) && di.getTo().equals(destiny))
				return true;
		}	
		return false;
	}
	
	public float getPriceBookedRides(Ride ride) {
		for(Ride r: bookedRides) {
			if(r.equals(ride))
				return r.getPrice();
		}
		return 0;
	}
	
	public void checkDiscount(String origin, String destiny) {	
		int cont = 0;
		for(Ride r: acceptedRides) {
			if(r.getFrom().equals(origin) && r.getTo().equals(destiny))
				cont++;
		}
		if(cont == 5) 
			listDiscounts.add(new DiscountInfo(origin, destiny));	
	}
	
	public List<DiscountInfo> getDiscountList() {
		return listDiscounts;
	}
	
	public List<Ride> getBookedRidesFromDate(String from, String to, Date date){
		List<Ride> res = new ArrayList<Ride>();
		for(Ride r: bookedRides) {
			if(r.getDate().getTime() == date.getTime() && r.getFrom().equals(from) && r.getTo().equals(to))
				res.add(r);
		}
		return res;
	}
	
	public List<Ride> getAcceptedRides(){
		return acceptedRides;
	}
	
	public List<Ride> getBookedRides(){
		return bookedRides;
	}
	
	public Ride getRideFromList(String from, String to, Date date) {
		for(Ride r: bookedRides) {
			if(r.getFrom().equals(from) && r.getTo().equals(to) && r.getDate().getTime() == date.getTime())
				return r;
		}
		return null;
	}
	
	public float getCash() {
		return cash;
	}
	
	public void addCash(float money) {
		cash+=money;
	}

	public void subCash(float price) {
		cash -= price;
	}
	
	/*
	public void addPayment(float payment, Ride ride) {
		PaymentInfo pi = new PaymentInfo(payment, ride);
		listPayments.add(pi);
		cash -= payment;
	}
	*/
		
	/*public List<PaymentInfo> getListPayments(Ride ride) {
		return listPayments;
	}
	*/
		
	public void cancelRide(Ride ride) {		
		if(ride.hasDiscount()) 
			cash = (float) (cash + (ride.getPrice() - ride.getPrice() * 0.1) * 0.9);
		else
			cash = (float) (cash + ride.getPrice() * 0.9);
		bookedRides.remove(ride);
	}

}