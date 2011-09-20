/*
 * Port of OpenIntents simulator to Android 2.1, extension to multi
 * emulator support, and GPS and battery simulation is developed as a
 * diploma thesis of Josip Balic at the University of Zagreb, Faculty of
 * Electrical Engineering and Computing.
 *
 * Copyright (C) 2008-2010 OpenIntents.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * 09/Apr/08 Dale Thatcher <openintents at dalethatcher dot com>
 *           Added wii-mote data collection.
 */

package org.openintents.tools.sensorsimulator.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.JSlider;

import org.openintents.tools.sensorsimulator.IMobilePanel;
import org.openintents.tools.sensorsimulator.ISensorSimulator;
import org.openintents.tools.sensorsimulator.Vector;

/**
 * Displays a mobile phone in a panel and calculates sensor physics.
 *
 * @author Peli
 * @author Josip Balic
 */
public class MobilePanel extends JPanel implements IMobilePanel {

	private static final long serialVersionUID = -112203026209081563L;

	/**
	 * Reference to SensorSimulator for accessing widgets.
	 */
	private ISensorSimulator mSensorSimulator;

	/**
	 * Current read-out value of accelerometer x-component.
	 *
	 * This value is updated only at the desired
	 * updateSensorRate().
	 */
	/** Current read-out value of temperature. */
	private double read_temperature;

	/**Current read-out value of barcode. */

	/** Current read-out value of light. */
	private float read_light;

	/** Current read-out value of proximity. */
    private float read_proximity;
    
    private double read_carbon_monoxide;
    private double read_red_gas;
    private double read_ox_gas;
    private double read_humidity;
    private double read_pressure;
    private double read_infrared;
    
	/** Duration (in milliseconds) between two updates.
	 * This is the inverse of the update rate.
	 */
	private long temperature_update_duration;

	/** Time of next update required.
	 * The time is compared to System.currentTimeMillis().
	 */
	private long temperature_next_update;

	/**
	 * Duration (in milliseconds) between two updates. This is the inverse of
	 * the update rate.
	 */
	private long light_update_duration;

	/**
	 * Time of next update required. The time is compared to
	 * System.currentTimeMillis().
	 */
	private long light_next_update;

    /**
     * Duration (in milliseconds) between two updates. This is the inverse of
     * the update rate.
     */
    private long proximity_update_duration;

    /**
     * Time of next update required. The time is compared to
     * System.currentTimeMillis().
     */
    private long proximity_next_update;
    
    /**
     * Duration (in milliseconds) between two updates. This is the inverse of
     * the update rate.
     */
    private long carbon_monoxide_update_duration;

    /**
     * Time of next update required. The time is compared to
     * System.currentTimeMillis().
     */
    private long carbon_monoxide_next_update;
    
    /**
     * Duration (in milliseconds) between two updates. This is the inverse of
     * the update rate.
     */
    private long red_gas_update_duration;

    /**
     * Time of next update required. The time is compared to
     * System.currentTimeMillis().
     */
    private long red_gas_next_update;
    
    /**
     * Duration (in milliseconds) between two updates. This is the inverse of
     * the update rate.
     */
    private long ox_gas_update_duration;

    /**
     * Time of next update required. The time is compared to
     * System.currentTimeMillis().
     */
    private long ox_gas_next_update;
    
    /**
     * Duration (in milliseconds) between two updates. This is the inverse of
     * the update rate.
     */
    private long humidity_update_duration;

    /**
     * Time of next update required. The time is compared to
     * System.currentTimeMillis().
     */
    private long humidity_next_update;
    
    /**
     * Duration (in milliseconds) between two updates. This is the inverse of
     * the update rate.
     */
    private long pressure_update_duration;

    /**
     * Time of next update required. The time is compared to
     * System.currentTimeMillis().
     */
    private long pressure_next_update;
    
    /**
     * Duration (in milliseconds) between two updates. This is the inverse of
     * the update rate.
     */
    private long infrared_update_duration;

    /**
     * Time of next update required. The time is compared to
     * System.currentTimeMillis().
     */
    private long infrared_next_update;
    
	/** Duration in milliseconds until user setting
	 * changes are read out.
	 */
	private long user_settings_duration;

	/** Time of next update for reading user settings from widgets.
	 * The time is compared to System.currentTimeMillis().
	 */
	private long user_settings_next_update;

	/** Partial read-out value of temperature. */
	private double partial_temperature;
	/** Number of summands in partial sum for temperature. */
	private int partial_temperature_n;
	/** Whether to form the average over the last duration when reading out sensors.
	 * Alternative is to just take the current value.
	 */
	private boolean average_temperature;

