package HorseRacer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.swing.JTextArea;

/**
 * A race simulation with three horses running in their respective lanes for a given distance.
 * Each horse's movement depends on its confidence rating.
 * The race ends when one of the horses reaches the finish line or falls.
 * Winners gain confidence, encouraging them to perform better in subsequent races.
 * 
 * @author McFarewell
 * @version 1.0
 */
public class Race {
    private int raceLength;
    private Horse lane1Horse;
    private Horse lane2Horse;
    private Horse lane3Horse;
    private JTextArea raceDisplayArea;

    /**
     * Creates a race with a specified length and GUI area for display.
     * 
     * @param distance The length of the racetrack.
     * @param newGUI The GUI area for displaying race progress.
     */
    public Race(int distance, JTextArea newGUI) {
        raceLength = distance;
        lane1Horse = null;
        lane2Horse = null;
        lane3Horse = null;
        raceDisplayArea = newGUI;
    }
    
    /**
     * Saves the winner's name to a file named "winners.txt".
     * 
     * @param winnerName The name of the winner to be saved.
     */
    private void saveWinnerToFile(String winnerName) {
        try (FileWriter writer = new FileWriter("winners.txt", true)) {
            writer.write(winnerName + "\n");
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    
    /**
     * Adds a horse to the race in the specified lane.
     * 
     * @param theHorse The horse to be added to the race.
     * @param laneNumber The lane where the horse will be added.
     */
    
        public void addHorse(Horse theHorse, int laneNumber) {
        if (laneNumber == 1) {
            lane1Horse = theHorse;
        } else if (laneNumber == 2) {
            lane2Horse = theHorse;
        } else if (laneNumber == 3) {
            lane3Horse = theHorse;
        } else {
            raceDisplayArea.append("Cannot add horse to lane " + laneNumber + ". Invalid lane.\n");
        }
    }

    /**
     * Checks if all horses have fallen.
     * 
     * @return True if all horses have fallen, false otherwise.
     */
    private boolean allHorsesFallen() {
        return lane1Horse.hasFallen() && lane2Horse.hasFallen() && (lane3Horse != null && lane3Horse.hasFallen());
    }
    
    /**
     * Starts the race, repeatedly moving horses until one wins or all fall.
     */
    public void startRace() {
        boolean finished = false;
        Horse winner = null;
        
        resetHorses();
                      
        while (!finished && !allHorsesFallen()) {
            moveHorse(lane1Horse);
            moveHorse(lane2Horse);
            if (lane3Horse != null) {
                moveHorse(lane3Horse);
            }   
                        
            printRace();
            
            if (raceWonBy(lane1Horse)){
                finished = true;
                winner = lane1Horse;
            } else if (raceWonBy(lane2Horse)){
                finished = true;
                winner = lane2Horse;
            } else if (lane3Horse != null && raceWonBy(lane3Horse)){
                finished = true;
                winner = lane3Horse;
            }
           
            try { 
                TimeUnit.MILLISECONDS.sleep(100);
            } catch(Exception e){}
        }
        if (winner != null) {
            winner.setConfidence(Math.min(1.0, winner.getConfidence() + 0.1));
            printRace();
            raceDisplayArea.append("The winner is " + winner.getName() + "!\n");
        }
    }
    
    /**
     * Moves a horse forward or causes it to fall based on its confidence.
     * 
     * @param theHorse The horse to be moved.
     */
    private void moveHorse(Horse theHorse) {
        if  (!theHorse.hasFallen()) {
            if (Math.random() < theHorse.getConfidence()) {
               theHorse.moveForward();
            }
            
            if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence())) {
                theHorse.fall();
            }
        }
    }
        
    /**
     * Checks if a horse has won the race.
     *
     * @param theHorse The horse being evaluated.
     * @return True if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse) {
        return theHorse != null && theHorse.getDistanceTravelled() == raceLength;
    }
    
    /**
     * Prints the race progress to the display area.
     */
    private void printRace() {
        raceDisplayArea.setText("");  // Clear the display area
        
        printRaceEdge('=');
        raceDisplayArea.append("\n");
        
        printLane(lane1Horse);
        raceDisplayArea.append("\n");
        
        printLane(lane2Horse);
        raceDisplayArea.append("\n");
        
        if (lane3Horse != null) {
            printLane(lane3Horse);
            raceDisplayArea.append("\n");
        } else {
            raceDisplayArea.append(String.format("|%" + (raceLength + 2) + "s|\n", ""));
        }
        
        printRaceEdge('=');
        raceDisplayArea.append("\n");    
    }
    
    /**
     * Prints a horse's lane during the race.
     * 
     * @param theHorse The horse whose lane will be printed.
     */
    private void printLane(Horse theHorse) {
        if (theHorse == null) return;
        
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
        
        raceDisplayArea.append('|' + "");
        printSpace(' ', spacesBefore);
        
        if (theHorse.hasFallen()) {
            raceDisplayArea.append('X' + "");
        } else {
            raceDisplayArea.append(theHorse.getSymbol() + "");
        }

        printSpace(' ', spacesAfter);
        
        raceDisplayArea.append('|' + "");
        raceDisplayArea.append(theHorse.getName() + " (Confidence: " + String.format("%.1f", theHorse.getConfidence()) + ")");
    }
    
    /**
     * Prints multiple occurrences of a character.
     * 
     * @param aChar The character to be printed.
     * @param times The number of times the character should be printed.
     */
    private void printSpace(char aChar, int times) {
        int i = 0;
        while (i < times) {
            raceDisplayArea.append(aChar + "");
            i++;
        }
    }
    
    /**
     * Prints multiple occurrences of a character for race edge.
     * 
     * @param aChar The character to be printed.
     */
    private void printRaceEdge(char aChar) {
        printSpace(aChar, raceLength / 2);
    }
    
    /**
     * Resets all horses to their starting positions.
     */
    private void resetHorses() {
        lane1Horse.goBackToStart();
        lane2Horse.goBackToStart();
        if (lane3Horse != null) {
            lane3Horse.goBackToStart();
        }
    }
}
