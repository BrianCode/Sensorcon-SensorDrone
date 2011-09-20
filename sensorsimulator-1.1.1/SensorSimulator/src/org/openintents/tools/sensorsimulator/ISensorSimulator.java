/**
 *
 */
package org.openintents.tools.sensorsimulator;

import javax.swing.JTextField;

/**
 *
 * @author Lee Sanghoon
 */
public interface ISensorSimulator {

	// Supported sensors:
	static final String TEMPERATURE = "temperature";
	static final String LIGHT = "light";
	static final String PROXIMITY = "proximity";
	static final String CARBON_MONOXIDE = "carbon monoxide";
	static final String RED_GAS = "reducing gases";
	static final String OX_GAS = "oxidizing gases";
	static final String PRESSURE = "pressures";
	static final String HUMIDITY = "humidity";
	static final String INFRARED = "infrared temp.";

	static final String BINARY_PROXIMITY = "binary proximity";
	
	static final String AVERAGE_TEMPERATURE = "average temperature";
	static final String AVERAGE_LIGHT = "average light";
	static final String AVERAGE_PROXIMITY = "average proximity";
	
	static final String AVERAGE_CARBON_MONOXIDE = "average carbon monoxide";
	static final String AVERAGE_RED_GAS = "average reducing gases";
	static final String AVERAGE_OX_GAS = "average oxidizing gases";
	static final String AVERAGE_PRESSURE = "average pressures";
	static final String AVERAGE_HUMIDITY = "average humidity";
	static final String AVERAGE_INFRARED = "average infrared temp.";


	static final String DISABLED = "DISABLED";

	// Constant giving the unicode value of degrees symbol.
	final static public String DEGREES = "\u00B0";
	final static public String MICRO = "\u00b5";
	final static public String PLUSMINUS = "\u00b1";
	final static public String SQUARED = "\u00b2"; // superscript two

    // Action Commands:
    static String move = "move";
    static String timerAction = "timer";
    static String setPortString = "set port";
    //action for telnet connection and send gps
    static String connectViaTelnet = "connectViaTelnet";
    static String recordReplay="replay Record";
    static String playbackReplay="replay Playback";
    static String emulateBattery = "emulate battery";
    static String nextTimeEvent = "next Time Event";


	/**
	 * @param string
	 */
	public void addMessage(String string);

	public int getMouseMode();

	public WiiMoteData getWiiMoteData();

	public void newClient();

	public int getDelay();
	/**
	 * mSensorSimulator.delay = (int) newdelay;
	 * mSensorSimulator.timer.setDelay(mSensorSimulator.delay);
	 *
	 * @param delay
	 */
	public void setDelay(int delay);


	/*
	 *
	 */
	public IMobilePanel getMobilePanel();

	/*
	 *
	 */
	public int getPort();
	public int getTelnetPort();

	/*
	 * Support Sensors
	 */
    public boolean isSupportedTemperature();
    public boolean isSupportedLight();
    public boolean isSupportedProximity();
    public boolean isSupportedCarbonMonoxide();
    public boolean isSupportedRedGas();
    public boolean isSupportedOxGas();
    public boolean isSupportedHumidity();
    public boolean isSupportedPressure();
    public boolean isSupportedInfrared();
    
    /*
     * Enabled Sensors
     */
    public boolean isEnabledTemperature();
    public boolean isEnabledLight();
    public boolean isEnabledProximity();
    public boolean isEnabledCarbonMonoxide();
    public boolean isEnabledRedGas();
    public boolean isEnabledOxGas();
    public boolean isEnabledHumidity();
    public boolean isEnabledPressure();
    public boolean isEnabledInfrared();

    /**
     * mSensorSimulator.setEnabledAccelerometer(enable);
     * mSensorSimulator.mRefreshEmulatorAccelerometerLabel.setText("-");
     *
     * @param enable
     * @return
     */
    public void setEnabledTemperature(boolean enable);
    public void setEnabledLight(boolean enable);
    public void setEnabledProximity(boolean enable);
    public void setEnabledCarbonMonoxide(boolean enable);
    public void setEnabledRedGas(boolean enable);
    public void setEnabledOxGas(boolean enable);
    public void setEnabledHumidity(boolean enable);
    public void setEnabledPressure(boolean enable);
    public void setEnabledInfrared(boolean enable);

