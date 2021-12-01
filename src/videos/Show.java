package videos;

import entertainment.Season;
import fileio.SerialInputData;

import java.util.ArrayList;
import java.util.HashMap;

public class Show extends Video {
    private int numberOfSeasons;
    private ArrayList<Season> seasons;

    private ArrayList<HashMap<String, Double>> seasonsRatings;

    public Show(final SerialInputData serialInputData) {
        super(serialInputData.getTitle(), serialInputData.getYear(),
                serialInputData.getCast(), serialInputData.getGenres());

        this.numberOfSeasons = serialInputData.getNumberSeason();
        this.seasons = serialInputData.getSeasons();
        this.seasonsRatings = new ArrayList<>();

        for (int i = 0; i < this.numberOfSeasons; i++) {
            this.seasonsRatings.add(new HashMap<>());
        }
    }

    /**
     * @return numarul de sezoane
     */
    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    /**
     * @param numberOfSeasons numarul de sezoane
     */
    public void setNumberOfSeasons(final int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    /**
     * @return lista de sezoane
     */
    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    /**
     * @param seasons lista de sezoane
     */
    public void setSeasons(final ArrayList<Season> seasons) {
        this.seasons = seasons;
    }

    /**
     * @return rating-urile sezoanelor
     */
    public ArrayList<HashMap<String, Double>> getSeasonsRatings() {
        return seasonsRatings;
    }

    /**
     * @param username nume utilizator
     * @param season numar sezon
     * @param rating rating
     */
    public void addRating(final String username, final Integer season, final Double rating) {
        this.seasons.get(season - 1).getRatings().add(rating);
        this.seasonsRatings.get(season - 1).put(username, rating);
    }

    /**
     * @return rating-ul serialului
     */
    @Override
    public double getRating() {
        double sumRating = 0;

        for (int i = 0; i < this.numberOfSeasons; i++) {
            HashMap<String, Double> seasonI = this.seasonsRatings.get(i);

            if (seasonI.size() == 0) {
                continue;
            }

            double ratingSeasonI = 0;

            for (Double x : seasonI.values()) {
                ratingSeasonI += x;
            }

            ratingSeasonI /= seasonI.size();

            sumRating += ratingSeasonI;
        }

        return sumRating / (double) this.numberOfSeasons;
    }

    /**
     * @return durata serialului
     */
    @Override
    public int getDuration() {
        int duration = 0;

        for (Season season : this.seasons) {
            duration += season.getDuration();
        }

        return duration;
    }
}
