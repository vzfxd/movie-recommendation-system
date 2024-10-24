package system.recommendation.similarity;

import system.recommendation.models.Entity;
import system.recommendation.service.RatingService;

import java.util.Set;

public class EuclideanDistance<T extends Entity> implements Similarity<T>{
    private final RatingService<T> ratingService;

    public EuclideanDistance(RatingService<T> ratingService){
        this.ratingService = ratingService;
    }

    @Override
    public double calculate(T a, T b) {
        double result = 0;
        Set<Integer> common = a.getCommon(b);
        if(common.isEmpty()) return 0;

        for(Integer id: common){
            int aID = a.getId();
            int bID = b.getId();
            double r1 = ratingService.getRating(aID,id);
            double r2 = ratingService.getRating(bID,id);
            result += Math.pow(r1-r2,2);

        }

        return Math.sqrt(result);
    }
}