	/** Partial read-out value of light. */
	private float partial_light;
	/** Number of summands in partial sum for light. */
	private int partial_light_n;
	/**
	 * Whether to form the average over the last duration when reading out
	 * sensors. Alternative is to just take the current value.
	 */
	private boolean average_light;

	/** Partial read-out value of proximity. */
    private float partial_proximity;
    /** Number of summands in partial sum for proximity. */
    private int partial_proximity_n;
    /**
     * Whether to form the average over the last duration when reading out
     * sensors. Alternative is to just take the current value.
     */
    private boolean average_proximity;
    
	/** Partial read-out value of carbon_monoxide. */
	private double partial_carbon_monoxide;
	/** Number of summands in partial sum for carbon_monoxide. */
	private int partial_carbon_monoxide_n;
	/** Whether to form the average over the last duration when reading out sensors.
	 * Alternative is to just take the current value.
	 */
	private boolean average_carbon_monoxide;
	
	/** Partial read-out value of red_gas. */
	private double partial_red_gas;
	/** Number of summands in partial sum for red_gas. */
	private int partial_red_gas_n;
	/** Whether to form the average over the last duration when reading out sensors.
	 * Alternative is to just take the current value.
	 */
	private boolean average_red_gas;
	
	/** Partial read-out value of ox_gas. */
	private double partial_ox_gas;
	/** Number of summands in partial sum for ox_gas. */
	private int partial_ox_gas_n;
	/** Whether to form the average over the last duration when reading out sensors.
	 * Alternative is to just take the current value.
	 */
	private boolean average_ox_gas;
	
	/** Partial read-out value of humidity. */
	private double partial_humidity;
	/** Number of summands in partial sum for humidity. */
	private int partial_humidity_n;
	/** Whether to form the average over the last duration when reading out sensors.
	 * Alternative is to just take the current value.
	 */
	private boolean average_humidity;
	
	/** Partial read-out value of pressure. */
	private double partial_pressure;
	/** Number of summands in partial sum for pressure. */
	private int partial_pressure_n;
	/** Whether to form the average over the last duration when reading out sensors.
	 * Alternative is to just take the current value.
	 */
	private boolean average_pressure;
	
	/** Partial read-out value of infrared. */
	private double partial_infrared;
	/** Number of summands in partial sum for infrared. */
	private int partial_infrared_n;
	/** Whether to form the average over the last duration when reading out sensors.
	 * Alternative is to just take the current value.
	 */
	private boolean average_infrared;
    
	// thermometer
	private double temperature;

	// light
	private float light;
    
	// proximity
    private float proximity;
    
    private double carbonMonoxide;
    private double redGas;
    private double oxGas;
    private double humidity;
    private double pressure;
    private double infrared;
    
	/** Gravity constant.
	 *
	 * This takes the value 9.8 m/s^2.
	 * */
	private double g;

	private Random r;


	/*
	 * http://code.google.com/android/reference/android/hardware/Sensors.html
	 *
	 * With the device lying flat on a horizontal surface in front of the user,
	 * oriented so the screen is readable by the user in the normal fashion,
	 * the X axis goes from left to right, the Y axis goes from the user
	 * toward the device, and the Z axis goes upwards perpendicular to the
	 * surface.
	 */
//	// Mobile size
	
