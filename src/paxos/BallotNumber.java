package paxos;

public class BallotNumber implements Comparable<BallotNumber> {
	public int num;
	public int processId;

	public BallotNumber(int num, int processId) {
		this.num = num;
		this.processId = processId;
	}

	public void increase() {
		this.num++;
	}

	public BallotNumber copy() {
		return new BallotNumber(num, processId);
	}

	public BallotNumber(String msg) {
		String[] s = msg.split(";");
		num = Integer.parseInt(s[0].trim());
		processId = Integer.parseInt(s[1].trim());
	}

	public String toMsg() {
		return num + ";" + processId + ";";
	}

	@Override
	public int compareTo(BallotNumber other) {
		if (this.num > other.num
				|| (this.num == other.num && this.processId > other.processId))
			return 1;
		if (this.num == other.num && this.processId == other.processId)
			return 0;
		return -1;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof BallotNumber) {
			BallotNumber bal = (BallotNumber) other;
			return bal.num == this.num && bal.processId == this.processId;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.num ^ this.processId;
	}
}
