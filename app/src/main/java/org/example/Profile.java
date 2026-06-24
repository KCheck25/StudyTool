package org.example;
import java.util.*;
public class Profile {
    private String name;
    private List<FlashCard> flashCards;
    private List<Deck> decks;
    public Profile(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name is invalid.");
        }
        this.name = name;
        this.flashCards = new ArrayList<>();
        this.decks = new ArrayList<>();
    }
    public void changeName(String newName) {
        if (newName == null) {
            throw new IllegalArgumentException("new name is invalid.");
        }
        this.name = newName;
    }
    public void createFlashCard(String topic, int priorityScore, String question, String answer, boolean isLoadingFromFiles) throws Exception {
        if (topic == null || priorityScore < 1 || priorityScore > 3 || question == null || answer == null) {
            throw new IllegalArgumentException();
        }
        this.flashCards.add(new FlashCard(topic, priorityScore, question, answer, isLoadingFromFiles));
    }
    public void createDeck(String subject, boolean isSorted) {
        if (subject == null) {
            throw new IllegalArgumentException("subject is invalid");
        }
        this.decks.add(new Deck(subject, isSorted));
    }
    public boolean removeCard(FlashCard card) {
        if (card == null) {
            throw new IllegalArgumentException("card is invalid.");
        }
        if (this.flashCards.contains(card)) {
            this.flashCards.remove(card);
            return true;
        }
        return false;
    }
    public boolean moveCard(FlashCard card, Deck deck, boolean toDeck) {
        if (card == null || deck == null) {
            throw new IllegalArgumentException("card or deck is invalid.");
        }
        if (toDeck) {
            if (this.flashCards.contains(card) && this.decks.contains(deck)) {
                this.flashCards.remove(card);
                deck.addCard(card);
                return true;
            }
        }
        if (this.decks.contains(deck) && deck.containsCard(card)) {
            deck.removeCard(card);
            this.flashCards.add(card);
            return true;
        }
        return false;
    }
    public boolean moveCard(FlashCard card, Deck deck1, Deck deck2) {
        if (card == null || deck1 == null || deck2 == null) {
            throw new IllegalArgumentException("deck or card is invalid.");
        }
        if (this.decks.contains(deck1) && this.decks.contains(deck2) && deck1.containsCard(card)) {
            deck1.removeCard(card);
            deck2.addCard(card);
            return true;
        }
        return false;
    }
    public boolean removeDeck(Deck deck) {
        if (deck == null) {
            throw new IllegalArgumentException("deck is invalid.");
        }
        if (this.decks.contains(deck)) {
            this.decks.remove(deck);
            return true;
        }
        return false;
    }
    public boolean hasDeck(String subject) {
        if (subject == null) {
            throw new IllegalArgumentException("subject is invalid.");
        }
        for (Deck deck : this.decks) {
            if (deck.getSubject().equalsIgnoreCase(subject)) {
                return true;
            }
        }
        return false;
    }
    public int getNumOfCards() {
        return this.flashCards.size();
    }
    public int getNumOfDecks() {
        return this.decks.size();
    }
    public List<FlashCard> getCards() {
        return new ArrayList<>(this.flashCards);
    }
    public List<Deck> getDecks() {
        return new ArrayList<>(this.decks);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append("\n");
        sb.append(this.getNumOfCards()).append(" total flashcards").append("\n");
        sb.append(this.getNumOfDecks()).append(" total decks");
        return sb.toString();
    }
}