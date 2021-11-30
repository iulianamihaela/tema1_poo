package main;

import Users.User;
import Videos.Movie;
import Videos.Show;
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

	public List<Actor> getActors() {
		return actors;
	}

	public void setActors(ArrayList<Actor> actors) {
		this.actors = actors;
	}

	public List<Movie> getMovies() {
		return movies;
	}

	public void setMovies(ArrayList<Movie> movies) {
		this.movies = movies;
	}

	public List<Show> getShows() {
		return shows;
	}

	public void setShows(ArrayList<Show> shows) {
		this.shows = shows;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public void addActor(Actor actor) {
		this.actors.add(actor);
	}

	public void addUsers(User user) {
		this.users.add(user);
	}

	public void addMovies(Movie movie) {
		this.movies.add(movie);
	}

	public void addSerials(Show show) {
		this.shows.add(show);
	}

	public User getUserByUsername(String username) {
		User user = null;

		for (User u : this.users) {
			if (u.getUsername().equals(username)) {
				user = u;
				break;
			}
		}

		return user;
	}

	public Movie getMovieByTitle(String title) {
		Movie movie = null;

		for (Movie m : this.movies) {
			if (m.getTitle().equals(title)) {
				movie = m;
				break;
			}
		}

		return movie;
	}

	public Show getShowByTitle(String title) {
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
