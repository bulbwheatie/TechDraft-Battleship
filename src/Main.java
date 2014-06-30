import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Main {

	private String baseUrl = "https://student.people.co/api/challenge/battleship/6f921afeac9c/boards/";
	private static String board = "live_board_4";
	String charset = "UTF-8";
	//0 unknown, -1 nothing, 1 hit
	private int[][] currentBoard = new int[10][11];
	
	private boolean destoryer = false;
	private boolean submarine = false;
	private boolean patrol = false;
	private boolean battleship = false;
	private boolean carrier = false;
	
	private int largestShip = 5;
	private int lastHitX = 0;
	private int lastHitY = 0;
	private int lastDirection = 0;
	private int searchRadius = 0;
	
	public int counter = 0;
	
	public static void main(String[] args) {
		//while not finished, calculate new shot
		Main program = new Main();
		program.runBattleship(board);
		
	}
	
	public void runBattleship(String board) {
		boolean hit = false;
		
		while (!getStatus(board)) {
			String coord = calculateNextShot(hit);
			if (fireShot(coord)) {
				hit = true;
				System.out.println("Hit at " + coord);
				//printBoard(currentBoard);
			} else {
				hit = false;
			}
		} 
		System.out.println("We're done!");
	}
	
	public boolean getStatus(String board) {
		boolean isFinished = false;
		String value = "false";
		String statusRequest = baseUrl + board;
		URLConnection connection = null;
		InputStream response = null;
		try {
			connection = new URL(statusRequest).openConnection();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (connection != null) {
			connection.setRequestProperty("Accept-Charset", charset);

			try {
				response = connection.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(response));
			String inputLine;
			try {
				while ((inputLine = in.readLine()) != null) {
				    //System.out.println(inputLine);
				    value = jsonGetValue(inputLine, "is_finished", value);
				    //System.out.println("Value " + value);
				   /* if (Boolean.parseBoolean(jsonGetValue(inputLine, "sunk_carrier", value))) {
				    	largestShip = 4;
				    	if (Boolean.parseBoolean(jsonGetValue(inputLine, "sunk_battleship", value))) {
				    		largestShip = 3;
				    		if (Boolean.parseBoolean(jsonGetValue(inputLine, "sunk_patrol", value)) && Boolean.parseBoolean(jsonGetValue(inputLine, "sunk_destroyer", value))) {
				    			largestShip = 2;
						    }
					    }
				    }	*/		    
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		isFinished = Boolean.parseBoolean(value);
		//System.out.println("IsFinished: " + value);
		return isFinished;
	}
	
	public boolean fireShot(String coord) {
		boolean hit = false;
		String fireUrl = baseUrl + board + "/" + coord; 
		String value = "false";
		String sunk = "";
		URLConnection connection = null;
		InputStream response = null;
		try {
			connection = new URL(fireUrl).openConnection();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (connection != null) {
			connection.setRequestProperty("Accept-Charset", charset);

			try {
				response = connection.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(response));
			String inputLine;
			try {
				while ((inputLine = in.readLine()) != null) {
				    //System.out.println(inputLine);
				    value = jsonGetValue(inputLine, "is_hit", value);
				    //sunk = jsonGetValue(inputLine, "sunk", sunk);
				    //System.out.println(value);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		hit = Boolean.parseBoolean(value);
		//System.out.println("Hit " + hit);
		return hit;
	}
	
	//Input: boolean - true if the last shot was a hit
	//		 (x, y) - coordinates of last hit
	public String calculateNextShot(boolean hit) {
		String coord = randomShot();
		return coord;
	}
	
	public String randomShot() {
		String coordinate = "";
		int x = (int) (Math.random() * 10);
		int y = (int) (Math.random() * 10);
		while (currentBoard[x][y] != 0) {
			x = (int) (Math.random() * 10);
			y = (int) (Math.random() * 10);
		}
		currentBoard[x][y] = 1;
		char col = (char) (x + 65);
		coordinate = "" + col + (y + 1);
		System.out.println("Fire on " + coordinate);
		return coordinate;		
	}
	
	//Search in all directions with the largest possible ship radius left
	public String finishShip() {
		String coordinate = "";
		//Take the coordinate of the last hit and expand in each direction for the largest ship size left.
		return coordinate;
	}
	
	//Update the largest ship left
	public void updateLargestShip(String shipSunk) {
		switch(shipSunk) {
		case "destroyer":
			break;
		case "carrier":
			break;
		case "battleship":
			break;
		case "submarine":
			break;
		case "patrol":
			break;
		default:
			break;
		}
	}
	
	public String jsonGetValue(String json, String field, String curr) {
		String value = "";
		if (json.toLowerCase().contains(field)) {
			value = json.split(":")[1].trim().replace("\"", "").replace(",","");
			//System.out.println(value);
			return value;
		} else {
			return curr;
		}

	}
	
	public void printBoard(int[][] board) {
		for (int x = 0; x < 10; x++) {
			System.out.println("");
			for (int y = 0; y < 10; y++) {
				System.out.print("" + board[x][y]);
			}
		}
		System.out.println("");
	}
}

