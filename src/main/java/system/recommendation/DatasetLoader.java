package system.recommendation;

import system.recommendation.models.Movie;
import system.recommendation.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DatasetLoader {

    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, Movie> movies = new HashMap<>();
    private final Map<Integer, Integer> moviesFakeRealID = new HashMap<>();

    public DatasetLoader(String datasetFolderName) throws FileNotFoundException {
        System.out.println("Loading data from " + datasetFolderName);
        addMovies(datasetFolderName);
        handleRatings(datasetFolderName);
        System.out.println("Finished loading data from " + datasetFolderName);
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public Map<Integer, Movie> getMovies() {
        return movies;
    }

    private void handleRatings(String datasetFolderName) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(datasetFolderName + "/ratings.csv"))){
            scanner.nextLine();
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] data = line.split(",");
                int userId = Integer.parseInt(data[0]);
                int movieFakeId = Integer.parseInt(data[1]);
                double rating = Double.parseDouble(data[2]);

                User user;
                Integer movieId = moviesFakeRealID.get(movieFakeId);
                Movie movie = movies.get(movieId);

                if(!users.containsKey(userId)){
                    user = new User(userId);
                    users.put(userId, user);
                }else{
                    user = users.get(userId);
                }

                movie.addUser(user);
                user.addRating(movieId, rating);
            }
        }
    }

    private void addMovies(String datasetFolderName) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(datasetFolderName + "/movies.csv"))){
            scanner.nextLine();
            int ctr = 1;
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] data = line.split(",");
                int movieId = Integer.parseInt(data[0]);
                moviesFakeRealID.put(movieId, ctr);
                movies.put(ctr, new Movie(ctr));
                ctr++;
            }
        }
    }
}
