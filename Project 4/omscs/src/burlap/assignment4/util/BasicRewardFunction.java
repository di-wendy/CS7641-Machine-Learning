package burlap.assignment4.util;

import burlap.assignment4.BasicGridWorld;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;

public class BasicRewardFunction implements RewardFunction {

	int goalX;
	int goalY;
	int goalA;
	int goalB;

	public BasicRewardFunction(int goalX, int goalY,int goalA, int goalB) {
		this.goalX = goalX;
		this.goalY = goalY;
		this.goalA = goalA;
		this.goalB = goalB;
	}

	@Override
	public double reward(State s, GroundedAction a, State sprime) {

		// get location of agent in next state
		ObjectInstance agent = sprime.getFirstObjectOfClass(BasicGridWorld.CLASSAGENT);
		int ax = agent.getIntValForAttribute(BasicGridWorld.ATTX);
		int ay = agent.getIntValForAttribute(BasicGridWorld.ATTY);

		// are they at goal location?
		if (ax == this.goalX && ay == this.goalY) {
			return 100.;
		}
		
		//For easy Grid world
		/*if (ax == this.goalX && ay == this.goalY) {
			return 1.;
		}
		
		if (ay==0){
			return -10.;
		}*/
		
		
		//For Hard Grid World
		
		if (ax==3&&(ay==3||ay==4||ay==5||ay==6||ay==7||ay==8||ay==9||ay==10)){
			return -100.;
		}
		

		return -1;
	}

}
