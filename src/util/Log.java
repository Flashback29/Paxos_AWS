package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import paxos.BallotNumber;

public class Log {
	
	private ArrayList<String> print;
	private ArrayList<Double> values;
	private ArrayList<BallotNumber> numbers;
	private String path = null;
	private BufferedWriter writer = null;

	public Log() {
		values = new ArrayList<Double>();
		numbers = new ArrayList<BallotNumber>();
		print = new ArrayList<String>();
	}

	public Log(String path) {
		this();
		this.path = path;
		try {
			Scanner in = new Scanner(new File(path));
			String line = "";
			while ((line = in.nextLine()) != null) {
				String msg = line.substring(line.indexOf(":") + 1);
				numbers.add(new BallotNumber(msg.split("_")[0]));
				values.add(Double.parseDouble(msg.split("_")[1]));
				print.add(msg.split("_")[2]);
			}
		} catch (Exception e) {
		}
	}

	public synchronized void Write(BallotNumber bal, Double val, int logIndex) {
		if (writer == null)
			try {
				writer = new BufferedWriter(new FileWriter(path, true));
			} catch (IOException e) {
				e.printStackTrace();
			}
		while (numbers.size() <= logIndex) {
			String operation = "";
			if((values.size()>=1)){
				if((val-values.get(values.size()-1)>=0)){
					operation = "Deposit:"+(val-values.get(values.size()-1));
				}
				else{
					operation = "Withdraw:"+(values.get(values.size()-1)-val);
				}
			}
			else{
				operation = "Deposit:"+val; 
			}
			
			values.add(val);
			numbers.add(bal);
			print.add(operation);
			
			try {
				writer.append(logIndex + " : " + bal.toMsg() + "_" + val+ "_" +operation);
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void EnhancedWrite(BallotNumber bal, ArrayList<Double> val, int logIndex) {
		if (writer == null)
			try {
				writer = new BufferedWriter(new FileWriter(path, true));
			} catch (IOException e) {
				e.printStackTrace();
			}
		while (numbers.size() <= logIndex) {
			String[] operation = new String[val.size()];
			if((values.size()>=1)){
				for(int i=0;i>val.size();i++){
					if(val.get(i)>0){
						operation[i] = "Deposit:"+ val.get(i);
					}
					else{
						operation[i] = "Withdraw:"+ val.get(i);
					}
				}
			}
			else{
				for(int i=0;i>val.size();i++){
					operation[i] = "Deposit:"+ val.get(i);
				}
			}
			
			for(int i=0;i<val.size();i++){
				values.add(values.get(values.size()-1)+val.get(i));
				print.add(operation[i]);
			}
			
			numbers.add(bal);
						
			try {
				for(int i=0;i<val.size();i++){
					writer.append(logIndex + " : " + bal.toMsg() + "_" + val+ "_" + operation[i] );
					writer.newLine();	
				}
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void Print(){
		for(int i=0;i<print.size();i++){
			System.out.println(print.get(i)+"\n");
		}
	}
	
	public int Size() {
		return values.size();
	}

	public void SetPath(String path) {
		this.path = path;
	}

	public Double GetValue() {
		if(values.size()<=0){
			return 0.0;
		}
		return values.get(values.size() - 1);
	}
}