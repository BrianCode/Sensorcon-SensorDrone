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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.openintents.tools.sensorsimulator.FileData;
import org.openintents.tools.sensorsimulator.IMobilePanel;
import org.openintents.tools.sensorsimulator.ISensorSimulator;
import org.openintents.tools.sensorsimulator.SensorServer;
import org.openintents.tools.sensorsimulator.TelnetServer;
import org.openintents.tools.sensorsimulator.WiiMoteData;

/**
 * Class of SensorSimulator.
 * 
 * The SensorSimulator is a Java stand-alone application.
 * 
 * It simulates various sensors. An Android application can connect through
 * TCP/IP with the settings shown to the SensorSimulator to simulate
 * accelerometer, compass, orientation sensor, and thermometer.
 * 
 * @author Peli
 * @author Josip Balic
 */
public class SensorSimulator extends JPanel implements ISensorSimulator,
		ActionListener, WindowListener, ChangeListener, ItemListener {

	private static final long serialVersionUID = -587503580193069930L;

	// Simulation delay:
	private int delay;
	private Timer timer;

	// for measuring updates:
	private int updateSensorCount;
	private long updateSensorTime;
	private int updateEmulatorThermometerCount;
	private long updateEmulatorThermometerTime;
	private int updateEmulatorLightCount;
	private long updateEmulatorLightTime;
	private int updateEmulatorProximityCount;
	private long updateEmulatorProximityTime;

	private int updateEmulatorCarbonMonoxideCount;
	private long updateEmulatorCarbonMonoxideTime;
	private int updateEmulatorRedGasCount;
	private long updateEmulatorRedGasTime;
	private int updateEmulatorOxGasCount;
	private long updateEmulatorOxGasTime;
	private int updateEmulatorHumidityCount;
	private long updateEmulatorHumidityTime;
	private int updateEmulatorPressureCount;
	private long updateEmulatorPressureTime;
	private int updateEmulatorInfraredCount;
	private long updateEmulatorInfraredTime;
	private int updateEmulatorRedCount;
	private long updateEmulatorRedTime;
	private int updateEmulatorGreenCount;
	private long updateEmulatorGreenTime;
	private int updateEmulatorBlueCount;
	private long updateEmulatorBlueTime;
	private int updateEmulatorExternalCount;
	private long updateEmulatorExternalTime;

	private int mouseMode;

	// Displays the mobile phone
	private IMobilePanel mobile;

	// Text fields:
	private JTextField socketText;

	// Text fields and button for Telnet socket port
	private JTextField telnetSocketText;

	// Field for socket related output:
	private JScrollPane areaScrollPane;
	private JTextArea ipselectionText;

	// Field for sensor simulator data output:
	private JScrollPane scrollPaneSensorData;
	private JTextArea textAreaSensorData;

	// Settings
	// Supported sensors
	private JCheckBox mSupportedTemperature;
	private JCheckBox mSupportedLight;
	private JCheckBox mSupportedProximity;
	private JCheckBox mSupportedCarbonMonoxide;
	private JCheckBox mSupportedRedGas;
	private JCheckBox mSupportedOxGas;
	private JCheckBox mSupportedHumidity;
	private JCheckBox mSupportedPressure;
	private JCheckBox mSupportedInfrared;

	// Enabled sensors
	private JCheckBox mEnabledTemperature;
	private JCheckBox mEnabledLight;
	private JCheckBox mEnabledProximity;
	private JCheckBox mEnabledCarbonMonoxide;
	private JCheckBox mEnabledRedGas;
	private JCheckBox mEnabledOxGas;
	private JCheckBox mEnabledHumidity;
	private JCheckBox mEnabledPressure;
	private JCheckBox mEnabledInfrared;

	// Simulation update
	private JTextField mUpdateRatesThermometerText;
	private JTextField mDefaultUpdateRateThermometerText;
	private JTextField mCurrentUpdateRateThermometerText;
	/** Whether to form an average at each update */
	private JCheckBox mUpdateAverageThermometer;

	private JTextField mUpdateRatesLightText;
	private JTextField mDefaultUpdateRateLightText;
	private JTextField mCurrentUpdateRateLightText;
	/** Whether to form an average at each update */
	private JCheckBox mUpdateAverageLight;

	private JTextField mUpdateRatesProximityText;
	private JTextField mDefaultUpdateRateProximityText;
	private JTextField mCurrentUpdateRateProximityText;
	/** Whether to form an average at each update */
	private JCheckBox mUpdateAverageProximity;

	private JTextField mUpdateRatesCarbonMonoxideText;
	private JTextField mDefaultUpdateRateCarbonMonoxideText;
	private JTextField mCurrentUpdateRateCarbonMonoxideText;
	/** Whether to form an average at each update */
	private JCheckBox mUpdateAverageCarbonMonoxide;

	private JTextField mUpdateRatesRedGasText;
	private JTextField mDefaultUpdateRateRedGasText;
	private JTextField mCurrentUpdateRateRedGasText;
	/** Whether to form an average at each update */
	private JCheckBox mUpdateAverageRedGas;

	private JTextField mUpdateRatesOxGasText;
	private JTextField mDefaultUpdateRateOxGasText;
	private JTextField mCurrentUpdateRateOxGasText;
	/** Whether to form an average at each update */
	private JCheckBox mUpdateAverageOxGas;

	private JTextField mUpdateRatesHumidityText;
	private JTextField mDefaultUpdateRateHumidityText;
	private JTextField mCurrentUpdateRateHumidityText;
	/** Whether to form an average at each update */
	private JCheckBox mUpdateAverageHumidity;

	private JTextField mUpdateRatesPressureText;
	private JTextField mDefaultUpdateRatePressureText;
	private JTextField mCurrentUpdateRatePressureText;
	/** Whether to form an average at each update */
	private JCheckBox mUpdateAveragePressure;

	private JTextField mUpdateRatesInfraredText;
	private JTextField mDefaultUpdateRateInfraredText;
	private JTextField mCurrentUpdateRateInfraredText;
	/** Whether to form an average at each update */
	private JCheckBox mUpdateAverageInfrared;

	private JTextField mUpdateText;
	private JTextField mRefreshCountText;
	private JLabel mRefreshSensorsLabel;
	private JLabel mRefreshEmulatorThermometerLabel;
	private JLabel mRefreshEmulatorLightLabel;
	private JLabel mRefreshEmulatorProximityLabel;
	private JLabel mRefreshEmulatorCarbonMonoxideLabel;
	private JLabel mRefreshEmulatorRedGasLabel;
	private JLabel mRefreshEmulatorOxGasLabel;
	private JLabel mRefreshEmulatorHumidityLabel;
	private JLabel mRefreshEmulatorPressureLabel;
	private JLabel mRefreshEmulatorInfraredLabel;

	// Temperature
	private JTextField mTemperatureText;

	// Light
	private JTextField mLightText;

	// Proximity
	private JTextField mProximityText;
	private JTextField mProximityRangeText;
	private JCheckBox mBinaryProximity;
	private JRadioButton mProximityNear;
	private JRadioButton mProximityFar;
	private ButtonGroup mProximityButtonGroup;

	private JTextField mCarbonMonoxideText;
	private JTextField mRedGasText;
	private JTextField mOxGasText;
	private JTextField mHumidityText;
	private JTextField mPressureText;
	private JTextField mInfraredText;

	// Random contribution
	private JTextField mRandomTemperatureText;
	private JTextField mRandomLightText;
	private JTextField mRandomProximityText;
	private JTextField mRandomCarbonMonoxideText;
	private JTextField mRandomRedGasText;
	private JTextField mRandomOxGasText;
	private JTextField mRandomHumidityText;
	private JTextField mRandomPressureText;
	private JTextField mRandomInfraredText;

	// Real device bridge
	private JCheckBox mRealDeviceThinkpad;
	private JCheckBox mRealDeviceWiimote;
	private JTextField mRealDevicePath;
	private JLabel mRealDeviceOutputLabel;

	// Replay
	private JButton replayRecord;
	private JButton replayPlayback;

	// TelnetSimulations variables
	private JFileChooser fileChooser;
	private JButton openButton;

	// Server for sending out sensor data
	private SensorServer mSensorServer;
	private int mIncomingConnections;

	// telnet server variable
	private TelnetServer mTelnetServer;

	WiiMoteData wiiMoteData = new WiiMoteData();
	FileData replayData = new FileData();

	public SensorSimulator() {
		// Initialize variables
		mIncomingConnections = 0;

		setLayout(new BorderLayout());

		// /////////////////////////////////////////////////////////////
		// Left panel

		GridBagLayout myGridBagLayout = new GridBagLayout();
		// myGridLayout.
		GridBagConstraints c = new GridBagConstraints();
		JPanel leftPanel = new JPanel(myGridBagLayout);

		JPanel mobilePanel = new JPanel(new BorderLayout());

		// Add the mobile
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		mobile = new MobilePanel(this);
		mobilePanel.add((MobilePanel) mobile);

		leftPanel.add(mobilePanel, c);

		// Add IP address properties:
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		JLabel socketLabel = new JLabel("Socket", JLabel.LEFT);
		leftPanel.add(socketLabel, c);

		c.gridx = 1;
		socketText = new JTextField(5);
		leftPanel.add(socketText, c);

		c.gridx = 2;
		JButton socketButton = new JButton("Set");
		leftPanel.add(socketButton, c);
		socketButton.setActionCommand(setPortString);
		socketButton.addActionListener(this);

		// add telnet JLabel, text field and button
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		JLabel telnetSocketLabel = new JLabel("Telnet socket port", JLabel.LEFT);
		leftPanel.add(telnetSocketLabel, c);

		c.gridx = 1;
		telnetSocketText = new JTextField(5);
		leftPanel.add(telnetSocketText, c);

		c.gridx = 2;
		JButton telnetSocketButton = new JButton("Set");
		leftPanel.add(telnetSocketButton, c);
		telnetSocketButton.setActionCommand(connectViaTelnet);
		telnetSocketButton.addActionListener(this);

		// add scrollPane for info output
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 3;
		ipselectionText = new JTextArea(3, 10);

		areaScrollPane = new JScrollPane(ipselectionText);
		areaScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(250, 80));

		leftPanel.add(areaScrollPane, c);

		// add scrollPane for sensor output
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 3;
		textAreaSensorData = new JTextArea(3, 10);
		scrollPaneSensorData = new JScrollPane(textAreaSensorData);
		scrollPaneSensorData
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneSensorData.setPreferredSize(new Dimension(250, 80));

		leftPanel.add(scrollPaneSensorData, c);

		leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// /////////////////////////////////////////////////////////////
		// Center panel
		JLabel simulatorLabel = new JLabel("OpenIntents Sensor Simulator",
				JLabel.CENTER);
		simulatorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		Font font = new Font("SansSerif", Font.PLAIN, 22);
		simulatorLabel.setFont(font);
		simulatorLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 20, 5));

		// GridBagLayout
		myGridBagLayout = new GridBagLayout();
		// myGridLayout.
		// GridBagConstraints
		c = new GridBagConstraints();
		JPanel centerPanel = new JPanel(myGridBagLayout);

		// Now add a scrollable panel with more controls:
		JPanel settingsPane = new JPanel(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();

		JScrollPane settingsScrollPane = new JScrollPane(settingsPane);
		settingsScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		settingsScrollPane.setPreferredSize(new Dimension(250, 250));

		JLabel settingsLabel = new JLabel("Settings", JLabel.CENTER);
		// settingsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.anchor = GridBagConstraints.NORTHWEST;
		c2.gridwidth = 1;
		c2.gridx = 0;
		c2.gridy = 0;
		settingsPane.add(settingsLabel, c2);

		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		c2.gridy++;
		settingsPane.add(separator, c2);

		// /////////////////////////////
		// Checkbox for sensors
		JPanel supportedSensorsPane = new JPanel();
		supportedSensorsPane.setLayout(new BoxLayout(supportedSensorsPane,
				BoxLayout.PAGE_AXIS));

		supportedSensorsPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Supported sensors"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		mSupportedTemperature = new JCheckBox(TEMPERATURE);
		mSupportedTemperature.setSelected(false);
		mSupportedTemperature.addItemListener(this);
		supportedSensorsPane.add(mSupportedTemperature);

		mSupportedLight = new JCheckBox(LIGHT);
		mSupportedLight.setSelected(false);
		mSupportedLight.addItemListener(this);
		supportedSensorsPane.add(mSupportedLight);

		mSupportedProximity = new JCheckBox(PROXIMITY);
		mSupportedProximity.setSelected(false);
		mSupportedProximity.addItemListener(this);
		supportedSensorsPane.add(mSupportedProximity);

		mSupportedCarbonMonoxide = new JCheckBox(CARBON_MONOXIDE);
		mSupportedCarbonMonoxide.setSelected(false);
		mSupportedCarbonMonoxide.addItemListener(this);
		supportedSensorsPane.add(mSupportedCarbonMonoxide);

		mSupportedRedGas = new JCheckBox(RED_GAS);
		mSupportedRedGas.setSelected(false);
		mSupportedRedGas.addItemListener(this);
		supportedSensorsPane.add(mSupportedRedGas);

		mSupportedOxGas = new JCheckBox(OX_GAS);
		mSupportedOxGas.setSelected(false);
		mSupportedOxGas.addItemListener(this);
		supportedSensorsPane.add(mSupportedOxGas);

		mSupportedHumidity = new JCheckBox(HUMIDITY);
		mSupportedHumidity.setSelected(false);
		mSupportedHumidity.addItemListener(this);
		supportedSensorsPane.add(mSupportedHumidity);

		mSupportedPressure = new JCheckBox(PRESSURE);
		mSupportedPressure.setSelected(false);
		mSupportedPressure.addItemListener(this);
		supportedSensorsPane.add(mSupportedPressure);

		mSupportedInfrared = new JCheckBox(INFRARED);
		mSupportedInfrared.setSelected(false);
		mSupportedInfrared.addItemListener(this);
		supportedSensorsPane.add(mSupportedInfrared);

		c2.gridy++;
		settingsPane.add(supportedSensorsPane, c2);

		// /////////////////////////////
		// Checkbox for sensors
		JPanel enabledSensorsPane = new JPanel();
		enabledSensorsPane.setLayout(new BoxLayout(enabledSensorsPane,
				BoxLayout.PAGE_AXIS));

		enabledSensorsPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Enabled sensors"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		mEnabledTemperature = new JCheckBox(TEMPERATURE);
		mEnabledTemperature.setSelected(false);
		mEnabledTemperature.addItemListener(this);
		enabledSensorsPane.add(mEnabledTemperature);

		mEnabledLight = new JCheckBox(LIGHT);
		mEnabledLight.setSelected(false);
		mEnabledLight.addItemListener(this);
		enabledSensorsPane.add(mEnabledLight);

		mEnabledProximity = new JCheckBox(PROXIMITY);
		mEnabledProximity.setSelected(false);
		mEnabledProximity.addItemListener(this);
		enabledSensorsPane.add(mEnabledProximity);

		mEnabledCarbonMonoxide = new JCheckBox(CARBON_MONOXIDE);
		mEnabledCarbonMonoxide.setSelected(false);
		mEnabledCarbonMonoxide.addItemListener(this);
		enabledSensorsPane.add(mEnabledCarbonMonoxide);

		mEnabledRedGas = new JCheckBox(RED_GAS);
		mEnabledRedGas.setSelected(false);
		mEnabledRedGas.addItemListener(this);
		enabledSensorsPane.add(mEnabledRedGas);

		mEnabledOxGas = new JCheckBox(OX_GAS);
		mEnabledOxGas.setSelected(false);
		mEnabledOxGas.addItemListener(this);
		enabledSensorsPane.add(mEnabledOxGas);

		mEnabledHumidity = new JCheckBox(HUMIDITY);
		mEnabledHumidity.setSelected(false);
		mEnabledHumidity.addItemListener(this);
		enabledSensorsPane.add(mEnabledHumidity);

		mEnabledPressure = new JCheckBox(PRESSURE);
		mEnabledPressure.setSelected(false);
		mEnabledPressure.addItemListener(this);
		enabledSensorsPane.add(mEnabledPressure);

		mEnabledInfrared = new JCheckBox(INFRARED);
		mEnabledInfrared.setSelected(false);
		mEnabledInfrared.addItemListener(this);
		enabledSensorsPane.add(mEnabledInfrared);

		c2.gridy++;
		settingsPane.add(enabledSensorsPane, c2);

		JLabel label;
		GridBagConstraints c3;

		// //////////////////////////////
		// Sensor output update frequency
		// and measure frequency
		// Also update connected sensor frequency.
		JPanel updateFieldPane = new JPanel(new GridBagLayout());
		c3 = new GridBagConstraints();
		c3.fill = GridBagConstraints.HORIZONTAL;
		c3.anchor = GridBagConstraints.NORTHWEST;
		c3.gridwidth = 3;
		c3.gridx = 0;
		c3.gridy = 0;

		updateFieldPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Sensor update rate"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		// ------------------

		mUpdateRatesThermometerText = new JTextField(5);
		mDefaultUpdateRateThermometerText = new JTextField(5);
		mCurrentUpdateRateThermometerText = new JTextField(5);
		mUpdateAverageThermometer = new JCheckBox(AVERAGE_TEMPERATURE);
		AddUpdateRate(updateFieldPane, c3, "Thermometer",
				mUpdateRatesThermometerText, "0.1, 1",
				mDefaultUpdateRateThermometerText, "1",
				mCurrentUpdateRateThermometerText, "1",
				mUpdateAverageThermometer);

		mUpdateRatesLightText = new JTextField(5);
		mDefaultUpdateRateLightText = new JTextField(5);
		mCurrentUpdateRateLightText = new JTextField(5);
		mUpdateAverageLight = new JCheckBox(AVERAGE_LIGHT);
		AddUpdateRate(updateFieldPane, c3, "Light", mUpdateRatesLightText, "1",
				mDefaultUpdateRateLightText, "1", mCurrentUpdateRateLightText,
				"1", mUpdateAverageLight);

		mUpdateRatesProximityText = new JTextField(5);
		mDefaultUpdateRateProximityText = new JTextField(5);
		mCurrentUpdateRateProximityText = new JTextField(5);
		mUpdateAverageProximity = new JCheckBox(AVERAGE_PROXIMITY);
		AddUpdateRate(updateFieldPane, c3, "Proximity",
				mUpdateRatesProximityText, "1",
				mDefaultUpdateRateProximityText, "1",
				mCurrentUpdateRateProximityText, "1", mUpdateAverageProximity);

		mUpdateRatesCarbonMonoxideText = new JTextField(5);
		mDefaultUpdateRateCarbonMonoxideText = new JTextField(5);
		mCurrentUpdateRateCarbonMonoxideText = new JTextField(5);
		mUpdateAverageCarbonMonoxide = new JCheckBox(AVERAGE_CARBON_MONOXIDE);
		AddUpdateRate(updateFieldPane, c3, "CarbonMonoxide",
				mUpdateRatesCarbonMonoxideText, "1",
				mDefaultUpdateRateCarbonMonoxideText, "1",
				mCurrentUpdateRateCarbonMonoxideText, "1",
				mUpdateAverageCarbonMonoxide);

		mUpdateRatesRedGasText = new JTextField(5);
		mDefaultUpdateRateRedGasText = new JTextField(5);
		mCurrentUpdateRateRedGasText = new JTextField(5);
		mUpdateAverageRedGas = new JCheckBox(AVERAGE_RED_GAS);
		AddUpdateRate(updateFieldPane, c3, "RedGas", mUpdateRatesRedGasText,
				"1", mDefaultUpdateRateRedGasText, "1",
				mCurrentUpdateRateRedGasText, "1", mUpdateAverageRedGas);

		mUpdateRatesOxGasText = new JTextField(5);
		mDefaultUpdateRateOxGasText = new JTextField(5);
		mCurrentUpdateRateOxGasText = new JTextField(5);
		mUpdateAverageOxGas = new JCheckBox(AVERAGE_OX_GAS);
		AddUpdateRate(updateFieldPane, c3, "OxGas", mUpdateRatesOxGasText, "1",
				mDefaultUpdateRateOxGasText, "1", mCurrentUpdateRateOxGasText,
				"1", mUpdateAverageOxGas);

		mUpdateRatesHumidityText = new JTextField(5);
		mDefaultUpdateRateHumidityText = new JTextField(5);
		mCurrentUpdateRateHumidityText = new JTextField(5);
		mUpdateAverageHumidity = new JCheckBox(AVERAGE_HUMIDITY);
		AddUpdateRate(updateFieldPane, c3, "Humidity",
				mUpdateRatesHumidityText, "1", mDefaultUpdateRateHumidityText,
				"1", mCurrentUpdateRateHumidityText, "1",
				mUpdateAverageHumidity);

		mUpdateRatesPressureText = new JTextField(5);
		mDefaultUpdateRatePressureText = new JTextField(5);
		mCurrentUpdateRatePressureText = new JTextField(5);
		mUpdateAveragePressure = new JCheckBox(AVERAGE_PRESSURE);
		AddUpdateRate(updateFieldPane, c3, "Pressure",
				mUpdateRatesPressureText, "1", mDefaultUpdateRatePressureText,
				"1", mCurrentUpdateRatePressureText, "1",
				mUpdateAveragePressure);

		mUpdateRatesInfraredText = new JTextField(5);
		mDefaultUpdateRateInfraredText = new JTextField(5);
		mCurrentUpdateRateInfraredText = new JTextField(5);
		mUpdateAverageInfrared = new JCheckBox(AVERAGE_INFRARED);
		AddUpdateRate(updateFieldPane, c3, "Infrared",
				mUpdateRatesInfraredText, "1", mDefaultUpdateRateInfraredText,
				"1", mCurrentUpdateRateInfraredText, "1",
				mUpdateAverageInfrared);

		// Update panel ends

		// Add update panel to settings
		c2.gridx = 0;
		c2.gridwidth = 1;
		c2.gridy++;
		settingsPane.add(updateFieldPane, c2);

		// //////////////////////////////
		// Sensor output update frequency
		// and measure frequency
		// Also update connected sensor frequency.
		JPanel updateSimulationFieldPane = new JPanel(new GridBagLayout());
		c3 = new GridBagConstraints();
		c3.fill = GridBagConstraints.HORIZONTAL;
		c3.anchor = GridBagConstraints.NORTHWEST;
		c3.gridwidth = 3;
		c3.gridx = 0;
		c3.gridy = 0;

		updateSimulationFieldPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Simulation update"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		// ---------------------
		label = new JLabel("Update sensors: ", JLabel.LEFT);
		c3.gridwidth = 1;
		c3.gridx = 0;
		c3.gridy++;
		updateSimulationFieldPane.add(label, c3);

		mUpdateText = new JTextField(5);
		mUpdateText.setText("10");
		c3.gridx = 1;
		updateSimulationFieldPane.add(mUpdateText, c3);

		label = new JLabel(" ms", JLabel.LEFT);
		c3.gridx = 2;
		updateSimulationFieldPane.add(label, c3);

		label = new JLabel("Refresh after: ", JLabel.LEFT);
		c3.gridwidth = 1;
		c3.gridx = 0;
		c3.gridy++;
		updateSimulationFieldPane.add(label, c3);

		mRefreshCountText = new JTextField(5);
		mRefreshCountText.setText("10");
		c3.gridx = 1;
		updateSimulationFieldPane.add(mRefreshCountText, c3);

		label = new JLabel(" times", JLabel.LEFT);
		c3.gridx = 2;
		updateSimulationFieldPane.add(label, c3);

		label = new JLabel("Sensor update: ", JLabel.LEFT);
		c3.gridwidth = 1;
		c3.gridx = 0;
		c3.gridy++;
		updateSimulationFieldPane.add(label, c3);

		mRefreshSensorsLabel = new JLabel("0", JLabel.LEFT);
		c3.gridx = 1;
		updateSimulationFieldPane.add(mRefreshSensorsLabel, c3);

		label = new JLabel("Emulator update: ", JLabel.LEFT);
		c3.gridwidth = 1;
		c3.gridx = 0;
		c3.gridy++;
		updateSimulationFieldPane.add(label, c3);

		mRefreshEmulatorThermometerLabel = new JLabel("-", JLabel.LEFT);
		AddSimulationUpdate(updateSimulationFieldPane, c3, " * Thermometer: ",
				mRefreshEmulatorThermometerLabel);

		mRefreshEmulatorLightLabel = new JLabel("-", JLabel.LEFT);
		AddSimulationUpdate(updateSimulationFieldPane, c3, " * Light: ",
				mRefreshEmulatorLightLabel);

		mRefreshEmulatorProximityLabel = new JLabel("-", JLabel.LEFT);
		AddSimulationUpdate(updateSimulationFieldPane, c3, " * Proximity: ",
				mRefreshEmulatorProximityLabel);

		mRefreshEmulatorCarbonMonoxideLabel = new JLabel("-", JLabel.LEFT);
		AddSimulationUpdate(updateSimulationFieldPane, c3,
				" * Carbon Monoxide: ", mRefreshEmulatorCarbonMonoxideLabel);
		mRefreshEmulatorRedGasLabel = new JLabel("-", JLabel.LEFT);
		AddSimulationUpdate(updateSimulationFieldPane, c3, " * Reducing Gas: ",
				mRefreshEmulatorRedGasLabel);
		mRefreshEmulatorOxGasLabel = new JLabel("-", JLabel.LEFT);
		AddSimulationUpdate(updateSimulationFieldPane, c3,
				" * Oxidizing Gas: ", mRefreshEmulatorOxGasLabel);
		mRefreshEmulatorHumidityLabel = new JLabel("-", JLabel.LEFT);
		AddSimulationUpdate(updateSimulationFieldPane, c3, " * Humidity: ",
				mRefreshEmulatorHumidityLabel);
		mRefreshEmulatorPressureLabel = new JLabel("-", JLabel.LEFT);
		AddSimulationUpdate(updateSimulationFieldPane, c3, " * Pressure: ",
				mRefreshEmulatorPressureLabel);
		mRefreshEmulatorInfraredLabel = new JLabel("-", JLabel.LEFT);
		AddSimulationUpdate(updateSimulationFieldPane, c3,
				" * Infrared Temp.: ", mRefreshEmulatorInfraredLabel);

		// Update panel ends

		// Add update panel to settings
		c2.gridx = 0;
		c2.gridwidth = 1;
		c2.gridy++;
		settingsPane.add(updateSimulationFieldPane, c2);

		// //////////////////////////////
		// Temperature (in �C: Centigrade Celsius)

		mTemperatureText = new JTextField(5);
		addSimpleSensorValue(settingsPane, c2, "Temperature", "17.7",
				mTemperatureText, " " + DEGREES + "C");

		// ////////////////////////////
		// Light (in lux)

		mLightText = new JTextField(5);
		addSimpleSensorValue(settingsPane, c2, "Light", "400", mLightText,
				" lux");

		/*
		 * Settings for the proximity in centimetres: Value FAR corresponds to
		 * the maximum value of the proximity. Value NEAR corresponds to any
		 * value less than FAR.
		 */
		JPanel proximityFieldPane = new JPanel(new GridBagLayout());
		c3 = new GridBagConstraints();
		c3.fill = GridBagConstraints.HORIZONTAL;
		c3.anchor = GridBagConstraints.NORTHWEST;
		c3.gridwidth = 3;
		c3.gridx = 0;
		c3.gridy = 0;

		proximityFieldPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Proximity"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		label = new JLabel("Proximity: ", JLabel.LEFT);
		c3.gridwidth = 1;
		c3.gridx = 0;
		c3.gridy++;
		proximityFieldPane.add(label, c3);

		mProximityText = new JTextField(5);
		mProximityText.setText("10");
		c3.gridx = 1;
		mProximityText.setEnabled(false);
		proximityFieldPane.add(mProximityText, c3);

		label = new JLabel(" cm", JLabel.LEFT);
		c3.gridx = 2;
		proximityFieldPane.add(label, c3);

		label = new JLabel("Maximum range: ", JLabel.LEFT);
		c3.gridwidth = 1;
		c3.gridx = 0;
		c3.gridy++;
		proximityFieldPane.add(label, c3);

		mProximityRangeText = new JTextField(5);
		mProximityRangeText.setText("10");

		/*
		 * On key press, update the proximity text to reflect the value of the
		 * maximum range if the FAR option is selected, otherwise set the
		 * proximity to any random number less than the maximum range.
		 */
		mProximityRangeText.getDocument().addDocumentListener(
				new DocumentListener() {

					public void changedUpdate(DocumentEvent arg0) {
						updateProximityText();
					}

					public void insertUpdate(DocumentEvent arg0) {
						updateProximityText();
					}

					public void removeUpdate(DocumentEvent arg0) {
						updateProximityText();
					}

					public void updateProximityText() {
						if (mProximityFar.isSelected()) {
							mProximityText.setText(mProximityRangeText
									.getText());
						} else {
							Random r = new Random();
							int currentMaximumRange = Integer
									.parseInt(mProximityRangeText.getText());
							int reduction = r.nextInt(currentMaximumRange);
							int randomNearProximity = currentMaximumRange
									- reduction;
							mProximityText.setText(Integer
									.toString(randomNearProximity));
						}
					}

				});

		c3.gridx = 1;
		proximityFieldPane.add(mProximityRangeText, c3);

		label = new JLabel(" cm", JLabel.LEFT);
		c3.gridx = 2;
		proximityFieldPane.add(label, c3);

		mBinaryProximity = new JCheckBox(BINARY_PROXIMITY);
		mBinaryProximity.setSelected(true);
		mBinaryProximity.addItemListener(this);
		c3.gridwidth = 1;
		c3.gridx = 0;
		c3.gridy++;
		proximityFieldPane.add(mBinaryProximity, c3);

		mProximityNear = new JRadioButton("NEAR", false);
		mProximityFar = new JRadioButton("FAR", true);
		mProximityButtonGroup = new ButtonGroup();
		mProximityButtonGroup.add(mProximityFar);
		mProximityButtonGroup.add(mProximityNear);

		ActionListener proximityOptions = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String selectedOption = mProximityButtonGroup.getSelection()
						.getActionCommand();
				if (selectedOption.equalsIgnoreCase("FAR")) {
					mProximityText.setText(mProximityRangeText.getText());
				} else if (selectedOption.equalsIgnoreCase("NEAR")) {
					Random r = new Random();
					int currentMaximumRange = Integer
							.parseInt(mProximityRangeText.getText());
					int reduction = r.nextInt(currentMaximumRange);
					int randomNearProximity = currentMaximumRange - reduction;
					mProximityText.setText(Integer
							.toString(randomNearProximity));
				}
			}
		};

		mProximityNear.setActionCommand("NEAR");
		mProximityFar.setActionCommand("FAR");

		mProximityNear.addActionListener(proximityOptions);
		mProximityFar.addActionListener(proximityOptions);

		c3.gridwidth = 1;
		c3.gridx = 0;
		c3.gridy++;

		proximityFieldPane.add(mProximityNear, c3);
		c3.gridx++;
		proximityFieldPane.add(mProximityFar, c3);

		// Proximity panel ends

		// Add proximity panel to settings
		c2.gridx = 0;
		c2.gridwidth = 1;
		c2.gridy++;
		settingsPane.add(proximityFieldPane, c2);

		// ////////////////////////////
		// Carbon Monoxide

		mCarbonMonoxideText = new JTextField(5);
		addSimpleSensorValue(settingsPane, c2, "Carbon Monoxide", "100",
				mCarbonMonoxideText, " ppm");

		// ////////////////////////////
		// Reducing Gases
		
		mRedGasText = new JTextField(5);
		addSimpleSensorValue(settingsPane, c2, "Reducing Gases", "100",
				mRedGasText, " ppm");

		// ////////////////////////////
		// Oxidizing Gases
		mOxGasText = new JTextField(5);
		addSimpleSensorValue(settingsPane, c2, "Oxidizing Gases", "100",
				mOxGasText, " ppm");

		// ////////////////////////////
		// Humidity
		mHumidityText = new JTextField(5);
		addSimpleSensorValue(settingsPane, c2, "Humidity", "100",
				mHumidityText, " ppm");

		// ////////////////////////////
		// Pressure
		mPressureText = new JTextField(5);
		addSimpleSensorValue(settingsPane, c2, "Pressure", "100",
				mPressureText, " kPa");
		
		// ////////////////////////////
		// Infrared

		mInfraredText = new JTextField(5);
		addSimpleSensorValue(settingsPane, c2, "Infrared Temp.", "100",
				mInfraredText, " " + DEGREES + "C");

		// /////////////////////////////
		// Random contribution to sensor values

		JPanel randomFieldPane = new JPanel(new GridBagLayout());
		c3 = new GridBagConstraints();
		c3.fill = GridBagConstraints.HORIZONTAL;
		c3.anchor = GridBagConstraints.NORTHWEST;
		c3.gridwidth = 3;
		c3.gridx = 0;
		c3.gridy = 0;

		randomFieldPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Random component"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		mRandomTemperatureText = new JTextField(5);
		addSimpleRandomValue(randomFieldPane, c3, "Temperature", "0",
				mRandomTemperatureText, " " + DEGREES + "C");
		
		mRandomLightText = new JTextField(5);
		addSimpleRandomValue(randomFieldPane, c3, "Light", "0",
				mRandomLightText, " lux");
		
		mRandomProximityText = new JTextField(5);
		addSimpleRandomValue(randomFieldPane, c3, "Proximity", "0",
				mRandomProximityText, " cm");
		
		mRandomCarbonMonoxideText = new JTextField(5);
		addSimpleRandomValue(randomFieldPane, c3, "Carbon Monoxide", "0",
				mRandomCarbonMonoxideText, " ppm");
		
		mRandomRedGasText = new JTextField(5);
		addSimpleRandomValue(randomFieldPane, c3, "Reducing Gases", "0",
				mRandomRedGasText, " ppm");
		
		mRandomOxGasText = new JTextField(5);
		addSimpleRandomValue(randomFieldPane, c3, "Oxidizing Gases", "0",
				mRandomOxGasText, " ppm");
		
		mRandomHumidityText = new JTextField(5);
		addSimpleRandomValue(randomFieldPane, c3, "Humidity", "0",
				mRandomHumidityText, " ppm");
		
		mRandomPressureText = new JTextField(5);
		addSimpleRandomValue(randomFieldPane, c3, "Pressure", "0",
				mRandomPressureText, " kPa");
		
		mRandomInfraredText = new JTextField(5);
		addSimpleRandomValue(randomFieldPane, c3, "Infrared Temp.", "0",
				mRandomInfraredText, " " + DEGREES + "C");
		
		// Random field panel ends

		// Add random field panel to settings
		c2.gridx = 0;
		c2.gridwidth = 1;
		c2.gridy++;
		settingsPane.add(randomFieldPane, c2);

		// ///////////////////////////////////////////////////

		// /////////////////////////////
		// Real sensor bridge

