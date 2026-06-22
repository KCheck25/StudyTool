package org.example;
import java.util.*;
import java.io.*;
public class Deck {
    private static final Random RAND = new Random();
    private String subject;
    private Set<FlashCard> flashCards;
    private Set<FlashCard> viewedFlashCards;
    public Deck(String subject, boolean sorted) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject is invalid.");
        }
        this.subject = subject;
        if (sorted) {
            this.flashCards = new TreeSet<>();
            this.viewedFlashCards = new TreeSet<>();
        } else {
            this.flashCards = new HashSet<>();
            this.viewedFlashCards = new HashSet<>();
        }
    }
    public Deck(String subject, boolean sorted, Set<FlashCard> flashCards) {
        this(subject, sorted);
        if (flashCards == null || flashCards.isEmpty()) {
            throw new IllegalArgumentException("There are no flashcards.");
        }
        this.flashCards.addAll(flashCards);
    }
    public String getSubject() {
        return this.subject;
    }
    public int getSize() {
        return this.flashCards.size() + this.viewedFlashCards.size();
    }
    public int getSubSize(boolean viewed) {
        if (!viewed) {
            return this.flashCards.size();
        }
        return this.viewedFlashCards.size();
    }
    public double getCompletionPercentage() {
        if (this.getSize() == 0) {
            return 0.0;
        }
        double percentage = (double) this.getSubSize(true) * 100.0 / this.getSize();
        return Math.round(percentage * 100.0) / 100.0;
    }
    public void changeSubject(String newSubject) {
        if (newSubject == null) {
            throw new IllegalArgumentException("New subject is invalid.");
        }
        this.subject = newSubject;
    }
    public FlashCard getCard(String questionOrAnswer, char option, boolean viewed) {
        this.validateDeck(viewed);
        if (option != 'q' || option != 'Q' || option != 'a' || option != 'A') {
            throw new IllegalArgumentException("option is invalid.");
        }
        Set<FlashCard> cards = this.selectSet(viewed);
        for (FlashCard card : cards) {
            if (option == 'q' || option == 'Q') {
                if (card.getQuestion().equalsIgnoreCase(questionOrAnswer)) {
                    return card;
                }
            } else {
                if (card.getAnswer().equalsIgnoreCase(questionOrAnswer)) {
                    return card;
                }
            }
        }
        return null;
    }
    public Set<FlashCard> getAllCards(boolean viewed) {
        Set<FlashCard> cards = this.selectSet(viewed);
        Set<FlashCard> result = this.assignSorting();
        result.addAll(cards);
        if (!viewed) {
            this.viewedFlashCards.addAll(cards);
            cards.clear();
        }
        return result;
    }
    public void addCard(FlashCard card) {
        if (card == null) {
            throw new IllegalArgumentException("Flashcard is invalid.");
        }
        this.flashCards.add(card);
    }
    public void removeCard(FlashCard card) {
        if (!this.flashCards.contains(card) && !this.viewedFlashCards.contains(card)) {
            throw new IllegalStateException("The Deck does not contain the card.");
        }
        if (this.flashCards.contains(card)) {
            this.flashCards.remove(card);
        } else {
            this.viewedFlashCards.remove(card);
        }
    }
    public void removeAllCards(boolean viewed) {
        Set<FlashCard> cards = this.selectSet(viewed);
        cards.clear();
    }
    public void restoreCard(FlashCard card) {
        if (!this.viewedFlashCards.contains(card)) {
            throw new IllegalStateException("this card has not been viewed.");
        }
        this.viewedFlashCards.remove(card);
        this.flashCards.add(card);
    }
    public void restoreAll() {
        this.validateDeck(true);
        this.flashCards.addAll(this.viewedFlashCards);
        this.viewedFlashCards.clear();
    }
    public void restoreAll(Set<FlashCard> cards) {
        this.validateDeck(true);
        if (cards == null) {
            throw new IllegalArgumentException("Cards are invalid.");
        }
        Iterator<FlashCard> it = this.viewedFlashCards.iterator();
        while (it.hasNext()) {
            FlashCard card = it.next();
            if (cards.contains(card)) {
                it.remove();
                this.flashCards.add(card);
            }
        }
    }
    public String showAllCards(boolean viewed) {
        if (!viewed) {
            if (this.flashCards.isEmpty()) {
                return "";
            }
        } else {
            if (this.viewedFlashCards.isEmpty()) {
                return "";
            }
        }
        Set<FlashCard> cards = this.selectSet(viewed);
        StringBuilder sb = new StringBuilder();
        for (FlashCard card : cards) {
            sb.append(card.reset()).append("\n");
        }
        return sb.toString().indent(2).strip();
    }
    public FlashCard nextCard() {
        Iterator<FlashCard> it = this.flashCards.iterator();
        if (!it.hasNext()) {
            return null;
        }
        FlashCard card = it.next();
        it.remove();
        this.viewedFlashCards.add(card);
        return card;
    }
    public Set<FlashCard> getByTopic(String topic, boolean viewed) {
        Set<FlashCard> cards = this.selectSet(viewed);
        if (topic == null) {
            throw new IllegalArgumentException("Topic is invalid.");
        }
        Set<FlashCard> result = this.assignSorting();
        Iterator<FlashCard> it = cards.iterator();
        while (it.hasNext()) {
            FlashCard card = it.next();
            if (card.getTopic().equalsIgnoreCase(topic)) {
                result.add(card);
                if (!viewed) {
                    it.remove();
                    this.viewedFlashCards.add(card);
                }
            }
        }
        return result;
    }
    public Set<FlashCard> getByPriorityScore(int priorityScore, boolean viewed) {
        Set<FlashCard> cards = this.selectSet(viewed);
        if (priorityScore < 1 || priorityScore > 3) {
            throw new IllegalArgumentException("Priority score must be between 1-3");
        }
        Set<FlashCard> result = this.assignSorting();
        Iterator<FlashCard> it = cards.iterator();
        while (it.hasNext()) {
            FlashCard card = it.next();
            if (card.getPriorityScore() == priorityScore) {
                result.add(card);
                if (!viewed) {
                    it.remove();
                    this.viewedFlashCards.add(card);
                }
            }
        }
        return result;
    }
    public FlashCard getRandomCard() {
        this.validateDeck(false);
        int randomNumber = RAND.nextInt(1, this.flashCards.size() + 1);
        Iterator<FlashCard> it = this.flashCards.iterator();
        FlashCard card = null;
        for (int i = 0; i < randomNumber; i++) {
            card = it.next();
        }
        it.remove();
        this.viewedFlashCards.add(card);
        return card;
    }
    public Set<String> getAllTopics(boolean viewed) {
        Set<FlashCard> cards = this.selectSet(viewed);
        Set<String> result = this.assignSorting();
        for (FlashCard card : cards) {
            result.add(card.getTopic());
        }
        return result;
    }
    public boolean containsTopic(String topic) {
        if (topic == null) {
            throw new IllegalArgumentException("Topic is invalid.");
        }
        for (FlashCard card : this.flashCards) {
            if (card.getTopic().equalsIgnoreCase(topic)) {
                return true;
            }
        }
        return false;
    }
    public boolean containsPriorityScore(int priorityScore) {
        if (priorityScore < 1 || priorityScore > 3) {
            throw new IllegalArgumentException("Priority score must be between 1 and 3.");
        }
        for (FlashCard card : this.flashCards) {
            if (card.getPriorityScore() == priorityScore) {
                return true;
            }
        }
        return false;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(this.subject).append("]");
        sb.append("   ").append(this.getSize()).append(" Total cards").append("\n");
        if (this.flashCards instanceof TreeSet) {
            sb.append("✅");
        } else {
            sb.append("❌");
        }
        sb.append("   ").append(this.getCompletionPercentage()).append("% complete");
        return sb.toString();
    }
    public boolean containsCard(FlashCard card) {
        if (card == null) {
            throw new IllegalArgumentException("card is invalid.");
        }
        if (this.flashCards.contains(card) || this.viewedFlashCards.contains(card)) {
            return true;
        }
        return false;
    }

    private <T> Set<T> assignSorting() {
        Set<T> set = null;
        if (this.flashCards instanceof TreeSet) {
            set = new TreeSet<>();
        } else {
            set = new HashSet<>();
        }
        return set;
    }
    private void validateDeck(boolean viewed) {
        if (!viewed) {
            if (this.flashCards.isEmpty()) {
                throw new IllegalStateException("Deck is empty.");
            }
        } else {
            if (this.viewedFlashCards.isEmpty()) {
                throw new IllegalStateException("There are no viewed cards.");
            }
        }
    }
    private Set<FlashCard> selectSet(boolean viewed) {
        Set<FlashCard> cards = null;
        if (!viewed) {
            cards = this.flashCards;
        } else {
            cards = this.viewedFlashCards;
        }
        return cards;
    }
}