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

    /**
     * @param actorInputData datele de intrare despre actor
     */
    public Actor(final ActorInputData actorInputData) {
        this.name = actorInputData.getName();
        this.careerDescription = actorInputData.getCareerDescription();
        this.filmography = actorInputData.getFilmography();
        this.awards = actorInputData.getAwards();
    }

    /**
     * @return numele actorului
     */
    public String getName() {
        return name;
    }

    /**
     * @param name numele actorului
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return descrierea actorului
     */
    public String getCareerDescription() {
        return careerDescription;
    }

    /**
     * @param careerDescription descrierea actorului
     */
    public void setCareerDescription(final String careerDescription) {
        this.careerDescription = careerDescription;
    }

    /**
     * @return filmografia actorului
     */
    public ArrayList<String> getFilmography() {
        return filmography;
    }

    /**
     * @param filmography filmografia actorului
     */
    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    /**
     * @return premiile actorului
     */
    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    /**
     * @param awards premiile actorului
     */
    public void setAwards(final Map<ActorsAwards, Integer> awards) {
        this.awards = awards;
    }

    /**
     * @return rating-ul actorului
     */
    public double getRating() {
        return rating;
    }

    /**
     * @param rating rating-ul actorului
     */
    public void setRating(final double rating) {
        this.rating = rating;
    }

    /**
     * @return numarul de premii
     */
    public int getNumberOfAwards() {
        int total = 0;

        for (Integer count : this.awards.values()) {
            total += count;
        }

        return total;
    }
}
