import models.Parking;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job{
	public void doJob() {
		if (Parking.count()== 0){
			Fixtures.loadModels("initial-data.yml");
			System.out.println("Database populated with initial data.");
		}
	}
}