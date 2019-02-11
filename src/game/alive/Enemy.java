package game.alive;

public class Enemy extends Alive {

	private String type;

	public Enemy(int attackDamage, int healthPoint, int position, String type) {
		super(attackDamage, healthPoint);
		this.position = position;
		this.type=Type.findType(type);
	}

	@Override
	public void fight(Enemy alive) {
		// TODO Auto-generated method stub

	}

	@Override
	public void die() {
		// TODO Auto-generated method stub

	}

	@Override
	public void kill(Enemy enemy) {
		// TODO Auto-generated method stub

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
