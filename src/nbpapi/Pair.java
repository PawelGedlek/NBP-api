package nbpapi;

public class Pair{
	public float newMax;
	public float newMin;
	
	public Pair(float newMax, float newMin) {
		this.newMax = newMax;
		this.newMin = newMin;
	}
	public float getNewMax() {
		return newMax;
	}
	public void setNewMax(float newMax) {
		this.newMax = newMax;
	}
	public float getNewMin() {
		return newMin;
	}
	public void setNewMin(float newMin) {
		this.newMin = newMin;
	}
	
}
