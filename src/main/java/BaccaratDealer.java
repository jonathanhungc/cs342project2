import java.util.*;

public class BaccaratDealer {
    ArrayList<Card> deck;

    public void generateDeck() {

        deck = new ArrayList<>();

        String[] suit = {"clubs", "spades", "hearts", "diamonds"};

        for (String s : suit) {

            // Jack = 11
            // Queen = 12
            // King = 13
            // Ace = 14
            for (int j = 2; j < 15; j++) {
                deck.add(new Card(s, j));
            }
        }
    }

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
    }

    public Card drawOne() {

        if (deck.isEmpty())
            shuffleDeck(); // generate deck if there are no cards

        return deck.remove(new Random().nextInt(deck.size()));
    }

    public int deckSize() {
        return deck.size();
    }

    public void shuffleDeck() {
        generateDeck();
        Collections.shuffle(deck, new Random(System.currentTimeMillis()));
    }
}
