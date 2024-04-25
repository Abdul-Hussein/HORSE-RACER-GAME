package com.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import HorseRacer.Horse;
import HorseRacer.Race;

public class HorseRaceGUI {
	private JFrame frame;
	private JPanel panel;
	private Race race;

	public HorseRaceGUI() {
		frame = new JFrame("Horse Racing Game");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel(new BorderLayout());
		panel.setBackground(new Color(240, 248, 255));

		JLabel titleLabel = new JLabel("Welcome to the Horse Racing Game");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

		JPanel rulesPanel = new JPanel();
		rulesPanel.setBackground(new Color(255, 255, 255, 200));
		rulesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel rulesLabel = new JLabel(
				"<html><b>Rules of the Game:</b><br>" + "1. Enter the number of lanes and race distance.<br>"
						+ "2. Enter the name of your horse for each lane.<br>" + "3. Click 'OK' to start the race.<br>"
						+ "4. Wait for the race results.<br>" + "5. Have fun!</html>");
		rulesLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		rulesLabel.setForeground(Color.DARK_GRAY);

		
		
		
		rulesPanel.add(rulesLabel);
		
		
		 // Create "Preview Statistics" button
		// Create "Preview Statistics" button
		JButton statisticsButton = new JButton("Preview Statistics");
		statisticsButton.setBackground(new Color(0, 102, 204));
		statisticsButton.setForeground(Color.WHITE);
		statisticsButton.setFont(new Font("Arial", Font.BOLD, 16)); // Decrease font size for smaller button text
		statisticsButton.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20)); // Adjust padding for smaller button
		statisticsButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        displayStatistics();
		    }
		});
		statisticsButton.setPreferredSize(new Dimension(150, 40)); // Set preferred size for the button

		// Add "Preview Statistics" button to the panel
		panel.add(statisticsButton, BorderLayout.WEST); // Adjust the layout as needed

        // Add "Preview Statistics" button to the panel
        panel.add(statisticsButton, BorderLayout.WEST); // Adjust the layout as needed
		// Existing code...
		JButton startButton = new JButton("Start Race");
		startButton.setBackground(new Color(139, 69, 19));
		startButton.setForeground(Color.WHITE);
		startButton.setFont(new Font("Arial", Font.BOLD, 20));
		startButton.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startRace();
			}
		});

		panel.add(titleLabel, BorderLayout.NORTH);
		panel.add(rulesPanel, BorderLayout.CENTER);
		panel.add(startButton, BorderLayout.SOUTH);

		frame.add(panel);
		frame.setVisible(true);
	}

	private void startRace() {
		boolean validInput = false;
		while (!validInput) {
			JPanel trackDesignPanel = new JPanel();
			trackDesignPanel.setLayout(new GridLayout(3, 2));
			trackDesignPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			JLabel lanesLabel = new JLabel("Number of Lanes:");
			JSpinner lanesSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 3, 1));
			trackDesignPanel.add(lanesLabel);
			trackDesignPanel.add(lanesSpinner);

			JLabel distanceLabel = new JLabel("Race Distance (5-50m):");
			JTextField distanceField = new JTextField("Race distance here ");
			trackDesignPanel.add(distanceLabel);
			trackDesignPanel.add(distanceField);

		

			JPanel rulesPanel = new JPanel();
			rulesPanel.setBackground(new Color(255, 255, 255, 200));
			JLabel rulesLabel = new JLabel(
					"<html><b>Rules of the Game:</b><br>" + "1. Choose a race track between 5 and 50.<br>"
							+ "2. Choose either 2 or 3 lanes.<br>" + "3. Have fun😊!</html>");
			rulesPanel.add(rulesLabel);
			

			JPanel mainPanel = new JPanel(new BorderLayout());
			mainPanel.add(trackDesignPanel, BorderLayout.CENTER);
			mainPanel.add(rulesPanel, BorderLayout.SOUTH);

			UIManager.put("OptionPane.background", new Color(240, 248, 255));
			UIManager.put("Panel.background", new Color(240, 248, 255));

			int result = JOptionPane.showConfirmDialog(frame, mainPanel, "Track Design", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				try {
					int lanes = (int) lanesSpinner.getValue();
					int distance = Integer.parseInt(distanceField.getText());
					if (lanes < 1 || lanes > 3 || distance < 5 || distance > 50) {
						throw new IllegalArgumentException("Invalid input.");
					}

					String userHorseName = JOptionPane.showInputDialog(frame, "Enter the name of your horse:");
					if (userHorseName != null && !userHorseName.trim().isEmpty()) {
						initializeRaceWithUserHorse(lanes, distance, userHorseName);
					} else {
						initializeRace(lanes, distance, userHorseName);
					}
					
			
					validInput = true;
				
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(frame, "Please enter valid numbers for lanes and distance.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (IllegalArgumentException ex) {
					JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				return;
			}
		}
	}

	private void printRaceFrames() {
		
	    JFrame raceFrame = new JFrame("Race Progress");
	    raceFrame.setSize(400, 300);
	    raceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    raceFrame.setLayout(new GridLayout(race.laneHorse.size() + 2, 1));

	    JPanel topPanel = new JPanel();
	    JLabel topEdgeLabel = new JLabel(multiplePrint("=", race.raceLength + 3));
	    topPanel.add(topEdgeLabel);
	    raceFrame.add(topPanel);
	    if (raceFrame != null) {
	        raceFrame.dispose(); // Dispose of the previous race's progress window
	    }
	    raceFrame = new JFrame("Race Progress");
	    raceFrame.setSize(400, 300);
	    raceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    raceFrame.setLayout(new GridLayout(race.laneHorse.size() + 2, 1));

	    for (Horse horse : race.laneHorse) {
	        JPanel lanePanel = new JPanel();
	        lanePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	        JLabel laneLabel = new JLabel(
	                printLane(horse) + horse.getName() + " (Current Confidence " + horse.getConfidence() + ")");
	        lanePanel.add(laneLabel);
	        raceFrame.add(lanePanel);
	    }

	    JPanel bottomPanel = new JPanel();
	    bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	    JLabel bottomEdgeLabel = new JLabel(multiplePrint("=", race.raceLength + 3));
	    bottomPanel.add(bottomEdgeLabel);
	    raceFrame.add(bottomPanel);

	    raceFrame.setVisible(true);
	}

	private String multiplePrint(String s, int times) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < times; i++) {
			sb.append(s);
		}
		return sb.toString();
	}
	private String printLane(Horse horse) {
	    StringBuilder lane = new StringBuilder();
	    for (int i = 0; i < race.raceLength; i++) {
	        if (i == horse.getDistanceTravelled()) {
	            lane.append(horse.getSymbol());
	        } else if (i < horse.getDistanceTravelled()) {
	            lane.append(horse.getSymbol());
	        } else {
	            lane.append("-");
	        }
	    }
	    return lane.toString();
	}


	private void initializeRaceWithUserHorse(int lanes, int distance, String userHorseName) {
	    ArrayList<Horse> horses = new ArrayList<>();

	    // Prompt the user to select a symbol for their horse
	    String[] symbols = {"🏇", "🐎", "🐴", "🦄"}; // List of available symbols
	    String selectedSymbol = (String) JOptionPane.showInputDialog(frame,
	            "Select a symbol for your horse:", "Select Symbol",
	            JOptionPane.PLAIN_MESSAGE, null, symbols, symbols[0]);

	    if (selectedSymbol == null) {
	        // If user cancels the selection, use default symbol
	        selectedSymbol = "🏇";
	    }

	    // Add user's horse with the selected symbol
	    horses.add(new Horse(selectedSymbol, userHorseName, Math.random()));

	    // Add other horses with symbols chosen by the user
	    for (int i = 1; i < lanes; i++) {
	        String horseName = JOptionPane.showInputDialog(frame,
	                "Enter the name of horse " + (i + 1) + " for lane " + (i + 1) + ":");
	        String horseSymbol = (String) JOptionPane.showInputDialog(frame,
	                "Select a symbol for horse " + (i + 1) + ":", "Select Symbol",
	                JOptionPane.PLAIN_MESSAGE, null, symbols, symbols[0]);

	        if (horseSymbol == null) {
	            // If user cancels the selection, use default symbol
	            horseSymbol = "🏇";
	        }

	        horses.add(new Horse(horseSymbol, horseName, Math.random()));
	    }

	    race = new Race(distance);
	    for (int i = 0; i < lanes && i < horses.size(); i++) {
	        race.addHorse(horses.get(i), i + 1);
	    }

	    race.startRace();

	    displayResults(userHorseName);
	}

	private void initializeRace(int lanes, int distance, String userHorseName) {
	    ArrayList<Horse> horses = new ArrayList<>();
	    String[] symbols = {"🏇", "🐎", "🐴", "🦄"}; // List of available symbols

	    for (int i = 0; i < lanes; i++) {
	        // Prompt user to select a symbol
	        String selectedSymbol = (String) JOptionPane.showInputDialog(frame,
	                "Select a symbol for horse " + (i + 1) + ":", "Select Symbol",
	                JOptionPane.PLAIN_MESSAGE, null, symbols, symbols[0]);

	        if (selectedSymbol == null) {
	            // If user cancels the selection, use default symbol
	            selectedSymbol = "🏇";
	        }

	        String horseName = JOptionPane.showInputDialog(frame,
	                "Enter the name of horse " + (i + 1) + " for lane " + (i + 1) + ":");
	        horses.add(new Horse(selectedSymbol, horseName, Math.random()));
	    }

	    race = new Race(distance);
	    for (int i = 0; i < lanes && i < horses.size(); i++) {
	        race.addHorse(horses.get(i), i + 1);
	    }

	    race.startRace();

	    displayResults(userHorseName);
	}

	private String extractWinnerName(String winnerString) {
		String[] parts = winnerString.split(" won the race");
		String winnerName = parts[0];
		return winnerName.trim();
	}

	private void animateCongratulations() {
		JLabel congratsLabel = new JLabel("Congratulations!");
		congratsLabel.setFont(new Font("Arial", Font.BOLD, 30));
		congratsLabel.setForeground(Color.GREEN);

		JPanel splashPanel = new JPanel();
		splashPanel.setLayout(new GridBagLayout());
		splashPanel.setBackground(new Color(0, 0, 0, 0));
		splashPanel.add(congratsLabel);

		Thread animationThread = new Thread(() -> {
			float alpha = 0.0f;
			while (alpha <= 1.0f) {
				alpha += 0.1f;
				splashPanel.setBackground(new Color(0, 0, 0, (int) (alpha * 255)));
				splashPanel.repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		});

		animationThread.start();

		JOptionPane.showMessageDialog(frame, splashPanel, "Congratulations!", JOptionPane.PLAIN_MESSAGE);
	}
	
	

	    // Other variables and methods...

	    private void displayStatistics() {
	        Map<String, Integer> horsePoints = calculatePoints();

	        JFrame statsFrame = new JFrame("Race Statistics");
	        statsFrame.setSize(400, 300);

	        JPanel panel = new JPanel(new BorderLayout());

	        DefaultTableModel model = new DefaultTableModel();
	        model.addColumn("Horse");
	        model.addColumn("Points");

	        for (Map.Entry<String, Integer> entry : horsePoints.entrySet()) {
	            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
	        }

	        JTable table = new JTable(model);
	        JScrollPane scrollPane = new JScrollPane(table);

	        panel.add(scrollPane, BorderLayout.CENTER);
	        statsFrame.add(panel);
	        statsFrame.setVisible(true);
	    }

	    private Map<String, Integer> calculatePoints() {
	        Map<String, Integer> horseWins = new HashMap<>();
	        File winnersFile = new File("winners.txt");

	        try (BufferedReader reader = new BufferedReader(new FileReader(winnersFile))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                horseWins.put(line, horseWins.getOrDefault(line, 0) + 1);
	            }
	        } catch (IOException e) {
	            System.err.println("Error reading file: " + e.getMessage());
	        }

	        Map<String, Integer> horsePoints = new HashMap<>();
	        for (Map.Entry<String, Integer> entry : horseWins.entrySet()) {
	            int points = entry.getValue() * 3; // Each win earns 3 points
	            horsePoints.put(entry.getKey(), points);
	        }
	        return horsePoints;
	    }

	    // Other methods and variables...
	
	    private void displayResults(String userHorseName) {
	        String winnerString = race.printWinner();
	        String winner = extractWinnerName(winnerString);
	        File winnersNames = new File("winners.txt"); // Corrected file declaration

	        // Writing the winner's name to the file
	        try (PrintWriter writer = new PrintWriter(winnersNames)) {
	            writer.println(winner);
	            if (!winner.equals(userHorseName)) {
	                writer.println(userHorseName); // Append user's horse name if it's not the winner
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        // Reading the winner's names from the file
	        ArrayList<String> winners = new ArrayList<>();
	        try (BufferedReader reader = new BufferedReader(new FileReader(winnersNames))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                winners.add(line);
	            }
	        } catch (IOException e) {
	            System.err.println("Error reading file: " + e.getMessage());
	        }

	        frame.getContentPane().removeAll();
	        if (winner != null && winner.equals(userHorseName)) {
	            animateCongratulations();
	            JOptionPane.showMessageDialog(frame, "Congratulations " + winner + " won the race!", "Race Result",
	                    JOptionPane.INFORMATION_MESSAGE);
	        } else if (!winner.equals(userHorseName)) {
	            frame.getContentPane().setBackground(new Color(255, 99, 71));
	            JOptionPane.showMessageDialog(frame, winner + " won the race. Better luck next time!", "Race Result",
	                    JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            frame.getContentPane().setBackground(new Color(240, 248, 255));
	            JOptionPane.showMessageDialog(frame, "The race is a draw!", "Race Result", JOptionPane.INFORMATION_MESSAGE);
	        }

	        int option = JOptionPane.showConfirmDialog(frame, "Do you want to preview the race?", "Preview Race", JOptionPane.YES_NO_OPTION);
	        if (option == JOptionPane.YES_OPTION) {
	            printRaceFrames();
	        }

	        playAgain();
	    }

	private void playAgain() {
		int choice = JOptionPane.showConfirmDialog(frame, "Do you want to play again?", "Play Again",
				JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			frame.getContentPane().removeAll();
			frame.repaint();
			startRace();
		} else {
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new HorseRaceGUI();
			}
		});
	}
}