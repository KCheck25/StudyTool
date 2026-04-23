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
    // parameters: question, answer, topic, priorityScore, isLoadingFromFiles.
    // throws an IllegalArgumentException if topic,question, or answer is null.
    public FlashCard(String topic, String question, String answer, int priorityScore, boolean isLoadingFromFiles) throws Exception {
        if (question == null || answer == null || topic == null || priorityScore < 1 || priorityScore > 3) {
            throw new IllegalArgumentException("The question, answer, priority score, or topic is invalid.");
        }
        this.question = question;
        this.answer = answer;
        this.topic = topic;
        this.priorityScore = priorityScore;
        this.flipped = false;
        if (!isLoadingFromFiles) {
            this.playSound("flashcardCreation.wav");
        }
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
        } else {
            this.flipped = false;
            this.playSound("flipToQuestion.wav");
        }
        return this.toString();
    }
    // gets the current side of the FlashCard.
    // returns the current side of the FlashCard.
    public String getCurrentSide() {
        return this.toString();
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
    // compares two flashcards with one another for sorting.
    // compares based on the priority score 1st, with flashcards
    // holding higher priority scores coming first.
    // if both flashcards have the same priority score, compares alphabetically starting from the topic.
    // if topics are equal alphabetically, compares similarly with questions followed by the answers
    // if the questions are equal as well.
    // parameters: other, the other flashcard to compare with.
    // returns either a negative, positive, or zero to be able to sort.
    public int compareTo(FlashCard other) {
        if (other.priorityScore < this.priorityScore) {
            return -1;
        } else if (this.priorityScore == other.priorityScore) {
            if (this.topic.compareTo(other.topic) < 0) {
                return -1;
            } else if (this.topic.compareTo(other.topic) == 0) {
                if (this.question.compareTo(other.question) < 0) {
                    return -1;
                } else if (this.question.compareTo(other.question) == 0) {
                    if (this.answer.compareTo(other.answer) < 0) {
                        return -1;
                    } else if (this.answer.compareTo(other.answer) == 0) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }
    public int hashCode() {
        return Objects.hash(this.topic, this.question, this.answer, this.priorityScore);
    }
    // checks if two objects are equal to one another.
    // parameters: other, the other object to compare with.
    // returns: true if other is the same as the flashcard, true if other is a flashcard and has
    // same topic, question, answer, and priority score as the flash card.
    // false otherwise.
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
    // returns a representation of the flashcard.
    // the topic, question, and priority score each on its own line if the flashcard is on the question’s side,
    // or the answer if the flashcard is on the answer’s side.
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.flipped) {
            sb.append("Answer: ").append(this.answer);
        } else {
            sb.append("Topic: ").append(this.topic).append("\n");
            sb.append("Question: ").append(this.question).append("\n");
            sb.append("Priority Score: ").append(this.priorityScore);
        }
        return sb.toString();
    }
}