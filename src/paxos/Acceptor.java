package paxos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import util.CommService;

public class Acceptor {
	private int id;
	private Paxos paxos;
	private CommService commService;
	private HashSet<BallotNumber> first;

	public Acceptor(int numberOfMajority, int id, Paxos paxos,
			CommService commService) {
		this.id = id;
		this.paxos = paxos;
		this.commService = commService;
		this.first = new HashSet<BallotNumber>();
	}

	public void ReceivePrepare(BallotNumber bal, String ip, int port) {
		if (bal.compareTo(paxos.ballotNumber) >= 0) {
			paxos.ballotNumber = bal.copy();
			// send ack to serverId
			commService.SendACK(paxos.ballotNumber, paxos.acceptNumber,
					paxos.acceptVal, ip, port, paxos.logIndex);
		}
	}

	public void ReceiveAccept(BallotNumber bal, Double val) {
		if (bal.compareTo(paxos.ballotNumber) >= 0) {
			paxos.ballotNumber = bal.copy();
			paxos.acceptVal = val;

			if (!first.contains(bal)) {
				commService.SendAccept(bal, val, paxos.logIndex);
				first.add(bal);
			}
			// send accept to all
		}
	}
	
	public void ReceiveEnhancedAccept(BallotNumber bal, ArrayList<Double> val) {
		if (bal.compareTo(paxos.ballotNumber) >= 0) {
			paxos.ballotNumber = bal.copy();
			paxos.appendAcceptVal = val;

			if (!first.contains(bal)) {
				commService.SendEnhancedAccept(bal, val, paxos.logIndex);
				first.add(bal);
			}
			// send accept to all
		}
	}
}
