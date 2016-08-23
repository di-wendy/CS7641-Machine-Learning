package burlap.assignment4.util;

import burlap.assignment4.BasicGridWorld;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;

public class BasicTerminalFunction implements TerminalFunction {

	int goalX;
	int goalY;
	int goalA;
	int goalB;

	public BasicTerminalFunction(int goalX, int goalY, int goalA, int goalB) {
		this.goalX = goalX;
		this.goalY = goalY;
		this.goalA = goalA;
		this.goalB = goalB;
	}

	@Override
	public boolean isTerminal(State s) {

		// get location of agent in next state
		ObjectInstance agent = s.getFirstObjectOfClass(BasicGridWorld.CLASSAGENT);
		int ax = agent.getIntValForAttribute(BasicGridWorld.ATTX);
		int ay = agent.getIntValForAttribute(BasicGridWorld.ATTY);

		// are they at goal location?
		if (ax == this.goalX && ay == this.goalY) {
			return true;
		}
		
		//For easy Grid World
		/*if (ax == this.goalA && ay == this.goalB) {
			return true;
		}
		
		if (ay==0&&ax!=0){
			return true;
		}*/
		
		//For Hard world
		if (ax==3&&(ay==3||ay==4||ay==5||ay==6||ay==7||ay==8||ay==9||ay==10)){
			return true;
		}

		return false;
	}

}
