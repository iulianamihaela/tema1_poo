package users;

import fileio.UserInputData;

import java.util.ArrayList;
import java.util.Map;

public class User {
    private String username;
    private String subscriptionType;
    private Map<String, Integer> history;
    private ArrayList<String> favoriteMovies;

    /**
     * @param userInputData datele de intrare ale utilizatorului
     */
    public User(final UserInputData userInputData) {
        this.username = userInputData.getUsername();
        this.subscriptionType = userInputData.getSubscriptionType();
        this.history = userInputData.getHistory();
        this.favoriteMovies = userInputData.getFavoriteMovies();
    }

    /**
     * @return numele utilizatorului
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username numele utilizatorului
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return tipul subscriptiei
     */
    public String getSubscriptionType() {
        return subscriptionType;
    }

    /**
     * @param subscriptionType tipul subscriptiei
     */
    public void setSubscriptionType(final String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    /**
     * @return istoricul de vizionari
     */
    public Map<String, Integer> getHistory() {
        return history;
    }

    /**
     * @param history istoricul de vizionari
     */
    public void setHistory(final Map<String, Integer> history) {
        this.history = history;
    }

    /**
     * @return lista de filme favorite
     */
    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    /**
     * @param favoriteMovies lista de filme favorite
     */
    public void setFavoriteMovies(final ArrayList<String> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
    }
}
