import system.recommendation.DatasetLoader;
import system.recommendation.QualityMeasure;
import system.recommendation.matrixfactorization.*;
import system.recommendation.models.Movie;
import system.recommendation.models.User;
import system.recommendation.particleswarm.*;
import system.recommendation.service.RatingService;
import system.recommendation.service.UserService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@SuppressWarnings("unchecked")
public class MatrixFactorizationTest {
    private final static double learningRate = 0.0002;
    private final static double regularization = 0.02;
    private final static int k = 10;
    private final static int populationSize = 50;
    private final static int epochs = 100;
    private static double gradientWeight = 1;
    private static double mutationRate = 0.3;

    public static void run(DatasetLoader datasetLoader) throws IOException, InvocationTargetException {
        Map<Integer, User> users = datasetLoader.getUsers();
        Map<Integer, Movie> movies = datasetLoader.getMovies();
        RatingService<User,Movie> userService = new UserService(users,movies);
        ParticleProvider rmFprovider = new RMFprovider(userService,k,learningRate,regularization);
        double mae1 = swarmTest(userService,rmFprovider)[0];
        double mae2 = MMMFtest(userService)[0];
        double mae3 = RMFtest(userService)[0];
        double mae4 = NMFtest(userService)[0];
        double mae5 = RMFGAtest(userService)[0];

    }

    private static double[] NMFtest(RatingService<User,Movie> userService) throws IOException {
        MatrixFactorization mf = new NMF(userService,k,learningRate,0.01);
        mf.gd(epochs);

        double[][] ratings = mf.getPredictedRatings();
        return new double[]{QualityMeasure.MAE(ratings,userService,false),QualityMeasure.RMSE(ratings,userService)};
    }

    private static double[] RMFtest(RatingService<User,Movie> userService) throws IOException {
        RMF mf = new RMF(userService,k,learningRate,regularization,0.01);
        mf.gd(epochs);

        double[][] ratings = mf.getPredictedRatings();

        return new double[]{QualityMeasure.MAE(ratings,userService,false),QualityMeasure.RMSE(ratings,userService)};
    }

    private static double[] MMMFtest(RatingService<User,Movie> userService) throws IOException {
        MatrixFactorization mf = new MMMF(userService,k,learningRate,regularization,0.01);
        mf.gd(epochs);

        double[][] ratings = mf.getPredictedRatings();
        return new double[]{QualityMeasure.MAE(ratings,userService,false),QualityMeasure.RMSE(ratings,userService)};
    }

    private static double[] RMFGAtest(RatingService<User,Movie> userService){
        RMFGA mf = new RMFGA(userService,k,learningRate,regularization);
        RMF best = mf.run(populationSize,epochs,mutationRate);

        double[][] ratings = best.getPredictedRatings();

        return new double[]{QualityMeasure.MAE(ratings,userService,false),QualityMeasure.RMSE(ratings,userService)};
    }

    private static <T extends MatrixFactorization> double[] swarmTest(RatingService<User,Movie> ratingService, ParticleProvider pp) throws InvocationTargetException {
        ParticleSwarm ps = new ParticleSwarm(pp,populationSize,gradientWeight);
        T best = (T) ps.run(epochs);
        double[][] predicted = best.getPredictedRatings();

        return new double[]{QualityMeasure.MAE(predicted,ratingService,false),QualityMeasure.RMSE(predicted,ratingService)};
    }
}
