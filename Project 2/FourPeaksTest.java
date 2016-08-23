package opt.test;

import java.util.Arrays;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.SingleCrossOver;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class FourPeaksTest {
    /** The n value */
    private static final int N = 60;
    /** The t value */
    private static final int T = N / 5;
    
    public static void main(String[] args) {
        int[] ranges = new int[N];
        Arrays.fill(ranges, 2);
        EvaluationFunction ef = new FourPeaksEvaluationFunction(T);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new SingleCrossOver();
        Distribution df = new DiscreteDependencyTree(.1, ranges); 
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        
        int iter = 200;
        int N1 = 100;
        double[] result_rhc = new double[N1];
        double[] result_sa = new double[N1];
        double[] result_ga = new double[N1];
        double[] result_mimic = new double[N1];
        
        for(int i = 0; i < N1; i++) {
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);      
        FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 10*iter);
        fit.train();
        //System.out.println("RHC: " + ef.value(rhc.getOptimal()));
        result_rhc[i]=ef.value(rhc.getOptimal());
        
        SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
        fit = new FixedIterationTrainer(sa, 10*iter);
        fit.train();
        //System.out.println("SA: " + ef.value(sa.getOptimal()));
        result_sa[i]=ef.value(sa.getOptimal());
        
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 20, gap);
        fit = new FixedIterationTrainer(ga, iter);
        fit.train();
        //System.out.println("GA: " + ef.value(ga.getOptimal()));
        result_ga[i]=ef.value(ga.getOptimal());
        
        MIMIC mimic = new MIMIC(200, 20, pop);
        fit = new FixedIterationTrainer(mimic, iter);
        fit.train();
        //System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));
        result_mimic[i]=ef.value(mimic.getOptimal());
        }
        
        /*System.out.println("GHC: "+average(result_rhc));
        System.out.println("SA: "+average(result_sa));
        System.out.println("GA: "+average(result_ga));
        System.out.println("MIMIC: "+average(result_mimic))*/
        
        System.out.println(average(result_rhc));
        System.out.println(average(result_sa));
        System.out.println(average(result_ga));
        System.out.println(average(result_mimic));
        
        System.out.println(endlocal(result_rhc,N));
        System.out.println(endlocal(result_sa,N));
        System.out.println(endlocal(result_ga,N));
        System.out.println(endlocal(result_mimic,N));
        
        System.out.println(endglobal(result_rhc,N));
        System.out.println(endglobal(result_sa,N));
        System.out.println(endglobal(result_ga,N));
        System.out.println(endglobal(result_mimic,N));
        }
    
    public static double average(double[] result_list) {
        // 'average' is undefined if there are no elements in the list.
        // Calculate the summation of the elements in the list
        long sum = 0;
        int n = result_list.length;
        // Iterating manually is faster than using an enhanced for loop.
        for (int i = 0; i < n; i++)
            sum += result_list[i];
        // We don't want to perform an integer division, so the cast is mandatory.
        return ((double) sum) / n;
    }
    
    public static int endlocal(double[] result_list,int local){
    	int time = 0;
    	int n = result_list.length;
    	for (int i = 0; i < n; i++){
    		if (result_list[i]<=local){
    			time +=1;
    		}
    	}
    	return time;
    	}
    
    public static int endglobal(double[] result_list,int global){
    	int time = 0;
    	int n = result_list.length;
    	double d = global*1.8-1;
    	for (int i = 0; i < n; i++){
    		if (result_list[i]==d){
    			time +=1;
    		}
    	}
    	return time;
    	}
    	
    }


