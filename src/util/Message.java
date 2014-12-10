package util;

import java.util.ArrayList;

import paxos.BallotNumber;

public class Message {
	public boolean modify = false;

	public static class Prepare {
		public BallotNumber bal;
		public int logIndex;

		public Prepare(BallotNumber bal, int logIndex) {
			this.bal = bal;
			this.logIndex = logIndex;
		}

		public Prepare(String msg) {
			String[] res = msg.split(";");
			if (res.length >= 2) {
				bal = new BallotNumber(Integer.parseInt(res[0]),
						Integer.parseInt(res[1]));
				logIndex = Integer.parseInt(res[2]);
			}
		}
		public int getProcessorID(){
			return bal.processId;
		}

		public String toMsg() {
			return "Prepare:" + bal.toMsg() + logIndex;
		}
	}

	public static class ACK {
		public BallotNumber bal;
		public BallotNumber accpetNum;
		public Double acceptVal;
		public int logIndex;

		public ACK(BallotNumber bal, BallotNumber accpetNum, Double acceptVal,
				int logIndex) {
			this.bal = bal;
			this.accpetNum = accpetNum;
			this.acceptVal = acceptVal;
			this.logIndex = logIndex;
		}

		public ACK(String msg) {
			String[] res = msg.split(";");
			if (res.length >= 6) {
				bal = new BallotNumber(Integer.parseInt(res[0]),
						Integer.parseInt(res[1]));
				accpetNum = new BallotNumber(Integer.parseInt(res[2]),
						Integer.parseInt(res[3]));
				try {
					acceptVal = Double.parseDouble(res[4]);
				} catch (Exception e) {
					acceptVal = null;
				}
				logIndex = Integer.parseInt(res[5]);
			}
		}

		public String toMsg() {
			return "ACK:" + bal.toMsg() + accpetNum.toMsg() + acceptVal + ";"
					+ logIndex;
		}
	}

	public static class Accept {
		public BallotNumber accpetNum;
		public Double acceptVal;
		public ArrayList<Double> acceptAppendVal;
		public int logIndex;
		public boolean modify = false;

		public Accept(BallotNumber accpetNum, Double acceptVal, int logIndex) {
			this.accpetNum = accpetNum;
			this.acceptVal = acceptVal;
			this.logIndex = logIndex;
		}

		public Accept(String msg) {
			String[] res = msg.split(";");
			if (res.length >= 4) {
				accpetNum = new BallotNumber(Integer.parseInt(res[0]),
						Integer.parseInt(res[1]));
				try {
					if(res[2].contains("-")){
						modify = true;
						String[] doubleParts = res[2].split("-");
						for(int i=0;i<doubleParts.length;i++){
							acceptAppendVal.add(Double.parseDouble(doubleParts[i]));
						}
					}
					else{
						acceptVal = Double.parseDouble(res[2]);
					}
				} catch (Exception e) {
					acceptVal = null;
				}
				logIndex = Integer.parseInt(res[3]);
			}
		}

		public String toMsg() {
			if(!modify)
				return "Accept:" + accpetNum.toMsg() + acceptVal.toString() + ";"+ logIndex;
			else{
				String s = "";
				for(int i=0;i<acceptAppendVal.size();i++){
					s = s+ acceptAppendVal.get(i).toString()+"-";
				}
				s = s.substring(0, acceptAppendVal.size()-1);
				
				return "Accept:" + accpetNum.toMsg() + s + ";"+ logIndex;
			}
				
		}
	}
	
	public static class EnhancedAccept {
		public BallotNumber accpetNum;
		public ArrayList<Double> acceptVal;
		public int logIndex;

		public EnhancedAccept(BallotNumber accpetNum, ArrayList<Double> acceptVal, int logIndex) {
			this.accpetNum = accpetNum;
			this.acceptVal = acceptVal;
			this.logIndex = logIndex;
		}

		public EnhancedAccept(String msg) {
			String[] res = msg.split(";");
			if (res.length >= 4) {
				accpetNum = new BallotNumber(Integer.parseInt(res[0]),
						Integer.parseInt(res[1]));
				try {
					String[] doubleParts = res[2].split("-");
					for(int i=0;i<doubleParts.length;i++){
						acceptVal.add(Double.parseDouble(doubleParts[i]));
					}
				} catch (Exception e) {
					acceptVal = null;
				}
				logIndex = Integer.parseInt(res[3]);
			}
		}

		public String toMsg() {
			String s = "";
			for(int i=0;i<acceptVal.size();i++){
				s = s+ acceptVal.get(i).toString()+"-";
			}
			s = s.substring(0, acceptVal.size()-1);
			
			return "Accept:" + accpetNum.toMsg() + s + ";"+ logIndex;
		}
	}
	
	public static class EnhancedDecide {
		public BallotNumber accpetNum;
		public ArrayList<Double> acceptVal;
		public int logIndex;

		public EnhancedDecide(BallotNumber accpetNum, ArrayList<Double> acceptVal, int logIndex) {
			this.accpetNum = accpetNum;
			this.acceptVal = acceptVal;
			this.logIndex = logIndex;
		}

		public EnhancedDecide(String msg) {
			String[] res = msg.split(";");
			if (res.length >= 4) {
				accpetNum = new BallotNumber(Integer.parseInt(res[0]),
						Integer.parseInt(res[1]));
				try {
					String[] doubleParts = res[2].split("-");
					for(int i=0;i<doubleParts.length;i++){
						acceptVal.add(Double.parseDouble(doubleParts[i]));
					}
				} catch (Exception e) {
					acceptVal = null;
				}
				logIndex = Integer.parseInt(res[3]);
			}
		}

		public String toMsg() {
			String s = "";
			for(int i=0;i<acceptVal.size();i++){
				s = s+ acceptVal.get(i).toString()+"-";
			}
			s = s.substring(0, acceptVal.size()-1);
			
			return "Decide:" + accpetNum.toMsg() + s + ";"
					+ logIndex;
		}
	}
	
	public static class Decide {
		public BallotNumber accpetNum;
		public Double acceptVal;
		public ArrayList<Double> acceptAppendVal;
		public int logIndex;
		public boolean modify = false;

		public Decide(BallotNumber accpetNum, Double acceptVal, int logIndex) {
			this.accpetNum = accpetNum;
			this.acceptVal = acceptVal;
			this.logIndex = logIndex;
		}

		public Decide(String msg) {
			String[] res = msg.split(";");
			if (res.length >= 4) {
				accpetNum = new BallotNumber(Integer.parseInt(res[0]),
						Integer.parseInt(res[1]));
				try {
					if(res[2].contains("-")){
						modify = true;
						String[] doubleParts = res[2].split("-");
						for(int i=0;i<doubleParts.length;i++){
							acceptAppendVal.add(Double.parseDouble(doubleParts[i]));
						}
					}
					else{
						acceptVal = Double.parseDouble(res[2]);
					}

				} catch (Exception e) {
					acceptVal = null;
				}
				logIndex = Integer.parseInt(res[3]);
			}
		}

		public String toMsg() {
			if(!modify)
				return "Decide:" + accpetNum.toMsg() + acceptVal.toString() + ";"
						+ logIndex;
			else{
				String s = "";
				for(int i=0;i<acceptAppendVal.size();i++){
					s = s+ acceptAppendVal.get(i).toString()+"-";
				}
				s = s.substring(0, acceptAppendVal.size()-1);
				
				return "Decide:" + accpetNum.toMsg() + s + ";"
						+ logIndex;
			}
		}
	}

	public String type;
}
