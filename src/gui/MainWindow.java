package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import mp3.Mp3PlayerThread;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import utils.Printer;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainWindow
{
	private static final String DEFAULT_STATUS_STRING   = "No alarm set";
	private static final String DEFAULT_SONG_LABEL_TEXT = "No song selected";
	
	private static File                     selectedSong;
	private static Mp3PlayerThread          playerThread;
	private static ScheduledExecutorService scheduler;
	private static int alarmHours;
	private static int alarmMinutes;
	
	
	public static void main(String[] args)
	{
		// ---- TIME PICKING PANEL -------------------------------------------//
		// Get current time to set default values for jspinner.
		DateTime now = new DateTime();
		// Hour picker.
		SpinnerModel hoursSpinnerModel = new SpinnerNumberModel(
				1, // initial  value
				0, // min
				23, // max
				1);// step
		JSpinner hoursSpinner = new JSpinner(hoursSpinnerModel);
		hoursSpinner.setValue(now.getHourOfDay());
		alarmHours = now.getHourOfDay();
		((JSpinner.NumberEditor) hoursSpinner.getEditor()).getTextField()
				.setFont(new Font("DroidSansMono", Font.PLAIN, 30));
		// Seperator (:).
		JLabel seperator = new JLabel(":");
		seperator.setFont(new Font("DroidSansMono", Font.PLAIN, 30));
		
		// Minute picker.
		SpinnerModel minutesSpinnerModel = new SpinnerNumberModel(
				1, // initial value
				0, // min
				59, // max
				1);// step
		JSpinner minutesSpinner = new JSpinner(minutesSpinnerModel);
		minutesSpinner.setValue(now.getMinuteOfHour());
		alarmMinutes = now.getMinuteOfHour();
		((JSpinner.NumberEditor) minutesSpinner.getEditor()).getTextField()
				.setFont(new Font("DroidSansMono", Font.PLAIN, 30));
		
		// ---- SONG SELECTION -----------------------------------------------//
		// Label for the filename.
		final JLabel lblSelectedSong = new JLabel(DEFAULT_SONG_LABEL_TEXT);
		lblSelectedSong.setPreferredSize(new Dimension(300,15));
		// Filechooser to pick only mp3 files.
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select song");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Mp3", "mp3"));
		
		// Button to browse for a file.
		JButton btnSelectSong = new JButton();
		btnSelectSong.setText("Browse..");

		// ---- SET ALARM SECTION --------------------------------------------//
		JButton btnSetAlarm = new JButton();
		btnSetAlarm.setText("Set");
		btnSetAlarm.setFont(new Font("DroidSansMono", Font.PLAIN, 30));
		

		
		// ---- STATUS BAR ---------------------------------------------------//
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		final JLabel statusLabel = new JLabel(DEFAULT_STATUS_STRING);
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		
		// ---- ACTION LISTENERS ---------------------------------------------//
		hoursSpinner.addChangeListener(new ChangeListener()
		{
			
			@Override
			public void stateChanged(ChangeEvent e)
			{
				MainWindow.alarmHours = (int) ((JSpinner) e.getSource()).getValue();
			}
		});
		minutesSpinner.addChangeListener(new ChangeListener()
		{
			
			@Override
			public void stateChanged(ChangeEvent e)
			{
				MainWindow.alarmMinutes = (int) ((JSpinner) e.getSource()).getValue();
			}
		});
		btnSetAlarm.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// If we have task scheduled cancel it.
				if(scheduler != null)
				{	
					playerThread.abortThread();
					scheduler.shutdownNow();
				}
				
				if (MainWindow.selectedSong == null)
				{
					Printer.debugMessage(this.getClass(), "No song selected");
				} else
				{
					Printer.debugMessage(this.getClass(), "Selected: "
							+ MainWindow.selectedSong.getAbsolutePath());
					FileInputStream fis;
					try
					{
						// Prepare the player thread.
						fis = new FileInputStream(
								MainWindow.selectedSong.getAbsolutePath());
						BufferedInputStream bis = new BufferedInputStream(fis);
						playerThread = new Mp3PlayerThread(bis);
						
						// Calculate seconds to alarm.
						DateTime now          = new DateTime();
						int      alarmHour    = MainWindow.alarmHours;
						int      alarmMinutes = MainWindow.alarmMinutes;
						int currentHour = now.getHourOfDay();
						int currentMinutes = now.getMinuteOfHour();
						DateTime alarmDateTime;
						
						// Check if alarm is for tomorrow or today.
						if(alarmHour < currentHour || (alarmHour == currentHour && alarmMinutes < currentMinutes))
							alarmDateTime = new DateTime().plusDays(1);
						else
							alarmDateTime = new DateTime();
						
						//TODO can I do this cleaner? 
						// Create alarm DateTime object.
						DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MM/dd/yyyy");
						String dateString = dtfOut.print(alarmDateTime);
						
						DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyy HH:mm");
						alarmDateTime = formatter.parseDateTime(String.format("%s %02d:%02d", dateString, alarmHour, alarmMinutes));
						Printer.debugMessage(this.getClass(), "alarm set at " + alarmDateTime);
						
						// Update GUI label.
						statusLabel.setText("Alarm set at: " + formatter.print(alarmDateTime));
						
						// Schedule runnable.
						Seconds seconds = Seconds.secondsBetween(now, alarmDateTime);
						Printer.debugMessage(this.getClass(), String.format("alarm set in %d seconds", seconds.getSeconds()));
						// Schedule the task to execute at set date.
						scheduler = Executors.newScheduledThreadPool(2);
						scheduler.schedule(playerThread, seconds.getSeconds(), TimeUnit.SECONDS);
					} catch (FileNotFoundException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		btnSelectSong.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Show the file dialog.
				int returnValue = fileChooser.showOpenDialog(null);
				if(returnValue == JFileChooser.APPROVE_OPTION)
				{
					// Save the selected file.
					File selectedFile = fileChooser.getSelectedFile();
					MainWindow.selectedSong = selectedFile;
					
					// Update the label.
					lblSelectedSong.setText(selectedFile.getName());
					Printer.debugMessage(this.getClass(), String.format("selected %s\n", selectedFile.getAbsoluteFile()));
				}
			}
		});
		// ---- MAIN JPANEL --------------------------------------------------//
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		int currentRow = 0;
		//mainPanel.setPreferredSize(new Dimension(300,600));
		
		// Padding for all components.
		Insets defaultPadding = new Insets(2, 2, 10, 5);
		// Hours spinner.
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx   = 0;
		gbc.gridy   = currentRow;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = defaultPadding;
		mainPanel.add(hoursSpinner, gbc);
		
		// Seperator (:).
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx   = 1;
		gbc.gridy   = currentRow;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets = defaultPadding;
		mainPanel.add(seperator, gbc);
		
		// Minutes spinner.
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx   = 2;
		gbc.gridy   = currentRow;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = defaultPadding;
		mainPanel.add(minutesSpinner, gbc);
		currentRow++;
		// Seperator (line).
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx     = 0;
		gbc.gridy     = currentRow;
		gbc.gridwidth = 3;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.insets = defaultPadding;
		mainPanel.add(new JSeparator(), gbc);
		currentRow++;
		
		// Song select button.
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx     = 0;
		gbc.gridy     = currentRow;
		gbc.gridwidth = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = defaultPadding;
		mainPanel.add(btnSelectSong, gbc);
		currentRow++;
		// Song label.
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = currentRow;
		gbc.gridwidth = 3;
		gbc.weightx = 0.0;
		gbc.weighty = 1.0;
		gbc.insets = defaultPadding;
		mainPanel.add(lblSelectedSong, gbc);
		currentRow++;
		
		// btnSetAlarm
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx     = 0;
		gbc.gridy     = currentRow;
		gbc.gridwidth = 3;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = gbc.insets    = new Insets(20, 0, 0, 5);;
		mainPanel.add(btnSetAlarm, gbc);
		currentRow++;
		
		// Status panel
		gbc           = new GridBagConstraints();
		gbc.fill      = GridBagConstraints.BOTH;
		gbc.gridx     = 0;
		gbc.gridy     = currentRow;
		gbc.gridwidth = 3;
		gbc.weightx   = 1.0;
		gbc.weighty   = 1.0;
		gbc.insets    = new Insets(20, 0, 0, 0);
		mainPanel.add(statusPanel, gbc);
		
		// ---- JFRAME PART --------------------------------------------------//
		JFrame frame = new JFrame("JAlarm");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);

	}
}