	/**
	 * Constructor of MobilePanel.
	 *
	 * @param newSensorSimulator, SensorSimulator that needs MobilePanel in it's frame.
	 */
	public MobilePanel(ISensorSimulator newSensorSimulator) {
		mSensorSimulator = newSensorSimulator;

		g = 9.80665; // meter per second^2

		user_settings_duration = 500;  // Update every half second. This should be enough.
		user_settings_next_update = System.currentTimeMillis(); // First update is now.


		r = new Random();

		//this.setDoubleBuffered(true);

	    addMouseListener(new MouseAdapter() {
	        public void mousePressed(MouseEvent e) {
	            //moveSquare(e.getX(),e.getY());
//	        	mousedownx = e.getX();
//	        	mousedowny = e.getY();
//	        	mousedownyaw = mSensorSimulator.getYaw();
//	        	mousedownpitch = mSensorSimulator.getPitch();
//	        	mousedownroll = mSensorSimulator.getRoll();
//	        	mousedownmovex = movex;
//	        	mousedownmovez = movez;

	        }
	    });

	    addMouseMotionListener(new MouseMotionListener() {
	        public void mouseDragged(MouseEvent e) {
	            //moveSquare(e.getX(),e.getY());
//	        	if (mSensorSimulator.getMouseMode()
//	        			== ISensorSimulator.mouseYawPitch) {
//		        	// Control yawDegree
//		        	int newyaw = mousedownyaw - (e.getX() - mousedownx);
//		        	while (newyaw > 180) newyaw -= 360;
//		        	while (newyaw < -180) newyaw += 360;
//		        	mSensorSimulator.setYaw(newyaw);
//		        	yawDegree = newyaw;
//
//		        	// Control pitch
//		        	int newpitch = mousedownpitch - (e.getY() - mousedowny);
//		        	while (newpitch > 180) newpitch -= 360;
//		        	while (newpitch < -180) newpitch += 360;
//		        	mSensorSimulator.setPitch(newpitch);
//		        	pitchDegree = newpitch;
//	        	} else if (mSensorSimulator.getMouseMode()
//	        			== ISensorSimulator.mouseRollPitch) {
//		        	// Control roll
//		        	int newroll = mousedownroll + (e.getX() - mousedownx);
//		        	while (newroll > 180) newroll -= 360;
//		        	while (newroll < -180) newroll += 360;
//		        	mSensorSimulator.setRoll(newroll);
//		        	rollDegree = newroll;
//
//		        	// Control pitch
//		        	int newpitch = mousedownpitch - (e.getY() - mousedowny);
//		        	while (newpitch > 180) newpitch -= 360;
//		        	while (newpitch < -180) newpitch += 360;
//		        	mSensorSimulator.setPitch(newpitch);
//		        	pitchDegree = newpitch;
//	        	} else if (mSensorSimulator.getMouseMode()
//	        			== ISensorSimulator.mouseMove) {
//		        	// Control roll
//		        	int newmovex = mousedownmovex + (e.getX() - mousedownx);
//		        	movex = newmovex;
//
//		        	// Control pitch
//		        	int newmovez = mousedownmovez - (e.getY() - mousedowny);
//		        	movez = newmovez;
//	        	}
//
//	        	repaint();
	        }

            public void mouseMoved(MouseEvent evt) {
                // NOOP
            }
	    });
	}

