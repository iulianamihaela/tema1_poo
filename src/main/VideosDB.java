package main;

import org.json.simple.JSONObject;
import users.User;
import videos.Movie;
import videos.Show;
import videos.Video;
import actor.Actor;
import common.Constants;
import fileio.Writer;
import org.json.simple.JSONArray;
import utils.Utils;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Arrays;

public class VideosDB {
    private Database database;
    private ArrayList<Action> actions;

    public VideosDB() {
        this.database = new Database();
        this.actions = new ArrayList<>();
    }

    /**
     * @return lista de actiuni
     */
    public ArrayList<Action> getActions() {
        return actions;
    }

    /**
     * @param actions lista de actiuni
     */
    public void setActions(final ArrayList<Action> actions) {
        this.actions = actions;
    }

    /**
     * @return baza de date
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * @param database baza de date
     */
    public void setDatabase(final Database database) {
        this.database = database;
    }

    /**
     * @param writer writer pentru output
     * @return array de json-uri obtinute in urma actiunilor
     */
    public JSONArray executeActions(final Writer writer) {
        JSONArray results = new JSONArray();

        for (Action action : this.actions) {
            String message = executeAction(action);

            try {
                JSONObject result = writer.writeFile(action.getActionId(), "", message);

                results.add(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    /**
     * @param action actiune spre a fi executata
     * @return rezultatul actiunii
     */
    public String executeAction(final Action action) {
        switch (action.getActionType()) {
            case Constants.COMMAND:
                switch (action.getType()) {
                    case Constants.FAVORITE:
                        return addFavoriteTitleForUser(action.getUsername(),
                                action.getTitle());
                    case Constants.VIEW:
                        return markTitleAsViewedForUser(action.getUsername(),
                                action.getTitle());
                    case Constants.RATING:
                        if (action.getSeasonNumber() == 0) {
                            return rateMovie(action.getUsername(),
                                    action.getTitle(),
                                    action.getGrade());
                        } else {
                            return rateShow(action.getUsername(),
                                    action.getTitle(),
                                    action.getSeasonNumber(),
                                    action.getGrade());
                        }
                    default:
                        break;
                }
                break;
            case Constants.QUERY:
                switch (action.getObjectType()) {
                    case Constants.ACTORS:
                        switch (action.getCriteria()) {
                            case Constants.AVERAGE:
                                return executeAverageQuery(action.getNumber(),
                                        action.getSortType());
                            case Constants.AWARDS:
                                return executeAwardsQuery(
                                        action.getFilters().get(Constants.AWARDS_FILTER_INDEX),
                                        action.getSortType());
                            case Constants.FILTER_DESCRIPTIONS:
                                return executeDescriptionQuery(
                                        action.getFilters().get(Constants.WORDS_FILTER_INDEX),
                                        action.getSortType());
                            default:
                                break;
                        }
                        break;
                    case Constants.MOVIES:
                    case Constants.SHOWS:
                        boolean onMovies = false;
                        boolean onShows = false;

                        if (action.getObjectType().equals(Constants.MOVIES)) {
                            onMovies = true;
                        }
                        if (action.getObjectType().equals(Constants.SHOWS)) {
                            onShows = true;
                        }

                        switch (action.getCriteria()) {
                            case Constants.RATINGS:
                                return executeRatingsVideoQuery(action.getFilters(),
                                        action.getNumber(),
                                        action.getSortType(),
                                        onMovies,
                                        onShows);
                            case Constants.FAVORITE:
                                return executeFavoriteVideoQuery(action.getFilters(),
                                        action.getNumber(),
                                        action.getSortType(),
                                        onMovies,
                                        onShows);
                            case Constants.LONGEST:
                                return executeLongestVideoQuery(action.getFilters(),
                                        action.getNumber(),
                                        action.getSortType(),
                                        onMovies,
                                        onShows);
                            case Constants.MOST_VIEWED:
                                return executeMostViewedVideoQuery(action.getFilters(),
                                        action.getNumber(),
                                        action.getSortType(),
                                        onMovies,
                                        onShows);
                            default:
                                break;
                        }
                        break;
                    case Constants.USERS:
                        if (action.getCriteria().equals(Constants.NUM_RATINGS)) {
                            return executeNumRatingsQuery(action.getNumber(),
                                    action.getSortType());
                        }
                        break;
                    default:
                        break;
                }
                break;
            case Constants.RECOMMENDATION:
                switch (action.getType()) {
                    case Constants.STANDARD:
                        return executeStandardRecommendation(action.getUsername());
                    case Constants.BEST_UNSEEN:
                        return executeBestUnseenRecommendation(action.getUsername());
                    case Constants.POPULAR:
                        return executePopularRecommendation(action.getUsername());
                    case Constants.FAVORITE:
                        return executeFavoriteRecommendation(action.getUsername());
                    case Constants.SEARCH:
                        return executeSearchRecommendation(action.getUsername(),
                                action.getGenre());
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        return Utils.error("Action not found");
    }

    /**
     * @param username nume utilizator
     * @return rezultatul recomandarii standard
     */
    private String executeStandardRecommendation(final String username) {
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

    /**
     * @param username nume de utilizator
     * @return rezultatul recomandarii best_unseen
     */
    private String executeBestUnseenRecommendation(final String username) {
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

    /**
     * @param username nume de utilizator
     * @return rezultatul recomandarii popular
     */
    private String executePopularRecommendation(final String username) {
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
                            genresPopularity.replace(genre,
                                    genresPopularity.get(genre) + viewsForVideo.getValue());
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
                            genresPopularity.replace(genre,
                                    genresPopularity.get(genre) + viewsForVideo.getValue());
                        } else {
                            genresPopularity.put(genre, viewsForVideo.getValue());
                        }
                    }
                }
            }
        }

        List<Map.Entry<String, Integer>> genres = new ArrayList<>(genresPopularity.entrySet());

        Comparator<Map.Entry<String, Integer>> comparator = Comparator
                .comparing(Map.Entry<String, Integer>::getValue);

        genres.sort(comparator.reversed());

        for (Map.Entry<String, Integer> genrePop : genres) {
            for (Movie movie : this.database.getMovies()) {
                if (movie.getGenres().contains(genrePop.getKey())
                        && !viewedVideos.contains(movie.getTitle())) {
                    return "PopularRecommendation result: " + movie.getTitle();
                }
            }
            for (Show show : this.database.getShows()) {
                if (show.getGenres().contains(genrePop.getKey())
                        && !viewedVideos.contains(show.getTitle())) {
                    return "PopularRecommendation result: " + show.getTitle();
                }
            }
        }

        return "PopularRecommendation cannot be applied!";
    }

    /**
     * @param username nume de utilizator
     * @return rezultatul recomandarii favorite
     */
    private String executeFavoriteRecommendation(final String username) {
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

    /**
     * @param username nume de utilizator
     * @param genre genul video-ului
     * @return rezultatul recomandarii search
     */
    private String executeSearchRecommendation(final String username, final String genre) {
        User user = this.database.getUserByUsername(username);

        if (!user.getSubscriptionType().equals(Constants.PREMIUM)) {
            return "SearchRecommendation cannot be applied!";
        }

        Set<String> viewedVideos = user.getHistory().keySet();

        ArrayList<Video> videos = new ArrayList<>(this.database.getMovies());
        videos.addAll(this.database.getShows());

        videos = (ArrayList<Video>) videos.stream().filter(v -> v.getGenres().contains(genre)
                && !viewedVideos.contains(v.getTitle())).collect(Collectors.toList());

        Comparator<Video> comparator = Comparator
                .comparing(Video::getRating)
                .thenComparing(Video::getTitle);

        videos.sort(comparator);

        if (videos.size() > 0) {
            return "SearchRecommendation result: " + videos.stream()
                    .map(Video::getTitle).collect(Collectors.toList());
        }

        return "SearchRecommendation cannot be applied!";
    }

    /**
     * @param maxNoActors numarul maxim de actori
     * @param sortType tipul sortarii
     * @return rezultatul query-ului average
     */
    private String executeAverageQuery(final int maxNoActors, final String sortType) {
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

        actorsByRating = (ArrayList<Actor>) actorsByRating.stream()
                .filter(x -> x.getRating() != 0).collect(Collectors.toList());

        Comparator<Actor> compareByRating = Comparator
                .comparing(Actor::getRating)
                .thenComparing(Actor::getName);

        if (sortType.equals(Constants.ASC)) {
            actorsByRating.sort(compareByRating);
        } else if (sortType.equals(Constants.DESC)) {
            actorsByRating.sort(compareByRating.reversed());
        }

        return "Query result: " + actorsByRating.stream().limit(maxNoActors)
                .map(Actor::getName).collect(Collectors.toList());
    }

    /**
     * @param filters filtrele ce se doresc a fi aplicate
     * @param sortType tipul sortarii
     * @return rezultatul query-ului awards
     */
    private String executeAwardsQuery(final List<String> filters, final String sortType) {
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

        return "Query result: " + actorsByAwards.stream().map(Actor::getName)
                .collect(Collectors.toList());
    }

    /**
     * @param filters filtrele ce se doresc a fi aplicate
     * @param sortType tipul sortarii
     * @return rezultatul query-ului description
     */
    private String executeDescriptionQuery(final List<String> filters, final String sortType) {
        ArrayList<Actor> actorsByDescription = new ArrayList<>();

        for (Actor actor : this.database.getActors()) {
            boolean ok = true;

            List<String> description = Arrays.asList(actor.getCareerDescription()
                    .toLowerCase().split(" |[.]|[?]|[!]|-|,|;|:"));

            for (String keyword : filters) {
                if (!description.contains(keyword.toLowerCase())) {
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

        return "Query result: " + actorsByDescription.stream()
                .map(Actor::getName).collect(Collectors.toList());
    }

    /**
     * @param filters filtrele ce se doresc a fi aplicate
     * @param onMovies trebuie executat asupra filmelor
     * @param onShows trebuie executat asupra serialelor
     * @return lista videoclipurilor ce respecta conditiile primite ca parametri
     */
    private ArrayList<Video> getFilteredVideos(final List<List<String>> filters,
                                               final boolean onMovies,
                                               final boolean onShows) {
        ArrayList<Video> videos = new ArrayList<>();

        if (onMovies) {
            videos.addAll(this.database.getMovies());
        }
        if (onShows) {
            videos.addAll(this.database.getShows());
        }

        if (filters.get(0).get(0) != null && filters.get(0).size() > 0) {
            Integer year = Integer.parseInt(filters.get(0).get(0));

            videos = (ArrayList<Video>) videos.stream()
                    .filter(v -> Objects.equals(v.getYear(), year))
                    .collect(Collectors.toList());
        }
        if (filters.get(Constants.GENRE_FILTER_INDEX) != null
                && filters.get(Constants.GENRE_FILTER_INDEX).get(0) != null) {
            String genre = filters.get(Constants.GENRE_FILTER_INDEX).get(0);

            videos = (ArrayList<Video>) videos.stream()
                    .filter(v -> v.getGenres().contains(genre))
                    .collect(Collectors.toList());
        }

        return videos;
    }

    /**
     * @param filters filtrele ce se doresc a fi aplicate
     * @param maxNoVideos numarul maxim de video-uri
     * @param sortType tipul sortarii
     * @param onMovies se aplica pe filme
     * @param onShows se aplica pe seriale
     * @return rezultatul query-ului ratings
     */
    private String executeRatingsVideoQuery(final List<List<String>> filters,
                                            final int maxNoVideos,
                                            final String sortType,
                                            final boolean onMovies,
                                            final boolean onShows) {
        ArrayList<Video> videos = getFilteredVideos(filters, onMovies, onShows);

        videos = (ArrayList<Video>) videos.stream()
                .filter(v -> v.getRating() > 0)
                .collect(Collectors.toList());

        Comparator<Video> comparator = Comparator
                .comparing(Video::getRating)
                .thenComparing(Video::getTitle);

        if (sortType.equals(Constants.ASC)) {
            videos.sort(comparator);
        } else if (sortType.equals(Constants.DESC)) {
            videos.sort(comparator.reversed());
        }

        return "Query result: " + videos.stream().limit(maxNoVideos).map(Video::getTitle)
                .collect(Collectors.toList());
    }

    /**
     * @param filters filtrele ce se doresc a fi aplicate
     * @param maxNoVideos numarul maxim de video-uri
     * @param sortType tipul sortarii
     * @param onMovies se aplica pe filme
     * @param onShows se aplica pe seriale
     * @return rezultatul query-ului favorite
     */
    private String executeFavoriteVideoQuery(final List<List<String>> filters,
                                             final int maxNoVideos,
                                             final String sortType,
                                             final boolean onMovies,
                                             final boolean onShows) {
        ArrayList<Video> videos = getFilteredVideos(filters, onMovies, onShows);
        ArrayList<String> filteredVideos = (ArrayList<String>) videos.stream()
                .map(Video::getTitle)
                .collect(Collectors.toList());

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

        List<Map.Entry<String, Integer>> favorites = new ArrayList<>(favoriteCount.entrySet());

        Comparator<Map.Entry<String, Integer>> comparator = Comparator
                .comparing(Map.Entry<String, Integer>::getValue)
                .thenComparing(Map.Entry::getKey);

        if (sortType.equals(Constants.ASC)) {
            favorites.sort(comparator);
        } else if (sortType.equals(Constants.DESC)) {
            favorites.sort(comparator.reversed());
        }

        return "Query result: " + favorites.stream().limit(maxNoVideos).map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * @param filters filtrele ce se doresc a fi aplicate
     * @param maxNoVideos numarul maxim de video-uri
     * @param sortType tipul sortarii
     * @param onMovies se aplica pe filme
     * @param onShows se aplica pe seriale
     * @return rezultatul query-ului longest
     */
    private String executeLongestVideoQuery(final List<List<String>> filters,
                                            final int maxNoVideos,
                                            final String sortType,
                                            final boolean onMovies,
                                            final boolean onShows) {
        ArrayList<Video> videos = getFilteredVideos(filters, onMovies, onShows);

        Comparator<Video> comparator = Comparator
                .comparing(Video::getDuration)
                .thenComparing(Video::getTitle);

        if (sortType.equals(Constants.ASC)) {
            videos.sort(comparator);
        } else if (sortType.equals(Constants.DESC)) {
            videos.sort(comparator.reversed());
        }

        return "Query result: " + videos.stream().limit(maxNoVideos).map(Video::getTitle)
                .collect(Collectors.toList());
    }

    /**
     * @param filters filtrele ce se doresc a fi aplicate
     * @param maxNoVideos numarul maxim de video-uri
     * @param sortType tipul sortarii
     * @param onMovies se aplica pe filme
     * @param onShows se aplica pe seriale
     * @return rezultatul query-ului most_viewed
     */
    private String executeMostViewedVideoQuery(final List<List<String>> filters,
                                               final int maxNoVideos,
                                               final String sortType,
                                               final boolean onMovies,
                                               final boolean onShows) {
        ArrayList<Video> videos = getFilteredVideos(filters, onMovies, onShows);
        ArrayList<String> filteredVideos = (ArrayList<String>) videos.stream()
                .map(Video::getTitle)
                .collect(Collectors.toList());

        HashMap<String, Integer> viewsCount = new HashMap<>();

        for (User user : this.database.getUsers()) {
            for (String title : user.getHistory().keySet()) {
                if (filteredVideos.contains(title)) {
                    if (viewsCount.containsKey(title)) {
                        viewsCount.replace(title,
                                viewsCount.get(title) + user.getHistory().get(title));
                    } else {
                        viewsCount.put(title, user.getHistory().get(title));
                    }
                }
            }
        }

        List<Map.Entry<String, Integer>> viewed = new ArrayList<>(viewsCount.entrySet());

        Comparator<Map.Entry<String, Integer>> comparator = Comparator
                .comparing(Map.Entry<String, Integer>::getValue)
                .thenComparing(Map.Entry::getKey);

        if (sortType.equals(Constants.ASC)) {
            viewed.sort(comparator);
        } else if (sortType.equals(Constants.DESC)) {
            viewed.sort(comparator.reversed());
        }

        return "Query result: " + viewed.stream()
                .limit(maxNoVideos)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * @param maxNoUsers numarul maxim de utilizatori
     * @param sortType tipul sortarii
     * @return rezultatul query-ului num_ratings
     */
    private String executeNumRatingsQuery(final int maxNoUsers, final String sortType) {
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

        List<Map.Entry<String, Integer>> users = new ArrayList<>(usersRatings.entrySet());

        Comparator<Map.Entry<String, Integer>> comparator = Comparator
                .comparing(Map.Entry<String, Integer>::getValue)
                .thenComparing(Map.Entry::getKey);

        if (sortType.equals(Constants.ASC)) {
            users.sort(comparator);
        } else if (sortType.equals(Constants.DESC)) {
            users.sort(comparator.reversed());
        }

        return "Query result: " + users.stream().limit(maxNoUsers).map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * @param username numele utilizatorului
     * @param title titlul video-ului
     * @return rezultatul adaugarii video-ului ca favorit
     */
    public String addFavoriteTitleForUser(final String username, final String title) {
        User user = database.getUserByUsername(username);

        if (user == null) {
            return Utils.error("User doesn't exist.");
        }

        if (user.getFavoriteMovies().contains(title)) {
            return Utils.error(title + " is already in favourite list");
        }

        if (user.getHistory().containsKey(title)) {
            user.getFavoriteMovies().add(title);
            return Utils.success(title + " was added as favourite");
        } else {
            return Utils.error(title + " is not seen");
        }
    }

    /**
     * @param username numele utilizatorului
     * @param title titlul video-ului
     * @return rezultatul marcarii video-ului ca vizionat
     */
    public String markTitleAsViewedForUser(final String username, final String title) {
        User user = database.getUserByUsername(username);

        if (user == null) {
            return Utils.error("User doesn't exist.");
        }

        if (user.getHistory().containsKey(title)) {
            int noViews = user.getHistory().get(title);

            noViews++;

            user.getHistory().replace(title, noViews);

            return Utils.success(title + " was viewed with total views of " + noViews);
        } else {
            user.getHistory().put(title, 1);

            return Utils.success(title + " was viewed with total views of 1");
        }
    }

    /**
     * @param username nume utilizator
     * @param title titlul serilaului
     * @param rating rating
     * @return rezultatul oferirii rating-ului unui film
     */
    public String rateMovie(final String username, final String title, final Double rating) {
        User user = database.getUserByUsername(username);
        Movie movie = database.getMovieByTitle(title);

        if (user == null) {
            return Utils.error("User doesn't exist.");
        }

        if (movie == null) {
            return Utils.error("Movie doesn't exist.");
        }

        if (!user.getHistory().containsKey(title)) {
            return Utils.error(title + " is not seen");
        }

        if (movie.getRatings().containsKey(username)) {
            return Utils.error(title + " has been already rated");
        }

        movie.addRating(username, rating);

        return Utils.success(title + " was rated with " + rating + " by " + username);
    }

    /**
     * @param username nume utilizator
     * @param title titlul serilaului
     * @param season numarul sezonului
     * @param rating rating
     * @return rezultatul oferirii rating-ului unui serial
     */
    public String rateShow(final String username, final String title,
                           final Integer season, final Double rating) {
        User user = database.getUserByUsername(username);
        Show show = database.getShowByTitle(title);

        if (user == null) {
            return Utils.error("User doesn't exist.");
        }

        if (show == null) {
            return Utils.error("Show doesn't exist.");
        }

        if (!user.getHistory().containsKey(title)) {
            return Utils.error(title + " is not seen");
        }

        if (show.getSeasonsRatings().get(season - 1).containsKey(username)) {
            return Utils.error(title + " has been already rated");
        }

        show.addRating(username, season, rating);
        return Utils.success(title + " was rated with " + rating + " by " + username);
    }
}
