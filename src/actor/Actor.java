package actor;

import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.Map;

public class Actor {
	private String name;
	private String careerDescription;
	private ArrayList<String> filmography;
	private Map<ActorsAwards, Integer> awards;

	private double rating;

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

	public Map<ActorsAwards, Integer> getAwards() {
		return awards;
	}

	public void setAwards(Map<ActorsAwards, Integer> awards) {
		this.awards = awards;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getNumberOfAwards() {
		int total = 0;

		for (Integer count : this.awards.values()) {
			total += count;
		}

		return total;
	}
}
