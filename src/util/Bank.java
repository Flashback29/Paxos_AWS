package util;

import java.util.ArrayList;
import java.util.Scanner;

public class Bank {

	static Server server;

	public Bank(Server server) {
		this.server = server;
	}

	public Bank(int id, int numberOfMajority, ArrayList<Endpoint> servers,
			int port) {
		server = new Server(id, numberOfMajority, servers, port);
	}

	public static boolean Deposit(Double value) {
		double bal = Balance();
		return server.SetValue(value + bal);
	}

	public static boolean Withdraw(Double value) {
		double bal = Balance();
		if (bal < value)
			return false;
		return server.SetValue(bal - value);
	}

	public static Double Balance() {
		return server.log.GetValue();
	}

	public static void Fail() {
		server.commService.running = false;
	}

	public static void Unfail() {
		server.commService.running = true;
	}
	public static void Print(){
		server.log.Print();
	}
	public static void Modify(){
		server.commService.modify = true;
	}

	public static void main(String[] args) throws InterruptedException {
		ArrayList<Endpoint> servers = new ArrayList<Endpoint>();
		int port1 = 11111;
		int n = 5;
		int[] port = new int[n];
		Endpoint[] e = new Endpoint[n];
		servers.add(new Endpoint("54.149.87.52",11111));
		servers.add(new Endpoint("54.154.0.20",11111));
		servers.add(new Endpoint("54.169.187.65",11111));
		servers.add(new Endpoint("54.79.80.1",11111));
		servers.add(new Endpoint("54.94.231.222",11111));
		
		/*
		for (int i = 0; i < n; i++) {
			port[i] = port1 + i;
			e[i] = new Endpoint("127.0.0.1", port[i]);
			servers.add(e[i]);
		}*/
		Bank bank = new Bank(Integer.parseInt(args[0]),
				Integer.parseInt(args[1]), servers, Integer.parseInt(args[2]));
		Scanner in = new Scanner(System.in);
		String line = "";
		System.out.println("Deposit: 1");
		System.out.println("Withdraw: 2");
		System.out.println("Balance: 3");
		System.out.println("Fail: 4");
		System.out.println("UnFail: 5");
		System.out.println("Print: 6");
		System.out.println("Modiefied ISPaxos 7");
		while (in.hasNextInt()) {
			int opt = in.nextInt();
			switch (opt) {
			case 1:
				Deposit(in.nextDouble());
				break;
			case 2:
				if(!Withdraw(in.nextDouble())){
					System.out.println("Withdrew Fail. Your bank value is less than the value you want to withdraw.");
				}
				break;
			case 3:
				System.out.println(Balance());
				break;
			case 4:
				Fail();
				break;
			case 5:
				Unfail();
				break;
			case 6:
				Print();
				break;
			case 7:
				Modify();
			}
		}

	}
}
