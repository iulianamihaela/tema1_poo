package videos;

import java.util.ArrayList;

public class Video {
    private String title;
    private Integer year;
    private ArrayList<String> cast;
    private ArrayList<String> genres;

    /**
     * @param title Titlul video-ului
     * @param year Anul de aparitie
     * @param cast Cast-ul video-ului
     * @param genres Genurile video-ului
     */
    public Video(final String title, final int year,
                 final ArrayList<String> cast, final ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
    }

    /**
     * @return titlul video-ului
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return genurile video-ului
     */
    public ArrayList<String> getGenres() {
        return genres;
    }

    /**
     * @param title titlul video-ului
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * @param genres genurile video-ului
     */
    public void setGenres(final ArrayList<String> genres) {
        this.genres = genres;
    }

    /**
     * @return anul de aparitie al video-ului
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year anul video-ului
     */
    public void setYear(final Integer year) {
        this.year = year;
    }

    /**
     * @return cast-ul video-ului
     */
    public ArrayList<String> getCast() {
        return cast;
    }

    /**
     * @param cast cast-ul video-ului
     */
    public void setCast(final ArrayList<String> cast) {
        this.cast = cast;
    }

    /**
     * @return rating-ul video-ului
     */
    public double getRating() {
        return 0;
    };

    /**
     * @return durata video-ului
     */
    public int getDuration() {
        return 0;
    }
}
