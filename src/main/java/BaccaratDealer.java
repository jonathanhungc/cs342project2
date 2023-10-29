/*
 * Author: Yamaan Nandolia & Jonathan Hung
 * NetID: ynand3@uic.edu & jhung9@uic.edu
 * File Name: BaccaratDealer.java
 * Project Name: Baccarat JavaFX GUI
 * System: VSCode on Mac
 * File Description: Represents the dealer in the Baccarat game, responsible for managing the deck and dealing card
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/*
* BaccaratDealer: This class manages the deck of cards and performs actions such as dealing hands and shuffling the deck.
*/
public class BaccaratDealer {
    ArrayList<Card> deck;

    /*
     * generateDeck: Generates a standard deck of cards with four suits.
     *              - Jack = 11
     *              - Queen = 12
     *              - King = 13
     *              - Ace = 14
     */
    public void generateDeck() {

        deck = new ArrayList<>();

        String[] suit = {"clubs", "spades", "hearts", "diamonds"};

        for (String s : suit) {

            for (int j = 2; j < 15; j++) {
                deck.add(new Card(s, j));
            }
        }
    } // end of generateDeck()

    /*
     * dealHand: Deals a hand of two cards from the deck.
     */
    public ArrayList<Card> dealHand() {

        if (deck.isEmpty())
            shuffleDeck(); // generate deck if there are no cards

        ArrayList<Card> hand = new ArrayList<>();

        for (int i = 0; i < 2; i++) {

            hand.add(deck.remove(new Random().nextInt(deck.size())));

            if (deck.isEmpty())
                shuffleDeck(); // generate deck if there are no cards
        }

        return hand;
    } // end of dealHand()

    /*
     * drawOne: Draws a single card from the deck.
     */
    public Card drawOne() {

        if (deck.isEmpty())
            shuffleDeck(); // generate deck if there are no cards

        return deck.remove(new Random().nextInt(deck.size()));
    } // end of drawOne()

    /*
     * deckSize: Returns the current size of the deck.
     */
    public int deckSize() {
        return deck.size();
    } // end of deckSize()

    /*
     * shuffleDeck: Shuffles the deck using a random seed.
     */
    public void shuffleDeck() {
        generateDeck();
        Collections.shuffle(deck, new Random(System.currentTimeMillis()));
    } // end of shuffleDeck()
    // end of class
}
