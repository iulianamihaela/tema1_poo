package actor;

import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.Map;

public class Actor {
	private String name;
	private String careerDescription;
	private ArrayList<String> filmography;
	private Map<ActorsAwards, Integer> awards;

	public Actor(ActorInputData actorInputData) {
		this.name = actorInputData.getName();
		this.careerDescription = actorInputData.getCareerDescription();
		this.filmography = actorInputData.getFilmography();
		this.awards = actorInputData.getAwards();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCareerDescription() {
		return careerDescription;
	}

	public void setCareerDescription(String careerDescription) {
		this.careerDescription = careerDescription;
	}

	public ArrayList<String> getFilmography() {
		return filmography;
	}

	public void setFilmography(ArrayList<String> filmography) {
		this.filmography = filmography;
	}
}
