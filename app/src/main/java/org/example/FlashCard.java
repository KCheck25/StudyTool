package org.example;
import java.util.*;
import java.io.*;
public class FlashCard {
    private String question;
    private String answer;
    private String topic;
    private boolean fliped;
// constructs a new FlashCard object
    // initializes a question, answer, and a topic for the FlashCard.
    // parameters: question, answer, topic.
    // throws an IllegalArgumentException if any of the parameters is null.
    public FlashCard(String question, String answer, String topic) {
        if (question == null || answer == null || topic == null) {
            throw new IllegalArgumentException("The question, answer, or topic is invalid.");
        }
        this.question = question;
        this.answer = answer;
        this.topic = topic;
        this.fliped = false;
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
    // flips the FlashCard from question to answer and vice versa
    // depending on the current state of the FlashCard.
    // flips to the answer if the FlashCard is currently on the question and vice versa.
    // returns the answer if the FlashCard flips to it, or question if the FlashCard flips to it.
    public String flip() {
        if (!this.fliped) {
            this.fliped = true;
            return this.answer;
        } else {
            this.fliped = false;
            return this.question;
        }
    }
}