package org.example;
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;
public class FlashCard implements Comparable<FlashCard> {
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
    public String flip() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (!this.flipped) {
            this.flipped = true;
            this.playSound("flipToAnswer.wav");
            return this.answer;
        } else {
            this.flipped = false;
            this.playSound("flipToQuestion.wav");
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
    public boolean makeGuess(String guess) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (guess == null) {
            throw new IllegalArgumentException("Guess is invalid.");
        }
        boolean isCorrect = guess.equalsIgnoreCase(this.answer);
        if (isCorrect) {
            if (this.priorityScore == 1) {
                this.playSound("correctGuess.wav");
            } else if (this.priorityScore == 2) {
                this.playSound("correctGuess2.wav");
            } else {
                this.playSound("andHisNameIsJohnCena.wav");
            }
        } else {
            if (this.priorityScore == 1) {
                this.playSound("wrongGuess.wav");
            } else if (this.priorityScore == 2) {
                this.playSound("wrongGuess2.wav");
            } else {
                this.playSound("CuteChicken.wav");
            }
        }
        return isCorrect;
    }
    public String reset() {
        this.flipped = false;
        return this.question;
    }
    private void playSound(String fileName) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("sounds/" + fileName);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(stream);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
        clip.drain();
    }
    public int compareTo(FlashCard other) {
        if (this.priorityScore < other.priorityScore) {
            return -1;
        } else if (this.priorityScore == other.priorityScore) {
            return 0;
        } else {
            return 1;
        }
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
        StringBuilder sb = new StringBuilder();
        sb.append("Topic: ").append(this.topic).append("\n");
        sb.append(" Question: ").append(this.question).append("\n");
        sb.append(" Answer: ").append(this.answer).append("\n");
        sb.append(" Priority Score: ").append(this.priorityScore).append("\n");
        return sb.toString();
    }
}