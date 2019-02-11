package game.util;

import java.util.ArrayList;

public class Log {

	private static ArrayList<String> log = new ArrayList<>();

	public static void add(String line) {
		log.add(line);
	}

	public static ArrayList<String> getLog() {
		return log;
	}

	public static void setLog(ArrayList<String> log) {
		Log.log = log;
	}
	
	public static void clearLog(){
		log.clear();
	}

}
