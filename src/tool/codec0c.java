package tool;

import struct.StructClass;
import struct.StructField;

@StructClass
public class codec0c {
	
	@StructField(order = 0)
	private double altitude;
	
	@StructField(order = 1)
	private double longitude;

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
