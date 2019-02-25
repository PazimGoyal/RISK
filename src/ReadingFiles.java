import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class ReadingFiles {
	// public static HashMap<String, Player> playerObject;
	public static HashMap<Integer, Player> playerId;
	public static ArrayList<Integer> players;
	public static ArrayList<String> CountriesNames, ContinentNames; // List of countries Strings
	public static HashMap<String, Country> CountryNameObject;
	public static HashMap<String, Continent> ContinentNameObject; // HashMAp COntaining Country name as // // key and
	// returns country object

	public static void Reads() throws IOException {
		CountryNameObject = new HashMap<>();
		CountriesNames = new ArrayList<>();
		ContinentNames = new ArrayList<>();
		ContinentNameObject = new HashMap<>();
		// Reading Country File
		FileReader file = new FileReader("Resources/Asia.map");
		BufferedReader bufferedReader = new BufferedReader(file);
		String temp = "";
		StringBuffer buffer = new StringBuffer();
		while ((temp = bufferedReader.readLine()) != null) {
			buffer.append(temp + "\n");
		}
		bufferedReader.close();
		String string = "\\[.*]";
		String[] aaa = buffer.toString().trim().replaceAll("\n+", "\n").split(string);
		String InfoString = aaa[1].trim();
		String ContinentsString = aaa[2].trim();
		String CountriesString = aaa[3].trim();
		String[] tempContinentArray = ContinentsString.split("\n");

		for (int i = 0; i < tempContinentArray.length; i++) {
			String temporary = tempContinentArray[i].split("=")[0].trim().toLowerCase();
			int value = Integer.parseInt(tempContinentArray[i].split("=")[1].trim());
			ContinentNames.add(temporary);
			ContinentNameObject.put(temporary, new Continent(value, temporary));
		}
		String[] tempCountryArray = CountriesString.split("\n");
		for (int i = 0; i < tempCountryArray.length; i++) {
			String a = tempCountryArray[i].split(",")[0].trim();
			CountriesNames.add(a);
			CountryNameObject.put(a, new Country(a));
		}
		System.out.println(CountriesString);
		for (int i = 0; i < tempCountryArray.length; i++) {
		String[] a = tempCountryArray[i].split(",");
		Country temp1 = CountryNameObject.get(a[0].trim());
			Continent temp2 = ContinentNameObject.get(a[3].trim().toLowerCase());
			temp1.setContinent(temp2);

			temp2.addCountrie(temp1);
			for (int j = 4; j < a.length; j++) {
				temp1.addNeighbors(CountryNameObject.get(a[j].trim()));
			}
		}
		ArrayList<String> CountriesNames2 =  (ArrayList<String>) CountriesNames.clone();
		Collections.shuffle(CountriesNames2);

//		playerObject = new HashMap<>();
		players=new ArrayList<>();
		playerId = new HashMap<>();
		ArrayList<Color> arrayListc = new ArrayList<>();
		arrayListc.add(Color.cyan);
		arrayListc.add(Color.GREEN);
		arrayListc.add(Color.decode("#ffff00"));
		arrayListc.add(Color.decode("#FF6600"));
		arrayListc.add(Color.WHITE);
		arrayListc.add(Color.decode("#CCFF00"));
		int noofplayers=Integer.parseInt( AssignCountries.NumberOfPlayers);
		for (int i = 0; i < noofplayers; i++) {
			Player player = new Player(i);
			player.setPlayerColor(arrayListc.get(i));
			playerId.put(i, player);
			players.add(i);
		}
		try {
			int n=noofplayers;
		for (int i = 0; i < CountriesNames2.size(); i++) {
				for (int j = 0; j < noofplayers; j++) {
					Country temp1=CountryNameObject.get(CountriesNames2.get(i+j));
					Player tempPlayer=playerId.get(j);
					temp1.setPlayer(tempPlayer);
					tempPlayer.addCountriesOccupied(temp1);
				}
				i=i+n-1;
			}
		} catch (Exception e) {
		}

	}
}