	/**
	 * Updates physical model of all sensors by minimum time-step.
	 *
	 * This internal update provides the close-to-continuum
	 * description of the sensors.
	 * It does not yet provide the values that
	 * are read out by the Sensors class (which are
	 * obtained by further time-selection or averaging).
	 */
	public void updateSensorPhysics() {
		Vector vec;
		double random;

		// Update the timer if necessary:
		double newdelay;
		newdelay = mSensorSimulator.getUpdateSensors();
		if (newdelay > 0) {
			mSensorSimulator.setDelay((int) newdelay);
		}

//		g = mSensorSimulator.getGravityConstant();
//		if (g != 0) {
//			ginverse = 1 / g;
//		}
//		meterperpixel = 1 / mSensorSimulator.getPixelsPerMeter();
//		k = mSensorSimulator.getSpringConstant();
//		gamma = mSensorSimulator.getDampingConstant();

	/*
		// Calculate velocity induced by mouse:
		double f = meterperpixel / g;
		vx = f * ((double) (movex - oldx)) / dt;
		vz = f * ((double) (movez - oldz)) / dt;

		// Calculate acceleration induced by mouse:
		ax = (vx - oldvx) / dt;
		az = (vz - oldvz) / dt;
*/
		// New physical model of acceleration:
		// Have accelerometer be steered by string.
		// We will treat this 2D only, as the rest is a linear
		// transformation, and we assume all three accelerometer
		// directions behave the same.

		// F = m * a
		// F = - k * x

		// First calculate the force acting on the
		// sensor test particle, assuming that
		// the accelerometer is mounted by a string:
		// F = - k * x
//		Fx = + k * (movex - accx);
//		Fz = + k * (movez - accz);
//
//		// a = F / m
//		ax = Fx / m;
//		az = Fz / m;

		// Calculate velocity by integrating
		// the current acceleration.
		// Take into account damping
		// by damping constant gamma.
		// integrate dv/dt = a - v*gamma
		//vx += (ax - vx * gamma) * dt;
		//vz += (az - vz * gamma) * dt;

//		vx += (ax) * dt;
//		vz += (az) * dt;

		// Now this is the force that tries to adjust
		// the accelerometer back
		// integrate dx/dt = v;
//		accx += vx * dt;
//		accz += vz * dt;

		// We put damping here: We don't want to damp for
		// zero motion with respect to the background,
		// but with respect to the mobile phone:
//		accx += gamma * (movex - accx) * dt;
//		accz += gamma * (movez - accz) * dt;

		/*
		// Old values:
		oldx = movex;
		oldz = movez;
		oldvx = vx;
		oldvz = vz;
		*/

		// Calculate acceleration by gravity:
//		double gravityax;
//		double gravityay;
//		double gravityaz;
//
//		gravityax = mSensorSimulator.getGravityX();
//		gravityay = mSensorSimulator.getGravityY();
//		gravityaz = mSensorSimulator.getGravityZ();
//

		////
		// Now calculate this into mobile phone acceleration:
		// ! Mobile phone's acceleration is just opposite to
		// lab frame acceleration !
//		vec = new Vector(-ax * meterperpixel + gravityax, gravityay, -az * meterperpixel + gravityaz);
//
//		// we reverse roll, pitch, and yawDegree,
//		// as this is how the mobile phone sees the coordinate system.
//		vec.reverserollpitchyaw(rollDegree, pitchDegree, yawDegree);
//
//		if (mSensorSimulator.isEnabledAccelerometer()) {
//			if (mSensorSimulator.useRealDeviceWiimtoe()) {
//				accelx = mSensorSimulator.getWiiMoteData().getX() * g;
//				accely = mSensorSimulator.getWiiMoteData().getY() * g;
//				accelz = mSensorSimulator.getWiiMoteData().getZ() * g;
//			}
//			else {
//				accelx = vec.x;
//				accely = vec.y;
//				accelz = vec.z;
//
//				if (mSensorSimulator.useRealDeviceThinkpad()) {
//					// We will use data directly from sensor instead:
//
//					// Read data from file
//					String line = "";
//					try {
//					  //FileReader always assumes default encoding is OK!
//					  BufferedReader input =  new BufferedReader(
//							  new FileReader(mSensorSimulator.getRealDevicePath()));
//					  try {
//						  line = input.readLine();
//					  } finally {
//					    input.close();
//					    //mSensorSimulator.mRealDeviceThinkpadOutputLabel.setBackground(Color.WHITE);
//					  }
//					}
//					catch (IOException ex){
//					  ex.printStackTrace();
//					  //mSensorSimulator.mRealDeviceThinkpadOutputLabel.setBackground(Color.RED);
//					  line = "Error reading file!";
//					}
//
//					// Show the line content:
//					mSensorSimulator.setRealDeviceOutput(line);
//
//					// Assign values
//
//					// Create z-component (optional)
//
//				}
//
//				// Add random component:
//				random = mSensorSimulator.getRandomAccelerometer();
//				if (random > 0) {
//					accelx += getRandom(random);
//					accely += getRandom(random);
//					accelz += getRandom(random);
//				}
//
//				// Add accelerometer limit:
//				double limit = g * mSensorSimulator.getAccelerometerLimit();
//				if (limit > 0) {
//					// limit on each component separately, as each is
//					// a separate sensor.
//					if (accelx > limit) accelx = limit;
//					if (accelx < -limit) accelx = -limit;
//					if (accely > limit) accely = limit;
//					if (accely < -limit) accely = -limit;
//					if (accelz > limit) accelz = limit;
//					if (accelz < -limit) accelz = -limit;
//				}
//			}
//		} else {
//			accelx = 0;
//			accely = 0;
//			accelz = 0;
//		}
//
//		// Calculate magnetic field:
//		// Calculate acceleration by gravity:
//		double magneticnorth;
//		double magneticeast;
//		double magneticvertical;
//
//		if (mSensorSimulator.isEnabledMagneticField()) {
//			magneticnorth = mSensorSimulator.getMagneticFieldNorth();
//			magneticeast = mSensorSimulator.getMagneticFieldEast();
//			magneticvertical = mSensorSimulator.getMagneticFieldVertical();
//
//			// Add random component:
//			random = mSensorSimulator.getRandomMagneticField();
//			if (random > 0) {
//				magneticnorth += getRandom(random);
//				magneticeast += getRandom(random);
//				magneticvertical += getRandom(random);
//			}
//
//			// Magnetic vector in phone coordinates:
//			vec = new Vector(magneticeast, magneticnorth, -magneticvertical);
//			vec.scale(0.001); // convert from nT (nano-Tesla) to �T (micro-Tesla)
//
//			// we reverse roll, pitch, and yawDegree,
//			// as this is how the mobile phone sees the coordinate system.
//			vec.reverserollpitchyaw(rollDegree, pitchDegree, yawDegree);
//
//			compassx = vec.x;
//			compassy = vec.y;
//			compassz = vec.z;
//		} else {
//			compassx = 0;
//			compassy = 0;
//			compassz = 0;
//		}
//
//		// Orientation is currently not affected:
//		if (mSensorSimulator.isEnabledOrientation()) {
//			//yaw = Math.toRadians(yawDegree);
//			//pitch = Math.toRadians(pitchDegree);
//			//roll = Math.toRadians(rollDegree);
//			// Since OpenGL uses degree as input,
//			// and it seems also more user-friendly,
//			// let us stick to degree.
//			// (it seems, professional sensors also use
//			//  degree output.)
//			yaw = yawDegree;
//			pitch = pitchDegree;
//			roll = rollDegree;
//
//			// Add random component:
//			random = mSensorSimulator.getRandomOrientation();
//			if (random > 0) {
//				yaw += getRandom(random);
//				pitch += getRandom(random);
//				roll += getRandom(random);
//			}
//		} else {
//			yaw = 0;
//			pitch = 0;
//			roll = 0;
//		}

		// Thermometer
		if (mSensorSimulator.isEnabledTemperature()) {
			temperature = mSensorSimulator.getTemperature();

			// Add random component:
			random = mSensorSimulator.getRandomTemperature();
			if (random > 0) {
				temperature += getRandom(random);
			}
		} else {
			temperature = 0;
		}

		// Light
		if (mSensorSimulator.isEnabledLight()) {
			light = mSensorSimulator.getLight();

			// Add random component:
			random = mSensorSimulator.getRandomLight();
			if (random > 0) {
				light += getRandom(random);
			}
		} else {
			light = 0;
		}
		
	    // Proximity
        if (mSensorSimulator.isEnabledProximity()) {
            proximity = mSensorSimulator.getProximity();

            // Add random component:
            random = mSensorSimulator.getRandomProximity();
            if (random > 0) {
                proximity += getRandom(random);
            }
        } else {
            proximity = 0;
        }
        
	    // Proximity
        if (mSensorSimulator.isEnabledProximity()) {
            proximity = mSensorSimulator.getProximity();

            // Add random component:
            random = mSensorSimulator.getRandomProximity();
            if (random > 0) {
                proximity += getRandom(random);
            }
        } else {
            proximity = 0;
        }
        
	    // CarbonMonoxide
        if (mSensorSimulator.isEnabledCarbonMonoxide()) {
            carbonMonoxide = mSensorSimulator.getCarbonMonoxide();

            // Add random component:
            random = mSensorSimulator.getRandomCarbonMonoxide();
            if (random > 0) {
                carbonMonoxide += getRandom(random);
            }
        } else {
            carbonMonoxide = 0;
        }
        
	    // RedGas
        if (mSensorSimulator.isEnabledRedGas()) {
            redGas = mSensorSimulator.getRedGas();

            // Add random component:
            random = mSensorSimulator.getRandomRedGas();
            if (random > 0) {
                redGas += getRandom(random);
            }
        } else {
            redGas = 0;
        }
        
	    // OxGas
        if (mSensorSimulator.isEnabledOxGas()) {
            oxGas = mSensorSimulator.getOxGas();

            // Add random component:
            random = mSensorSimulator.getRandomOxGas();
            if (random > 0) {
                oxGas += getRandom(random);
            }
        } else {
            oxGas = 0;
        }
        
	    // Humidity
        if (mSensorSimulator.isEnabledHumidity()) {
            humidity = mSensorSimulator.getHumidity();

            // Add random component:
            random = mSensorSimulator.getRandomHumidity();
            if (random > 0) {
                humidity += getRandom(random);
            }
        } else {
            humidity = 0;
        }
        
	    // Pressure
        if (mSensorSimulator.isEnabledPressure()) {
            pressure = mSensorSimulator.getPressure();

            // Add random component:
            random = mSensorSimulator.getRandomPressure();
            if (random > 0) {
                pressure += getRandom(random);
            }
        } else {
            pressure = 0;
        }
        
	    // Infrared
        if (mSensorSimulator.isEnabledInfrared()) {
            infrared = mSensorSimulator.getInfrared();

            // Add random component:
            random = mSensorSimulator.getRandomInfrared();
            if (random > 0) {
                infrared += getRandom(random);
            }
        } else {
            infrared = 0;
        }
}

