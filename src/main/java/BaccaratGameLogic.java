/*
 * Author: Yamaan Nandolia & Jonathan Hung
 * NetID: ynand3@uic.edu & jhung9@uic.edu
 * File Name: BaccaratGameLogic.java
 * Project Name: Baccarat JavaFX GUI
 * System: VSCode on Mac
 * File Description: This class contains the logic for the Baccarat game, including determining the winner,
 *                   calculating hand totals, and evaluating whether the player or banker should draw another card.
 */

import java.util.ArrayList;

public class BaccaratGameLogic {

    /*
     * whoWon: Determines the winner between player and banker hands.
     */
    public String whoWon(ArrayList<Card> hand1, ArrayList<Card> hand2) {

        int playerHand = handTotal(hand1);
        int bankerHand = handTotal(hand2);

        // Checking for natural win, if player or bank have 8 or 9
        if (playerHand == 9 && bankerHand == 9)
            return "Draw";
        else if (playerHand == 9 && bankerHand == 8)
            return "Player";
        else if (playerHand == 8 && bankerHand == 9)
            return "Banker";
        else if (playerHand == 8 && bankerHand == 8)
            return "Draw";

        // If there's no natural win, check for other possibilities
        if (playerHand > bankerHand)
            return "Player";
        else if (handTotal(hand1) < handTotal(hand2))
            return "Banker";
        else
            return "Draw";
    } // end of whoWon()

    /*
     * handTotal: Calculates the total value of a hand.
     */
    public int handTotal(ArrayList<Card> hand) {

        int total = 0;

        for (Card card : hand) {
            // If card is 10, J, K or Q
            if (card.value == 10 || card.value == 11 || card.value == 12 || card.value == 13) {
                total += 0;
            // If card is A
            } else if (card.value == 14) {
                total += 1;
            } else {
                total += card.value;
            }
        }
        return total % 10; // Get a value between 0-9
    } // end of handTotal()

    /*
     * evaluateBankerDraw: Evaluates whether the banker should draw another card.
     */
    public boolean evaluateBankerDraw(ArrayList<Card> hand, Card playerCard) {

        int handValue = handTotal(hand);

        if (handValue == 7) { return false; }
        else if (handValue <= 2) { return true; }

        // Player card is 2, 3, 4, 5, 6, 7, 9, 10, J, K, Q, A
        else if (handValue == 3 && playerCard.value != 8) { return true; }

        // Player card is 2, 3, 4, 5, 6, 7
        else if (handValue == 4 && (playerCard.value == 2 || playerCard.value == 3 ||
                playerCard.value == 4 || playerCard.value == 5 ||
                playerCard.value == 6 || playerCard.value == 7)) { return true; }

        // Player card is 4, 5, 6, 7
        else if (handValue == 5 && (playerCard.value == 4 || playerCard.value == 5 ||
                playerCard.value == 6 || playerCard.value == 7)) { return true; }

        // Player card is 6, 7
        else return handValue == 6 && (playerCard.value == 6 || playerCard.value == 7);

    } // end of evaluateBankerDraw()

    /*
     * evaluatePlayerDraw: Evaluates whether the player should draw another card.
     */
    public boolean evaluatePlayerDraw(ArrayList<Card> hand) {

        return handTotal(hand) <= 5;
    } // end of evaluatePlayerDraw()
} // end of BaccaratGameLogic class
