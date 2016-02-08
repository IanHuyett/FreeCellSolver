
public class CardNode {
	public Card getCard() {
		return card;
	}

	public CardNode getPrevious() {
		return previous;
	}

	//linked list node for card
	public Card card;
	public CardNode previous;
	
	public CardNode(Card card, CardNode previous) {
		this.card = card;
		this.previous = previous;
	}

}
