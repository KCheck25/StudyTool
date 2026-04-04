package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class TestClient {
    
    public static void main(String[] args) {
        runNotesLoop();
    }

    public static void runNotesLoop() {
        NotePage note = new NotePage();
        boolean running = true;
        while (running) {
            Scanner console = new Scanner(System.in);
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

        int[] start = {0, 6};
        int[] end = {1, 4};

        System.out.printf("Full Doc:\n\"%s\"\n", note.getFull());
        note.replaceLine("potato");
        System.out.printf("Word: \"%s\"\n", note.getWord());
        //System.out.printf("Full line: \"%s\"\n", note.getLine());
        //System.out.println();
        //note.insert("goodbye");
        //note.replace("hehe", start, end);
        System.out.printf("Full Doc:\n\"%s\"\n", note.getFull());
    }

}
