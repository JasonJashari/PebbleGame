
public class Pebble {
	private int weight;
	
	public Pebble(int weight) {
		this.weight = weight;
	}
	
	public int getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return String.valueOf(this.weight);
	}
}
