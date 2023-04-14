import java.util.Random;
class Game {
    public static void main (String[] args) {
        Tableau g1 = new Tableau();
        g1.play();
    }
}

final class Card
{

//  RANK NAME. Printable names of card ranks.

    private static final String [] rankName =
            {
                    "ace",       //   0
                    "two",       //   1
                    "three",     //   2
                    "four",      //   3
                    "five",      //   4
                    "six",       //   5
                    "seven",     //   6
                    "eight",     //   7
                    "nine",      //   8
                    "ten",       //   9
                    "jack",      //  10
                    "queen",     //  11
                    "king"       //  12
            };

//  SUIT NAME. Printable names of card suits.

    private static final String [] suitName =
            {
                    "clubs",     //  0
                    "diamonds",  //  1
                    "hearts",    //  2
                    "spades"     //  3
            };

    private int rank;  //  Rank of this CARD, between 0 and 12.
    private int suit;  //  Suit of this CARD, between 0 and 3.

//  CARD. Constructor. Make a new CARD, with a given RANK and SUIT.

    public Card(int rank, int suit)
    {
        if (0 <= rank && rank <= 12 && 0 <= suit && suit <= 3)
        {
            this.rank = rank;
            this.suit = suit;
        }
        else
        {
            throw new IllegalArgumentException("Illegal rank or suit.");
        }
    }

//  GET RANK. Return the RANK of this CARD.

    public int getRank()
    {
        return rank;
    }

//  GET SUIT. Return the SUIT of this CARD.

    public int getSuit()
    {
        return suit;
    }

//  TO STRING. Return a STRING that describes this CARD. For printing only!

    public String toString()
    {
        return rankName[rank] + " (" + rank + ") of " + suitName[suit];
    }
}

class Deck {
    Card deck[];
    int count;
    Random r = new Random();
    public Deck() {
        int suit = 0;
        int rank = 0;
        deck = new Card[52];
        for (int i = 0; i < 52; i++) {
            if (i % 13 == 0 && i != 0) {
                suit++;
                rank = 0;
            }
            deck[i] = new Card(rank, suit);
            rank++;
        }
        count = 52;
        }
    public Card deal() {
        if (isEmpty()) {
            throw new IllegalStateException("There are no cards left in the deck.");
        }
        else {
            Card last;
            last = deck[count - 1];
            pop();
            return last;
        }
    }
    public void shuffle() {
        if (count < 52) {
            throw new IllegalStateException("Cannot shuffle the deck after dealing has started.");
        }
        else {
            int j;
            Card temp;
            for (int i = 51; i >= 0; i--) {
                j = r.nextInt(i + 1);
                if (j != i) {
                    temp = deck[j];
                    deck[j] = deck[i];
                    deck[i] = temp;
                }
            }
        }
    }
    public void pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot pop from an empty array");
        }
        else {
            count -= 1;
            deck[count] = null;
        }
    }
    public boolean isEmpty() {
        return count == 0;
    }
}

class Pile {

    private class Layer {
        Card card;
        Layer next;
        public Layer(Card card, Layer next) {
            this.card = card;
            this.next = next;
        }
    }
    private Layer head;
    public Pile() {
        head = null;
    }
    public void add(Card card) {
        head = new Layer(card, head);
    }
    public Card draw() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot draw from an empty pile");
        }
        else {
            Card temp = head.card;
            head = head.next;
            return temp;
        }
    }
    public boolean isEmpty() {
        return head == null;
    }
}

class Tableau {
    Pile[] pileArray = new Pile[13];
    Deck gameDeck = new Deck();
    public Tableau() {
        Deck gameDeck = new Deck();
        gameDeck.shuffle();
        for (int i = 0; i < pileArray.length; i++) {
            pileArray[i] = new Pile();
            for (int j = 0; j < 4; j++) {
                pileArray[i].add(gameDeck.deal());
            }
        }
    }
    public void play() {
        int p = 0;
        Card c1 = pileArray[p].draw();
        System.out.println("Got " + c1 + " from pile " + p);
        Card c2;
        while (!pileArray[p].isEmpty()) {
            c2 = pileArray[p].draw();
            System.out.println("Got " + c2 + " from pile " + p);
            if (c1.getSuit() == c2.getSuit()) {
                p = c1.getRank();
                c1 = c2;
            }
            else {
                p = c2.getRank();
                c1 = c2;
            }
        }
        for (int i = 0; i < pileArray.length; i++) {
            if (!pileArray[i].isEmpty()) {
                System.out.println("Pile " + p + " is empty. We lost!");
                return;
            }
        }
        System.out.println("We won!");
    }
}