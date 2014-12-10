package paxos;

import java.util.ArrayList;

public class Paxos {
	public int numberOfMajority;
	public BallotNumber ballotNumber;
	public BallotNumber acceptNumber;
	public Double acceptVal;
	public ArrayList<Double> appendAcceptVal;
	public Integer logIndex;

	public Paxos(int numberOfMajority, int logIndex) {
		this.numberOfMajority = numberOfMajority;
		this.ballotNumber = new BallotNumber(0, 0);
		this.acceptNumber = new BallotNumber(0, 0);
		this.acceptVal = null;
		this.appendAcceptVal = null;
		this.logIndex = logIndex;
	}

	public void Increase(int processerId) {
		this.ballotNumber.num++;
		this.ballotNumber.processId = processerId;
	}

}
