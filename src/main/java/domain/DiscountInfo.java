package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DiscountInfo {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer discountNumber;
	private String from;
	private String to;
	
	public DiscountInfo(String from, String to) {
		this.from = from;
		this.to = to;
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getTo() {
		return to;
	}
}
