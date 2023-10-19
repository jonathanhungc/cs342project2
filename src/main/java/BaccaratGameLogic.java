import java.lang.reflect.Array;
import java.util.ArrayList;

public class BaccaratGameLogic {

    // player = hand1, banker = hand2
    public String whoWon(ArrayList<Card> hand1, ArrayList<Card> hand2) {

        int playerHand = handTotal(hand1);
        int bankerHand = handTotal(hand2);

        // checking for natural win, if player or bank have 8 or 9
        if (playerHand == 9 && bankerHand == 9)
            return "Draw";
        else if (playerHand == 9 && bankerHand == 8)
            return "Player";
        else if (playerHand == 8 && bankerHand == 9)
            return "Banker";
        else if (playerHand == 8 && bankerHand == 8)
            return "Draw";

        // if there's no natural win, check for other possibilities
        if (playerHand > bankerHand)
            return "Player";
        else if (handTotal(hand1) < handTotal(hand2))
            return "Banker";
        else
            return "Draw";
    }

    public int handTotal(ArrayList<Card> hand) {

        int total = 0;

        for (Card card : hand) {
            // if card is 10, J, K or Q
            if (card.value == 10 || card.value == 11 || card.value == 12 || card.value == 13) {
                total += 0;

            // if card is A
            } else if (card.value == 14) {
                total += 1;

            } else {
                total += card.value;
            }
        }
        return total % 10; // get a value between 0-9
    }

    public boolean evaluateBankerDraw(ArrayList<Card> hand, Card playerCard) {

        int handValue = handTotal(hand);

        if (handValue == 7) { return false; }

        else if (handValue <= 2) { return true; }

        // player hand is 1, 2, 3, 4, 5, 6, 7, 9
        else if (handValue == 3 && playerCard.value != 8) { return true; }

        // player hand is 2, 3, 4, 5, 6, 7
        else if (handValue == 4 && (playerCard.value == 2 || playerCard.value == 3 ||
                playerCard.value == 4 || playerCard.value == 5 ||
                playerCard.value == 6 || playerCard.value == 7)) { return true; }

        // player hand is 4, 5, 6, 7
        else if (handValue == 5 && (playerCard.value == 4 || playerCard.value == 5 ||
                playerCard.value == 6 || playerCard.value == 7)) { return true; }

        // player hand is 6, 7
        else return handValue == 6 && (playerCard.value == 6 || playerCard.value == 7);

    }

    public boolean evaluatePlayerDraw(ArrayList<Card> hand) {

        return handTotal(hand) <= 5;
    }
}
