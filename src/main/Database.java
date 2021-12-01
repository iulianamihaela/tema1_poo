package main;

import users.User;
import videos.Movie;
import videos.Show;
import actor.Actor;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private ArrayList<Actor> actors;
    private ArrayList<Movie> movies;
    private ArrayList<Show> shows;
    private ArrayList<User> users;

    public Database() {
        this.actors = new ArrayList<>();
        this.movies = new ArrayList<>();
        this.shows = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    /**
     * @return lista de actori
     */
    public List<Actor> getActors() {
        return actors;
    }

    /**
     * @param actors lista de actori
     */
    public void setActors(final ArrayList<Actor> actors) {
        this.actors = actors;
    }

    /**
     * @return lista de filme
     */
    public List<Movie> getMovies() {
        return movies;
    }

    /**
     * @param movies lista de filme
     */
    public void setMovies(final ArrayList<Movie> movies) {
        this.movies = movies;
    }

    /**
     * @return lista de seriale
     */
    public List<Show> getShows() {
        return shows;
    }

    /**
     * @param shows lista de seriale
     */
    public void setShows(final ArrayList<Show> shows) {
        this.shows = shows;
    }

    /**
     * @return lista de utilizatori
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * @param users lista de utilizatori
     */
    public void setUsers(final ArrayList<User> users) {
        this.users = users;
    }

    /**
     * @param actor actorul spre a fi adaugat
     */
    public void addActor(final Actor actor) {
        this.actors.add(actor);
    }

    /**
     * @param user utilizatorul spre a fi adaugat
     */
    public void addUser(final User user) {
        this.users.add(user);
    }

    /**
     * @param movie filmul spre a fi adaugat
     */
    public void addMovie(final Movie movie) {
        this.movies.add(movie);
    }

    /**
     * @param show serialul spre a fi adaugat
     */
    public void addSerial(final Show show) {
        this.shows.add(show);
    }

    /**
     * @param username nume de utilizator
     * @return utiliziator
     */
    public User getUserByUsername(final String username) {
        User user = null;

        for (User u : this.users) {
            if (u.getUsername().equals(username)) {
                user = u;
                break;
            }
        }

        return user;
    }

    /**
     * @param title numele filmului
     * @return filmul
     */
    public Movie getMovieByTitle(final String title) {
        Movie movie = null;

        for (Movie m : this.movies) {
            if (m.getTitle().equals(title)) {
                movie = m;
                break;
            }
        }

        return movie;
    }

    /**
     * @param title numele serialului
     * @return serialul
     */
    public Show getShowByTitle(final String title) {
        Show show = null;

        for (Show s : this.shows) {
            if (s.getTitle().equals(title)) {
                show = s;
                break;
            }
        }

        return show;
    }
}
