package paxos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import util.CommService;
import util.Log;

public class Proposer {

	private int id;
	private Paxos paxos;
	private CommService commService;
	protected HashMap<BallotNumber, Integer> countMajority = new HashMap<BallotNumber, Integer>();
	protected HashMap<BallotNumber, Integer> countMaxVotes = new HashMap<BallotNumber,Integer>();
	protected HashMap<BallotNumber, Double> MaxVotesValue = new HashMap<BallotNumber,Double>();
	private Map.Entry<BallotNumber, Integer> maxEntry = null;
	private Double myVal;
	private ArrayList<Double> myAppendVal;
	private Double proposedVal;
	private BallotNumber bal;
	private Log log;
	public boolean succeed = false;

	public Proposer(int numberOfMajority, int id, Paxos paxos,
			CommService commService, Log log) {
		this.id = id;
		this.myVal = null;
		this.paxos = paxos;
		this.commService = commService;
		this.log = log;
	}

	public void SetLeader(Double proposedVal) {
		this.proposedVal = proposedVal;
		new Thread() {
			public void run() {
				while (log.Size() <= paxos.logIndex) {
					Prepare();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public void Prepare() {
		paxos.Increase(id);
		this.countMajority.put(paxos.ballotNumber, 0);
		commService.SendPrepare(paxos.ballotNumber, paxos.logIndex);
	}
	
	public synchronized void EnhancedReceiveACK(BallotNumber bal,
			BallotNumber accpetNum, Double acceptVal, Double logValue) {
		
		if(acceptVal!=null){
			//a val has been accepted but that val only gets Votes less than majority.
			if (!countMaxVotes.containsKey(accpetNum))
				countMaxVotes.put(accpetNum, 0);
			if (countMaxVotes.get(accpetNum) >= 0){
				countMaxVotes.put(accpetNum, countMaxVotes.get(accpetNum) + 1);
			}
			
			if(!MaxVotesValue.containsKey(accpetNum))
				MaxVotesValue.put(accpetNum, acceptVal);
			
			for (Map.Entry<BallotNumber, Integer> entry: countMaxVotes.entrySet() ){
				if(maxEntry==null || entry.getValue().compareTo(maxEntry.getValue())>0){
					maxEntry = entry;
				}
			}
			
			int maxVotes = maxEntry.getValue();
			Double maxVal = MaxVotesValue.get(maxEntry.getKey());
		
			if((maxVotes + 4 - countMajority.get(bal))<=2){

				if((maxVal>logValue && acceptVal>logValue)){
					if (!countMajority.containsKey(bal))
						countMajority.put(bal, 0);
					if (countMajority.get(bal) >= 0) {
						countMajority.put(bal, countMajority.get(bal) + 1);
					}
					
					this.myAppendVal.add(proposedVal-logValue);
					this.myAppendVal.add(acceptVal-logValue);
					this.bal = accpetNum;
					
					commService.SendEnhancedAccept(paxos.ballotNumber, myAppendVal,
							paxos.logIndex);
					succeed = true;
					this.countMajority.put(bal, -1);
				}
				else if(maxVal<logValue && acceptVal<logValue){
					if((logValue+acceptVal-logValue)<0){
						ReceiveACK(bal,
								accpetNum, acceptVal);
					}
					else{
						if (!countMajority.containsKey(bal))
							countMajority.put(bal, 0);
						if (countMajority.get(bal) >= 0) {
							countMajority.put(bal, countMajority.get(bal) + 1);
						}
						
						this.myAppendVal.add(proposedVal-logValue);
						this.myAppendVal.add(acceptVal-logValue);
						this.bal = accpetNum;
						
						commService.SendEnhancedAccept(paxos.ballotNumber, myAppendVal,
								paxos.logIndex);
						succeed = true;
						this.countMajority.put(bal, -1);
					}
				}
				else{
					ReceiveACK(bal,
							accpetNum, acceptVal);
				}
			}
			else if ( maxVotes>2 && maxVal != proposedVal ){
				ReceiveACK(bal,
						accpetNum, acceptVal);
			}
			else {
				ReceiveACK(bal,
					accpetNum, acceptVal);
			}	
		}
		else{
			ReceiveACK(bal,
					accpetNum, acceptVal);
		}					
		
	}
		// from majority
		// send back to all;
	
	public synchronized void ReceiveACK(BallotNumber bal,
			BallotNumber accpetNum, Double acceptVal) {
		if (!countMajority.containsKey(bal))
			countMajority.put(bal, 0);
		if (countMajority.get(bal) >= 0) {
			countMajority.put(bal, countMajority.get(bal) + 1);

			if (acceptVal != null) {
				if (myVal == null || accpetNum.compareTo(this.bal) > 0) {
					this.myVal = acceptVal;
					this.bal = accpetNum;
				}
			}

			if (this.countMajority.get(bal) >= this.paxos.numberOfMajority) {
				if (myVal == null)
					myVal = proposedVal;
				commService.SendAccept(paxos.ballotNumber, myVal,
						paxos.logIndex);
				succeed = true;
				this.countMajority.put(bal, -1);
			}
		}
		// from majority
		// send back to all;
	}

	public boolean Finished(int logIndex) {
		return log.Size() > logIndex;
	}

	public boolean Succeed() {
		return succeed;
	}
}
