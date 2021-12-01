package main;

import fileio.ActionInputData;

import java.util.List;

public class Action {
    private int actionId;
    private String actionType;
    private String type;
    private String username;
    private String objectType;
    private String sortType;
    private String criteria;
    private String title;
    private String genre;
    private int number;
    private double grade;
    private int seasonNumber;
    private List<List<String>> filters;

    /**
     * @param actionInputData datele de intrare ale actiunii
     */
    public Action(final ActionInputData actionInputData) {
        this.actionId = actionInputData.getActionId();
        this.actionType = actionInputData.getActionType();
        this.type = actionInputData.getType();
        this.username = actionInputData.getUsername();
        this.objectType = actionInputData.getObjectType();
        this.sortType = actionInputData.getSortType();
        this.criteria = actionInputData.getCriteria();
        this.title = actionInputData.getTitle();
        this.genre = actionInputData.getGenre();
        this.number = actionInputData.getNumber();
        this.grade = actionInputData.getGrade();
        this.seasonNumber = actionInputData.getSeasonNumber();
        this.filters = actionInputData.getFilters();
    }

    /**
     * @return id-ul actiunii
     */
    public int getActionId() {
        return actionId;
    }

    /**
     * @param actionId id-ul actiunii
     */
    public void setActionId(final int actionId) {
        this.actionId = actionId;
    }

    /**
     * @return tipul actiunii
     */
    public String getActionType() {
        return actionType;
    }

    /**
     * @param actionType tipul actiunii
     */
    public void setActionType(final String actionType) {
        this.actionType = actionType;
    }

    /**
     * @return tipul
     */
    public String getType() {
        return type;
    }

    /**
     * @param type tipul
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @return numele de utilizator
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username numele de utilizator
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return tipul obiectului
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * @param objectType tipul obiectului
     */
    public void setObjectType(final String objectType) {
        this.objectType = objectType;
    }

    /**
     * @return tipul sortarii
     */
    public String getSortType() {
        return sortType;
    }

    /**
     * @param sortType tipul sortarii
     */
    public void setSortType(final String sortType) {
        this.sortType = sortType;
    }

    /**
     * @return criteriu
     */
    public String getCriteria() {
        return criteria;
    }

    /**
     * @param criteria criteriu
     */
    public void setCriteria(final String criteria) {
        this.criteria = criteria;
    }

    /**
     * @return titlul video-ului
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title titlul video-ului
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * @return genul video-ului
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @param genre genul video-ului
     */
    public void setGenre(final String genre) {
        this.genre = genre;
    }

    /**
     * @return numarul maxim de rezultate
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number numarul maxim de rezultate
     */
    public void setNumber(final int number) {
        this.number = number;
    }

    /**
     * @return rating
     */
    public double getGrade() {
        return grade;
    }

    /**
     * @param grade rating
     */
    public void setGrade(final double grade) {
        this.grade = grade;
    }

    /**
     * @return numarul sezonului din serial
     */
    public int getSeasonNumber() {
        return seasonNumber;
    }

    /**
     * @param seasonNumber numarul sezonului din serial
     */
    public void setSeasonNumber(final int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    /**
     * @return lista de filtre ce se doresc a fi aplicate
     */
    public List<List<String>> getFilters() {
        return filters;
    }

    /**
     * @param filters lista de filtre ce se doresc a fi aplicate
     */
    public void setFilters(final List<List<String>> filters) {
        this.filters = filters;
    }
}
