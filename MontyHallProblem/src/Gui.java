import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;
import javax.swing.border.Border;
/**
 * Class that represent a simple GUI application that simulates the Monty Hall problem
 * @author Azra Demirovic
 *
 */
public class Gui extends JFrame {
	
	private static final long serialVersionUID = -1487740632670169373L;
	/**
	 * Number of times a door click was done in game. First click-reveals all except two.
	 * Second click - gets the result.
	 */
	private int count = 0;
	/**
	 * Track what was the first chosen door in order to know which strategy the player
	 * had chosen.
	 */
	private int lastChoice = 0;
	/**
	 * Sets the number of doors the game is played with depending on the options the player
	 * had chosen.
	 */
	private int numDoors = 3;

	public Gui() {
		super();
		initGUI();
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/** Method that reacts to door clicks. First click - exposes all the doors, except two.
	 * Second click - reveals what is behind clicked door. */
	private void click(JButton[] doors, int doorInd, MontyHall monty,
			JLabel[] lblResults) {
		// if this is first click
		if (count == 0) {
			++count;
			doors[doorInd].setBackground(Color.red);
			int remainsClosed = monty.openDoor(doorInd);
			for (int i = 0; i < numDoors; i++) {
				if (i != doorInd && i != remainsClosed) {
					doors[i].setIcon(new ImageIcon(getClass().getResource("goat.png")));
					doors[i].setEnabled(false);
				}
			}
			lastChoice = doorInd;
		// if this is second click
		} else if (count == 1) {
			boolean prize = monty.checkPrize(doorInd);
			// a car was chosen, reveal and update results
			if (prize == true) {
				doors[doorInd].setIcon(new ImageIcon(getClass().getResource("car.png")));
				// it was stay strategy
				if (lastChoice == doorInd) {
					int numWinsStay = Integer.parseInt(lblResults[1].getText());
					++numWinsStay;
					lblResults[1].setText(Integer.toString(numWinsStay));
				// it was switch strategy
				} else {
					int numWinsSwitch = Integer.parseInt(lblResults[0]
							.getText());
					++numWinsSwitch;
					lblResults[0].setText(Integer.toString(numWinsSwitch));
				}
			// a goat was chosen, reveal and update results
			} else {
				doors[doorInd].setIcon(new ImageIcon(getClass().getResource("goat.png")));
				// it was stay strategy
				if (lastChoice == doorInd) {
					int numLosesStay = Integer
							.parseInt(lblResults[3].getText());
					++numLosesStay;
					lblResults[3].setText(Integer.toString(numLosesStay));
				// it was switch strategy
				} else {
					int numLosesSwitch = Integer.parseInt(lblResults[2].getText());
					++numLosesSwitch;
					lblResults[2].setText(Integer.toString(numLosesSwitch));
				}
			}			
		}
	}

	/** Method that shows doors on panel and adds event listeners to doors */
	private void showDoors(final JButton[] doors, final MontyHall monty,
			final JLabel[] lblResults, JPanel panel) {
		Icon icon = createImageIcon("door.png", "");
		for (int i = 0; i < numDoors; i++) {
			doors[i] = new JButton(("? " + (i + 1)), icon);
			panel.add(doors[i]);
			// index for tracking which door was selected
			final int doorIndex = i;
			doors[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					click(doors, doorIndex, monty, lblResults);
				}
			});
		}
	}

	/** Method that sets up a GUI for Monty Hall problem */
	protected void initGUI() {
		final MontyHall monty = new MontyHall(numDoors);
		setLocation(20, 20);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Monty Hall Problem");
		this.getContentPane().setLayout(new StackLayout());
		
		//setting a label with information about the game
		JLabel infoLbl = new JLabel(
				"<html>Behind one door there is a car and behind the rest are goats. " +
				"The goal of the game is to find the car. After your first door choice, " +
				"all doors are exposed, except for the chosen" +
				" door and one more. Behind the two leftover doors are a goat and a car.<br>" +
				"The question is: Will you stick with your first choosen door (staying " +
				"strategy) or will you switch your choice to the other door (switching " +
				"strategy). Which strategy results in more wins?</html>");
		Border paddingBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		infoLbl.setBorder(paddingBorder);
		this.getContentPane().add(infoLbl);

		// setting a option panel from which user can choose whether he was to start a game with 3, 6 or 9 doors
		JPanel optionsPanel = new JPanel(new GridLayout(1, 4));
		//a button for restarting the game on option panel
		JButton restartBtn = new JButton("Restart");
		optionsPanel.add(restartBtn);
		// radio buttons for number of doors options
		final JRadioButton opt3doors = new JRadioButton("3 doors");		
		final JRadioButton opt6doors = new JRadioButton("6 doors");
		final JRadioButton opt9doors = new JRadioButton("9 doors");
		ButtonGroup grp = new ButtonGroup();
		grp.add(opt3doors);
		grp.add(opt6doors);
		grp.add(opt9doors);
		opt3doors.setSelected(true);
		optionsPanel.add(opt3doors);
		optionsPanel.add(opt6doors);
		optionsPanel.add(opt9doors);
		optionsPanel.setBorder(paddingBorder);
		this.getContentPane().add(optionsPanel);

		// setting a panel that will hold the doors
		final JPanel doorsPanel = new JPanel(new GridLayout(numDoors / 3, 3));
		this.getContentPane().add(doorsPanel);

		// setting a panel that will keep track of results
		JPanel resultsTrack = new JPanel(new GridLayout(3, 3));
		Border border = BorderFactory.createLineBorder(Color.black);
		// labels for column names of result tracker
		JLabel lblEmpty = new JLabel("");
		JLabel lblSwitch = new JLabel("Switch strategy", JLabel.CENTER);
		JLabel lblStay = new JLabel("Stay strategy", JLabel.CENTER);		
		lblEmpty.setBorder(border);
		lblSwitch.setBorder(border);
		lblStay.setBorder(border);
		resultsTrack.add(lblEmpty);
		resultsTrack.add(lblSwitch);
		resultsTrack.add(lblStay);
		// labels for row names for result tracker
		JLabel lblWins = new JLabel("# Wins:", JLabel.CENTER);
		JLabel lblLoss = new JLabel("# Loses:", JLabel.CENTER);
		lblWins.setBorder(border);
		lblLoss.setBorder(border);
		// labels for tracking results
		// index 0-# wins for switch strategy
		// index 1-# wins for stay strategy
		// index 2-# loses for switch strategy
		// index 3-# loses for stay strategy
		final JLabel[] lblResults = new JLabel[4];
		for (int i = 0; i < 4; i++) {
			lblResults[i] = new JLabel("0", JLabel.CENTER);
			lblResults[i].setBorder(border);
		}
		//adding labels to result tracker
		resultsTrack.add(lblWins);
		resultsTrack.add(lblResults[0]);
		resultsTrack.add(lblResults[1]);		
		resultsTrack.add(lblLoss);
		resultsTrack.add(lblResults[2]);
		resultsTrack.add(lblResults[3]);		
		resultsTrack.setBorder(paddingBorder);
		this.getContentPane().add(resultsTrack);
		
		// show doors on doorPanel
		final JButton doorBts[] = new JButton[9];
		showDoors(doorBts, monty, lblResults, doorsPanel);

		// adds a listener to restart button, that restart the game
		restartBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				count = 0;
				doorsPanel.removeAll();
				doorsPanel.revalidate();
				if (opt3doors.isSelected()) {
					numDoors = 3;
				} else if (opt6doors.isSelected()) {
					numDoors = 6;
				} else {
					numDoors = 9;
				}
				monty.MontyStart(numDoors);
				doorsPanel.setLayout(new GridLayout(numDoors / 3, 3));
				showDoors(doorBts, monty, lblResults, doorsPanel);
			}

		});

		this.pack();
		Dimension dim = doorsPanel.getSize();
		Dimension dim2 = this.getSize();
		this.setSize(dim2.width, dim2.height + (2 * dim.height));
		setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					@SuppressWarnings("unused")
					Gui window = new Gui();
				}
			});
		} catch (InterruptedException ignorable) {
			// ignore exception
		} catch (InvocationTargetException ignorable) {
			// ignore exception
		}

	}
}
