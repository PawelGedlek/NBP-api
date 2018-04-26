package nbpapi;

public class Currency implements Comparable<Currency>{
	private String name;
	private float value;
	public Currency(String name, float value) {
		this.name = name;
		this.value = value;
	}
	@Override
	public String toString() {
		return "Currency: "+ name + ", difference: " + value + "\n";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	@Override
	public int compareTo(Currency curr) {
		// TODO Auto-generated method stub
		int diff = Float.compare(this.value, curr.value);
		return diff;
	}
}
