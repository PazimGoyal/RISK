package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.*;

import model.CardTypes;
import model.Country;
import view.*;

/**
 * This class handles events of the User Interface
 * 
 * @author pazim and bhargav
 * @version 1.o
 */

@SuppressWarnings("deprecation")
public class MyActionListner extends Observable implements ActionListener {

	MFrame frame;
	MainControll controll;
	List<String> Phases;
	String currentPhase;
	int players = 3;
	public int currentPlayer = 0;
	Country attackCountry1, attackCountry2;
	Country fortifyCountry1, fortifyCountry2;

	public MyActionListner(MainControll controll) {
		this.controll = controll;

		Phases = new ArrayList<>();
		Phases.add("Finish Reinforcement");
		Phases.add("Finish Attack");
		Phases.add("Finish Fortification");
		currentPhase = Phases.get(0);
		players = controll.PlayerNo();
	}

	public void playerUpdate() {
		if (currentPlayer >= players - 1)
			currentPlayer = 0;
		else
			currentPlayer++;
	}

	/**
	 * This method display the armies that are not deployed
	 * 
	 * @param country:
	 *            object
	 */
	public void ReinforcementPhase2(Country country) {
		// controll.AddArmies(currentPlayer);
		String message = controll.reinforcementController.addarmies(country);
		controll.frame.noArmiesLeft = controll.playerObjet(currentPlayer).getPlayerArmiesNotDeployed();
		changed();
		if (!message.equals(""))
			controll.frame.error(message);
	}

	/**
	 * This method display number of armies player can deploy
	 */
	public void ReinforcementPhase() {

		MFrame3 frame3 = new MFrame3();
		List<CardTypes> cardTypes=new ArrayList<>();
		if(cardTypes.size()>0) {
		frame3.fun(cardTypes);}
		changed();
		controll.frame.ActivateAll();
		controll.OnlyNeeded(controll.playerObjet(currentPlayer).getTotalCountriesOccupied());
		controll.reinforcementController.calculateReinforcementArmies(controll.playerObjet(currentPlayer));
	}

	/**
	 * This method
	 */
	public void FortificationPhase() {
		changed();
		controll.frame.ActivateAll();
		controll.OnlyNeeded(controll.playerObjet(currentPlayer).getTotalCountriesOccupied());
		playerUpdate();
	}

	/**
	 * This method does the validations of fortification phase
	 * 
	 * @param country:
	 *            country object
	 * @throws IOException
	 */
	public void FortificationPhase2(Country country) throws IOException {

		if (fortifyCountry1 == null) {
			fortifyCountry1 = country;
			controll.frame.CCC = controll.NeighboursList(country);
			changed();
			controll.frame.error("Select One More Country You Want to move your Armies to");
		} else if (fortifyCountry2 == null) {
			fortifyCountry2 = country;
			if (fortifyCountry1.equals(fortifyCountry2)) {
				controll.frame.error("SAME COUNTRY SELECTED");
				fortifyCountry1 = null;
				fortifyCountry2 = null;

			} else {
			try {	
				String test1 = controll.frame.popupText(fortifyCountry1.getNoOfArmies() - 1);
				String message = controll.fortificationController.moveArmies(fortifyCountry1, fortifyCountry2,
						Integer.parseInt(test1));
				if (!message.equals("")) {
					controll.frame.error(message);
					fortifyCountry1 = null;
					fortifyCountry2 = null;
				} else {
					controll.RefreshButtons();
					currentPhase = "Finish Reinforcement";
					controll.frame.nextAction.setText("Finish Reinforcement");
					// playerUpdate();
					fortifyCountry1 = null;
					fortifyCountry2 = null;
					controll.frame.ActivateAll();
					ReinforcementPhase();
				}}catch (Exception e) {
					// TODO: handle exception
					controll.frame.error("Invalid Number");
				}

			}
		}

	}

	/**
	 * This method check validations of attack phase
	 * 
	 * @param country:
	 *            object
	 * @throws IOException
	 */
	public void AttackPhase(Country country) throws IOException {
		changed();
		if (attackCountry1 == null) {
			attackCountry1 = country;
			controll.frame.ActivateAll();
			List<Country> abc = controll.attackController.getMyNeighborsForAttack(country);
			if (abc.size() < 1) {
				controll.frame.ActivateAll();
				attackCountry1 = null;
				attackCountry2 = null;
				controll.frame.error("No Neighbours to attack");
				controll.OnlyNeeded(controll.playerObjet(currentPlayer).getTotalCountriesOccupied());
				controll.RefreshButtons();
			} else {
				controll.frame.OnlyNeeded(abc);
				controll.RefreshButtons();
			}
		} else if (attackCountry2 == null) {
			attackCountry2 = country;
			String test1 = controll.frame.popupText(2);
			String reply = controll.attackController.attackButton(attackCountry1, attackCountry2);
			if (!reply.equals("")) {
				controll.frame.error(reply);
			}
			controll.frame.AAA = controll.attackController.attackerDiceRollOutput.toString();
			controll.frame.BBB = controll.attackController.defenderDiceRollOutput.toString();
			changed();
			controll.attackController.attackerDiceRollOutput.clear();
			controll.attackController.defenderDiceRollOutput.clear();
			controll.frame.ActivateAll();
			attackCountry1 = null;
			attackCountry2 = null;
			controll.OnlyNeeded(controll.playerObjet(currentPlayer).getTotalCountriesOccupied());
			controll.RefreshButtons();
			controll.PaintCountries();

		} else {
			attackCountry1 = null;
			attackCountry2 = null;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (Phases.contains(e.getActionCommand())) {
			if (e.getActionCommand() == "Finish Reinforcement") {
				if (controll.playerObjet(currentPlayer).getPlayerArmiesNotDeployed() > 0) {
					controll.frame.error("Connot End Reinforcement Untill All armies are deployed");
				} else {
					currentPhase = "Finish Attack";
					controll.frame.nextAction.setText("Finish Attack");
					attackCountry1 = null;
					attackCountry2 = null;
				}

			} else if (e.getActionCommand() == "Finish Attack") {
				currentPhase = "Finish Fortification";
				controll.frame.nextAction.setText("Finish Fortification");
				fortifyCountry1 = null;
				fortifyCountry2 = null;
				FortificationPhase();
			} else if (e.getActionCommand() == "Finish Fortification") {
				currentPhase = "Finish Reinforcement";
				controll.frame.nextAction.setText("Finish Reinforcement");
				ReinforcementPhase();
			}

		} else {
			controll.frame.noArmiesLeft = controll.playerObjet(currentPlayer).getPlayerArmiesNotDeployed();
			String Cname = e.getActionCommand().split("\\|")[0].trim();
			// JButton temp = controll.frame.hashButton.get(Cname);
			Country temp2 = controll.countryObjects().get(Cname);
			if (currentPhase.equals("Finish Reinforcement")) {
				ReinforcementPhase2(temp2);
				try {
					controll.RefreshButtons();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} else if (currentPhase.equals("Finish Fortification")) {
				try {
					FortificationPhase2(temp2);
				} catch (IOException e2) {
					e2.printStackTrace();
				}

			} else if (currentPhase.equals("Finish Attack")) {
				try {
					AttackPhase(temp2);
				} catch (IOException e2) {
					e2.printStackTrace();
				}

			}

		}

	}

	public ArrayList<Float> CountriesPercentage() {
		return controll.CountriesPercentage();
	}

	public ArrayList<String> ContinentsOccupied() {
		return controll.ContinentsOccupied();
	}

	public void changed() {

		setChanged();
		notifyObservers();
	}

}