    /*
     * Sensor Update Rate
     */
    public double[] getUpdateRatesThermometer();
    public double getDefaultUpdateRateThermometer();
    public double getCurrentUpdateRateThermometer();
    public boolean updateAverageThermometer();
    public double[] getUpdateRatesLight();
    public double getDefaultUpdateRateLight();
    public double getCurrentUpdateRateLight();
    public boolean updateAverageLight();
    public double[] getUpdateRatesProximity();
    public double getDefaultUpdateRateProximity();
    public double getCurrentUpdateRateProximity();
    public boolean updateAverageProximity();
    
    public double getDefaultUpdateRateCarbonMonoxide();
    public double getCurrentUpdateRateCarbonMonoxide();
    public boolean updateAverageCarbonMonoxide();
    
    public double getDefaultUpdateRateRedGas();
    public double getCurrentUpdateRateRedGas();
    public boolean updateAverageRedGas();
    
    public double getDefaultUpdateRateOxGas();
    public double getCurrentUpdateRateOxGas();
    public boolean updateAverageOxGas();
    
    public double getDefaultUpdateRateHumidity();
    public double getCurrentUpdateRateHumidity();
    public boolean updateAverageHumidity();
    
    public double getDefaultUpdateRatePressure();
    public double getCurrentUpdateRatePressure();
    public boolean updateAveragePressure();
    
    public double getDefaultUpdateRateInfrared();
    public double getCurrentUpdateRateInfrared();
    public boolean updateAverageInfrared();
    
    
    public void setCurrentUpdateRateThermometer(double value);
    public void setCurrentUpdateRateLight(double value);
    public void setCurrentUpdateRateProximity(double value);
    
    public void setCurrentUpdateRateCarbonMonoxide(double value);
    public void setCurrentUpdateRateRedGas(double value);
    public void setCurrentUpdateRateOxGas(double value);
    public void setCurrentUpdateRateHumidity(double value);
    public void setCurrentUpdateRatePressure(double value);
    public void setCurrentUpdateRateInfrared(double value);
    
    /*
     * Simulation Update
     */
    public double getUpdateSensors();
    public double getRefreshAfter();

    public void updateSensorRefresh();
	public void updateEmulatorThermometerRefresh();
	public void updateEmulatorLightRefresh();
	public void updateEmulatorProximityRefresh();
	
	public void updateEmulatorCarbonMonoxideRefresh();
	public void updateEmulatorRedGasRefresh();
	public void updateEmulatorOxGasRefresh();
	public void updateEmulatorHumidityRefresh();
	public void updateEmulatorPressureRefresh();
	public void updateEmulatorInfraredRefresh();
	
    /*
     * Temperature
     */
    public double getTemperature();

    /*
     * Light 
     */
    public float getLight();

    /*
     * Proximity
     */
    public float getProximity();
    
    public float getCarbonMonoxide();
    public float getRedGas();
    public float getOxGas();
    public float getHumidity();
    public float getPressure();
    public float getInfrared();
    
    /*
     * Random Component
     */
    public double getRandomTemperature();
    public double getRandomLight();
    public double getRandomProximity();
    
    public double getRandomCarbonMonoxide();
    public double getRandomRedGas();
    public double getRandomOxGas();
    public double getRandomHumidity();
    public double getRandomPressure();
    public double getRandomInfrared();
    
    /*
     * Real Sensor Bridge
     */
	public boolean useRealDeviceThinkpad();
	public boolean useRealDeviceWiimtoe();
	public String getRealDevicePath();
	public void setRealDeviceOutput(String text);



  /**
   * @param textField It can be JTextField (for Swing) or Text (for SWT)
   * @param defaultValue
   * @return
   */
  public double getSafeDouble(JTextField textField, double defaultValue);

  /**
   * @param textField It can be JTextField (for Swing) or Text (for SWT)
   * @return
   */
  public double getSafeDouble(JTextField textField);

  /**
   * @param textField It can be JTextField (for Swing) or Text (for SWT)
   * @return
   */
  public double[] getSafeDoubleList(JTextField textField);

}
