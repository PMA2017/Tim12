package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.Reservation;
import play.db.jpa.JPA;
import play.mvc.Controller;

public class Reservations extends Controller{

	public static void create(){
		String jsonAsString = params.get("body");
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = (JsonObject)jsonParser.parse(jsonAsString);
		models.Reservation newReservation = new Gson().fromJson(jsonObject, models.Reservation.class);
		
		try{
			Boolean success = newReservation.validateAndCreate();
			if (success){
				renderJSON(newReservation);
			}else{
				badRequest("Unable to validate and create entity!");
			}
		}catch(Exception e){
			error("Unable to create entity!");
		}
	}
	
	public static void reserve(){
		String jsonAsString = params.get("body");
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = (JsonObject)jsonParser.parse(jsonAsString);
		models.Reservation newReservation = new Gson().fromJson(jsonObject, models.Reservation.class);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String reportDate = df.format(newReservation.timeFrom);
		String reportDate1 = df.format(newReservation.timeTo);
		
		Date date = new Date();
		Query query1 = JPA.em().createQuery("delete Reservation where timeto < date.now()");
		//query1.setParameter(1, df.format(date));
		query1.executeUpdate();
		
		Query query = JPA.em().createQuery("select count(*) from Reservation where parking=? and (timefrom between ? and ? or timeto between ? and ? or timefrom < ? and timeto > ? )");
		query.setParameter(1, "Centar");
		query.setParameter(2, reportDate);
		query.setParameter(3, reportDate1);
		query.setParameter(4, reportDate);
		query.setParameter(5, reportDate1);
		query.setParameter(6, reportDate);
		query.setParameter(7, reportDate1);
		List<Long> result = query.getResultList();
		Long smor = result.get(0);
		if(smor > 0){
			List<models.Parking> parkings = models.Parking.find("byParkingname", newReservation.parking).fetch();
			models.Parking parking = parkings.get(0);
			Long smor1 = (long)parking.totalNumberOfSpaces;
			if(smor < smor1){
				newReservation.save();
				renderJSON(result.get(0));
			}else{
				return;
			}
		}else{
			newReservation.save();
			renderJSON(result.get(0));
		}
	}
	
	public static void deleteReservation(){
		String jsonAsString = params.get("body");
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = (JsonObject)jsonParser.parse(jsonAsString);
		models.Reservation newReservation = new Gson().fromJson(jsonObject, models.Reservation.class);
		
		Query query1 = JPA.em().createQuery("delete Reservation where user = ? and parking = ? and timeto = ?");
		query1.setParameter(1, newReservation.user);
		query1.setParameter(2, newReservation.parking);
		query1.setParameter(3, newReservation.timeTo);
		try{
			query1.executeUpdate();
			renderJSON(0);
		}catch(Exception e){
			error("Unable to delete entity!");
		}
	}
	
}