	/**
	 * Updates sensor values in time intervals as specified by updateSensorRate().
	 */
	public void updateSensorReadoutValues() {
		long currentTime = System.currentTimeMillis();

		// From time to time we
        
		if (average_temperature) {
			// Form the average
			partial_temperature += temperature;
			partial_temperature_n++;
		}

		if (average_light) {
			// Form the average
			partial_light += light;
			partial_light_n++;
		}

	    if (average_proximity) {
	        // Form the average
	        partial_proximity += proximity;
	        partial_proximity_n++;
	    }
	    
	    if (average_carbon_monoxide) {
	        // Form the average
	        partial_carbon_monoxide += carbonMonoxide;
	        partial_carbon_monoxide_n++;
	    }
	    
	    if (average_red_gas) {
	        // Form the average
	        partial_red_gas += redGas;
	        partial_red_gas_n++;
	    }
	    
	    if (average_ox_gas) {
	        // Form the average
	        partial_ox_gas += oxGas;
	        partial_ox_gas_n++;
	    }
	    
	    if (average_humidity) {
	        // Form the average
	        partial_humidity += humidity;
	        partial_humidity_n++;
	    }
	    
	    if (average_pressure) {
	        // Form the average
	        partial_pressure += pressure;
	        partial_pressure_n++;
	    }
	    
	    if (average_infrared) {
	        // Form the average
	        partial_infrared += infrared;
	        partial_infrared_n++;
	    }
	    
		if (currentTime >= temperature_next_update) {
			// Update
			temperature_next_update += temperature_update_duration;
			if (temperature_next_update < currentTime) {
				// Don't lag too much behind.
				// If we are too slow, then we are too slow.
				temperature_next_update = currentTime;
			}

			if (average_temperature) {
				// form average
				read_temperature = partial_temperature / partial_temperature_n;

				// reset average
				partial_temperature = 0;
				partial_temperature_n = 0;

			} else {
				// Only take current value
				read_temperature = temperature;
			}

		}

		if (currentTime >= light_next_update) {
			// Update
			light_next_update += light_update_duration;
			if (light_next_update < currentTime) {
				// Don't lag too much behind.
				// If we are too slow, then we are too slow.
				light_next_update = currentTime;
			}

			if (average_light) {
				// form average
				read_light = partial_light / partial_light_n;

				// reset average
				partial_light = 0;
				partial_light_n = 0;

			} else {
				// Only take current value
				read_light = light;
			}

		}
		
		if (currentTime >= proximity_next_update) {
            // Update
            proximity_next_update += proximity_update_duration;
            if (proximity_next_update < currentTime) {
                // Don't lag too much behind.
                // If we are too slow, then we are too slow.
                proximity_next_update = currentTime;
            }

            if (average_proximity) {
                // form average
                read_proximity = partial_proximity / partial_proximity_n;

                // reset average
                partial_proximity = 0;
                partial_proximity_n = 0;

            } else {
                // Only take current value
                read_proximity = proximity;
            }
        }
		
		if (currentTime >= carbon_monoxide_next_update) {
            // Update
            carbon_monoxide_next_update += carbon_monoxide_update_duration;
            if (carbon_monoxide_next_update < currentTime) {
                // Don't lag too much behind.
                // If we are too slow, then we are too slow.
                carbon_monoxide_next_update = currentTime;
            }

            if (average_carbon_monoxide) {
                // form average
                read_carbon_monoxide = partial_carbon_monoxide / partial_carbon_monoxide_n;

                // reset average
                partial_carbon_monoxide = 0;
                partial_carbon_monoxide_n = 0;

            } else {
                // Only take current value
                read_carbon_monoxide = carbonMonoxide;
            }
        }
		
		if (currentTime >= red_gas_next_update) {
            // Update
            red_gas_next_update += red_gas_update_duration;
            if (red_gas_next_update < currentTime) {
                // Don't lag too much behind.
                // If we are too slow, then we are too slow.
                red_gas_next_update = currentTime;
            }

            if (average_red_gas) {
                // form average
                read_red_gas = partial_red_gas / partial_red_gas_n;

                // reset average
                partial_red_gas = 0;
                partial_red_gas_n = 0;

            } else {
                // Only take current value
                read_red_gas = redGas;
            }
        }
		
		if (currentTime >= ox_gas_next_update) {
            // Update
            ox_gas_next_update += ox_gas_update_duration;
            if (ox_gas_next_update < currentTime) {
                // Don't lag too much behind.
                // If we are too slow, then we are too slow.
                ox_gas_next_update = currentTime;
            }

            if (average_ox_gas) {
                // form average
                read_ox_gas = partial_ox_gas / partial_ox_gas_n;

                // reset average
                partial_ox_gas = 0;
                partial_ox_gas_n = 0;

            } else {
                // Only take current value
                read_ox_gas = oxGas;
            }
        }
		
		if (currentTime >= humidity_next_update) {
            // Update
            humidity_next_update += humidity_update_duration;
            if (humidity_next_update < currentTime) {
                // Don't lag too much behind.
                // If we are too slow, then we are too slow.
                humidity_next_update = currentTime;
            }

            if (average_humidity) {
                // form average
                read_humidity = partial_humidity / partial_humidity_n;

                // reset average
                partial_humidity = 0;
                partial_humidity_n = 0;

            } else {
                // Only take current value
                read_humidity = humidity;
            }
        }
		
		if (currentTime >= pressure_next_update) {
            // Update
            pressure_next_update += pressure_update_duration;
            if (pressure_next_update < currentTime) {
                // Don't lag too much behind.
                // If we are too slow, then we are too slow.
                pressure_next_update = currentTime;
            }

            if (average_pressure) {
                // form average
                read_pressure = partial_pressure / partial_pressure_n;

                // reset average
                partial_pressure = 0;
                partial_pressure_n = 0;

            } else {
                // Only take current value
                read_pressure = pressure;
            }
        }
		
		if (currentTime >= infrared_next_update) {
            // Update
            infrared_next_update += infrared_update_duration;
            if (infrared_next_update < currentTime) {
                // Don't lag too much behind.
                // If we are too slow, then we are too slow.
                infrared_next_update = currentTime;
            }

            if (average_infrared) {
                // form average
                read_infrared = partial_infrared / partial_infrared_n;

                // reset average
                partial_infrared = 0;
                partial_infrared_n = 0;

            } else {
                // Only take current value
                read_infrared = infrared;
            }
        }
	}

