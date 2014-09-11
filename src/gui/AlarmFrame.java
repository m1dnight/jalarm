/******************************************************************************
 * JAlarm
 * @author  : Christophe De Troyer
 * Last edit: 11-sep-2014 19:39:13                                                   
 * Full source can be found on GitHub      :
 * https://github.com/m1dnight/JAlarm                                
 ******************************************************************************/

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import javax.swing.KeyStroke;
import javax.swing.SpinnerDateModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import mp3.Mp3PlayerThread;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import utils.Parser;
import utils.Printer;



/**
 *
 * @author ChristopheRosaFreddy
 */
public class AlarmFrame extends javax.swing.JFrame
{
	private static final long serialVersionUID = 1L;
	public AlarmFrame()
	{
		initComponents();
		// Set the icon.
	    ImageIcon programIcon = new ImageIcon(getClass().getClassLoader().getResource("icon.png"));
	    setIconImage(programIcon.getImage());
	    
	    // Set initial alarm time to current time.
	    Date value = (Date) jspAlarmTime.getValue();
		DateTime selectedValue = new DateTime(value);
		
		int hourOfDay = selectedValue.getHourOfDay();
		int minutesOfDay = selectedValue.getMinuteOfHour();
		alarmTime = Parser.parseHourStamp(hourOfDay, minutesOfDay);
	   

	}

