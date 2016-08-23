package burlap.assignment4;

import burlap.assignment4.util.AnalysisAggregator;
import burlap.assignment4.util.AnalysisRunner;
import burlap.assignment4.util.BasicRewardFunction;
import burlap.assignment4.util.BasicTerminalFunction;
import burlap.assignment4.util.MapPrinter;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.environment.SimulatedEnvironment;
import burlap.oomdp.singleagent.explorer.VisualExplorer;
import burlap.oomdp.visualizer.Visualizer;

public class HardGridWorldLauncher {
	//These are some boolean variables that affect what will actually get executed
	private static boolean visualizeInitialGridWorld = true; //Loads a GUI with the agent, walls, and goal
	
	//runValueIteration, runPolicyIteration, and runQLearning indicate which algorithms will run in the experiment
	private static boolean runValueIteration = true; 
	private static boolean runPolicyIteration = true;
	private static boolean runQLearning = true;
	
	//showValueIterationPolicyMap, showPolicyIterationPolicyMap, and showQLearningPolicyMap will open a GUI
	//you can use to visualize the policy maps. Consider only having one variable set to true at a time
	//since the pop-up window does not indicate what algorithm was used to generate the map.
	private static boolean showValueIterationPolicyMap = true; 
	private static boolean showPolicyIterationPolicyMap = true;
	private static boolean showQLearningPolicyMap = false;
	
	private static Integer MAX_ITERATIONS = 200;
	private static Integer NUM_INTERVALS = 200;

	protected static int[][] userMap = new int[][] { 
										{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1},
										{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1},
										{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
										{ 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1},
										{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
										{ 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1},
										{ 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1},
										{ 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1},
										{ 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1},
										{ 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1},
										{ 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1},
										{ 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1},
										{ 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1},
										{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
										{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},};

//	private static Integer mapLen = map.length-1;

	public static void main(String[] args) {
		// convert to BURLAP indexinga
		int[][] map = MapPrinter.mapToMatrix(userMap);
		int maxX = map.length-1;
		int maxY = map[0].length-1;
		// 

		BasicGridWorld gen = new BasicGridWorld(map,maxX,maxY); //0 index map is 15X15
		Domain domain = gen.generateDomain();

		State initialState = BasicGridWorld.getExampleState(domain);

		RewardFunction rf = new BasicRewardFunction(0,14,0,14); //Goal is at 0,14
		TerminalFunction tf = new BasicTerminalFunction(0,14,0,14);
		
		SimulatedEnvironment env = new SimulatedEnvironment(domain, rf, tf,
				initialState);
		//Print the map that is being analyzed
		System.out.println("/////Hard Grid World Analysis/////\n");
		MapPrinter.printMap(MapPrinter.matrixToMap(map));
		
		if (visualizeInitialGridWorld) {
			visualizeInitialGridWorld(domain, gen, env);
		}
		
		AnalysisRunner runner = new AnalysisRunner(MAX_ITERATIONS,NUM_INTERVALS);
		if(runValueIteration){
			runner.runValueIteration(gen,domain,initialState, rf, tf, showValueIterationPolicyMap);
		}
		if(runPolicyIteration){
			runner.runPolicyIteration(gen,domain,initialState, rf, tf, showPolicyIterationPolicyMap);
		}
		if(runQLearning){
			runner.runQLearning(gen,domain,initialState, rf, tf, env, showQLearningPolicyMap);
		}
		AnalysisAggregator.printAggregateAnalysis();
	}



	private static void visualizeInitialGridWorld(Domain domain,
			BasicGridWorld gen, SimulatedEnvironment env) {
		Visualizer v = gen.getVisualizer();
		VisualExplorer exp = new VisualExplorer(domain, env, v);

		exp.addKeyAction("w", BasicGridWorld.ACTIONNORTH);
		exp.addKeyAction("s", BasicGridWorld.ACTIONSOUTH);
		exp.addKeyAction("d", BasicGridWorld.ACTIONEAST);
		exp.addKeyAction("a", BasicGridWorld.ACTIONWEST);

		exp.initGUI();

	}
	

}
