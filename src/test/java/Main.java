import system.recommendation.DatasetLoader;
import system.recommendation.geneticalgorithm.GeneticAlgorithm;
import system.recommendation.strategy.KMeans;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/*
WAŻNE : ustawić dobrze itemBased na true/false w zależności od przyjętej strategii user based lub item based
 */
public class Main {
    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DatasetLoader datasetLoader = new DatasetLoader("ml-latest-small",true,true);
//        KnnTest.run(datasetLoader);
//        KNNGATEST.run(datasetLoader);
//        ParticleSwarmTest.run(datasetLoader);
//        MatrixFactorizationTest.run(datasetLoader);
        KMeansTest.run(datasetLoader);
    }
}