	private void initComponents()
	{
		// Bind space as the snooze button.
		spaceKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
		spaceAction = new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (doSnooze)
				{
					Printer.debugMessage("Space action", "Pausing playback");
					playerThread.snoozeThread(AlarmFrame.this.snoozeIntervalMinutes * 60000);
				}
			}
		};
		this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(spaceKeyStroke, "SPACE");
		this.getRootPane().getActionMap().put("SPACE", spaceAction);
		
		
		buttonGroup1 = new javax.swing.ButtonGroup();
		jspAlarmTime = new javax.swing.JSpinner();
		btnBrowseFiles = new javax.swing.JButton();
		jPanel1 = new javax.swing.JPanel();
		cbxRepeat = new javax.swing.JCheckBox();
		cbxSnooze = new javax.swing.JCheckBox();
		ddwnSnoozeInterval = new javax.swing.JComboBox<String>();
		jLabel2 = new javax.swing.JLabel();
		lblSongName = new javax.swing.JLabel();
		lblAlarmStatus = new javax.swing.JLabel();
		jButton1 = new javax.swing.JButton();
		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select song");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Mp3", "mp3"));

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("JAlarm");
		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		setIconImages(null);
		setResizable(false);

		jspAlarmTime.setModel(new SpinnerDateModel());
		DateEditor timeEditor = new JSpinner.DateEditor(jspAlarmTime,
				"HH:mm:ss");
		jspAlarmTime.setEditor(timeEditor);
		jspAlarmTime.setValue(new Date()); // will only show the current time
		jspAlarmTime.setFont(new java.awt.Font("Tahoma", 0, 56)); // NOI18N
		jspAlarmTime.addChangeListener(new javax.swing.event.ChangeListener()
		{
			@Override
			public void stateChanged(javax.swing.event.ChangeEvent evt)
			{
				jspAlarmTimeStateChanged(evt);
			}
		});

		btnBrowseFiles.setText("Browse..");
		btnBrowseFiles.addActionListener(new java.awt.event.ActionListener()
		{
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				btnBrowseFilesActionPerformed(evt);
			}
		});

		jPanel1.setBorder(BorderFactory.createTitledBorder("Settings"));
		cbxRepeat.setText("Repeat");
		cbxRepeat.addChangeListener(new javax.swing.event.ChangeListener()
		{
			@Override
			public void stateChanged(javax.swing.event.ChangeEvent evt)
			{
				cbxRepeatStateChanged(evt);
			}
		});

		cbxSnooze.setText("Snooze");
		cbxSnooze.addActionListener(new java.awt.event.ActionListener()
		{
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				cbxSnoozeActionPerformed(evt);
			}
		});

		ddwnSnoozeInterval.setModel(new DefaultComboBoxModel<String>(
				new String[]
				{ "1", "2", "3", "5", "8", "13", "21" }));
		ddwnSnoozeInterval.setEnabled(false);
		ddwnSnoozeInterval
				.addActionListener(new java.awt.event.ActionListener()
				{
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						ddwnSnoozeIntervalActionPerformed(evt);
					}
				});

		jLabel2.setText("minutes");
		jLabel2.setFocusable(false);
		jLabel2.setOpaque(true);

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(cbxRepeat)
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addComponent(
																				cbxSnooze)
																		.addGap(18,
																				18,
																				18)
																		.addComponent(
																				ddwnSnoozeInterval,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(
																				jLabel2)))
										.addContainerGap(70, Short.MAX_VALUE)));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(cbxRepeat)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(cbxSnooze)
														.addComponent(
																ddwnSnoozeInterval,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(jLabel2))
										.addContainerGap(19, Short.MAX_VALUE)));

		lblSongName.setText("No song selected");

		lblAlarmStatus.setText("No alarm set");

		jButton1.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
		jButton1.setText("Set");
		jButton1.setFocusable(false);
		jButton1.addActionListener(new java.awt.event.ActionListener()
		{
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				jButton1ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.TRAILING)
												.addComponent(
														jButton1,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														lblAlarmStatus,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														jPanel1,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														lblSongName,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addGroup(
														javax.swing.GroupLayout.Alignment.LEADING,
														layout.createSequentialGroup()
																.addComponent(
																		btnBrowseFiles)
																.addGap(0,
																		0,
																		Short.MAX_VALUE))
												.addComponent(
														jspAlarmTime,
														javax.swing.GroupLayout.Alignment.LEADING))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(jspAlarmTime,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										72,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18)
								.addComponent(btnBrowseFiles)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(lblSongName)
								.addGap(18, 18, 18)
								.addComponent(jPanel1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18)
								.addComponent(jButton1,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE).addGap(18, 18, 18)
								.addComponent(lblAlarmStatus).addContainerGap()));

		pack();
	}// </editor-fold>

	//------------------------------------------------------------------------//
	//---- EVENT HANDLERS ----------------------------------------------------//
	//------------------------------------------------------------------------//
	private void btnBrowseFilesActionPerformed(java.awt.event.ActionEvent evt)
	{
		
		// Show the file dialog.
		int returnValue = fileChooser.showOpenDialog(this);
		if(returnValue == JFileChooser.APPROVE_OPTION)
		{
			// Save the selected file.
			File selectedFile = fileChooser.getSelectedFile();
			selectedSong = selectedFile;
			
			// Update the label.
			lblSongName.setText(selectedFile.getName());
			Printer.debugMessage(this.getClass(), String.format("selected %s\n", selectedFile.getAbsoluteFile()));
		}
	}

	private void cbxSnoozeActionPerformed(java.awt.event.ActionEvent evt)
	{
		// New checked state.
		boolean isChecked = ((JCheckBox) evt.getSource()).isSelected();
		doSnooze = isChecked;
		snoozeIntervalMinutes = Integer.parseInt((String) ddwnSnoozeInterval
				.getSelectedItem());
		ddwnSnoozeInterval.setEnabled(isChecked);
		Printer.debugMessage(this.getClass(), String.format("snooze enabled: " + isChecked));
		Printer.debugMessage(this.getClass(), String.format("snooze interval %d\n", snoozeIntervalMinutes));
	}

	private void jspAlarmTimeStateChanged(javax.swing.event.ChangeEvent evt)
	{
		Date value = (Date) ((JSpinner) evt.getSource()).getValue();
		DateTime selectedValue = new DateTime(value);
		
		int hourOfDay = selectedValue.getHourOfDay();
		int minutesOfDay = selectedValue.getMinuteOfHour();
		alarmTime = Parser.parseHourStamp(hourOfDay, minutesOfDay);
		Printer.debugMessage(this.getClass(), String.format("new alarm date %s\n", alarmTime));
	}

	private void cbxRepeatStateChanged(javax.swing.event.ChangeEvent evt)
	{
		// New checked state.
		boolean isChecked = ((JCheckBox) evt.getSource()).isSelected();
		doRepeat = isChecked;
		Printer.debugMessage(this.getClass(), String.format("reapeat is %s\n", doRepeat));

	}

	private void ddwnSnoozeIntervalActionPerformed(
			java.awt.event.ActionEvent evt)
	{
		snoozeIntervalMinutes = Integer.parseInt((String) ddwnSnoozeInterval
				.getSelectedItem());
		Printer.debugMessage(this.getClass(), String.format("reapeat is %s\n", snoozeIntervalMinutes));
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)
	{
		// If we have task scheduled cancel it.
		if(scheduler != null)
		{	
			//TODO If we cancel the alarm thread using abortThread() and subsequently call
			// shutdownNow() the thread throws an interrupted exception. Fix this.
			playerThread.abortThread();
			scheduler.shutdownNow();
		}
		
		// If we don't have a song scheduled, we cancel the task.
		if (selectedSong == null)
		{
			Printer.debugMessage(this.getClass(), "No song selected");
		} else
		{
			Printer.debugMessage(this.getClass(), "Selected: "
					+ selectedSong.getAbsolutePath());
			try
			{
				// Prepare the player thread.
				FileInputStream fis = new FileInputStream(selectedSong.getAbsolutePath());
				BufferedInputStream bis = new BufferedInputStream(fis);
				playerThread = new Mp3PlayerThread(bis);

				// Schedule runnable.
				Seconds seconds = Seconds.secondsBetween(new DateTime(), alarmTime);
				Printer.debugMessage(this.getClass(), String.format("alarm set in %d seconds", seconds.getSeconds()));
				// Schedule the task to execute at set date.
				scheduler = Executors.newScheduledThreadPool(2);
				scheduler.schedule(playerThread, seconds.getSeconds(), TimeUnit.SECONDS);
				Printer.debugMessage(this.getClass(), "alarm set at " + alarmTime);

				// Update the label accordingly.
				updateStatusLabel();
				
			} catch (FileNotFoundException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	//------------------------------------------------------------------------//
	//---- HELPERS METHODS ---------------------------------------------------//
	//------------------------------------------------------------------------//
	private void updateStatusLabel()
	{
		// Update GUI label.
		lblAlarmStatus.setText("Alarm set at: "  + alarmTime);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
		// Parsing the date
		lblAlarmStatus.setText(dtf.print(alarmTime));
	}
	
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[])
	{
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		try
		{
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels())
			{
				if ("Nimbus".equals(info.getName()))
				{
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex)
		{
			java.util.logging.Logger.getLogger(AlarmFrame.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex)
		{
			java.util.logging.Logger.getLogger(AlarmFrame.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex)
		{
			java.util.logging.Logger.getLogger(AlarmFrame.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex)
		{
			java.util.logging.Logger.getLogger(AlarmFrame.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new AlarmFrame().setVisible(true);
			}
		});
	}

	//------------------------------------------------------------------------//
	//---- VARIABLE DECLARATIONS ---------------------------------------------//
	//------------------------------------------------------------------------//
	// Variables declaration - do not modify
	private javax.swing.JButton btnBrowseFiles;
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.JCheckBox cbxRepeat;
	private javax.swing.JCheckBox cbxSnooze;
	private javax.swing.JComboBox<String> ddwnSnoozeInterval;
	private javax.swing.JButton jButton1;
	private javax.swing.JLabel lblAlarmStatus;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JSpinner jspAlarmTime;
	private javax.swing.JLabel lblSongName;
	
	private JFileChooser fileChooser;
	
	// Snooze actions.
	private Action spaceAction;
	private KeyStroke spaceKeyStroke;
	
	// State variables
	private boolean doSnooze = false;
	private boolean doRepeat = false;
	private int snoozeIntervalMinutes = -1;
	private static DateTime alarmTime;
	
	private static File selectedSong;
	private static Mp3PlayerThread playerThread;
	private static ScheduledExecutorService scheduler;

}