	/**
	 * Updates user settings by reading out the widgets.
	 * This is done after some duration
	 * set by user_settings_duration.
	 */
	public void updateUserSettings() {
		long currentTime = System.currentTimeMillis();
		double rate;

		// From time to time we get the user settings:
		if (currentTime >= user_settings_next_update) {
			// Do update
			user_settings_next_update += user_settings_duration;
			if (user_settings_next_update < currentTime) {
				// Skip time if we are already behind:
				user_settings_next_update = System.currentTimeMillis();
			}

			average_temperature = mSensorSimulator.updateAverageThermometer();
			rate = mSensorSimulator.getCurrentUpdateRateThermometer();
			if (rate != 0) {
				temperature_update_duration = (long) (1000. / rate);
			} else {
				temperature_update_duration = 0;
			}

			average_light = mSensorSimulator.updateAverageLight();
			rate = mSensorSimulator.getCurrentUpdateRateLight();
			if (rate != 0) {
				light_update_duration = (long) (1000. / rate);
			} else {
				light_update_duration = 0;
			}
			
	         average_proximity = mSensorSimulator.updateAverageProximity();
	         rate = mSensorSimulator.getCurrentUpdateRateProximity();
	         if (rate != 0) {
	             proximity_update_duration = (long) (1000. / rate);
	         } else {
	             proximity_update_duration = 0;
	         }
	         
	         average_carbon_monoxide = mSensorSimulator.updateAverageCarbonMonoxide();
	         rate = mSensorSimulator.getCurrentUpdateRateCarbonMonoxide();
	         if (rate != 0) {
	             carbon_monoxide_update_duration = (long) (1000. / rate);
	         } else {
	             carbon_monoxide_update_duration = 0;
	         }
	         
	         average_red_gas = mSensorSimulator.updateAverageRedGas();
	         rate = mSensorSimulator.getCurrentUpdateRateRedGas();
	         if (rate != 0) {
	             red_gas_update_duration = (long) (1000. / rate);
	         } else {
	             red_gas_update_duration = 0;
	         }
	         
	         average_ox_gas = mSensorSimulator.updateAverageOxGas();
	         rate = mSensorSimulator.getCurrentUpdateRateOxGas();
	         if (rate != 0) {
	             ox_gas_update_duration = (long) (1000. / rate);
	         } else {
	             ox_gas_update_duration = 0;
	         }
	         
	         average_humidity = mSensorSimulator.updateAverageHumidity();
	         rate = mSensorSimulator.getCurrentUpdateRateHumidity();
	         if (rate != 0) {
	             humidity_update_duration = (long) (1000. / rate);
	         } else {
	             humidity_update_duration = 0;
	         }
	         
	         average_pressure = mSensorSimulator.updateAveragePressure();
	         rate = mSensorSimulator.getCurrentUpdateRatePressure();
	         if (rate != 0) {
	             pressure_update_duration = (long) (1000. / rate);
	         } else {
	             pressure_update_duration = 0;
	         }
	         
	         average_infrared = mSensorSimulator.updateAverageInfrared();
	         rate = mSensorSimulator.getCurrentUpdateRateInfrared();
	         if (rate != 0) {
	             infrared_update_duration = (long) (1000. / rate);
	         } else {
	             infrared_update_duration = 0;
	         }
		}
	}

