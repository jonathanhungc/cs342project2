import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;

class MyTest {

	BaccaratGame game;
	BaccaratGameLogic gameLogic;
	BaccaratDealer dealer;
	ArrayList<Card> hand1;
	ArrayList<Card> hand2;
	ArrayList<Card> hand3;
	ArrayList<Card> hand4;

	@BeforeEach
	void init() {
		game = new BaccaratGame();
		gameLogic = new BaccaratGameLogic();
		dealer = new BaccaratDealer();
		dealer.shuffleDeck();

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

	}

	// BaccaratGame class
	@Test
	void evaluateWinnings() {
		game.gameLogic = gameLogic;

		// test 1
		game.playerHand = hand1; // hand1 total is 5
		game.bankerHand = hand2; // hand2 total is 6

		game.totalWinnings = 0;
		game.currentBet = 10;
		game.handBet = "Banker";
		assertEquals(19.5, game.evaluateWinnings()); // user bet 10 on banker, should get 19.5 back

		// test 2
		game.playerHand = hand2; // hand2 total is 6
		game.bankerHand = hand4; // hand4 total is 6

		game.totalWinnings = 0;
		game.currentBet = 10;
		game.handBet = "Draw";
		assertEquals(80, game.evaluateWinnings()); // user bet 10 on banker, should get 19.5 back
	}

	// BaccaratGameLogic
	@Test
	void whoWon() {
		assertEquals("Player", gameLogic.whoWon(hand3, hand1)); // hand3 = player (8), hand1 = banker (5)
		assertEquals("Banker", gameLogic.whoWon(hand1, hand4)); // hand1 = player (5), hand4 = banker (6)
		assertEquals("Draw", gameLogic.whoWon(hand2, hand4)); // both hands are worth 6
	}

	@Test
	void handTotal() {
		assertEquals(5, gameLogic.handTotal(hand1));
		assertEquals(6, gameLogic.handTotal(hand2));
		assertEquals(8, gameLogic.handTotal(hand3));
		assertEquals(6, gameLogic.handTotal(hand4));
	}

	@Test
	void evaluatePlayerDraw() {
        assertTrue(gameLogic.evaluatePlayerDraw(hand1)); // hand1 = 5, so should get another card (true)
		assertFalse(gameLogic.evaluatePlayerDraw(hand2)); // hand2 = 6, should return false
		assertFalse(gameLogic.evaluatePlayerDraw(hand3)); // hand3 = 8, should return false
	}

	@Test
	void evaluateBankerDraw() {
		// hand1 = 5 and card = 6, should return true
		assertTrue(gameLogic.evaluateBankerDraw(hand1, new Card("spades", 6)));

		// hand1 = 5 and card = 2, should return false
		assertFalse(gameLogic.evaluateBankerDraw(hand1, new Card("spades", 2)));

		// hand2 = 6 and card is 7, should return true
		assertTrue(gameLogic.evaluateBankerDraw(hand2, new Card("diamonds", 7)));
	}

	// BaccaratDealer
	@Test
	void generateDeck() {
		// creating a deck without shuffle, and testing the values
		dealer.generateDeck();
		assertEquals(new Card("clubs", 2).value, dealer.deck.get(0).value); // first card of deck should be 2 of clubs
		assertEquals(new Card("clubs", 4).value, dealer.deck.get(2).value); // card should be 4 of clubs
		assertEquals(new Card("spades", 12).value, dealer.deck.get(23).value); // card should be Queen of clubs
		assertEquals(new Card("spades", 12).suite, dealer.deck.get(23).suite); // card should be Queen of clubs
	}

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
	}
	@Test
	void drawOne() {
		// checking that drawCard() returns one card
		Card testCard1 = dealer.drawOne();
		assertTrue(testCard1.value >= 2 &&  testCard1.value <= 14);
		assertTrue(Arrays.asList(new String[]{"clubs", "spades", "hearts", "diamonds"}).contains(testCard1.suite));

		Card testCard2 = dealer.drawOne();
		assertTrue(testCard2.value >= 2 &&  testCard1.value <= 14);
		assertTrue(Arrays.asList(new String[]{"clubs", "spades", "hearts", "diamonds"}).contains(testCard2.suite));
	}

	@Test
	void shuffleDeck() {
		BaccaratDealer regularDeck = new BaccaratDealer();
		regularDeck.generateDeck();

		BaccaratDealer shuffledDeck = new BaccaratDealer();
		shuffledDeck.shuffleDeck();

		// shuffled decks shouldn't be the same as a deck without shuffle
		assertFalse(equalDecks(regularDeck, shuffledDeck));
		assertFalse(equalDecks(regularDeck, dealer));
	}

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
	}

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
	}

	// Card
	@Test
	void cardConstructor() {
		Card testCard= new Card("spades", 7);
		assertEquals("spades", testCard.suite);
		assertEquals(7, testCard.value);
	}

}