//		JPanel realSensorBridgeFieldPane = new JPanel(new GridBagLayout());
//		c3 = new GridBagConstraints();
//		c3.fill = GridBagConstraints.HORIZONTAL;
//		c3.anchor = GridBagConstraints.NORTHWEST;
//		c3.gridwidth = 3;
//		c3.gridx = 0;
//		c3.gridy = 0;
//
//		realSensorBridgeFieldPane.setBorder(BorderFactory.createCompoundBorder(
//				BorderFactory.createTitledBorder("Real sensor bridge"),
//				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
//
//		mRealDeviceThinkpad = new JCheckBox("Use Thinkpad accelerometer");
//		mRealDeviceThinkpad.setSelected(false);
//		mRealDeviceThinkpad.addItemListener(this);
//		c3.gridwidth = 1;
//		c3.gridx = 0;
//		realSensorBridgeFieldPane.add(mRealDeviceThinkpad, c3);
//
//		mRealDeviceWiimote = new JCheckBox("Use Wii-mote accelerometer");
//		mRealDeviceWiimote.setSelected(false);
//		mRealDeviceWiimote.addItemListener(this);
//		c3.gridy++;
//		realSensorBridgeFieldPane.add(mRealDeviceWiimote, c3);
//
//		mRealDevicePath = new JTextField(20);
//		mRealDevicePath.setText("/sys/devices/platform/hdaps/position");
//		c3.gridx = 0;
//		c3.gridy++;
//		realSensorBridgeFieldPane.add(mRealDevicePath, c3);
//
//		mRealDeviceOutputLabel = new JLabel("-", JLabel.LEFT);
//		c3.gridx = 0;
//		c3.gridy++;
//		realSensorBridgeFieldPane.add(mRealDeviceOutputLabel, c3);

		// Real sensor bridge ends

		// Add real sensor bridge field panel to settings
