import java.util.Random;
/**
 * A class that represents a Monty Hall problem simulator
 * @author Azra Demirovic
 *
 */
public class MontyHall {
	/**
	 * Number of doors in simulation
	 */
	private int numDoors = 3;
	/**
	 * Tracks behind which door is the prize car
	 */
	private int prize = 0;

	public MontyHall(int numDoors) {
		MontyStart(numDoors);
	}

	/**
	 * Method that starts the simulation
	 * @param numDoors: number od doors
	 */
	public void MontyStart(int numDoors) {
		this.numDoors = numDoors;
		Random randomGenerator = new Random();
		prize = randomGenerator.nextInt(numDoors);
	}

	/**
	 * Method that returns which door should remain closed with the chosen door.
	 * @param chosen: which door was chosen
	 * @return closed: which door should remain closed
	 */
	public int openDoor(int chosen) {
		//by default the closed door should be the door with the prize behind it
		int closed = prize;
		//but if the chosen door has a prize behind it, the closed door should be randomly selected
		if (chosen == prize) {
			while (true) {
				Random randomGenerator = new Random();
				closed = randomGenerator.nextInt(numDoors);
				if (closed != chosen) {
					break;
				}
			}
		}
		return closed;
	}

	/**
	 * Method that check if the chosen door has the prize behind it
	 * @param chosen: door that was chosen
	 * @return true it there is a prize, false otherwise
	 */
	public boolean checkPrize(int chosen) {
		return chosen == prize;
	}

}
