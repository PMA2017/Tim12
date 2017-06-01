package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="parking")
public class Parking extends Model{
	
	@Column
	public String parkingName;
	@Column
	public String adress;
	@Column
	public int totalNumberOfSpaces;
	@Column
	public int  numberOfFreeSpaces;
	@Column
	public double workingDayPrice;
	@Column
	public double weekendPrice;
	@Column
	public String workTime;
	@Column
	public String paymentWay;
	@Column
	public String informations;
	@Column
	public String image;
	@Column
	public double latitude;
	@Column
	public double longitude;
	
	
	public Parking(String parkingName, String adress, int totalNumberOfSpaces, int numberOfFreeSpaces,
			double workingDayPrice, double weekendPrice, String workTime, String paymentWay, String informations,
			String image, double latitude, double longitude) {
		super();
		this.parkingName = parkingName;
		this.adress = adress;
		this.totalNumberOfSpaces = totalNumberOfSpaces;
		this.numberOfFreeSpaces = numberOfFreeSpaces;
		this.workingDayPrice = workingDayPrice;
		this.weekendPrice = weekendPrice;
		this.workTime = workTime;
		this.paymentWay = paymentWay;
		this.informations = informations;
		this.image = image;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	

}