//		c2.gridx = 0;
//		c2.gridwidth = 1;
//		c2.gridy++;
//		settingsPane.add(realSensorBridgeFieldPane, c2);

		// ///////////////////////////////////////////////////

		// /////////////////////////////
		// Replay Pane

		JPanel replayFieldPane = new JPanel(new GridBagLayout());
		c3 = new GridBagConstraints();
		c3.fill = GridBagConstraints.HORIZONTAL;
		c3.anchor = GridBagConstraints.NORTHWEST;
		c3.gridwidth = 3;
		c3.gridx = 0;
		c3.gridy = 0;

		replayFieldPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Replay"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		replayRecord = new JButton("Record");
		replayFieldPane.add(replayRecord);
		replayRecord.setActionCommand(recordReplay);
		replayRecord.addActionListener(this);

		replayPlayback = new JButton("Playback");
		replayFieldPane.add(replayPlayback);
		replayPlayback.setActionCommand(playbackReplay);
		replayPlayback.addActionListener(this);

		// Replay panel ends

		// Add replay panel to settings
		c2.gridx = 0;
		c2.gridwidth = 1;
		c2.gridy++;
		settingsPane.add(replayFieldPane, c2);

		// ///////////////////////////////////////////////////

		// ///////////////////////////////////////////////////
		// Add settings scroll panel to right pane.
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy++;
		centerPanel.add(settingsScrollPane, c);

		// We put both into a Split pane:
		// Create a split pane with the two scroll panes in it.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				leftPanel, centerPanel);
		splitPane.setDividerLocation(271);

		// add such splitPane to Frame
		add(splitPane, BorderLayout.CENTER);

		// /////////////////////////////////////////////////////
		// // Right Panel.
		// //Create right Panel with BorderLayout
		// JPanel rightPanel = new JPanel(new BorderLayout());
		// JLabel telnetLabel = new JLabel("Telnet simulations", JLabel.CENTER);
		// telnetLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		// Font labelFont = new Font("SansSerif", Font.PLAIN, 22);
		// telnetLabel.setFont(labelFont);
		// telnetLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 20, 5));
		// rightPanel.add(telnetLabel, BorderLayout.NORTH);
		//
		// //Create new Panel with BorderLayout
		// JPanel telnetSimulationsPanel = new JPanel(new BorderLayout());
		//
		// JPanel batteryCapacityPanel = new JPanel(new BorderLayout());
		//
		// JLabel batteryLabel = new JLabel("Battery", JLabel.CENTER);
		// batteryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		//
		// batterySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		// batterySlider.addChangeListener(this);
		//
		// batterySlider.setMajorTickSpacing(10);
		// batterySlider.setMinorTickSpacing(5);
		// batterySlider.setPaintTicks(true);
		// batterySlider.setPaintLabels(true);
		// batterySlider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
		// batteryCapacityPanel.add(batteryLabel, BorderLayout.LINE_START);
		// batteryCapacityPanel.add(batterySlider, BorderLayout.CENTER);
		//
		// //add batteryCapacityPanel to telnetSimulationsPanel
		// telnetSimulationsPanel.add(batteryCapacityPanel, BorderLayout.NORTH);
		//
		// // Now add a scrollable panel with more controls and GridBagLayout
		// JPanel telnetSettingsPanel = new JPanel(new GridBagLayout());
		// //define GridBagConstraints
		// c2 = new GridBagConstraints();
		//
		// //Create ScrollPane for simulations through telnet connection
		// JScrollPane telnetSettingsScrollPane = new
		// JScrollPane(telnetSettingsPanel);
		// telnetSettingsScrollPane.setVerticalScrollBarPolicy(
		// JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// telnetSettingsScrollPane.setPreferredSize(new Dimension(300, 250));
		//
		// JLabel telnetSettingsLabel = new JLabel("Telnet Settings",
		// JLabel.CENTER);
		// c2.fill = GridBagConstraints.HORIZONTAL;
		// c2.anchor = GridBagConstraints.NORTHWEST;
		// c2.gridwidth = 1;
		// c2.gridx = 0;
		// c2.gridy = 0;
		// telnetSettingsPanel.add(telnetSettingsLabel, c2);
		//
		// JSeparator telnetSeparator = new
		// JSeparator(SwingConstants.HORIZONTAL);
		// c2.gridy++;
		// telnetSettingsPanel.add(telnetSeparator, c2);
		//
		// //Now add neccesary things for battery simulation
		// JPanel batteryPanel = new JPanel();
		// batteryPanel.setLayout(new BoxLayout(batteryPanel,
		// BoxLayout.PAGE_AXIS));
		//
		// batteryPanel.setBorder(BorderFactory.createCompoundBorder(
		// BorderFactory.createTitledBorder("Battery"),
		// BorderFactory.createEmptyBorder(5,5,5,5)));
		//
		// batteryPresence = new JCheckBox("Is Present");
		// batteryPresence.setSelected(true);
		// batteryPresence.addItemListener(this);
		// batteryPanel.add(batteryPresence);
		//
		// batteryAC = new JCheckBox("AC plugged");
		// batteryAC.setSelected(true);
		// batteryAC.addItemListener(this);
		// batteryPanel.add(batteryAC);
		//
		// c2.gridy++;
		// telnetSettingsPanel.add(batteryPanel,c2);
		//
		// //Now add neccesary things for battery simulation
		// JPanel batteryStatusPanel = new JPanel();
		// batteryStatusPanel.setLayout(new BoxLayout(batteryStatusPanel,
		// BoxLayout.PAGE_AXIS));
		//
		// batteryStatusPanel.setBorder(BorderFactory.createCompoundBorder(
		// BorderFactory.createTitledBorder("Battery Status"),
		// BorderFactory.createEmptyBorder(5,5,5,5)));
		//
		// String[] batteryStatus = { "unknown", "charging", "discharging",
		// "not-charging", "full" };
		// //Create the combo box, select item at index 4.
		// batteryStatusList = new JComboBox(batteryStatus);
		// batteryStatusList.setSelectedIndex(4);
		// batteryStatusList.addActionListener(this);
		// batteryStatusPanel.add(batteryStatusList);
		//
		// c2.gridy++;
		// telnetSettingsPanel.add(batteryStatusPanel,c2);
		//
		// //Now add neccesary things for battery simulation
		// JPanel batteryHealthPanel = new JPanel();
		// batteryHealthPanel.setLayout(new BoxLayout(batteryHealthPanel,
		// BoxLayout.PAGE_AXIS));
		//
		// batteryHealthPanel.setBorder(BorderFactory.createCompoundBorder(
		// BorderFactory.createTitledBorder("Battery Health"),
		// BorderFactory.createEmptyBorder(5,5,5,5)));
		//
		// String[] batteryHealth = { "unknown", "good", "overheat", "dead",
		// "overvoltage", "failure" };
		// //Create the combo box, select item at index 4.
		// batteryHealthList = new JComboBox(batteryHealth);
		// batteryHealthList.setSelectedIndex(2);
		// batteryHealthList.addActionListener(this);
		// batteryHealthPanel.add(batteryHealthList);
		//
		// c2.gridy++;
		// telnetSettingsPanel.add(batteryHealthPanel,c2);
		//
		// //Now add neccesary things for battery simulation
		// JPanel batteryFilePanel = new JPanel(new BorderLayout());
		//
		// batteryFilePanel.setBorder(BorderFactory.createCompoundBorder(
		// BorderFactory.createTitledBorder("Battery File"),
		// BorderFactory.createEmptyBorder(5,5,5,5)));
		//
		// //create everything need for battery emulation from file
		// fileChooser = new JFileChooser();
		// openButton = new JButton("Open a File");
		// openButton.addActionListener(this);
		// batteryFilePanel.add(openButton, BorderLayout.PAGE_START);
		//
		// batteryEmulation = new JButton("Emulate Battery");
		// batteryFilePanel.add(batteryEmulation, BorderLayout.WEST);
		// batteryEmulation.setActionCommand(emulateBattery);
		// batteryEmulation.addActionListener(this);
		//
		// batteryNext = new JButton("Next time event");
		// batteryFilePanel.add(batteryNext, BorderLayout.EAST);
		// batteryNext.setActionCommand(nextTimeEvent);
		// batteryNext.addActionListener(this);
		//
		// c2.gridy++;
		// telnetSettingsPanel.add(batteryFilePanel,c2);
		//
		// //add neccesary things for GPS emulation
		// JPanel gpsPanel = new JPanel(new GridBagLayout());
		// c3 = new GridBagConstraints();
		// c3.fill = GridBagConstraints.HORIZONTAL;
		// c3.anchor = GridBagConstraints.NORTHWEST;
		// c3.gridwidth = 2;
		// c3.gridx = 0;
		// c3.gridy = 0;
		//
		// gpsPanel.setBorder(BorderFactory.createCompoundBorder(
		// BorderFactory.createTitledBorder("GPS"),
		// BorderFactory.createEmptyBorder(5,5,5,5)));
		//
		// JLabel gpsLongitudeLabel = new JLabel("GPS Longitude: ",
		// JLabel.LEFT);
		// c3.gridwidth = 1;
		// c3.gridx = 0;
		// c3.gridy++;
		// gpsPanel.add(gpsLongitudeLabel, c3);
		//
		// gpsLongitudeText = new JTextField(10);
		// c3.gridx = 1;
		// gpsPanel.add(gpsLongitudeText, c3);
		//
		// JLabel gpsLongitudeUnitLabel = new JLabel("degress", JLabel.LEFT);
		// c3.gridx = 2;
		// gpsPanel.add(gpsLongitudeUnitLabel, c3);
		//
		// JLabel gpsLatitudeLabel = new JLabel("GPS Latitude: ", JLabel.LEFT);
		// c3.gridwidth = 1;
		// c3.gridx = 0;
		// c3.gridy++;
		// gpsPanel.add(gpsLatitudeLabel, c3);
		//
		// gpsLatitudeText = new JTextField(10);
		// c3.gridx = 1;
		// gpsPanel.add(gpsLatitudeText, c3);
		//
		// JLabel gpsLatitudeUnitLabel = new JLabel("degress", JLabel.LEFT);
		// c3.gridx = 2;
		// gpsPanel.add(gpsLatitudeUnitLabel, c3);
		//
		// JLabel gpsAltitudeLabel = new JLabel("GPS Altitude: ", JLabel.LEFT);
		// c3.gridwidth = 1;
		// c3.gridx = 0;
		// c3.gridy++;
		// gpsPanel.add(gpsAltitudeLabel, c3);
		//
		// gpsAltitudeText = new JTextField(10);
		// c3.gridx = 1;
		// gpsPanel.add(gpsAltitudeText, c3);
		//
		// JLabel gpsAltitudeUnitLabel = new JLabel("meters", JLabel.LEFT);
		// c3.gridx = 2;
		// gpsPanel.add(gpsAltitudeUnitLabel, c3);
		//
		// JLabel gpsLisNameLabel = new JLabel("LIS name: ", JLabel.LEFT);
		// c3.gridwidth = 1;
		// c3.gridx = 0;
		// c3.gridy++;
		// gpsPanel.add(gpsLisNameLabel, c3);
		//
		// lisName = new JTextField(10);
		// c3.gridx = 1;
		// gpsPanel.add(lisName, c3);
		//
		// gpsButton = new JButton("Send GPS");
		// c3.gridwidth = 1;
		// c3.gridx = 0;
		// c3.gridy++;
		//
		// gpsPanel.add(gpsButton, c3);
		// gpsButton.setActionCommand(sendGPS);
		// gpsButton.addActionListener(this);
		//
		// // Add gpsPanel to telnetSettingsPanel
		// c2.gridx = 0;
		// c2.gridwidth = 1;
		// c2.gridy++;
		// telnetSettingsPanel.add(gpsPanel, c2);
		//
		// //add telnetSettingsScrollPane to telnetSimulationsPanel
		// telnetSimulationsPanel.add(telnetSettingsScrollPane,
		// BorderLayout.CENTER);
		//
		// //add telnetSimulationsPanel to rightPanel
		// rightPanel.add(telnetSimulationsPanel, BorderLayout.CENTER);
		//
		//
		// /////////////////////////////////////////////////////
		// //add panels to GUI and use layout
		// // We put previous splitPane and our new JPanel into new SplitPanel
		// JSplitPane splitPanel2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		// splitPane, rightPanel);
		// splitPanel2.setDividerLocation(600);
		//
		// //add such splitPanel to the JFrame and place in CENTER
		// add(splitPanel2, BorderLayout.CENTER);

		// ///////////////////////////////////////////////////
		// Fill the possible values

		socketText.setText("8010");
		telnetSocketText.setText("5554");

		ipselectionText.append("Write emulator command port and\n"
				+ "click on set to create connection.\n");

		ipselectionText.append("Possible IP addresses:\n");
		try {
			Enumeration<NetworkInterface> nets = NetworkInterface
					.getNetworkInterfaces();
			for (NetworkInterface netint : Collections.list(nets)) {
				Enumeration<InetAddress> inetAddresses = netint
						.getInetAddresses();
				for (InetAddress inetAddress : Collections.list(inetAddresses)) {
					if (("" + inetAddress).compareTo("/127.0.0.1") != 0) {
						ipselectionText.append("" + inetAddress + "\n");
					}
				}

			}
		} catch (SocketException e) {
			ipselectionText
					.append("Socket exception. Could not obtain IP addresses.");
		}

		// Set up the server:
		mSensorServer = new SensorServer(this);

		// Variables for timing:
		updateSensorCount = 0;
		updateSensorTime = System.currentTimeMillis();
		// updateEmulatorAccelerometerCount = 0;
		// updateEmulatorAccelerometerTime = System.currentTimeMillis();

		// Set up a timer that calls this object's action handler.
		delay = 500;
		timer = new Timer(delay, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SensorSimulator.this.actionPerformed(new ActionEvent(evt
						.getSource(), evt.getID(), timerAction));
			}
		});
		// timer.setInitialDelay(delay * 7); //We pause animation twice per
		// cycle
		// by restarting the timer
		timer.setCoalesce(true);

		timer.start();

	}

	private void addSimpleRandomValue(JPanel fieldPane, GridBagConstraints c3, String name,
			String value, JTextField textField, String units) {
		
		JLabel label = new JLabel(name + ": ", JLabel.LEFT);
		c3.gridwidth = 1;
		c3.gridx = 0;
		c3.gridy++;
		fieldPane.add(label, c3);
		
		textField.setText(value);
		c3.gridx = 1;
		fieldPane.add(textField, c3);
		
		label = new JLabel(" " + units, JLabel.LEFT);
		c3.gridx = 2;
		fieldPane.add(label, c3);
				
	}

	private void addSimpleSensorValue(JPanel settingsPane,
			GridBagConstraints c2, String name, String defaultValue,
			JTextField textField, String defaultUnits) {

		GridBagConstraints c3;
		JPanel fieldPane = new JPanel(new GridBagLayout());
		c3 = new GridBagConstraints();
		c3.fill = GridBagConstraints.HORIZONTAL;
		c3.anchor = GridBagConstraints.NORTHWEST;
		c3.gridwidth = 3;
		c3.gridx = 0;
		c3.gridy = 0;

		fieldPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(name),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		JLabel label = new JLabel(name + ": ", JLabel.LEFT);
		c3.gridwidth = 1;
		c3.gridx = 0;
		c3.gridy++;
		fieldPane.add(label, c3);

		textField.setText(defaultValue);
		c3.gridx = 1;
		fieldPane.add(textField, c3);

		label = new JLabel(defaultUnits, JLabel.LEFT);
		c3.gridx = 2;
		fieldPane.add(label, c3);

		// Add panel to settings
		c2.gridx = 0;
		c2.gridwidth = 1;
		c2.gridy++;
		settingsPane.add(fieldPane, c2);

	}

	private void AddSimulationUpdate(JPanel updateSimulationFieldPane,
			GridBagConstraints c3, String name, JLabel mRefreshEmulatorLabel) {

		JLabel label;
		label = new JLabel(name, JLabel.LEFT);
		c3.gridwidth = 1;
		c3.gridx = 0;
		c3.gridy++;
		updateSimulationFieldPane.add(label, c3);

		c3.gridx = 1;
		updateSimulationFieldPane.add(mRefreshEmulatorLabel, c3);

	}

	private void AddUpdateRate(JPanel updateFieldPane, GridBagConstraints c3,
			String name, JTextField updateRates, String updateRatesString,
			JTextField defaultUpdateRate, String defaultRateString,
			JTextField currentUpdateRate, String currentUpdateRateString,
			JCheckBox averageCheckBox) {

		JLabel label;
		label = new JLabel(name, JLabel.LEFT);
		c3.gridwidth = 1;
		c3.gridx = 0;
		c3.gridy++;
		updateFieldPane.add(label, c3);

		label = new JLabel("Update rates: ", JLabel.LEFT);
		c3.gridwidth = 1;
		c3.gridx = 0;
		c3.gridy++;
		updateFieldPane.add(label, c3);

		updateRates.setText(updateRatesString);
		c3.gridx = 1;
		updateFieldPane.add(updateRates, c3);

		label = new JLabel("1/s", JLabel.LEFT);
		c3.gridx = 2;
		updateFieldPane.add(label, c3);

		label = new JLabel("Default rate: ", JLabel.LEFT);
		c3.gridwidth = 1;
		c3.gridx = 0;
		c3.gridy++;
		updateFieldPane.add(label, c3);

		defaultUpdateRate.setText(defaultRateString);
		c3.gridx = 1;
		updateFieldPane.add(defaultUpdateRate, c3);

		label = new JLabel("1/s", JLabel.LEFT);
		c3.gridx = 2;
		updateFieldPane.add(label, c3);

		label = new JLabel("Current rate: ", JLabel.LEFT);
		c3.gridwidth = 1;
		c3.gridx = 0;
		c3.gridy++;
		updateFieldPane.add(label, c3);

		currentUpdateRate.setText(currentUpdateRateString);
		c3.gridx = 1;
		updateFieldPane.add(currentUpdateRate, c3);

		label = new JLabel("1/s", JLabel.LEFT);
		c3.gridx = 2;
		updateFieldPane.add(label, c3);

		c3.gridwidth = 3;
		c3.gridx = 0;
		c3.gridy++;
		averageCheckBox.setSelected(true);
		averageCheckBox.addItemListener(this);
		updateFieldPane.add(averageCheckBox, c3);

		c3.gridy++;
		updateFieldPane.add(new JSeparator(SwingConstants.HORIZONTAL), c3);

	}

	/** Add a listener for window events. */
	void addWindowListener(Window w) {
		w.addWindowListener(this);
	}

	// React to window events.
	public void windowIconified(WindowEvent e) {
		timer.stop();
	}

	public void windowDeiconified(WindowEvent e) {
		timer.start();
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	/**
	 * Listener for checkbox events.
	 */
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();

		// Don't allow the thinkpad and wiimote to be selected at the same time
		if (source == mRealDeviceThinkpad
				&& e.getStateChange() == ItemEvent.SELECTED) {
			mRealDeviceWiimote.setSelected(false);
		} else if (source == mRealDeviceWiimote
				&& e.getStateChange() == ItemEvent.SELECTED) {
			mRealDeviceThinkpad.setSelected(false);
		}

		// if (source == mShowAcceleration) {
		// // Refresh the screen when this drawing element
		// // changes
		// mobile.doRepaint();
		// }

		if (source == mBinaryProximity) {
			mProximityText.setText(mProximityRangeText.getText());
			if (e.getStateChange() == ItemEvent.SELECTED) {
				mProximityText.setEnabled(false);
				mProximityRangeText.setEnabled(true);
				mProximityNear.setEnabled(true);
				mProximityNear.setSelected(true);
				mProximityFar.setEnabled(true);
				mProximityFar.setSelected(true);
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				mProximityText.setEnabled(true);
				mProximityRangeText.setEnabled(false);
				mProximityNear.setEnabled(false);
				mProximityFar.setEnabled(false);
			}
		}

	}

	// Called when the Timer fires, or selection is done
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals(move)) {
			// mouseMode = mouseMove;
		} else if (action.equals(timerAction)) {
			doTimer();
		} else if (action.equals(setPortString)) {
			setPort();
		} else if (action.equals(connectViaTelnet)) {
			connectViaTelnet();
		} else if (action.equals(emulateBattery)) {
			if (mTelnetServer != null) {
				mTelnetServer.slowEmulation();
			}
		} else if (action.equals(nextTimeEvent)) {
			if (mTelnetServer != null) {
				mTelnetServer.nextTimeEvent();
			}
		} else if (e.getSource() == openButton) {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (mTelnetServer != null) {
					mTelnetServer.openFile(file);
				} else {
					this.addMessage("Connect with emulator to start simulation");
				}
			} else {

			}

		} else if (action.equals(recordReplay)) {
			if (replayData.createFile()) {
				this.addMessage("Recording Started");
				replayRecord.setText("Stop");

			} else {
				this.addMessage("Recording Stopped");
				replayRecord.setText("Record");
			}

		} else if (action.equals(playbackReplay)) {
			if (replayData.openFile()) {
				if (replayData.isPlaying()) {
					this.addMessage("Playing back");
					replayPlayback.setText("Stop");
				} else {
					this.addMessage("Playback Stopped");
				}
			} else {
				this.addMessage("Finish recording first");
			}
		}
	}

	private void doTimer() {
//		if (mRealDeviceWiimote.isSelected()) {
//			updateFromWiimote();
//		}
//
//		updateFromFile();

		// Update sensors:
		mobile.updateSensorPhysics();

		mobile.updateSensorReadoutValues();

		mobile.updateUserSettings();

		// Measure refresh
		updateSensorRefresh();

		// Now show updated data:
		// System.out.println("DATA:"+mobile.getReadAccelerometerX()+"	"+mobile.getReadAccelerometerY()+" "+mobile.getReadAccelerometerZ());
		showSensorData();
	}

	/**
	 * Updates the information about sensor updates.
	 */
	public void updateSensorRefresh() {
		updateSensorCount++;
		long maxcount = (long) getSafeDouble(mRefreshCountText);
		if (maxcount >= 0 && updateSensorCount >= maxcount) {
			long newtime = System.currentTimeMillis();
			double ms = (double) (newtime - updateSensorTime)
					/ ((double) maxcount);

			DecimalFormat mf = new DecimalFormat("#0.0");

			mRefreshSensorsLabel.setText(mf.format(ms) + " ms");

			updateSensorCount = 0;
			updateSensorTime = newtime;
		}
	}

	/**
	 * Updates information about emulator updates.
	 */
	public void updateEmulatorThermometerRefresh() {
		updateEmulatorThermometerCount++;
		long maxcount = (long) getSafeDouble(mRefreshCountText);
		if (maxcount >= 0 && updateEmulatorThermometerCount >= maxcount) {
			long newtime = System.currentTimeMillis();
			double ms = (double) (newtime - updateEmulatorThermometerTime)
					/ ((double) maxcount);

			DecimalFormat mf = new DecimalFormat("#0.0");

			mRefreshEmulatorThermometerLabel.setText(mf.format(ms) + " ms");

			updateEmulatorThermometerCount = 0;
			updateEmulatorThermometerTime = newtime;
		}
	}

	public void updateEmulatorLightRefresh() {
		updateEmulatorLightCount++;
		long maxcount = (long) getSafeDouble(mRefreshCountText);
		if (maxcount >= 0 && updateEmulatorLightCount >= maxcount) {
			long newtime = System.currentTimeMillis();
			double ms = (double) (newtime - updateEmulatorLightTime)
					/ ((double) maxcount);

			DecimalFormat mf = new DecimalFormat("#0.0");

			mRefreshEmulatorLightLabel.setText(mf.format(ms) + " ms");

			updateEmulatorLightCount = 0;
			updateEmulatorLightTime = newtime;
		}
	}

	public void updateEmulatorProximityRefresh() {
		updateEmulatorProximityCount++;
		long maxcount = (long) getSafeDouble(mRefreshCountText);
		if (maxcount >= 0 && updateEmulatorProximityCount >= maxcount) {
			long newtime = System.currentTimeMillis();
			double ms = (double) (newtime - updateEmulatorProximityTime)
					/ ((double) maxcount);

			DecimalFormat mf = new DecimalFormat("#0.0");

			mRefreshEmulatorProximityLabel.setText(mf.format(ms) + " ms");

			updateEmulatorProximityCount = 0;
			updateEmulatorProximityTime = newtime;
		}
	}
	
	public void updateEmulatorCarbonMonoxideRefresh() {
		updateEmulatorCarbonMonoxideCount++;
		long maxcount = (long) getSafeDouble(mRefreshCountText);
		if (maxcount >= 0 && updateEmulatorCarbonMonoxideCount >= maxcount) {
			long newtime = System.currentTimeMillis();
			double ms = (double) (newtime - updateEmulatorCarbonMonoxideTime)
					/ ((double) maxcount);

			DecimalFormat mf = new DecimalFormat("#0.0");

			mRefreshEmulatorCarbonMonoxideLabel.setText(mf.format(ms) + " ms");

			updateEmulatorCarbonMonoxideCount = 0;
			updateEmulatorCarbonMonoxideTime = newtime;
		}
	}
	
	public void updateEmulatorRedGasRefresh() {
		updateEmulatorRedGasCount++;
		long maxcount = (long) getSafeDouble(mRefreshCountText);
		if (maxcount >= 0 && updateEmulatorRedGasCount >= maxcount) {
			long newtime = System.currentTimeMillis();
			double ms = (double) (newtime - updateEmulatorRedGasTime)
					/ ((double) maxcount);

			DecimalFormat mf = new DecimalFormat("#0.0");

			mRefreshEmulatorRedGasLabel.setText(mf.format(ms) + " ms");

			updateEmulatorRedGasCount = 0;
			updateEmulatorRedGasTime = newtime;
		}
	}
	
	public void updateEmulatorOxGasRefresh() {
		updateEmulatorOxGasCount++;
		long maxcount = (long) getSafeDouble(mRefreshCountText);
		if (maxcount >= 0 && updateEmulatorOxGasCount >= maxcount) {
			long newtime = System.currentTimeMillis();
			double ms = (double) (newtime - updateEmulatorOxGasTime)
					/ ((double) maxcount);

			DecimalFormat mf = new DecimalFormat("#0.0");

			mRefreshEmulatorOxGasLabel.setText(mf.format(ms) + " ms");

			updateEmulatorOxGasCount = 0;
			updateEmulatorOxGasTime = newtime;
		}
	}
	
	public void updateEmulatorHumidityRefresh() {
		updateEmulatorHumidityCount++;
		long maxcount = (long) getSafeDouble(mRefreshCountText);
		if (maxcount >= 0 && updateEmulatorHumidityCount >= maxcount) {
			long newtime = System.currentTimeMillis();
			double ms = (double) (newtime - updateEmulatorHumidityTime)
					/ ((double) maxcount);

			DecimalFormat mf = new DecimalFormat("#0.0");

			mRefreshEmulatorHumidityLabel.setText(mf.format(ms) + " ms");

			updateEmulatorHumidityCount = 0;
			updateEmulatorHumidityTime = newtime;
		}
	}
	
	public void updateEmulatorPressureRefresh() {
		updateEmulatorPressureCount++;
		long maxcount = (long) getSafeDouble(mRefreshCountText);
		if (maxcount >= 0 && updateEmulatorPressureCount >= maxcount) {
			long newtime = System.currentTimeMillis();
			double ms = (double) (newtime - updateEmulatorPressureTime)
					/ ((double) maxcount);

			DecimalFormat mf = new DecimalFormat("#0.0");

			mRefreshEmulatorPressureLabel.setText(mf.format(ms) + " ms");

			updateEmulatorPressureCount = 0;
			updateEmulatorPressureTime = newtime;
		}
	}
	
	public void updateEmulatorInfraredRefresh() {
		updateEmulatorInfraredCount++;
		long maxcount = (long) getSafeDouble(mRefreshCountText);
		if (maxcount >= 0 && updateEmulatorInfraredCount >= maxcount) {
			long newtime = System.currentTimeMillis();
			double ms = (double) (newtime - updateEmulatorInfraredTime)
					/ ((double) maxcount);

			DecimalFormat mf = new DecimalFormat("#0.0");

			mRefreshEmulatorInfraredLabel.setText(mf.format(ms) + " ms");

			updateEmulatorInfraredCount = 0;
			updateEmulatorInfraredTime = newtime;
		}
	}
	

	/**
	 * This method is used to show currently enabled sensor values in info pane.
	 */
	private void showSensorData() {
		DecimalFormat mf = new DecimalFormat("#0.00");

		String data = "";

		if (mSupportedTemperature.isSelected()) {
			data += TEMPERATURE + ": ";
			if (mEnabledTemperature.isSelected()) {
				data += mf.format(mobile.getReadTemperature());
			} else {
				data += DISABLED;
			}
			data += "\n";
		}

		if (mSupportedLight.isSelected()) {
			data += LIGHT + ": ";
			if (mEnabledLight.isSelected()) {
				data += mf.format(mobile.getReadLight());
			} else {
				data += DISABLED;
			}
			data += "\n";
		}

		if (mSupportedProximity.isSelected()) {
			data += PROXIMITY + ": ";
			if (mEnabledProximity.isSelected()) {
				data += mf.format(mobile.getReadProximity());
			} else {
				data += DISABLED;
			}
			data += "\n";
		}
		
		if (mSupportedCarbonMonoxide.isSelected()) {
			data += CARBON_MONOXIDE + ": ";
			if (mEnabledCarbonMonoxide.isSelected()) {
				data += mf.format(mobile.getReadCarbonMonoxide());
			} else {
				data += DISABLED;
			}
			data += "\n";
		}
		
		if (mSupportedRedGas.isSelected()) {
			data += RED_GAS + ": ";
			if (mEnabledRedGas.isSelected()) {
				data += mf.format(mobile.getReadRedGas());
			} else {
				data += DISABLED;
			}
			data += "\n";
		}
		
		if (mSupportedOxGas.isSelected()) {
			data += OX_GAS + ": ";
			if (mEnabledOxGas.isSelected()) {
				data += mf.format(mobile.getReadOxGas());
			} else {
				data += DISABLED;
			}
			data += "\n";
		}
		
		if (mSupportedHumidity.isSelected()) {
			data += HUMIDITY + ": ";
			if (mEnabledHumidity.isSelected()) {
				data += mf.format(mobile.getReadHumidity());
			} else {
				data += DISABLED;
			}
			data += "\n";
		}
		
		if (mSupportedPressure.isSelected()) {
			data += PRESSURE + ": ";
			if (mEnabledPressure.isSelected()) {
				data += mf.format(mobile.getReadPressure());
			} else {
				data += DISABLED;
			}
			data += "\n";
		}
		
		if (mSupportedInfrared.isSelected()) {
			data += INFRARED + ": ";
			if (mEnabledInfrared.isSelected()) {
				data += mf.format(mobile.getReadInfrared());
			} else {
				data += DISABLED;
			}
			data += "\n";
		}
		
		// Output to textArea:
		textAreaSensorData.setText(data);
	}

	/**
	 * Sets the socket port for listening
	 */
	private void setPort() {
		addMessage("Closing port " + mSensorServer.port);
		// First close all old ports:
		mSensorServer.stop();

		// now restart
		mSensorServer = new SensorServer(this);
	}

	/**
	 * Connect via telnet with emulator
	 */
	private void connectViaTelnet() {
		if (mTelnetServer == null) {
			mTelnetServer = new TelnetServer(this);
			mTelnetServer.connect();
		} else {
			addMessage("Closing telnet port " + mTelnetServer.port);
			mTelnetServer.disconnect();
			mTelnetServer = new TelnetServer(this);
			mTelnetServer.connect();
		}
	}

	/**
	 * Once sendGps is called, simulator sends geo fix command to emulator via
	 * telnet.
	 */
	private void sendGps() {
		if (mTelnetServer != null) {
			// mTelnetServer.sendGPS();
		}
	}

	/**
	 * Adds new message to message box. If scroll position is at end, it will
	 * scroll to new message.
	 * 
	 * @param msg
	 *            Message.
	 */
	public void addMessage(String msg) {

		// Determine whether the scrollbar is currently at the very bottom
		// position.
		JScrollBar vbar = areaScrollPane.getVerticalScrollBar();
		final int tolerance = 10; // some tolerance value
		boolean autoScroll = ((vbar.getValue() + vbar.getVisibleAmount() + tolerance) >= vbar
				.getMaximum());

		// append to the JTextArea (that's wrapped in a JScrollPane named
		// 'scrollPane'
		ipselectionText.append(msg + "\n");

		// now scroll if we were already at the bottom.
		if (autoScroll)
			ipselectionText.setCaretPosition(ipselectionText.getDocument()
					.getLength());

	}

	/**
	 * Get socket port number.
	 * 
	 * @return String containing port number.
	 */
	public int getPort() {
		String s = socketText.getText();
		int port = 0;
		try {
			port = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			addMessage("Invalid port number: " + s);
		}
		return port;
	}

	/**
	 * Get telnet socket port number.
	 * 
	 * @return String containing port number.
	 */
	public int getTelnetPort() {
		String s = telnetSocketText.getText();
		int port = 0;
		try {
			port = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			addMessage("Invalid port number: " + s);
		}
		return port;
	}

	/**
	 * This method is called by SensorServerThread when a new client connects.
	 */
	public void newClient() {
		mIncomingConnections++;
		if (mIncomingConnections <= 1) {
			// We have been connected for the first time.
			// Disable all sensors:
			// mEnabledAccelerometer.setSelected(false);
			// mEnabledMagneticField.setSelected(false);
			// mEnabledOrientation.setSelected(false);
			mEnabledTemperature.setSelected(false);
			// mEnabledBarcodeReader.setSelected(false);

			addMessage("First incoming connection:");
			addMessage("ALL SENSORS DISABLED!");
		}
	}

	/**
	 * Safely retries the double value of a text field. If the value is not a
	 * valid number, 0 is returned, and the field is marked red.
	 * 
	 * @param textfield
	 *            Textfield from which the value should be read.
	 * @param defaultValue
	 *            default value if input field is invalid.
	 * @return double value.
	 */
	public double getSafeDouble(JTextField textfield, double defaultValue) {
		double value = defaultValue;

		try {
			value = Double.parseDouble(textfield.getText());
			textfield.setBackground(Color.WHITE);
		} catch (NumberFormatException e) {
			// wrong user input in box - take default values.
			value = defaultValue;
			textfield.setBackground(Color.RED);
		}
		return value;
	}

	public double getSafeDouble(JTextField textfield) {
		return getSafeDouble(textfield, 0);
	}

	/**
	 * Safely retries the float value of a text field. If the value is not a
	 * valid number, 0 is returned, and the field is marked red.
	 * 
	 * @param textfield
	 *            Textfield from which the value should be read.
	 * @param defaultValue
	 *            default value if input field is invalid.
	 * @return float value.
	 */
	public float getSafeFloat(JTextField textfield, float defaultValue) {
		float value = defaultValue;
		try {
			value = Float.parseFloat(textfield.getText());
			textfield.setBackground(Color.WHITE);
		} catch (NumberFormatException e) {
			// wrong user input in box - take default values.
			value = defaultValue;
			textfield.setBackground(Color.RED);
		}
		return value;
	}

	public float getSafeFloat(JTextField textfield) {
		return getSafeFloat(textfield, 0);
	}

	/**
	 * Safely retries the a list of double values of a text field. If the list
	 * contains errors, null is returned, and the field is marked red.
	 * 
	 * @param textfield
	 *            Textfield from which the value should be read.
	 * @return list double[] with values or null.
	 */
	public double[] getSafeDoubleList(JTextField textfield) {
		double[] valuelist = null;

		try {
			String t = textfield.getText();
			// Now we have to split this into pieces
			String[] tlist = t.split(",");
			int len = tlist.length;
			if (len > 0) {
				valuelist = new double[len];
				for (int i = 0; i < len; i++) {
					valuelist[i] = Double.parseDouble(tlist[i]);
				}
			} else {
				valuelist = null;
			}
			textfield.setBackground(Color.WHITE);
		} catch (NumberFormatException e) {
			// wrong user input in box - take default values.
			valuelist = null;
			textfield.setBackground(Color.RED);
		}
		return valuelist;
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		final JFrame frame = new JFrame("SensorSimulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SensorSimulator simulator = new SensorSimulator();

		// Add content to the window.
		frame.add(simulator, BorderLayout.CENTER);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	private void updateFromWiimote() {
		// Read raw data
		wiiMoteData.setDataFilePath(mRealDevicePath.getText());

		boolean success = wiiMoteData.updateData();
		mRealDeviceOutputLabel.setText(wiiMoteData.getStatus());

		// if (success) {
		// // Update controls
		// yawSlider.setValue(0); // Wiimote can't support yaw
		// rollSlider.setValue(wiiMoteData.getRoll());
		// pitchSlider.setValue(wiiMoteData.getPitch());
		// }
	}

	/**
	 * is called from within doTimer() to record/playback values
	 * recording/playback is triggered from actionListener
	 */
	private void updateFromFile() {

		// replayData.recordData(mobile.getReadYaw(),mobile.getReadRoll(),mobile.getReadPitch());
		//
		// if (replayData.playData()) {
		// // Update sliders
		// // yawSlider.setValue(replayData.getYaw());
		// // rollSlider.setValue(replayData.getRoll());
		// // pitchSlider.setValue(replayData.getPitch());
		// }else{
		// replayPlayback.setText("Playback");
		// }
		//

	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	// ///////////////////////////////
	// implements ISensorSimulator
	// ///////////////////////////////

	public int getMouseMode() {
		return mouseMode;
	}

	public WiiMoteData getWiiMoteData() {
		return wiiMoteData;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
		this.timer.setDelay(delay);
	}

	public IMobilePanel getMobilePanel() {
		return mobile;
	}

	public boolean isSupportedTemperature() {
		return mSupportedTemperature.isSelected();
	}

	public boolean isSupportedLight() {
		return mSupportedLight.isSelected();
	}

	public boolean isSupportedProximity() {
		return mSupportedProximity.isSelected();
	}

	public boolean isEnabledTemperature() {
		return mEnabledTemperature.isSelected();
	}

	// public boolean isEnabledMagneticField() {
	// return mEnabledMagneticField.isSelected();
	// }

	public boolean isEnabledLight() {
		return mEnabledLight.isSelected();
	}

	public boolean isEnabledProximity() {
		return mEnabledProximity.isSelected();
	}

	public void setEnabledTemperature(boolean enable) {
		mEnabledTemperature.setSelected(enable);
		mRefreshEmulatorThermometerLabel.setText("-");
	}

	public void setEnabledLight(boolean enable) {
		mEnabledLight.setSelected(enable);
	}

	public void setEnabledProximity(boolean enable) {
		mEnabledProximity.setSelected(enable);
	}

	public double[] getUpdateRatesThermometer() {
		return getSafeDoubleList(mUpdateRatesThermometerText);
	}

	public double getDefaultUpdateRateThermometer() {
		return getSafeDouble(mDefaultUpdateRateThermometerText);
	}

	public double getCurrentUpdateRateThermometer() {
		return getSafeDouble(mCurrentUpdateRateThermometerText, 0);
	}

	public boolean updateAverageThermometer() {
		return mUpdateAverageThermometer.isSelected();
	}

	public double[] getUpdateRatesLight() {
		return getSafeDoubleList(mUpdateRatesLightText);
	}

	public double getDefaultUpdateRateLight() {
		return getSafeDouble(mDefaultUpdateRateLightText);
	}

	public double getCurrentUpdateRateLight() {
		return getSafeDouble(mCurrentUpdateRateLightText, 0);
	}

	public boolean updateAverageLight() {
		return mUpdateAverageLight.isSelected();
	}

	public double[] getUpdateRatesProximity() {
		return getSafeDoubleList(mUpdateRatesProximityText);
	}

	public double getDefaultUpdateRateProximity() {
		return getSafeDouble(mDefaultUpdateRateProximityText);
	}

	public double getCurrentUpdateRateProximity() {
		return getSafeDouble(mCurrentUpdateRateProximityText, 0);
	}

	public boolean updateAverageProximity() {
		return mUpdateAverageProximity.isSelected();
	}

	public void setCurrentUpdateRateThermometer(double value) {
		mCurrentUpdateRateThermometerText.setText(Double.toString(value));
	}

	public void setCurrentUpdateRateLight(double value) {
		mCurrentUpdateRateLightText.setText(Double.toString(value));
	}

	public void setCurrentUpdateRateProximity(double value) {
		mCurrentUpdateRateProximityText.setText(Double.toString(value));
	}

	public double getUpdateSensors() {
		return getSafeDouble(mUpdateText);
	}

	public double getRefreshAfter() {
		return getSafeDouble(mRefreshCountText);
	}

	public double getTemperature() {
		return getSafeDouble(mTemperatureText);
	}

	public float getLight() {
		return getSafeFloat(mLightText);
	}

	public float getProximity() {
		return getSafeFloat(mProximityText);
	}

	public double getRandomTemperature() {
		return getSafeDouble(mRandomTemperatureText);
	}

	public double getRandomLight() {
		return getSafeDouble(mRandomLightText);
	}

	public double getRandomProximity() {
		return getSafeDouble(mRandomProximityText);
	}

	public boolean useRealDeviceThinkpad() {
		return mRealDeviceThinkpad.isSelected();
	}

	public boolean useRealDeviceWiimtoe() {
		return mRealDeviceWiimote.isSelected();
	}

	public String getRealDevicePath() {
		return mRealDevicePath.getText();
	}

	public void setRealDeviceOutput(String text) {
		mRealDeviceOutputLabel.setText(text);
	}

	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean isSupportedCarbonMonoxide() {
		return mSupportedCarbonMonoxide.isSelected();
	}

	public boolean isSupportedRedGas() {
		return mSupportedRedGas.isSelected();
	}

	public boolean isSupportedOxGas() {
		return mSupportedOxGas.isSelected();
	}

	public boolean isSupportedHumidity() {
		return mSupportedHumidity.isSelected();
	}

	public boolean isSupportedPressure() {
		return mSupportedPressure.isSelected();
	}

	public boolean isSupportedInfrared() {
		return mSupportedInfrared.isSelected();
	}

	public boolean isEnabledCarbonMonoxide() {
		return mEnabledCarbonMonoxide.isSelected();
	}

	public boolean isEnabledRedGas() {
		return mEnabledRedGas.isSelected();
	}

	public boolean isEnabledOxGas() {
		return mEnabledOxGas.isSelected();
	}

	public boolean isEnabledHumidity() {
		return mEnabledHumidity.isSelected();
	}

	public boolean isEnabledPressure() {
		return mEnabledPressure.isSelected();
	}

	public boolean isEnabledInfrared() {
		return mEnabledInfrared.isSelected();
	}

	public void setEnabledCarbonMonoxide(boolean enable) {
		mEnabledCarbonMonoxide.setSelected(enable);
	}

	public void setEnabledRedGas(boolean enable) {
		mEnabledRedGas.setSelected(enable);
	}

	public void setEnabledOxGas(boolean enable) {
		mEnabledOxGas.setSelected(enable);
	}

	public void setEnabledHumidity(boolean enable) {
		mEnabledHumidity.setSelected(enable);
	}

	public void setEnabledPressure(boolean enable) {
		mEnabledPressure.setSelected(enable);
	}

	public void setEnabledInfrared(boolean enable) {
		mEnabledInfrared.setSelected(enable);
	}

	public double getDefaultUpdateRateCarbonMonoxide() {
		return getSafeDouble(mDefaultUpdateRateCarbonMonoxideText);
	}

	public double getCurrentUpdateRateCarbonMonoxide() {
		return getSafeDouble(mCurrentUpdateRateCarbonMonoxideText);
	}

	public boolean updateAverageCarbonMonoxide() {
		return mUpdateAverageCarbonMonoxide.isSelected();
	}

	public double getDefaultUpdateRateRedGas() {
		return getSafeDouble(mDefaultUpdateRateRedGasText);
	}

	public double getCurrentUpdateRateRedGas() {
		return getSafeDouble(mCurrentUpdateRateRedGasText);
	}

	public boolean updateAverageRedGas() {
		return mUpdateAverageRedGas.isSelected();
	}

	public double getDefaultUpdateRateOxGas() {
		return getSafeDouble(mDefaultUpdateRateOxGasText);
	}

	public double getCurrentUpdateRateOxGas() {
		return getSafeDouble(mCurrentUpdateRateOxGasText);
	}

	public boolean updateAverageOxGas() {
		return mUpdateAverageOxGas.isSelected();
	}

	public double getDefaultUpdateRateHumidity() {
		return getSafeDouble(mDefaultUpdateRateHumidityText);
	}

	public double getCurrentUpdateRateHumidity() {
		return getSafeDouble(mCurrentUpdateRateHumidityText);
	}

	public boolean updateAverageHumidity() {
		return mUpdateAverageHumidity.isSelected();
	}

	public double getDefaultUpdateRatePressure() {
		return getSafeDouble(mDefaultUpdateRatePressureText);
	}

	public double getCurrentUpdateRatePressure() {
		return getSafeDouble(mCurrentUpdateRatePressureText);
	}

	public boolean updateAveragePressure() {
		return mUpdateAveragePressure.isSelected();
	}

	public double getDefaultUpdateRateInfrared() {
		return getSafeDouble(mDefaultUpdateRateInfraredText);
	}

	public double getCurrentUpdateRateInfrared() {
		return getSafeDouble(mCurrentUpdateRateInfraredText);
	}

	public boolean updateAverageInfrared() {
		return mUpdateAverageInfrared.isSelected();
	}

	public void setCurrentUpdateRateCarbonMonoxide(double value) {
		mCurrentUpdateRateCarbonMonoxideText.setText(Double.toString(value));
	}

	public void setCurrentUpdateRateRedGas(double value) {
		mCurrentUpdateRateRedGasText.setText(Double.toString(value));
		
	}

	public void setCurrentUpdateRateOxGas(double value) {
		mCurrentUpdateRateOxGasText.setText(Double.toString(value));
	}

	public void setCurrentUpdateRateHumidity(double value) {
		mCurrentUpdateRateHumidityText.setText(Double.toString(value));
	}

	public void setCurrentUpdateRatePressure(double value) {
		mCurrentUpdateRatePressureText.setText(Double.toString(value));
	}

	public void setCurrentUpdateRateInfrared(double value) {
		mCurrentUpdateRateInfraredText.setText(Double.toString(value));
	}

	public float getCarbonMonoxide() {
		return getSafeFloat(mCarbonMonoxideText);
	}

	public float getRedGas() {
		return getSafeFloat(mRedGasText);
	}

	public float getOxGas() {
		return getSafeFloat(mOxGasText);
	}

	public float getHumidity() {
		return getSafeFloat(mHumidityText);
	}

	public float getPressure() {
		return getSafeFloat(mPressureText);
	}

	public float getInfrared() {
		return getSafeFloat(mInfraredText);
	}

	public double getRandomCarbonMonoxide() {
		return getSafeDouble(mRandomCarbonMonoxideText);
	}

	public double getRandomRedGas() {
		return getSafeDouble(mRandomRedGasText);
	}

	public double getRandomOxGas() {
		return getSafeDouble(mRandomOxGasText);
	}

	public double getRandomHumidity() {
		return getSafeDouble(mRandomHumidityText);
	}

	public double getRandomPressure() {
		return getSafeDouble(mRandomPressureText);
	}

	public double getRandomInfrared() {
		return getSafeDouble(mRandomInfraredText);
	}

}
