package org.example;
import java.util.*;
import java.io.*;
public class FlashCard {
    private String question;
    private String answer;
    private String topic;
    private int priorityScore;
    private boolean flipped;
// constructs a new FlashCard object
    // initializes a question, answer, and a topic for the FlashCard.
    // parameters: question, answer, topic.
    // throws an IllegalArgumentException if any of the parameters is null.
    public FlashCard(String question, String answer, String topic, int priorityScore) {
        if (question == null || answer == null || topic == null || priorityScore < 1 || priorityScore > 3) {
            throw new IllegalArgumentException("The question, answer, priority score, or topic is invalid.");
        }
        this.question = question;
        this.answer = answer;
        this.topic = topic;
        this.priorityScore = priorityScore;
        this.flipped = false;
    }
    // changes the current question on the FlashCard to a new one.
    // parameters: newQuestion the new question to be added.
    // throws an IllegalArgumentException if newQuestion is null.
    public void changeQuestion(String newQuestion) {
        if (newQuestion == null) {
            throw new IllegalArgumentException("Question is invalid.");
        }
        this.question = newQuestion;
    }
    // changes the current answer on the FlashCard to a new one.
    // parameters: newAnswer the new answer to be added.
    // throws an IllegalArgumentException if newAnswer is null.
    public void changeAnswer(String newAnswer) {
        if (newAnswer == null) {
            throw new IllegalArgumentException("The answer is invalid.");
        }
        this.answer = newAnswer;
    }
    // changes the current topic of the FlashCard to a new one.
    // parameters: newTopic the new topic to be added.
    // throws an IllegalArgumentException if newTopic is null.
    public void changeTopic(String newTopic) {
        if (newTopic == null) {
            throw new IllegalArgumentException("Topic is invalid.");
        }
        this.topic = newTopic;
    }
    public void changePriorityScore(int newPriorityScore) {
        if (newPriorityScore < 1 || newPriorityScore > 3) {
            throw new IllegalArgumentException("The priority score is invalid.");
        }
        this.priorityScore = newPriorityScore;
    }
    // flips the FlashCard from question to answer and vice versa
    // depending on the current state of the FlashCard.
    // flips to the answer if the FlashCard is currently on the question and vice versa.
    // returns the answer if the FlashCard flips to it, or question if the FlashCard flips to it.
    public String flip() {
        if (!this.flipped) {
            this.flipped = true;
            return this.answer;
        } else {
            this.flipped = false;
            return this.question;
        }
    }
    // gets the current side of the FlashCard.
    // returns the current side of the FlashCard.
    public String getCurrentSide() {
        if (!this.flipped) {
            return this.question;
        } else {
            return this.answer;
        }
    }
    public String getQuestion() {
        return this.question;
    }
    public String getAnswer() {
        return this.answer;
    }
    public String getTopic() {
        return this.topic;
    }
    public int getPriorityScore() {
        return this.priorityScore;
    }
    public boolean isFlipped() {
        return this.flipped;
    }
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof FlashCard) {
            FlashCard card = (FlashCard) other;
            return this.question.equals(card.question) && this.answer.equals(card.answer) && this.topic.equals(card.topic) && this.priorityScore == card.priorityScore;
        } else {
            return false;
        }
    }
    public String toString() {
        return "Topic: " + this.topic + ". Question: " + this.question + ". Priority score: " + this.priorityScore + ". Answer: " + this.answer;
    }
}