	/**
	 * get a random number in the range -random to +random
	 *
	 * @param random range of random number
	 * @return random number
	 */
	private double getRandom(double random) {
		double val;
		val = r.nextDouble();
		return (2*val - 1) * random;
	}

	/**
	 * Method that sets size of our Mobile Panel.
	 */
    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }

    /**
     * Draws the phone.
     * @param graphics
     */
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        // g.drawString("This is my custom Panel!",(int)yawDegree,(int)pitch);

        Graphics2D g2 = (Graphics2D) graphics;
        // draw Line2D.Double

        double centerx = 100;
        double centery = 100;
        double centerz = -150;
//        for (int i=0; i<phone.length; i+=2) {
//        	if (i==0) g2.setColor(Color.RED);
//        	if (i==24) g2.setColor(Color.BLUE);
//
//        	Vector v1 = new Vector(phone[i]);
//        	Vector v2 = new Vector(phone[i+1]);
//        	v1.rollpitchyaw(rollDegree, pitchDegree, yawDegree);
//        	v2.rollpitchyaw(rollDegree, pitchDegree, yawDegree);
//            g2.draw(new Line2D.Double(
//            		centerx + (v1.x + movex) * centerz / (centerz - v1.y),
//            		centery - (v1.z + movez) * centerz / (centerz - v1.y),
//            		centerx + (v2.x + movex) * centerz / (centerz - v2.y),
//            		centery - (v2.z + movez) * centerz / (centerz - v2.y)));
//        }
//
//        if (mSensorSimulator.isShowAcceleration()) {
//	        // Now we also draw the acceleration:
//	        g2.setColor(Color.GREEN);
//	    	Vector v1 = new Vector(0,0,0);
//	    	Vector v2 = new Vector(accelx, accely, accelz);
//	    	v2.scale(20 * ginverse);
//	        //Vector v2 = new Vector(1, 0, 0);
//	    	v1.rollpitchyaw(rollDegree, pitchDegree, yawDegree);
//	    	v2.rollpitchyaw(rollDegree, pitchDegree, yawDegree);
//	    	g2.draw(new Line2D.Double(
//	        		centerx + (v1.x + movex) * centerz / (centerz - v1.y),
//	        		centery - (v1.z + movez) * centerz / (centerz - v1.y),
//	        		centerx + (v2.x + movex) * centerz / (centerz - v2.y),
//	        		centery - (v2.z + movez) * centerz / (centerz - v2.y)));
//
//        }
    }

    /* (non-Javadoc)
     * @see org.openintents.tools.sensorsimulator.IMobilePanel#doRepaint()
     */
    public void doRepaint() {
    	repaint();
    }

	/* (non-Javadoc)
	 * @see org.openintents.tools.sensorsimulator.IMobilePanel#getReadAccelerometerX()
	 */
