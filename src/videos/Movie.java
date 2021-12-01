package videos;

import fileio.MovieInputData;

import java.util.HashMap;
import java.util.Map;

public class Movie extends Video {
    private int duration;
    private Map<String, Double> ratings;

    /**
     * @param movieInputData datele de intrare despre film
     */
    public Movie(final MovieInputData movieInputData) {
        super(movieInputData.getTitle(), movieInputData.getYear(),
                movieInputData.getCast(), movieInputData.getGenres());

        this.duration = movieInputData.getDuration();
        this.ratings = new HashMap<>();
    }

    /**
     * @return rating-urile filmului
     */
    public Map<String, Double> getRatings() {
        return this.ratings;
    }

    /**
     * @param username nume utilizator
     * @param rating rating
     */
    public void addRating(final String username, final Double rating) {
        this.ratings.put(username, rating);
    }

    /**
     * @return rating-ul filmului
     */
    @Override
    public double getRating() {
        if (this.ratings.size() == 0) {
            return 0;
        }

        double sum = 0;

        for (Double x : this.ratings.values()) {
            sum += x;
        }

        return sum / this.ratings.size();
    }

    /**
     * @return durata filmului
     */
    @Override
    public int getDuration() {
        return this.duration;
    }
}
