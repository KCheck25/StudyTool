package org.example;
import java.util.*;
import java.io.*;
public class FlashCard {
    private String question;
    private String answer;
    private String topic;
    private boolean fliped;
    public FlashCard(String question, String answer, String topic) {
        if (question.equals("null") || answer.equals("null") || topic.equals("null")) {
            throw new IllegalArgumentException("The question, answer, or topic is invalid.");
        }
        this.question = question;
        this.answer = answer;
        this.topic = topic;
        this.fliped = false;
    }
}