//	public double getReadAccelerometerX() {
//		return read_accelx;
//	}

	/* (non-Javadoc)
	 * @see org.openintents.tools.sensorsimulator.IMobilePanel#getReadAccelerometerY()
	 */
//	public double getReadAccelerometerY() {
//		return read_accely;
//	}

	/* (non-Javadoc)
	 * @see org.openintents.tools.sensorsimulator.IMobilePanel#getReadAccelerometerZ()
	 */
//	public double getReadAccelerometerZ() {
//		return read_accelz;
//	}

	/* (non-Javadoc)
	 * @see org.openintents.tools.sensorsimulator.IMobilePanel#getReadCompassX()
	 */
//	public double getReadCompassX() {
//		return read_compassx;
//	}

	/* (non-Javadoc)
	 * @see org.openintents.tools.sensorsimulator.IMobilePanel#getReadCompassY()
	 */
//	public double getReadCompassY() {
//		return read_compassy;
//	}

	/* (non-Javadoc)
	 * @see org.openintents.tools.sensorsimulator.IMobilePanel#getReadCompassZ()
//	 */
//	public double getReadCompassZ() {
//		return read_compassz;
//	}

	/* (non-Javadoc)
	 * @see org.openintents.tools.sensorsimulator.IMobilePanel#getReadYaw()
	 */
//	public double getReadYaw() {
//		return read_yaw;
//	}

	/* (non-Javadoc)
	 * @see org.openintents.tools.sensorsimulator.IMobilePanel#getReadPitch()
	 */
//	public double getReadPitch() {
//		return read_pitch;
//	}

	/* (non-Javadoc)
	 * @see org.openintents.tools.sensorsimulator.IMobilePanel#getReadRoll()
	 */
//	public double getReadRoll() {
//		return read_roll;
//	}

	/* (non-Javadoc)
	 * @see org.openintents.tools.sensorsimulator.IMobilePanel#getReadTemperature()
	 */
	public double getReadTemperature() {
		return read_temperature;
	}

	/* (non-Javadoc)
	 * @see org.openintents.tools.sensorsimulator.IMobilePanel#getBarcode()
	 */
//	public String getBarcode() {
//		return barcode;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openintents.tools.sensorsimulator.IMobilePanel#getReadLight()
	 */
	public float getReadLight() {
		return read_light;
	}
	
	/*
     * (non-Javadoc)
     * 
     * @see org.openintents.tools.sensorsimulator.IMobilePanel#getReadProximity()
     */
    public float getReadProximity() {
        return read_proximity;
    }

	public double getReadCarbonMonoxide() {
		return read_carbon_monoxide;
	}

	public double getReadRedGas() {
		return read_red_gas;
	}

	public double getReadOxGas() {
		return read_ox_gas;
	}

	public double getReadHumidity() {
		return read_humidity;
	}

	public double getReadPressure() {
		return read_pressure;
	}

	public double getReadInfrared() {
		return read_infrared;
	}

}
