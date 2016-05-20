
package uno;

import java.util.List;
import java.awt.*;

public class zb_UnoPlayer implements UnoPlayer {

    final private int COLOR_LEFT_IMP = 5;
    
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
        
        // THIS IS WHERE YOUR AMAZING CODE GOES
        for(int i = 0; i < hand.size(); i++) {
            /*if(hand.get(i).canPlayOn(upCard, calledColor)) {
                return i;
            }*/
            
            if(upCard.getRank().equals(Rank.WILD) || upCard.getRank().equals(Rank.WILD_D4)) {
                if(hand.get(i).getColor().equals(calledColor)) {
                    return i;
                }
            } else {
                if(hand.get(i).getColor().equals(upCard.getColor())) {
                    return i;
                } else if(hand.get(i).getNumber() == upCard.getNumber() && upCard.getNumber() != -1) {
                    return i;
                } else if(hand.get(i).getRank() == upCard.getRank() && !upCard.getRank().equals(Rank.NUMBER)) {
                    return i;
                }
            }
        }

        for(int i = 0; i < hand.size(); i++) {
            if(hand.get(i).getRank().equals(Rank.WILD_D4) || hand.get(i).getRank().equals(Rank.WILD)) { 
                return i;
            }
        }
        return -1;
    }

    public static void printDetails(String s, Card upCard, Card myCard) {
        Util.println("\n" + s + ".Current Card: " + upCard + "; Playing Card: " + myCard);
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
        // THIS IS WHERE YOUR AMAZING CODE GOES
        return Color.RED;
    }

}