package main;

import Users.User;
import Videos.Movie;
import Videos.Show;
import Videos.Video;
import actor.Actor;
import common.Constants;
import fileio.Writer;
import org.json.simple.JSONArray;
import utils.Utils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class VideosDB {
	private Database database;
	private ArrayList<Action> actions;

	public VideosDB() {
		this.database = new Database();
		this.actions = new ArrayList<Action>();
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public JSONArray executeActions(Writer writer) {
		JSONArray results = new JSONArray();

		for (Action action : this.actions) {
			String message = executeAction(action);

			try {
				results.add(writer.writeFile(action.getActionId(), "", message));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return results;
	}

	public String executeAction(Action action) {
		if (action.getActionType().equals(Constants.COMMAND)) {
			if (action.getType().equals(Constants.FAVORITE)) {
				return addFavoriteTitleForUser(action.getUsername(), action.getTitle());
			}
			else if (action.getType().equals(Constants.VIEW)) {
				return markTitleAsViewedForUser(action.getUsername(), action.getTitle());
			}
			else if (action.getType().equals(Constants.RATING)) {
				if (action.getSeasonNumber() == 0) {
					return rateMovie(action.getUsername(), action.getTitle(), action.getGrade());
				} else {
					return rateShow(action.getUsername(), action.getTitle(), action.getSeasonNumber(), action.getGrade());
				}
			}
		} else if (action.getActionType().equals(Constants.QUERY)) {
			if (action.getObjectType().equals(Constants.ACTORS)) {
				if (action.getCriteria().equals(Constants.AVERAGE)) {
					return executeAverageQuery(action.getNumber(), action.getSortType());
				} else if (action.getCriteria().equals(Constants.AWARDS)) {
					return executeAwardsQuery(action.getFilters().get(3), action.getSortType());
				} else if (action.getCriteria().equals(Constants.FILTER_DESCRIPTIONS)) {
					return executeDescriptionQuery(action.getFilters().get(2), action.getSortType());
				}
			} else if (action.getObjectType().equals(Constants.MOVIES) || action.getObjectType().equals(Constants.SHOWS)) {
				boolean onMovies = false;
				boolean onShows = false;

				if (action.getObjectType().equals(Constants.MOVIES)) {
					onMovies = true;
				}
				if (action.getObjectType().equals(Constants.SHOWS)) {
					onShows = true;
				}

				if (action.getCriteria().equals(Constants.RATINGS)) {
					return executeRatingsVideoQuery(action.getFilters(), action.getNumber(), action.getSortType(), onMovies, onShows);
				} else if (action.getCriteria().equals(Constants.FAVORITE)) {
					return executeFavoriteVideoQuery(action.getFilters(), action.getNumber(), action.getSortType(), onMovies, onShows);
				} else if (action.getCriteria().equals(Constants.LONGEST)) {
					return executeLongestVideoQuery(action.getFilters(), action.getNumber(), action.getSortType(), onMovies, onShows);
				} else if (action.getCriteria().equals(Constants.MOST_VIEWED)) {
					return executeMostViewedVideoQuery(action.getFilters(), action.getNumber(), action.getSortType(), onMovies, onShows);
				}
			} else if (action.getObjectType().equals(Constants.USERS)) {
				if (action.getCriteria().equals(Constants.NUM_RATINGS)) {
					return executeNumRatingsQuery(action.getNumber(), action.getSortType());
				}
			}
		} else if (action.getActionType().equals(Constants.RECOMMENDATION)) {
			if (action.getType().equals(Constants.STANDARD)) {
				return executeStandardRecommendation(action.getUsername());
			} else if (action.getType().equals(Constants.BEST_UNSEEN)) {
				return executeBestUnseenRecommendation(action.getUsername());
			} else if (action.getType().equals(Constants.POPULAR)) {
				return executePopularRecommendation(action.getUsername());
			} else if (action.getType().equals(Constants.FAVORITE)) {
				return executeFavoriteRecommendation(action.getUsername());
			} else if (action.getType().equals(Constants.SEARCH)) {
				return executeSearchRecommendation(action.getUsername(), action.getGenre());
			}
		}

		return Utils.Error("Action not found");
	}

	private String executeStandardRecommendation(String username) {
		User user = this.database.getUserByUsername(username);

		Set<String> viewedVideos = user.getHistory().keySet();

		for (Movie movie : this.database.getMovies()) {
			if (!viewedVideos.contains(movie.getTitle())) {
				return "StandardRecommendation result: " + movie.getTitle();
			}
		}

		for (Show show : this.database.getShows()) {
			if (!viewedVideos.contains(show.getTitle())) {
				return "StandardRecommendation result: " + show.getTitle();
			}
		}

		return "StandardRecommendation cannot be applied!";
	}

	private String executeBestUnseenRecommendation(String username) {
		User user = this.database.getUserByUsername(username);

		Set<String> viewedVideos = user.getHistory().keySet();

		ArrayList<Video> videos = new ArrayList<>(this.database.getMovies());
		videos.addAll(this.database.getShows());

		Comparator<Video> comparator = Comparator
				.comparing(Video::getRating);

		videos.sort(comparator.reversed());

		for (Video video : videos) {
			if (!viewedVideos.contains(video.getTitle())) {
				return "BestRatedUnseenRecommendation result: " + video.getTitle();
			}
		}

		return "BestRatedUnseenRecommendation cannot be applied!";
	}

	private String executePopularRecommendation(String username) {
		User user = this.database.getUserByUsername(username);

		if (!user.getSubscriptionType().equals(Constants.PREMIUM)) {
			return "PopularRecommendation cannot be applied!";
		}

		Set<String> viewedVideos = user.getHistory().keySet();

		HashMap<String, Integer> genresPopularity = new HashMap<>();

		for (User u : this.database.getUsers()) {
			for (Map.Entry<String, Integer> viewsForVideo : u.getHistory().entrySet()) {
				Movie movie = this.database.getMovieByTitle(viewsForVideo.getKey());

				if (movie != null) {
					for (String genre : movie.getGenres()) {
						if (genresPopularity.containsKey(genre)) {
							genresPopularity.replace(genre, genresPopularity.get(genre) + viewsForVideo.getValue());
						} else {
							genresPopularity.put(genre, viewsForVideo.getValue());
						}
					}

					continue;
				}

				Show show = this.database.getShowByTitle(viewsForVideo.getKey());

				if (show != null) {
					for (String genre : show.getGenres()) {
						if (genresPopularity.containsKey(genre)) {
							genresPopularity.replace(genre, genresPopularity.get(genre) + viewsForVideo.getValue());
						} else {
							genresPopularity.put(genre, viewsForVideo.getValue());
						}
					}

					continue;
				}
			}
		}

		List<Map.Entry<String, Integer>> genres = genresPopularity.entrySet().stream().collect(Collectors.toList());

		Comparator<Map.Entry<String, Integer>> comparator = Comparator
				.comparing(Map.Entry<String, Integer>::getValue);

		genres.sort(comparator.reversed());

		for (Map.Entry<String, Integer> genrePop : genres) {
			for (Movie movie : this.database.getMovies()) {
				if (movie.getGenres().contains(genrePop.getKey()) && !viewedVideos.contains(movie.getTitle())) {
					return "PopularRecommendation result: " + movie.getTitle();
				}
			}
			for (Show show : this.database.getShows()) {
				if (show.getGenres().contains(genrePop.getKey()) && !viewedVideos.contains(show.getTitle())) {
					return "PopularRecommendation result: " + show.getTitle();
				}
			}
		}

		return "PopularRecommendation cannot be applied!";
	}

	private String executeFavoriteRecommendation(String username) {
		User user = this.database.getUserByUsername(username);

		if (!user.getSubscriptionType().equals(Constants.PREMIUM)) {
			return "FavoriteRecommendation cannot be applied!";
		}

		Set<String> viewedVideos = user.getHistory().keySet();

		HashMap<String, Integer> mostFavorite = new HashMap<>();

		for (User u : this.database.getUsers()) {
			for (String fav : u.getFavoriteMovies()) {
				if (mostFavorite.containsKey(fav)) {
					mostFavorite.replace(fav, mostFavorite.get(fav) + 1);
				} else {
					mostFavorite.put(fav, 1);
				}
			}
		}

		String title = null;
		int favCount = -1;

		ArrayList<Video> videos = new ArrayList<>(this.database.getMovies());
		videos.addAll(this.database.getShows());

		for (Video video : videos) {
			if (!viewedVideos.contains(video.getTitle())
					&& mostFavorite.containsKey(video.getTitle())
					&& mostFavorite.get(video.getTitle()) > favCount) {
				favCount = mostFavorite.get(video.getTitle());
				title = video.getTitle();
			}
		}

		if (title != null) {
			return "FavoriteRecommendation result: " + title;
		}

		return "FavoriteRecommendation cannot be applied!";
	}

	private String executeSearchRecommendation(String username, String genre) {
		User user = this.database.getUserByUsername(username);

		if (!user.getSubscriptionType().equals(Constants.PREMIUM)) {
			return "SearchRecommendation cannot be applied!";
		}

		Set<String> viewedVideos = user.getHistory().keySet();

		ArrayList<Video> videos = new ArrayList<>(this.database.getMovies());
		videos.addAll(this.database.getShows());

		videos = (ArrayList<Video>)videos.stream().filter(v -> v.getGenres().contains(genre) && !viewedVideos.contains(v.getTitle())).collect(Collectors.toList());

		Comparator<Video> comparator = Comparator
				.comparing(Video::getRating)
				.thenComparing(Video::getTitle);

		videos.sort(comparator);

		if (videos.size() > 0) {
			return "SearchRecommendation result: " + videos.stream().map(v -> v.getTitle()).collect(Collectors.toList()).toString();
		}

		return "SearchRecommendation cannot be applied!";
	}

	private String executeAverageQuery(int maxNoActors, String sortType) {
		ArrayList<Actor> actorsByRating;

		for (Actor actor : this.database.getActors()) {
			double ratingSum = 0;
			int ratingCount = 0;

			for (Movie movie : this.database.getMovies()) {
				double movieRating = movie.getRating();

				if (movieRating != 0 && movie.getCast().contains(actor.getName())) {
					ratingSum += movieRating;
					ratingCount++;
				}
			}

			for (Show show : this.database.getShows()) {
				double showRating = show.getRating();

				if (showRating != 0 && show.getCast().contains(actor.getName())) {
					ratingSum += showRating;
					ratingCount++;
				}
			}

			if (ratingCount == 0) {
				actor.setRating(0);
			} else {
				actor.setRating(ratingSum / ratingCount);
			}
		}

		actorsByRating = new ArrayList<>(this.database.getActors());

		actorsByRating = (ArrayList<Actor>)actorsByRating.stream().filter(x -> x.getRating() != 0).collect(Collectors.toList());

		Comparator<Actor> compareByRating = Comparator
				.comparing(Actor::getRating)
				.thenComparing(Actor::getName);

		if (sortType.equals(Constants.ASC)) {
			actorsByRating.sort(compareByRating);
		} else if (sortType.equals(Constants.DESC)) {
			actorsByRating.sort(compareByRating.reversed());
		}

		return "Query result: " + actorsByRating.stream().limit(maxNoActors).map(a -> a.getName()).collect(Collectors.toList()).toString();
	}

	private String executeAwardsQuery(List<String> filters, String sortType)
	{
		ArrayList<Actor> actorsByAwards = new ArrayList<>();

		for (Actor actor : this.database.getActors()) {
			boolean ok = true;

			for (String award : filters) {
				if (!actor.getAwards().containsKey(Utils.stringToAwards(award))) {
					ok = false;
					break;
				}
			}

			if (ok) {
				actorsByAwards.add(actor);
			}
		}

		Comparator<Actor> compareByAwards = Comparator
				.comparing(Actor::getNumberOfAwards)
				.thenComparing(Actor::getName);

		if (sortType.equals(Constants.ASC)) {
			actorsByAwards.sort(compareByAwards);
		} else if (sortType.equals(Constants.DESC)) {
			actorsByAwards.sort(compareByAwards.reversed());
		}

		return "Query result: " + actorsByAwards.stream().map(a -> a.getName()).collect(Collectors.toList()).toString();
	}

	private String executeDescriptionQuery(List<String> filters, String sortType)
	{
		ArrayList<Actor> actorsByDescription = new ArrayList<>();

		for (Actor actor : this.database.getActors()) {
			boolean ok = true;

			for (String keyword : filters) {
				if (!actor.getCareerDescription().toLowerCase().contains(keyword.toLowerCase())) {
					ok = false;
					break;
				}
			}

			if (ok) {
				actorsByDescription.add(actor);
			}
		}

		Comparator<Actor> compareByName = Comparator
				.comparing(Actor::getName);

		if (sortType.equals(Constants.ASC)) {
			actorsByDescription.sort(compareByName);
		} else if (sortType.equals(Constants.DESC)) {
			actorsByDescription.sort(compareByName.reversed());
		}

		return "Query result: " + actorsByDescription.stream().map(a -> a.getName()).collect(Collectors.toList()).toString();
	}

	private ArrayList<Video> getFilteredVideos(List<List<String>> filters, boolean onMovies, boolean onShows) {
		ArrayList<Video> videos = new ArrayList<Video>();

		if (onMovies) {
			videos.addAll(this.database.getMovies());
		}
		if (onShows) {
			videos.addAll(this.database.getShows());
		}

		if (filters.get(0).get(0) != null && filters.get(0).size() > 0) {
			Integer year = Integer.parseInt(filters.get(0).get(0));

			videos = (ArrayList<Video>) videos.stream().filter(v -> Objects.equals(v.getYear(), year)).collect(Collectors.toList());
		}
		if (filters.get(1) != null) {
			String genre = filters.get(1).get(0);

			videos = (ArrayList<Video>)videos.stream().filter(v -> v.getGenres().contains(genre)).collect(Collectors.toList());
		}

		return videos;
	}

	private String executeRatingsVideoQuery(List<List<String>> filters, int maxNoVideos, String sortType,
	                                        boolean onMovies, boolean onShows)
	{
		ArrayList<Video> videos = getFilteredVideos(filters, onMovies, onShows);

		videos = (ArrayList<Video>)videos.stream().filter(v -> v.getRating() > 0).collect(Collectors.toList());

		Comparator<Video> comparator = Comparator
				.comparing(Video::getRating)
				.thenComparing(Video::getTitle);

		if (sortType.equals(Constants.ASC)) {
			videos.sort(comparator);
		} else if (sortType.equals(Constants.DESC)) {
			videos.sort(comparator.reversed());
		}

		return "Query result: " + videos.stream().limit(maxNoVideos).map(a -> a.getTitle()).collect(Collectors.toList()).toString();
	}

	private String executeFavoriteVideoQuery(List<List<String>> filters, int maxNoVideos, String sortType,
	                                         boolean onMovies, boolean onShows)
	{
		ArrayList<Video> videos = getFilteredVideos(filters, onMovies, onShows);
		ArrayList<String> filteredVideos = (ArrayList<String>)videos.stream().map(v -> v.getTitle()).collect(Collectors.toList());

		HashMap<String, Integer> favoriteCount = new HashMap<>();

		for (User user : this.database.getUsers()) {
			for (String fav : user.getFavoriteMovies()) {
				if (filteredVideos.contains(fav)) {
					if (favoriteCount.containsKey(fav)) {
						favoriteCount.replace(fav, favoriteCount.get(fav) + 1);
					} else {
						favoriteCount.put(fav, 1);
					}
				}
			}
		}

		List<Map.Entry<String, Integer>> favorites = favoriteCount.entrySet().stream().collect(Collectors.toList());

		Comparator<Map.Entry<String, Integer>> comparator = Comparator
				.comparing(Map.Entry<String, Integer>::getValue)
				.thenComparing(Map.Entry<String, Integer>::getKey);

		if (sortType.equals(Constants.ASC)) {
			favorites.sort(comparator);
		} else if (sortType.equals(Constants.DESC)) {
			favorites.sort(comparator.reversed());
		}

		return "Query result: " + favorites.stream().limit(maxNoVideos).map(a -> a.getKey()).collect(Collectors.toList()).toString();
	}

	private String executeLongestVideoQuery(List<List<String>> filters, int maxNoVideos, String sortType,
	                                        boolean onMovies, boolean onShows)
	{
		ArrayList<Video> videos = getFilteredVideos(filters, onMovies, onShows);

		Comparator<Video> comparator = Comparator
				.comparing(Video::getDuration)
				.thenComparing(Video::getTitle);

		if (sortType.equals(Constants.ASC)) {
			videos.sort(comparator);
		} else if (sortType.equals(Constants.DESC)) {
			videos.sort(comparator.reversed());
		}

		return "Query result: " + videos.stream().limit(maxNoVideos).map(a -> a.getTitle()).collect(Collectors.toList()).toString();
	}

	private String executeMostViewedVideoQuery(List<List<String>> filters, int maxNoVideos, String sortType,
	                                           boolean onMovies, boolean onShows)
	{
		ArrayList<Video> videos = getFilteredVideos(filters, onMovies, onShows);
		ArrayList<String> filteredVideos = (ArrayList<String>)videos.stream().map(v -> v.getTitle()).collect(Collectors.toList());

		HashMap<String, Integer> viewsCount = new HashMap<>();

		for (User user : this.database.getUsers()) {
			for (String title : user.getHistory().keySet()) {
				if (filteredVideos.contains(title)) {
					if (viewsCount.containsKey(title)) {
						viewsCount.replace(title, viewsCount.get(title) + user.getHistory().get(title));
					} else {
						viewsCount.put(title, user.getHistory().get(title));
					}
				}
			}
		}

		List<Map.Entry<String, Integer>> viewed = viewsCount.entrySet().stream().collect(Collectors.toList());

		Comparator<Map.Entry<String, Integer>> comparator = Comparator
				.comparing(Map.Entry<String, Integer>::getValue)
				.thenComparing(Map.Entry<String, Integer>::getKey);

		if (sortType.equals(Constants.ASC)) {
			viewed.sort(comparator);
		} else if (sortType.equals(Constants.DESC)) {
			viewed.sort(comparator.reversed());
		}

		return "Query result: " + viewed.stream().limit(maxNoVideos).map(a -> a.getKey()).collect(Collectors.toList()).toString();
	}

	private String executeNumRatingsQuery(int maxNoUsers, String sortType) {
		HashMap<String, Integer> usersRatings = new HashMap<>();

		for (User user : this.database.getUsers()) {
			String username = user.getUsername();

			for (Movie movie : this.database.getMovies()) {
				if (movie.getRatings().containsKey(username)) {
					if (usersRatings.containsKey(username)) {
						usersRatings.replace(username, usersRatings.get(username) + 1);
					} else {
						usersRatings.put(username, 1);
					}
				}
			}

			for (Show show : this.database.getShows()) {
				for (HashMap<String, Double> seasonRatings : show.getSeasonsRatings()) {
					if (seasonRatings.containsKey(username)) {
						if (usersRatings.containsKey(username)) {
							usersRatings.replace(username, usersRatings.get(username) + 1);
						} else {
							usersRatings.put(username, 1);
						}
					}
				}
			}
		}

		List<Map.Entry<String, Integer>> users = usersRatings.entrySet().stream().collect(Collectors.toList());

		Comparator<Map.Entry<String, Integer>> comparator = Comparator
				.comparing(Map.Entry<String, Integer>::getValue)
				.thenComparing(Map.Entry<String, Integer>::getKey);

		if (sortType.equals(Constants.ASC)) {
			users.sort(comparator);
		} else if (sortType.equals(Constants.DESC)) {
			users.sort(comparator.reversed());
		}

		return "Query result: " + users.stream().limit(maxNoUsers).map(a -> a.getKey()).collect(Collectors.toList()).toString();
	}

	public String addFavoriteTitleForUser(String username, String title) {
		User user = database.getUserByUsername(username);

		if (user == null) {
			return Utils.Error("User doesn't exist.");
		}

		if (user.getFavoriteMovies().contains(title)) {
			return Utils.Error(title + " is already in favourite list");
		}

		if (user.getHistory().containsKey(title)) {
			user.getFavoriteMovies().add(title);
			return Utils.Success(title + " was added as favourite");
		} else {
			return Utils.Error(title + " is not seen");
		}
	}

	public String markTitleAsViewedForUser(String username, String title) {
		User user = database.getUserByUsername(username);

		if (user == null) {
			return Utils.Error("User doesn't exist.");
		}

		if (user.getHistory().containsKey(title)) {
			int noViews = user.getHistory().get(title);

			noViews++;

			user.getHistory().replace(title, noViews);

			return Utils.Success(title + " was viewed with total views of " + noViews);
		} else {
			user.getHistory().put(title, 1);

			return Utils.Success(title + " was viewed with total views of 1");
		}
	}

	public String rateMovie(String username, String title, Double rating) {
		User user = database.getUserByUsername(username);
		Movie movie = database.getMovieByTitle(title);

		if (user == null) {
			return Utils.Error("User doesn't exist.");
		}

		if (movie == null) {
			return Utils.Error("Movie doesn't exist.");
		}

		if (!user.getHistory().containsKey(title)) {
			return Utils.Error(title + " is not seen");
		}

		if (movie.getRatings().containsKey(username)) {
			return Utils.Error(title + " has been already rated");
		}

		movie.addRating(username, rating);

		return Utils.Success(title + " was rated with " + rating + " by " + username);
	}

	public String rateShow(String username, String title, Integer season, Double rating) {
		User user = database.getUserByUsername(username);
		Show show = database.getShowByTitle(title);

		if (user == null) {
			return Utils.Error("User doesn't exist.");
		}

		if (show == null) {
			return Utils.Error("Show doesn't exist.");
		}

		if (!user.getHistory().containsKey(title)) {
			return Utils.Error(title + " is not seen");
		}

		if (show.getSeasonsRatings().get(season - 1).containsKey(username)) {
			return Utils.Error(title + " has been already rated");
		}

		show.addRating(username, season, rating);
		return Utils.Success(title + " was rated with " + rating + " by " + username);
	}
}
