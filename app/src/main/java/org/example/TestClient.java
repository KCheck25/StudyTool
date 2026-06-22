package org.example;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class TestClient {

    public static void main(String[] args) throws Exception {
        Scanner console = new Scanner(System.in);
        flashcardTesting(console);
        testDeck(console);
        runNotesLoop();
    }

    public static void runNotesLoop(Scanner console) {
        NotePage note = new NotePage();
        boolean running = true;
        while (running) {
            System.out.print("Enter text: ");
            String input = console.nextLine();
            if (input.equals("quit") || input.equals("q")) {
                running = false;
            } else {
                // sloppy code for demo purposes
                String command = input;
                if (input.contains(" ")) {
                    command = input.substring(0, input.indexOf(" "));
                }
                if (command.equals("i")) {
                    note.insert(input.substring(input.indexOf(" ") + 1));
                } else if (command.equals("iln")) {
                    note.insert(input.substring(input.indexOf(" ") + 1) + "\n");
                } else if (command.equals("p")) {
                    System.out.println(note.getFull());
                } else if (command.equals("pw")) {
                    System.out.println(note.getWord());
                } else if (command.equals("pl")) {
                    System.out.println(note.getLine());
                } else {
                    System.out.printf("Invalid command: \"%s\"\n", command);
                }
            }
        }
    }

    public static void notesTesting2() {
        NotePage note = new NotePage();
        note.insert("Hello\nthere!\nfriend");
        System.out.println(note.getFull());
        note.insert("yippeeee");
        note.insert("oop");
        System.out.println(note.getFull());
    }

    public static void notesTesting() {
        String testString = "Hello there\nsweet world!\nStranger danger\nblah blah";
        System.out.println(testString);
        NotePage note = new NotePage(testString, new int[2]);
        note.setCursorLoc(1, 2);

        int[] start = { 0, 6 };
        int[] end = { 1, 4 };

        System.out.printf("Full Doc:\n\"%s\"\n", note.getFull());
        note.replaceLine("potato");
        System.out.printf("Word: \"%s\"\n", note.getWord());
        // System.out.printf("Full line: \"%s\"\n", note.getLine());
        // System.out.println();
        // note.insert("goodbye");
        // note.replace("hehe", start, end);
        System.out.printf("Full Doc:\n\"%s\"\n", note.getFull());
    }
    public static void flashcardTesting(Scanner console) throws Exception {
        System.out.print("Enter topic: ");
        String topic = console.nextLine();
        System.out.print("Enter question: ");
        String question = console.nextLine();
        System.out.print("Enter answer: ");
        String answer = console.nextLine();
        System.out.print("Enter priority score 1-3: ");
        int priorityScore = Integer.parseInt(console.nextLine());
        FlashCard fc = new FlashCard(topic, priorityScore, question, answer, false);
        String o = "";
        while (!o.equalsIgnoreCase("q")) {
            o = console.nextLine();
            if (o.equalsIgnoreCase("f")) {
                String side = fc.flip();
                System.out.println(side);
            }
        }
        o = "blah";
        while (!o.equalsIgnoreCase("q")) {
            System.out.print("Enter Guess: ");
            o = console.nextLine();
            if (!o.equalsIgnoreCase("q")) {
                fc.makeGuess(o);
            }
        }
    }
    public static void testDeck(Scanner console) throws Exception {
        System.out.print("enter subject");
        String subject = console.nextLine();
        System.out.print("do you want your deck to be sorted? y for yes");
        String answer = console.nextLine();
        boolean isSorted = false;
        if (answer.equalsIgnoreCase("y")) {
            isSorted = true;
        }
        Deck deck = new Deck(subject, isSorted);
        String o = "";
        while (!o.equalsIgnoreCase("q")) {
            System.out.print("A: add, R: remove, N: next card, GT: get all topics, GBT: get by topic, GCP: get completion percentage: ");
            o = console.nextLine();
            if (o.equalsIgnoreCase("a")) {
                System.out.print("enter topic: ");
                String topic = console.nextLine();
                System.out.print("priority score: ");
                int priorityScore = Integer.parseInt(console.nextLine());
                System.out.print("question: ");
                String question = console.nextLine();
                System.out.print("answer: ");
                String answer2 = console.nextLine();
                FlashCard fc = new FlashCard(topic, priorityScore, question, answer2, false);
                deck.addCard(fc);
            } else if (o.equalsIgnoreCase("n")) {
                FlashCard fc = deck.nextCard();
                if (fc == null) {
                    System.out.println(fc);
                    return;
                }
                System.out.println(fc.reset());
                String o2 = "";
                while (!o2.equalsIgnoreCase("q")) {
                    o2 = console.nextLine();
                    if (o2.equalsIgnoreCase("f")) {
                        fc.flip();
                        System.out.println(fc.toString());
                    } else if (o2.equalsIgnoreCase("m")) {
                        System.out.print("enter guess: ");
                        fc.makeGuess(console.nextLine());
                    }
                }
            } else if (o.equalsIgnoreCase("GT")) {
                System.out.print("A for active, V for viewed: ");
                o = console.nextLine();
                Set<String> topics = null;
                if (o.equalsIgnoreCase("a")) {
                    topics = deck.getAllTopics(false);
                } else if (o.equalsIgnoreCase("v")) {
                    topics = deck.getAllTopics(true);
                } else {
                    System.out.println("invalid option.");
                }
                System.out.println(topics);
            } else if (o.equalsIgnoreCase("GBT")) {
                System.out.print("what topic would you like to get? ");
                o = console.nextLine();
                for (FlashCard fc : deck.getByTopic(o, false)) {
                    System.out.println(fc.reset());
                }
            }
        }
    }
}