package paxos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import util.CommService;
import util.Log;

public class Learner {

	private int id;
	private Paxos paxos;
	private CommService commService;
	private HashMap<BallotNumber, Integer> countAccept;
	private Set<String> countDecide;
	private Log log;
	private boolean sending = false;

	public Learner(int numberOfMajority, int id, Paxos paxos,
			CommService commService, Log log) {
		this.id = id;
		this.paxos = paxos;
		this.commService = commService;
		this.countAccept = new HashMap<BallotNumber, Integer>();
		//this.countDecide = new HashMap<BallotNumber, Integer>();
		this.countDecide = new HashSet<String>();;
		this.log = log;
	}

	public void ReceiveAccept(BallotNumber bal, Double val) {
		countAccept.put(bal,
				(countAccept.get(bal) == null ? 0 : countAccept.get(bal)) + 1);
		if (countAccept.get(bal) >= paxos.numberOfMajority) {
			// decide v
			paxos.acceptVal = null;
			commService.SendDecide(bal, val, paxos.logIndex);
		}
	}
	
	public void ReceiveEnhancedAccept(BallotNumber bal, ArrayList<Double> val) {
		countAccept.put(bal,
				(countAccept.get(bal) == null ? 0 : countAccept.get(bal)) + 1);
		if (countAccept.get(bal) == paxos.numberOfMajority) {
			// decide v
			paxos.appendAcceptVal = null;
			commService.SendEnhancedDecide(bal, val, paxos.logIndex);
		}
	}

	public synchronized void ReceiveDecide(final BallotNumber bal, final Double val, String ip) {
		// decide v
		paxos.acceptVal = null;
		log.Write(bal, val, paxos.logIndex);
		
		//countDecide.put(bal,
		//		(countDecide.get(bal) == null ? 0 : countDecide.get(bal)) + 1);
		countDecide.add(ip);
		if (!sending ) {
			sending = true;
			new Thread() {
				public void run() {
					while (true) {
						paxos.acceptVal = null;
						commService.SendDecide(bal, val, paxos.logIndex);
						System.out.println(countDecide.size());
						if (countDecide.size() == 5)
							break;
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
	}
	
	public void ReceiveEnhancedDecide(BallotNumber bal, ArrayList<Double> val) {
		// decide v
		paxos.acceptVal = null;
		log.EnhancedWrite(bal, val, paxos.logIndex);
		
		//countDecide.put(bal,
		//		(countDecide.get(bal) == null ? 0 : countDecide.get(bal)) + 1);
		//if (!sending&& countDecide.get(bal) < 5 ) {
		//	sending = true;
		//	new Thread() {
				
		//	}.start();
		//}
	}
}
