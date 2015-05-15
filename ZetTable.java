/**
 *  Represents a card table with a deck and an array of open cards for the game of Set.
 */

/*

  Tips:

  - The largest number of cards that don't have a "set" is 20.  There is no
    need ever to have more than 21 cards open.  The default number of open cards is 12.

  - Keep open cards in an array.  Although open cards are displayed on a two-dimensional
    grid, this is a matter for the display class to handle.  Here it is easier to use a
    one-dimensional array.

  - The open cards array may have gaps: if a card is missing, the corresponding element is
    set to null.

  - Don't forget to shuffle the deck after it has been created.

  - Note that getOpenCard(i) must check that i is within bounds and return null
    if it isn't.

  - open3Cards places each card into the first available slot.

  - In the toString method, separate strings for individual cards with "\n".

  - compactCards fills the gaps among the first dfltOpenCards cards with
    the cards available beyond the first dfltOpenCards.  A simple algorithm to
    accomplish that is similar to the partitioning algorithm:

      * Start at both ends of the array.
      * Proceed for as long as the left index is less than dfltOpenCards and
        the right index is greater or equal dfltOpenCards.
      * If you find a card on the left, increment the left index.
      * If you find a gap on the right, decrement the right index.
      * If you find a gap on the left and a card on the right, fill the gap
        with that card and advance both indices.

*/

public class ZetTable
{
  private final int dfltOpenCards = 12;
  private ZetCard[] openCards;
  private ZetDeck deck;

  /**
   *  Creates a new deck and opens dfltOpenCards cards.
   */
  public ZetTable()
  {
    deck = new ZetDeck();
    openCards = new ZetCard[21];
    deck.shuffle();
    for (int i = 0; i < dfltOpenCards; i++)
      openCards[i] = (ZetCard) deck.takeTop();
  }

  /**
   *  Returns the number of cards left in the deck.
   *  @return the number of cards left in the deck.
   */
  public int cardsInDeck()
  {
    return deck.getNumCards();
  }

  /**
   *  Returns the open card with a given index. If the index is out of bounds
   *  or the card with this index is not open, returns null.
   *  @return the open card with a given index, or null if the index
   *  is out of bounds.
   */
  public ZetCard getOpenCard(int i)
  {
    if (i >= openCards.length || i < 0)
      return null;
    return openCards[i];
  }

  /**
   *  Indicates whether there is a sufficient number of open cards.
   *  @return true if numOpenCards >= dfltOpenCards; false otherwise.
   */
  public boolean enoughOpen()
  {
    if (openCards.length >= dfltOpenCards)
      return true;
    else
      return false;
  }

  /**
   *  Finds a "set" in the open cards.
   *  @return an array of the indices of the three "set" cards,
   *  or null if no sets are found.
   */
  public int[] findZet()
  {
    return ZetAnalyzer.findZet(openCards);
  }

  /**
   *  Opens three cards from the deck if three cards are available in the deck.
   *  @return true if the cards are opened; false otherwise.
   */
  public boolean open3Cards()
  {
    if (deck.getNumCards() >= 3)
    {
      int total = 3;
      while (total > 0)
      {
        for (int j = 0; j < openCards.length; j++)
          if (openCards[j] == null)
          {
            openCards[j] = (ZetCard) deck.takeTop();
            total -= 1;
          }
        for (int i = openCards.length; i < openCards.length + 3; i++)
        {
          openCards[i] = (ZetCard) deck.takeTop();
          total -= 1;
        }
      }
      return true;
    }
    else
      return false;
  }

 /**
   *  Removes three cards with given indices from the open cards.
   *  @precondition The cards with indices[0], indices[1], indices[2] are open.
   */
  public void remove3Cards(int[] indices)
  {
    for (int n : indices)
      openCards[n] = null;
  }

  /**
   *  If there are gaps in the first dfltOpenCards and some open cards
   *  beyond dfltOpenCards, the latter are moved to fill the gaps.
   */
  public void compactOpenCards()
  {
    /*Partitioning algorithm (proceed from both ends):
    * Start at both ends of the array.
    * Proceed for as long as the left index is less than dfltOpenCards and
      the right index is greater or equal dfltOpenCards.
    * If you find a card on the left, increment the left index.
    * If you find a gap on the right, decrement the right index.
    * If you find a gap on the left and a card on the right, fill the gap
      with that card and advance both indices.*/
    int left = 0;
    int right = openCards.length - 1;
    while(left < dfltOpenCards && right >= dfltOpenCards)
    {
      if (openCards[left] != null)
        left++;
      else if (openCards[right] == null)
        right--;
      else
      {
        openCards[left] = openCards[right];
        openCards[right] = null;
        left++;
        right--;
      }
    }
  }

  /**
   *  Returns a string representation of this "set table".
   *  @return a string that lists all the open cards (including null cards)
   *  followed by an '\n' character, and finally the count of cards
   *  left in the deck.
   */
  public String toString()
  {
    String all = "";
    for (ZetCard z : openCards)
    {
      if (z == null)
        all += "null" + '\n';
      else
        all += z.toString() + '\n';
    }
    all += "" + deck.getNumCards();
    return all;
  }
}