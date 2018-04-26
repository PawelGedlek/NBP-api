package nbpapi;

public class Amplitude {
	private String name;
	private float minValue;
	private float maxValue;
	public Amplitude() { }
	
	public Amplitude(String name, float minValue, float maxValue) {
		this.name = name;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getMinValue() {
		return minValue;
	}
	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}
	public float getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	@Override
	public String toString() {
		return "Amplitude {name=" + name + ", minValue=" + minValue + ", maxValue=" + maxValue + "]";
	}
}
