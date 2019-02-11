package game.environment;

import java.util.ArrayList;

import game.alive.Enemy;
import game.alive.Volunteer;
import game.util.Log;
import ui.panel.Panel;

public class Map {

	private Volunteer volunteer;
	private ArrayList<Enemy> enemies;
	private int distance;

	public void startJourney(Panel panel) {
		Log.add("Hero started journey with " + volunteer.getHealthPoint() + " HP!");
		sortByPositions();
		int enemyQueue = 0;
		while (volunteer.getPosition() < distance && volunteer.isAlive()) {
			volunteer.walk();
			if (enemyQueue < enemies.size())
				if (volunteer.getPosition() == enemies.get(enemyQueue).getPosition()) {
					volunteer.fight(enemies.get(enemyQueue));
					enemyQueue++;
				}
		}
		if (volunteer.isAlive())
			Log.add("Hero Survived!");

	}

	public void sortByPositions() {
		for (int i = 0; i < enemies.size(); i++) {
			for (int j = 0; j < enemies.size() - 1; j++) {
				if (enemies.get(j).getPosition() > enemies.get(j + 1).getPosition()) {
					Enemy buff = enemies.get(j);
					enemies.set(j, enemies.get(j + 1));
					enemies.set(j + 1, buff);
				}
			}
		}
	}

	public Volunteer getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(Volunteer volunteer) {
		this.volunteer = volunteer;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public void setEnemies(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

}
