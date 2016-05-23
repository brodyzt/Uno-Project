
package uno;

import java.util.*;
import java.awt.Color;

public class zb_UnoPlayer implements UnoPlayer {

    final static private int IMP_COLOR_LEFT = 1;
    final static private int IMP_FEW_CARDS = 2000;
    final static private int IMP_LAST_PLAYED_WILD = 1;
    final static private int IMP_RID_FACE_CARDS = 500;
    final static private int IMP_NUMCARDS = 500;

    /**
     * play - This method is called when it's your turn and you need to
     * choose what card to play.
     *
     * The hand parameter tells you what's in your hand. You can call
     * getColor(), getRank(), and getNumber() on each of the cards it
     * contains to see what it is. The color will be the color of the card,
     * or "Color.NONE" if the card is a wild card. The rank will be
     * "Rank.NUMBER" for all numbered cards, and another value (e.g.,
     * "Rank.SKIP," "Rank.REVERSE," etc.) for special cards. The value of
     * a card's "number" only has meaning if it is a number card. 
     * (Otherwise, it will be -1.)
     *
     * The upCard parameter works the same way, and tells you what the 
     * up card (in the middle of the table) is.
     *
     * The calledColor parameter only has meaning if the up card is a wild,
     * and tells you what color the player who played that wild card called.
     *
     * Finally, the state parameter is a GameState object on which you can 
     * invoke methods if you choose to access certain detailed information
     * about the game (like who is currently ahead, what colors each player
     * has recently called, etc.)
     *
     * You must return a value from this method indicating which card you
     * wish to play. If you return a number 0 or greater, that means you
     * want to play the card at that index. If you return -1, that means
     * that you cannot play any of your cards (none of them are legal plays)
     * in which case you will be forced to draw a card (this will happen
     * automatically for you.)
     */
    public int play(List<Card> hand, Card upCard, Color calledColor,
    GameState state) {
        List<Card> validCards = new ArrayList<Card>();
        List<Integer> probabilities = new ArrayList<Integer>();

        for(Card c: hand) {
            if(c.canPlayOn(upCard, calledColor)) {
                addValidCard(validCards, probabilities, c);
            }
        }

        evaluateCards(hand, validCards, probabilities, state);

        return cardToPlay(hand, validCards, probabilities);
    }

    /**
     * callColor - This method will be called when you have just played a
     * wild card, and is your way of specifying which color you want to 
     * change it to.
     *
     * You must return a valid Color value from this method. You must not
     * return the value Color.NONE under any circumstances.
     */

    public Color callColor(List<Card> hand) {
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
        double[] percentageList = new double[4];
        
        for(int i = 0; i < colors.length; i++) {
            percentageList[i] = percentageOfColorInHand(hand, colors[i]);
        }

        return colors[max(percentageList)];
    }

    public static int max(double[] list) {
        int index = 0;
        double max = list[0];

        for(int i = 1; i < list.length; i++) {
            if(list[i] > max) {
                max = list[i];
                index = i;
            }
        }

        return index;
    }

    public static void printDeck(List<Card> deck) {
        for(Card c: deck) {
            println("\n" + c);
        }
    }

    public static void print(Object o) {
        System.out.print(o);
    }

    public static void println(Object o) {
        System.out.println(o);
    }

    public static void printDetails(String s, Card upCard, Card myCard) {
        println("\n" + s + ".Current Card: " + upCard + "; Playing Card: " + myCard);
    }

    public static Object lastInArray(List<?> l) {
        return l.get(l.size() - 1);
    }

    public static Card lastCard(List<Card> l) {
        return (Card)lastInArray(l);
    }

    public static void addValidCard(List<Card> validCards, List<Integer> probabilities, Card c) {
        validCards.add(c);
        probabilities.add(1);
    }

    public static void evaluateCards(List<Card> hand, List<Card> validCards, List<Integer> probabilities, GameState state) {
        for(int i = 0; i < validCards.size(); i++) {
            Card c = validCards.get(i);
            int p = probabilities.get(i);

            p += (int)(IMP_COLOR_LEFT * percentageOfColorInHand(hand, c.getColor()));
            
            if(c.getRank().equals(Rank.SKIP)
            || c.getRank().equals(Rank.REVERSE)
            || c.getRank().equals(Rank.DRAW_TWO)
            || c.getRank().equals(Rank.WILD_D4)) {
                int num = state.getNumCardsInHandsOfUpcomingPlayers()[0];
                p += (int)(IMP_FEW_CARDS * Math.pow(Math.E, -1.0/3 * (num-1))) + IMP_RID_FACE_CARDS;;
            } else {
                p += IMP_NUMCARDS * c.getNumber();
            }

            probabilities.set(i, p);
        }
        //print("Test");
    }

    public static double percentageOfColorInHand(List<Card> hand, Color color) {
        double count = 0;
        int numCards = hand.size();
        for(Card c: hand) {
            if(c.getColor().equals(color)) {
                count++;
            }
        }

        return count/numCards;
    }

    public static int cardToPlay(List<Card> hand, List<Card> validCards, List<Integer> probabilities) {
        if(validCards.size() == 0) {
            return -1;
        }

        int sum = 0;
        int index = 0;

        for(Integer i: probabilities) {
            sum += i;
        }

        double rValue = Math.random() * sum;

        int runningTotal = 0;

        for(int i = 0; i < validCards.size(); i++) {
            runningTotal += probabilities.get(i);
            if(rValue < runningTotal) {
                index = i;
                break;
            }
        }

        return hand.indexOf(validCards.get(index));
    }

}

/* Original Valid Card Finder
 * 
 * for(int i = 0; i < hand.size(); i++) {

if(upCard.getRank().equals(Rank.WILD) || upCard.getRank().equals(Rank.WILD_D4)) {
if(hand.get(i).getColor().equals(calledColor)) {
addValidCard(validCards, probabilities, hand.get(i));
}
} else {
if(hand.get(i).getColor().equals(upCard.getColor())) {
addValidCard(validCards, probabilities, hand.get(i));
} else if(hand.get(i).getNumber() == upCard.getNumber() && upCard.getNumber() != -1) {
addValidCard(validCards, probabilities, hand.get(i));
} else if(hand.get(i).getRank() == upCard.getRank() && !upCard.getRank().equals(Rank.NUMBER)) {
addValidCard(validCards, probabilities, hand.get(i));
}
}
}

for(int i = 0; i < hand.size(); i++) {
if(hand.get(i).getRank().equals(Rank.WILD_D4) || hand.get(i).getRank().equals(Rank.WILD)) { 
addValidCard(validCards, probabilities, hand.get(i));
}
}
 */