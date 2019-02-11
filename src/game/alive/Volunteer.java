package game.alive;

import game.util.Log;

public class Volunteer extends Alive {

	private boolean alive = true;

	public Volunteer(int attackDamage, int healthPoint) {
		super(attackDamage, healthPoint);
	}

	@Override
	public void fight(Enemy alive) {
		while (healthPoint > 0 && alive.healthPoint > 0) {
			healthPoint -= alive.attackDamage;
			alive.healthPoint -= attackDamage;
		}
		if (alive.healthPoint <= 0)
			kill(alive);
		if (healthPoint <= 0) {
			Log.add(alive.getType() + " defated Hero with " + alive.healthPoint + " HP remaining");
			die();
		}

	}

	@Override
	public void die() {
		Log.add("Hero is Dead!! Last seen at position " + position);
		alive = false;
	}

	@Override
	public void kill(Enemy enemy) {
		if (healthPoint < 0)
			healthPoint = 0;
		Log.add("Hero defeated " + enemy.getType() + " with " + healthPoint + " remaining");
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

}
