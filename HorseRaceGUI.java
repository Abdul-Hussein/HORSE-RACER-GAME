import javax.swing.*;
import javax.swing.border.Border;

import HorseRacer.Horse;
import HorseRacer.Race;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class HorseRaceGUI extends JFrame {
    private JTextArea raceDisplayArea;
    private JTextField raceLengthInput;
    private ArrayList<Horse> horseList;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HorseRaceGUI().setVisible(true);
            }
        });
    }

    public HorseRaceGUI() {
        setTitle("Horse Racing Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(true);

        horseList = new ArrayList<>();

        this.raceDisplayArea = new JTextArea(20, 40);
        raceDisplayArea.setEditable(false);
        raceDisplayArea.setFont(new Font("Monospace", Font.PLAIN, 16));
        raceDisplayArea.setBackground(new Color(222, 184, 135));
        raceDisplayArea.setForeground(new Color(139, 69, 19));

        JScrollPane scrollPane = new JScrollPane(raceDisplayArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(210, 180, 140));
        JLabel raceLengthLabel = new JLabel("Race Length:");
        raceLengthInput = new JTextField("20", 10);
        raceLengthInput.setToolTipText("Enter race length in meters");
        Border fieldBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
        raceLengthInput.setBorder(fieldBorder);
        topPanel.add(raceLengthLabel);
        topPanel.add(raceLengthInput);

        JButton addHorsesButton = new JButton("Add Horses");
        addHorsesButton.setFont(new Font("Arial", Font.BOLD, 14));
        addHorsesButton.setForeground(Color.WHITE);
        addHorsesButton.setBackground(Color.ORANGE);
        addHorsesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openHorseAdditionWindow();
            }
        });
        topPanel.add(addHorsesButton);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        JButton startRaceButton = new JButton("Start Race");
        startRaceButton.setFont(new Font("Arial", Font.BOLD, 14));
        startRaceButton.setForeground(Color.WHITE);
        startRaceButton.setBackground(Color.ORANGE);
        startRaceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int raceLength = Integer.parseInt(raceLengthInput.getText().trim());
                if (horseList.size() >= 2) {
                    Race race = new Race(raceLength, raceDisplayArea);
                    for (Horse horse : horseList) {
                        race.addHorse(horse, horseList.indexOf(horse) + 1);
                    }
                    new Thread(race::startRace).start();
                } else {
                    JOptionPane.showMessageDialog(null, "At least 2 horses are required to start the race.",
                            "Insufficient Horses", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        getContentPane().add(startRaceButton, BorderLayout.SOUTH);
        
        // Button to display rules
        JButton rulesButton = new JButton("Show Rules");
        rulesButton.setFont(new Font("Arial", Font.BOLD, 14));
        rulesButton.setForeground(Color.WHITE);
        rulesButton.setBackground(Color.BLUE); // Adjust color as desired
        rulesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayRules();
            }
        });
        topPanel.add(rulesButton);
        
        // Button to display winners
        JButton winnersButton = new JButton("Display Winners");
        winnersButton.setFont(new Font("Arial", Font.BOLD, 14));
        winnersButton.setForeground(Color.WHITE);
        winnersButton.setBackground(Color.GREEN); // Adjust color as desired
        winnersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayWinners();
            }
        });
        topPanel.add(winnersButton);
    }

    private void openHorseAdditionWindow() {
        HorseAdditionWindow horseAdditionWindow = new HorseAdditionWindow(this);
        horseAdditionWindow.setVisible(true);
    }

    public void updateRaceDisplay(String raceText) {
        raceDisplayArea.setText(raceText);
    }

    public void addHorseToRace(Horse horse) {
        horseList.add(horse);
        updateRaceDisplay("New horse added: " + horse.getName() + ", Symbol: " + horse.getSymbol() + ", Confidence: " + horse.getConfidence() + "\n");
    }

    // Method to display rules
    private void displayRules() {
        String rules = "Horse Racing Simulator Rules:\n\n" +
                "1. Add at least 2 horses to start the race.\n" +
                "2. Each horse moves forward a random distance in each iteration based on their confidence.\n" +
                "3. The race continues until one of the horses reaches or surpasses the specified race length.\n" +
                "4. The horse that reaches the finish line first wins the race.\n";
        JOptionPane.showMessageDialog(null, rules, "Game Rules", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Method to display winners
    private void displayWinners() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("winners.txt"));
            StringBuilder winnersText = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                winnersText.append(line).append("\n");
            }
            reader.close();
            JOptionPane.showMessageDialog(null, "Previous Winners:\n\n" + winnersText.toString(), "Winners", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}

class HorseAdditionWindow extends JFrame {
    private HorseRaceGUI parent;

    public HorseAdditionWindow(HorseRaceGUI parent) {
        super("Add Horses");
        this.parent = parent;
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.setBackground(new Color(222, 184, 135));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameInput = new JTextField();
        JLabel symbolLabel = new JLabel("Symbol:");
        JComboBox<String> symbolDropdown = new JComboBox<>(new String[] {"🐎", "🐴", "🦄", "🐕", "🐇", "🐘"});
        JLabel confidenceLabel = new JLabel("Confidence:");
        JSpinner confidenceSpinner = new JSpinner(new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1));

        panel.add(nameLabel);
        panel.add(nameInput);
        panel.add(symbolLabel);
        panel.add(symbolDropdown);
        panel.add(confidenceLabel);
        panel.add(confidenceSpinner);

        JButton addButton = new JButton("Add Horse");
        addButton.setFont(new Font("Arial", Font.BOLD, 12));
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(Color.ORANGE);
        addButton.addActionListener(e -> {
            String name = nameInput.getText();
            String symbol = (String) symbolDropdown.getSelectedItem();
            double confidence = (Double) confidenceSpinner.getValue();
            Horse horse = new Horse(symbol.charAt(0), name, confidence);
            parent.addHorseToRace(horse);
            dispose();
        });

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(addButton, BorderLayout.SOUTH);
    }
}
