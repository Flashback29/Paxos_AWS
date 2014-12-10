package util;

import java.util.ArrayList;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		int port1 = 11111;
		int n = 5;
		int[] port = new int[n];
		Endpoint[] e = new Endpoint[n];
		ArrayList<Endpoint> servers = new ArrayList<Endpoint>();
		for (int i = 0; i < n; i++) {
			port[i] = port1 + i;
			e[i] = new Endpoint("127.0.0.1", port[i]);
			servers.add(e[i]);
		}
		Server[] server = new Server[n];
		for (int i = 0; i < n; i++) {
			server[i] = new Server(i + 1, 3, servers, port[i]);
		}

		//server[0].SetValue(-222);
		server[4].SetValue(111.0);
		//server[2].SetValue(111);
		//server[1].commService.running = false;
		//server[3].SetValue(111);
		//server[4].SetValue(111);
		
		//server[0].SetValue(111);
	}

	private static class T1 extends Thread {

		@Override
		public void run() {
			// new CommService().SendTo(new Endpoint("169.231.53.15", 6666),
			// "test\n");
		}

	}

	private static class T2 extends Thread {

		@Override
		public void run() {
			// new CommService().Receive(6666);
		}

	}
}
