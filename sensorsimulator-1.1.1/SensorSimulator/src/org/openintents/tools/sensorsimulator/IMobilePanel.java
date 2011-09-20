/**
 *
 */
package org.openintents.tools.sensorsimulator;

/**
 *
 * @author Lee Sanghoon
 */
public interface IMobilePanel {

	public void doRepaint();

	public void updateSensorPhysics();
	public void updateSensorReadoutValues();
	public void updateUserSettings();

	public double getReadTemperature();

	public float getReadLight();
	
	public float getReadProximity();

	public double getReadCarbonMonoxide();

	public double getReadRedGas();

	public double getReadOxGas();

	public double getReadHumidity();

	public double getReadPressure();

	public double getReadInfrared();

}
