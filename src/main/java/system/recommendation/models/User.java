package system.recommendation.models;

import java.util.HashMap;

public class User extends Entity{
    private final int id;
    private double avgRating = 0;
    private final HashMap<Integer, Double> ratings = new HashMap<>();
    private final HashMap<Integer, Double> predictedRatings = new HashMap<>();

    public User(int id) {
        this.id = id;
    }

    public void addPredictedRating(int movieId, double rating) {
        predictedRatings.put(movieId, rating);
    }

    public void addRating(int movieId, double rating) {
        int ratingsNumber = this.ratings.size();
        this.avgRating = (this.avgRating*ratingsNumber+rating)/(ratingsNumber+1);
        ratings.put(movieId, rating);
    }

    public int getId() {
        return id;
    }

    public HashMap<Integer, Double> getPredictedRatings() {
        return predictedRatings;
    }

    public double getRating(int movieId) {
        return this.ratings.get(movieId);
    }

    public double getAvgRating() {
        return this.avgRating;
    }

    public HashMap<Integer, Double> getRatings() {
        return this.ratings;
    }
}
