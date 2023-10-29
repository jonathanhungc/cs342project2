/*
 * Author: Yamaan Nandolia & Jonathan Hung
 * NetID: ynand3@uic.edu & jhung9@uic.edu
 * File Name: MyTest.java
 * Project Name: Baccarat JavaFX GUI
 * System: VSCode on Mac
 * File Description: These are the test cases of the file. Test cases include tests for checking logic of the game,
 * 					 calculations of points, checking conditions including edge cases and also stress cases and lastly
 * 					 checking rules and aspects like randamization of cards. 
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Class for testing BaccaratGame functionality
class MyTest {

    BaccaratGame game;
    BaccaratGameLogic gameLogic;
    BaccaratDealer dealer;
    ArrayList<Card> hand1;
    ArrayList<Card> hand2;
    ArrayList<Card> hand3;
    ArrayList<Card> hand4;

    // Initialization before each test
    @BeforeEach
    void init() {
        game = new BaccaratGame();
        gameLogic = new BaccaratGameLogic();
        dealer = new BaccaratDealer();
        dealer.shuffleDeck();  // Shuffling the deck before each test

        // Setting up hands for testing
        hand1 = new ArrayList<>();
        hand1.add(new Card("hearts", 5)); // 5 of hearts
        hand1.add(new Card("clubs", 11)); // jack of clubs (worth 0)

        hand2 = new ArrayList<>();
        hand2.add(new Card("diamonds", 3)); // 3 of diamonds
        hand2.add(new Card("spades", 3)); // 3 of spades

        hand3 = new ArrayList<>();
        hand3.add(new Card("hearts", 3)); // 3 of hearts
        hand3.add(new Card("diamonds", 5)); // 5 of diamonds

        hand4 = new ArrayList<>();
        hand4.add(new Card("clubs", 5)); // 5 of clubs
        hand4.add(new Card("spades", 14)); // ace of diamonds (worth 1)
    } // end of init()

    // Testing the evaluateWinnings method in BaccaratGame class
    @Test
    void evaluateWinnings() {
        game.gameLogic = gameLogic;

        // Test case 1
        game.playerHand = hand1; // hand1 total is 5
        game.bankerHand = hand2; // hand2 total is 6

        game.totalWinnings = 0;
        game.currentBet = 10;
        game.handBet = "Banker";
        assertEquals(19.5, game.evaluateWinnings()); // user bet 10 on banker, should get 19.5 back

        // Test case 2
        game.playerHand = hand2; // hand2 total is 6
        game.bankerHand = hand4; // hand4 total is 6

        game.totalWinnings = 0;
        game.currentBet = 10;
        game.handBet = "Draw";
        assertEquals(90, game.evaluateWinnings()); // user bet 10 on banker, should get 19.5 back
    } // end of evaluateWinnings()

    // Testing the whoWon and handTotal methods in BaccaratGameLogic class
    @Test
    void whoWon() {
        assertEquals("Player", gameLogic.whoWon(hand3, hand1)); // hand3 = player (8), hand1 = banker (5)
        assertEquals("Banker", gameLogic.whoWon(hand1, hand4)); // hand1 = player (5), hand4 = banker (6)
        assertEquals("Draw", gameLogic.whoWon(hand2, hand4)); // both hands are worth 6
    } // end of whoWon()

    @Test
    void handTotal() {
        assertEquals(5, gameLogic.handTotal(hand1));
        assertEquals(6, gameLogic.handTotal(hand2));
        assertEquals(8, gameLogic.handTotal(hand3));
        assertEquals(6, gameLogic.handTotal(hand4));
    } // end of handTotal()

    // Testing the evaluatePlayerDraw and evaluateBankerDraw methods in BaccaratGameLogic class
    @Test
    void evaluatePlayerDraw() {
        assertTrue(gameLogic.evaluatePlayerDraw(hand1)); // hand1 = 5, so should get another card (true)
        assertFalse(gameLogic.evaluatePlayerDraw(hand2)); // hand2 = 6, should return false
        assertFalse(gameLogic.evaluatePlayerDraw(hand3)); // hand3 = 8, should return false
    } // end of evaluatePlayerDraw()

    @Test
    void evaluateBankerDraw() {
        // hand1 = 5 and card = 6, should return true
        assertTrue(gameLogic.evaluateBankerDraw(hand1, new Card("spades", 6)));

        // hand1 = 5 and card = 2, should return false
        assertFalse(gameLogic.evaluateBankerDraw(hand1, new Card("spades", 2)));

        // hand2 = 6 and card is 7, should return true
        assertTrue(gameLogic.evaluateBankerDraw(hand2, new Card("diamonds", 7)));
    } // end of evaluateBankerDraw()

    // Testing the generateDeck, dealHand, drawOne, shuffleDeck, and deckSize methods in BaccaratDealer class
    @Test
    void generateDeck() {
        dealer.generateDeck();
        assertEquals(new Card("clubs", 2).value, dealer.deck.get(0).value); // first card of deck should be 2 of clubs
        assertEquals(new Card("clubs", 4).value, dealer.deck.get(2).value); // card should be 4 of clubs
        assertEquals(new Card("spades", 12).value, dealer.deck.get(23).value); // card should be Queen of clubs
        assertEquals(new Card("spades", 12).suite, dealer.deck.get(23).suite); // card should be Queen of clubs
    } // end of generateDeck

    @Test
    void dealHand() {
        // checking that dealHand() returns two instances of Cards, and that its only two cards
        ArrayList<Card> testHand1 = dealer.dealHand();
        assertTrue(testHand1.get(0).value >= 2 &&  testHand1.get(0).value <= 14);
        assertTrue(Arrays.asList(new String[]{"clubs", "spades", "hearts", "diamonds"}).contains(testHand1.get(0).suite));
        assertEquals(2, testHand1.size());

        ArrayList<Card> testHand2 = dealer.dealHand();
        assertTrue(testHand2.get(0).value >= 2 &&  testHand2.get(0).value <= 14);
        assertTrue(Arrays.asList(new String[]{"clubs", "spades", "hearts", "diamonds"}).contains(testHand2.get(0).suite));
        assertEquals(2, testHand2.size());
    } // end of dealHand()

    @Test
    void drawOne() {
        // checking that drawCard() returns one card
        Card testCard1 = dealer.drawOne();
        assertTrue(testCard1.value >= 2 &&  testCard1.value <= 14);
        assertTrue(Arrays.asList(new String[]{"clubs", "spades", "hearts", "diamonds"}).contains(testCard1.suite));

        Card testCard2 = dealer.drawOne();
        assertTrue(testCard2.value >= 2 &&  testCard1.value <= 14);
        assertTrue(Arrays.asList(new String[]{"clubs", "spades", "hearts", "diamonds"}).contains(testCard2.suite));
    } // end of drawOne()

    @Test
    void shuffleDeck() {
        BaccaratDealer regularDeck = new BaccaratDealer();
        regularDeck.generateDeck();

        BaccaratDealer shuffledDeck = new BaccaratDealer();
        shuffledDeck.shuffleDeck();

        // shuffled decks shouldn't be the same as a deck without shuffle
        assertFalse(equalDecks(regularDeck, shuffledDeck));
        assertFalse(equalDecks(regularDeck, dealer));
    } // end of shuffleDeck()

    // Checks if two decks are the same. Returns true if so, false otherwise.
    private boolean equalDecks(BaccaratDealer regularDeck, BaccaratDealer shuffledDeck) {
        // there is a maximum of 52 cards per deck
        for (int i = 0; i < 52; i++) {
            //if card at pos i is different in both decks, then decks are different
            if (regularDeck.deck.get(i).value != shuffledDeck.deck.get(i).value ||
                    !regularDeck.deck.get(i).suite.equals(shuffledDeck.deck.get(i).suite))
                return false;
        }
        return true;
    } // end of equalDecks()

    @Test
    void deckSize() {
        // removing cards from deck and checking if deck contains a particular number of cards
        for (int i = 0; i < 5; i++) {
            dealer.drawOne();
        }

        assertEquals(47, dealer.deckSize()); // removing 5 cards from original 52 card deck

        for (int i = 0; i < 2; i++) {
            dealer.drawOne();
        }

        assertEquals(45, dealer.deckSize()); // removing 2 cards from 47 left cards
    } // end of deckSize()

    // Testing the Card class
    @Test
    void cardConstructor() {
        Card testCard= new Card("spades", 7);
        assertEquals("spades", testCard.suite);
        assertEquals(7, testCard.value);
    }
} // end of MyTest class
