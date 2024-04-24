package HorseRacer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.lang.Math;

/**
 * Race simulates a horse race with multiple lanes.
 */
public class Race {
	private int raceLength; // The length of the race track
	private ArrayList<Horse> laneHorse; // ArrayList to store horses in each lane
	private static final int MIN_TRACK_LENGTH = 5; // Minimum track length
	private static final int MAX_TRACK_LENGTH = 50; // Maximum track length

	/**
	 * Constructs a Race object with a specified race distance.
	 * 
	 * @param distance The length of the race track
	 */
	public Race(int distance) {
        if (distance < MIN_TRACK_LENGTH || distance > MAX_TRACK_LENGTH) {
            throw new IllegalArgumentException("Track length must be between " + MIN_TRACK_LENGTH + " and " + MAX_TRACK_LENGTH + " meters.");
        }
        raceLength = distance;
        laneHorse = new ArrayList<>();
    }

	/**
	 * Adds a horse to a specified lane.
	 * 
	 * @param theHorse   The horse to add to the race
	 * @param laneNumber The lane number where the horse will race
	 */
	public void addHorse(Horse theHorse, int laneNumber) {
		// Ensure the lane number is within the valid range
		if (laneNumber >= 1 && laneNumber <= 3) {
			laneHorse.add(laneNumber - 1, theHorse); // Add the horse to the specified lane
		} else {
			System.out.println("Cannot add horse to lane " + laneNumber + " because there is no such lane");
		}
	}

	/**
	 * Resets the positions of all horses to the start of the race.
	 */
	private void resetLanes() {
		// Iterate through each horse in the lane and reset its position
		for (Horse horse : laneHorse) {
			if (horse != null) {
				horse.goBackToStart();
			}
		}
	}

	/**
	 * Checks if all horses in the race have fallen.
	 * 
	 * @return True if all horses have fallen, otherwise false
	 */
	private boolean allHorsesHaveFallen() {
		// Iterate through each horse in the lane and check if it has fallen
		for (Horse horse : laneHorse) {
			if (horse != null && !horse.hasFallen()) {
				return false; // If any horse has not fallen, return false
			}
		}
		return true; // If all horses have fallen, return true
	}

	/**
	 * Starts the horse race simulation.
	 */
	public void startRace() {
		boolean hasWinner = false; // Flag to track if a winner has been determined

		resetLanes(); // Reset the positions of all horses

		// Continue racing until there is a winner or all horses have fallen
		while (!hasWinner) {
			try {
				moveAllHorses(); // Move all horses forward in their respective lanes
			} catch (NullPointerException e) {
				System.out.println("Please initialize all horses");
			}
			// Check if any horse has won the race or all horses have fallen
			if (raceWonByAny() || allHorsesHaveFallen()) {
				hasWinner = true; // Set the flag to true if there is a winner or all horses have fallen
				printRace(); // Print the final race results
			}
		}

		// Pause briefly before printing the winner
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			System.err.println("Error while sleeping: " + e.getMessage());
		} finally {
			printWinner(); // Print the name of the winning horse or declare a draw
		}
	}

	/**
	 * Moves all horses forward in their respective lanes.
	 */
	private void moveAllHorses() {
		// Iterate through each horse in the lane and move it forward
		for (Horse horse : laneHorse) {
			moveHorse(horse);
		}
	}

	/**
	 * Moves a horse forward with a certain probability and checks if it falls.
	 * 
	 * @param theHorse The horse to move
	 */
	private void moveHorse(Horse theHorse) {
		if (!theHorse.hasFallen() && Math.random() < theHorse.getConfidence()) {
			theHorse.moveForward();
		} else if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence())) {
			theHorse.fall();
		}
	}

	/**
	 * Prints the name of the winning horse or declares a draw.
	 */
	private void printWinner() {
		for (Horse horse : laneHorse) {
			if (raceWonBy(horse)) {
				System.out.println(horse.getName() + " won the race");
				return;
			}
		}
		System.out.println("All horses have fallen! The race is a draw.");
	}

	/**
	 * Checks if any horse has won the race.
	 * 
	 * @return True if any horse has won, otherwise false
	 */
	private boolean raceWonByAny() {
		for (Horse horse : laneHorse) {
			if (raceWonBy(horse)) {
				return true; // If any horse has won, return true
			}
		}
		return false; // If no horse has won, return false
	}

	/**
	 * Checks if a specific horse has won the race.
	 * 
	 * @param theHorse The horse to check
	 * @return True if the horse has won, otherwise false
	 */
	private boolean raceWonBy(Horse theHorse) {
		return theHorse != null && theHorse.getDistanceTravelled() == raceLength;
	}

	/**
	 * Prints the current state of the race, including horse positions and
	 * confidence levels.
	 */
	private void printRace() {
		System.out.print('\u000C'); // Clear the terminal window

		// Print top edge of track
		multiplePrint("=", raceLength + 3);
		System.out.println();

		// Print each lane with horse position and confidence
		for (Horse horse : laneHorse) {
			printLane(horse);
			System.out.print(horse.getName() + " (Current Confidence " + horse.getConfidence() + ")");
			System.out.println();
		}

		// Print bottom edge of track
		multiplePrint("=", raceLength + 3);
		System.out.println();
	}

	/**
	 * Prints a lane with a horse symbol or fallen marker.
	 * 
	 * @param theHorse The horse in the lane
	 */
	private void printLane(Horse theHorse) {
		int spacesBefore = theHorse != null ? theHorse.getDistanceTravelled() : 0;
		int spacesAfter = raceLength - spacesBefore - 1;

		// Adjust the position of the finish line marker to stay within the race
		// boundary
		if (spacesBefore >= raceLength) {
			spacesBefore = raceLength - 1;
			spacesAfter = 0;
		}

		System.out.print('|');
		multiplePrint(" ", spacesBefore);

		// Print the horse symbol or fallen marker with appropriate formatting
		if (theHorse != null && theHorse.hasFallen()) {
			System.out.print("\u001B[31mX\u001B[0m "); // Print a red X if the horse has fallen
		} else {
			System.out.print(theHorse != null ? theHorse.getSymbol() : ' '); // Print the horse symbol
		}

		multiplePrint(" ", spacesAfter);
		System.out.print('|');
	}

	/**
	 * Prints a specified character multiple times.
	 * 
	 * @param aChar The character to print
	 * @param times The number of times to print the character
	 */
	private void multiplePrint(String aChar, int times) {
		for (int i = 0; i < times; i++) {
			System.out.print(aChar);
		}
	}
}

