import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class PebbleGame implements PebbleGameInterface{
	// white bags
	ArrayList<Pebble> bagA = new ArrayList<>();
	ArrayList<Pebble> bagB = new ArrayList<>();
	ArrayList<Pebble> bagC = new ArrayList<>();
	
	// black bags
	ArrayList<Pebble> bagX = new ArrayList<>();
	ArrayList<Pebble> bagY = new ArrayList<>();
	ArrayList<Pebble> bagZ = new ArrayList<>();
	
	private int noOfPlayers;
	volatile private boolean gameOver = false;
	private boolean requirePlayerInput = true;

	public int getNoOfPlayers() {
		return noOfPlayers;
	}

	/**
	 * Player inner class
	 */
	public class Player implements Runnable {
		private ArrayList<Pebble> hand = new ArrayList<>();
		private int currentHand;
		private ArrayList<Pebble> nextWhiteBagDiscard;
		private int playerID;
		private File file;

		/**
		 * Take 10 random pebbles from a black bag, where black bag is chosen at random.
		 */
		public void generateStartingHand() {
			Random rand = new Random();
			double randDouble = rand.nextDouble();
			
			// select black bag at random
			if(randDouble < 1.0/3.0) {
				for(int i=0; i<10; i++) {
					hand.add(startingDraw("X"));
				}
				nextWhiteBagDiscard = bagA;
			}
			else if(randDouble < 2.0/3.0) {
				for(int i=0; i<10; i++) {
					hand.add(startingDraw("Y"));
				}
				nextWhiteBagDiscard = bagB;
			} else {
				for(int i=0; i<10; i++) {
					hand.add(startingDraw("Z"));
				}
				nextWhiteBagDiscard = bagC;
			}
			
		}

		/**
		 * Check if the weight of pebbles held by a player is exactly 100.
		 */
		public void checkWon() {
			currentHand = 0;
			for(Pebble p: hand) {
				currentHand += p.getWeight();
			}
			if(currentHand == 100) {
				// set flag
				gameOver = true;
				System.out.println("player" + playerID + " has won!");
			}
		}

		/**
		 * Randomly pick a pebble from the current hand of the player.
		 *
		 * @return randomly chosen pebble
		 */
		public Pebble choosePebble() {
			Random rand = new Random();
			int randInt = rand.nextInt(hand.size());
			return hand.get(randInt);
		}

		/**
		 * Discard a pebble to a white bag.
		 *
		 * @return the pebble that was discarded
		 */
		public Pebble discardPebble() {
			// safely add to white bag and remove from hand
			Pebble pebbleToDiscard = choosePebble();
			synchronized(nextWhiteBagDiscard) {
				nextWhiteBagDiscard.add(pebbleToDiscard);
			}
			hand.remove(pebbleToDiscard);
			return pebbleToDiscard;
		}

		/**
		 * Write the initial 10 pebbles of a player to player output file.
		 */
		public void writeStartingHandToFile () {
			//find the bag the pebbles came from
			String bag;
			if (nextWhiteBagDiscard == bagA) {
				bag = "X";
			} else if (nextWhiteBagDiscard == bagB) {
				bag = "Y";
			} else {
				bag = "Z";
			}

			//write starting hand to file
			BufferedWriter out = null;
			try {
				file = new File("player" + playerID + "_output.txt");
				out = new BufferedWriter(new FileWriter(file));
				out.write("player" + playerID + " starting hand from bag " + bag + " is " + hand.toString() + "\n\n");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}

		}

		/**
		 * Write to player output file, the discarded pebble value and the associated bag
		 *
		 * @param pebble discarded to white bag
		 */
		public void writeDiscardToFile(Pebble pebble) {
			//find the bag the pebble was discarded to
			String bag;
			if (nextWhiteBagDiscard == bagA) {
				bag = "A";
			} else if (nextWhiteBagDiscard == bagB) {
				bag = "B";
			} else {
				bag = "C";
			}

			//write draw details to file
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new FileWriter(file, true));
				out.write("player" + playerID + " has discarded a " + pebble + " to bag " + bag
						+ "\nplayer" + playerID + " hand is " + hand.toString() + "\n\n");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * Write to player output file, the drawn pebble value and the associated bag.
		 *
		 * @param pebble drawn from black bag
		 */
		public void writeDrawToFile(Pebble pebble) {
			//find the bag the last pebble came from
			String bag;
			if (nextWhiteBagDiscard == bagA) {
				bag = "X";
			} else if (nextWhiteBagDiscard == bagB) {
				bag = "Y";
			} else {
				bag = "Z";
			}

			//write draw details to file
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new FileWriter(file, true));
				out.write("player" + playerID + " has drawn a " + pebble + " from bag " + bag
						+ "\nplayer" + playerID + " hand is " + hand.toString() + "\n\n");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}

		public void run() {
			String playerName = Thread.currentThread().getName();
			playerID = Integer.parseInt(playerName.split("-")[1]) + 1;

			// take 10 pebbles from a black bag
			this.generateStartingHand();
			writeStartingHandToFile();
			
			// check if player won with initial hand
			checkWon();
			
			// each player now has 10 pebbles and the game can begin
			while(!gameOver) { //gameOver - volatile and data freshness
				// play

				Pebble discardedPebble = discardPebble();
				writeDiscardToFile(discardedPebble);
				
				
				// safely select black bag at random and draw pebble
				Pebble pebbleDrawn = drawFromBlackBag(this);
				hand.add(pebbleDrawn);

				writeDrawToFile(pebbleDrawn);

				checkWon();
			}
		}
	}

	/**
	 * Randomly draw pebble from black bag.
	 * If black bag is empty, refill with pebbles from the corresponding white bag.
	 *
	 * @param player
	 * @return pebble drawn
	 */
	public synchronized Pebble drawFromBlackBag(Player player) {
		// to keep track of nextWhiteBagDiscard on player, we pass player as argument
		Random rand = new Random();
		double randDouble = rand.nextDouble();
		
		if(randDouble < 1.0/3.0) {
			int rnd = new Random().nextInt(bagX.size());
			Pebble pebble = bagX.get(rnd);
			bagX.remove(rnd);
			player.nextWhiteBagDiscard = bagA;
			
			// check if black bag is empty
			if(bagX.size() == 0) {
				// empty white bag pebbles into black bag
				for(int i=0; i<bagA.size(); i++) {
					bagX.add(i, bagA.get(i));
				}
			}
			
			return pebble;
		}
		else if(randDouble < 2.0/3.0) {
			int rnd = new Random().nextInt(bagY.size());
			Pebble pebble = bagY.get(rnd);
			bagY.remove(rnd);
			player.nextWhiteBagDiscard = bagB;
			
			// check if black bag is empty
			if(bagY.size() == 0) {
				// empty white bag pebbles into black bag
				for(int i=0; i<bagB.size(); i++) {
					bagY.add(i, bagB.get(i));
				}
			}
			
			return pebble;
		} else {
			int rnd = new Random().nextInt(bagZ.size());
			Pebble pebble = bagZ.get(rnd);
			bagZ.remove(rnd);
			player.nextWhiteBagDiscard = bagC;
			
			// check if black bag is empty
			if(bagZ.size() == 0) {
				// empty white bag pebbles into black bag
				for(int i=0; i<bagC.size(); i++) {
					bagZ.add(i, bagC.get(i));
				}
			}
			
			return pebble;
		}
	}
	
	public synchronized Pebble startingDraw(String bag) {
		if(bag.equals("X")) {
			// draw from bagX
			Random rand = new Random();
			int randInt = rand.nextInt(this.bagX.size());
			Pebble pebbleDrawn = this.bagX.get(randInt);
			this.bagX.remove(randInt);
			return pebbleDrawn;
			
		}else if(bag.equals("Y")) {
			// draw from bagY
			Random rand = new Random();
			int randInt = rand.nextInt(this.bagY.size());
			Pebble pebbleDrawn = this.bagY.get(randInt);
			this.bagY.remove(randInt);
			return pebbleDrawn;
			
		}else {
			// draw from bagZ
			Random rand = new Random();
			int randInt = rand.nextInt(this.bagZ.size());
			Pebble pebbleDrawn = this.bagZ.get(randInt);
			this.bagZ.remove(randInt);
			return pebbleDrawn;
		}
		
	}

	/**
	 * Read from the file containing the weight of each of the pebbles used in a black bag
	 * at the start of the game and fill the black bag with the pebbles
	 *
	 * @param bagLocation
	 * @param bag
	 * @return
	 * @throws IllegalFileFormatException
	 * @throws IOException
	 * @throws NegativePebbleWeightException
	 */
	public static int initialiseBag(String bagLocation, ArrayList<Pebble> bag) throws IllegalFileFormatException, IOException, NegativePebbleWeightException{
		int noOfPebbles = 0;
		String expression = "^-?\\d+(,-?\\d+)*$";

		BufferedReader br = new BufferedReader(new FileReader(bagLocation));
		String line;
        while((line = br.readLine()) != null){
        	if(!(line.matches(expression))) {
        		throw new IllegalFileFormatException("Please provide legal values for file formatting."
						+ "\nThe legal format of the file is a comma-separated list of integers.\n\n");
            }
        	String[] values = line.split(",");
			for(String x: values){
				noOfPebbles++;
                x = x.replaceAll("\\s+","");
                x = x.replace("\n", "");
                bag.add(new Pebble(Integer.parseInt(x)));
                if (Integer.parseInt(x) <= 0) {
                	throw new NegativePebbleWeightException("Pebbles must be strictly positive integers.\n\n");
				}
             }
        }
		return noOfPebbles;
    }
	
	public void initialiseThreads() {
		for(int i=0; i<this.noOfPlayers; i++) {
			new Thread(new Player()).start();
		}
	}

	public boolean getUserPlayerInput(){
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter the number of players:");
		String userInput = input.nextLine();
		try{
			noOfPlayers = Integer.parseInt(userInput);
			if (noOfPlayers < 1) {
				System.out.println("Number of players must be a positive non-zero value.\n\n");
				return true;
			}
		} catch(NumberFormatException e){
			if(userInput.equals("E")){
				System.exit(0);
			}
			System.out.println("Number of players must be an integer value.\n\n");
			return true;
		}
		return false;
	}

	public boolean getUserBagInput(int bagNo){
		ArrayList<Pebble> bag;
		if(bagNo == 0){
			bag = bagX;
		}else if(bagNo == 1){
			bag = bagY;
		}else{
			bag = bagZ;
		}
		int noOfPebblesInBag;
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter location of bag number " + bagNo + " to load:");
		String bagLocation0 = input.nextLine();
		if(bagLocation0.equals("E")){
			System.exit(0);
		}
		try {
			noOfPebblesInBag = initialiseBag(bagLocation0, bag);
		} catch(IOException e) {
			System.out.println("File not found.\n\n");
			requirePlayerInput = true;
			return false;
		} catch(IllegalFileFormatException e) {
			System.out.println(e.getMessage());
			requirePlayerInput = true;
			return false;
		} catch(NegativePebbleWeightException e) {
			System.out.println(e.getMessage());
			requirePlayerInput = true;
			return false;
		}
		if (noOfPebblesInBag < (noOfPlayers * 11)) {
			System.out.println("Not enough pebbles in this bag for the number of players.\n\n");
			requirePlayerInput = true;
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		PebbleGame game = new PebbleGame();

		while (game.requirePlayerInput) {
			// get number of players
			game.requirePlayerInput = game.getUserPlayerInput();
			if(game.requirePlayerInput == true){
				continue;
			}

			// get bags
			boolean validInput = game.getUserBagInput(0);
			if(!validInput){
				continue;
			}

			boolean validInput1 = game.getUserBagInput(1);
			if(!validInput1){
				continue;
			}

			boolean validInput2 = game.getUserBagInput(2);
			if(!validInput2){
				continue;
			}
		}
        game.initialiseThreads();
	}

}
