package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="reservation")
public class Reservation extends Model{

	@Column
	public Date timeFrom;
	@Column
	public Date timeTo;
	@Column
	public String user;
	@Column
	public String parking;
	
	public Reservation(){
		
	}

	public Reservation(Date timeFrom, Date timeTo, String user, String parking) {
		super();
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.user = user;
		this.parking = parking;
	}
	
	
	

}
