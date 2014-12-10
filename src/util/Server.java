package util;

import java.util.ArrayList;
import java.util.HashMap;

import paxos.Acceptor;
import paxos.Learner;
import paxos.Paxos;
import paxos.Proposer;

public class Server {
	HashMap<Integer, Proposer> proposer;
	HashMap<Integer, Acceptor> acceptor;
	HashMap<Integer, Learner> learner;
	CommService commService;
	public Log log;

	public Server(int id, int numberOfMajority, ArrayList<Endpoint> servers,
			int port) {
		log = new Log(id + ".txt");
		commService = new CommService(servers, id, numberOfMajority);
		this.proposer = new HashMap<Integer, Proposer>();
		this.acceptor = new HashMap<Integer, Acceptor>();
		this.learner = new HashMap<Integer, Learner>();
		commService.Init(proposer, acceptor, learner, port, log);
		commService.start();
	}

	public boolean SetValue(Double value) {
		int logIndex = log.Size();
		commService.Create(logIndex);
		proposer.get(logIndex).SetLeader(value);
		while (!proposer.get(logIndex).Finished(logIndex)) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return proposer.get(logIndex).Succeed();
	}
}