package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import game.alive.Enemy;
import game.alive.Volunteer;
import game.environment.Map;

public class FileOperations implements File_IO {

	@Override
	public Map fileRead(File file) {
		FileReader in;
		BufferedReader reader;
		ArrayList<String> lines;
		try {
			in = new FileReader(file);
			reader = new BufferedReader(in);
			lines = new ArrayList<>();
			String buffer = reader.readLine();
			while (buffer != null) {
				lines.add(buffer);
				buffer = reader.readLine();
			}
			reader.close();
			in.close();
			return map(lines);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean fileWrite(File file, ArrayList<String> log) {
		try {
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			for (String line : log) {
				writer.println(line);
			}
			writer.close();
			return true;
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	private Map map(ArrayList<String> lines) {
		Map map = new Map();
		int hp = 0, attack;
		String[] types = null, hps = null, attacks = null, counts = null, positions;
		boolean hero = true;
		for (String line : lines) {
			if (line.contains("destination")) {
				map.setDistance(Integer.parseInt(line.split("=")[1]));
			} else if (line.contains("hp")) {
				if (hero) {
					hp = Integer.parseInt(line.split("=")[1]);
				} else {
					hps = line.split("=")[1].split(",");
				}
			} else if (line.contains("attack")) {
				if (hero) {
					attack = Integer.parseInt(line.split("=")[1]);
					map.setVolunteer(new Volunteer(attack, hp));
					hero = false;
				} else {
					attacks = line.split("=")[1].split(",");
				}
			} else if (line.contains("type")) {
				types = line.split("=")[1].split(",");
			} else if (line.contains("count")) {
				counts = line.split("=")[1].split(",");
			} else if (line.contains("position")) {
				positions = line.split("=")[1].split(",");
				ArrayList<Enemy> enemies = new ArrayList<>();
				int totalCount = 0;
				for (int i = 0; i < counts.length; i++) {
					for (int j = 0; j < Integer.parseInt(counts[i]); j++) {
						Enemy enemy = new Enemy(Integer.parseInt(attacks[i]), Integer.parseInt(hps[i]),
								Integer.parseInt(positions[j + totalCount]), types[i]);
						enemies.add(enemy);
					}
					totalCount += Integer.parseInt(counts[i]);
				}
				map.setEnemies(enemies);
			}
		}
		return map;
	}

}
