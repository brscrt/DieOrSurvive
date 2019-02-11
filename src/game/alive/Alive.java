package game.alive;

public abstract class Alive {

	protected int attackDamage;
	protected int healthPoint;
	protected int position;

	public Alive(int attackDamage, int healthPoint) {
		this.attackDamage = attackDamage;
		this.healthPoint = healthPoint;
	}

	public void walk() {
		position++;
	}

	public abstract void fight(Enemy alive);

	public abstract void die();

	public abstract void kill(Enemy enemy);

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getHealthPoint() {
		return healthPoint;
	}

	public void setHealthPoint(int healthPoint) {
		this.healthPoint = healthPoint;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
