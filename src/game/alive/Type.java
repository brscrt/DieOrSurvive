package game.alive;

public enum Type {
	zombie("Zombie"), mutant("Mutant"), zombiedog("Zombiedog"), bug("Bug"), lion("Lion");

	private final String type;

	private Type(String type) {
		this.type = type;
	}

	public boolean equalsName(String type) {
		return this.type.equalsIgnoreCase(type);
	}

	public String toString() {
		return type;
	}

	public static String findType(String type) {
		for (Type ty : Type.values()) {
			if (type.equalsIgnoreCase(ty.type))
				return ty.type;
		}
		return type;
	}
}
