package org.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class NotePage {

    private ArrayList<ArrayList<Character>> contents;
    private int[] cursorLoc;
    private static final HashSet<Character> separators = getSepListSet("\n -_");

    /**
     * Creates a new empty NotePage object
     */
    public NotePage() {
        this("", new int[2]);
    }

    /**
     * Creates a new NotePage object.
     * @param contents - the contents of the document
     * @param cursorLoc - the starting location of the cursor
     */
    public NotePage(String contents, int[] cursorLoc) {
        if (contents == null || cursorLoc == null) {
            throw new IllegalArgumentException();
        }
        this.cursorLoc = cursorLoc;
        this.contents = new ArrayList<>();
        this.contents.add(new ArrayList<>());
        int line = 0;
        for (char ch : contents.toCharArray()) {
            if (ch == '\n') {
                line ++;
                this.contents.add(new ArrayList<>());
            } else {
                this.contents.get(line).add(ch);
            }
        }

        clampCoords(cursorLoc);

        System.out.println(this.contents);

    }    

    /**
     * Returns a set of all characters in the given string
     * @param characters - string to source characters from
     * @return the set containing all characters in the given string
     */
    private static HashSet<Character> getSepListSet(String characters) {
        if (characters == null) {
            throw new IllegalArgumentException();
        }
        HashSet<Character> out = new HashSet<>();
        for (char ch : characters.toCharArray()) {
            out.add(ch);
        }
        return out;
    }

    public int[] getCursorLoc() {
        return cursorLoc;
    }

    /**
     * Returns the full contents of the document as a string
     * @return the contents of the document
     */
    public String getFull() {
        StringBuilder sb = new StringBuilder();
        for (ArrayList<Character> line: contents) {
            for (char ch : line) {
                sb.append(ch);
            }
            sb.append('\n');
        }
        sb.append("Cursor at: Ln " + (cursorLoc[0] + 1) + ", Col " + (cursorLoc[1] + 1));
        return sb.toString();
    }

    /**
     * Returns the word at the given coordinates as a string
     * @param coords - coordinates of word
     * @return the word at coords
     */
    public String getWord(int[] coords) {
        int[] bounds = getWordBounds(coords);

        StringBuilder sb = new StringBuilder();
        for (int i = bounds[0]; i <= bounds[1]; i++) {
            sb.append(contents.get(cursorLoc[0]).get(i));
        }

        return sb.toString();
    }

    /**
     * Returns the word underneath the cursor as a string
     * @return the selected word
     */
    public String getWord() {
        return getWord(cursorLoc);
    }

    /**
     * Returns the bounds of the word the given coordinates fall within
     * @param coords - coordinate inside word
     * @return beginning and end indices of word within its line
     */
    private int[] getWordBounds(int[] coords) {
        ArrayList<Character> line = contents.get(cursorLoc[0]);
        if (!separators.contains(line.get(cursorLoc[1]))) {
            int startIndex = cursorLoc[1];
            int endIndex = cursorLoc[1];
            while (startIndex >= 1 && !separators.contains(line.get(startIndex - 1))) {
                startIndex --;
            }

            while (endIndex < contents.get(cursorLoc[0]).size() - 1 && 
                    !separators.contains(line.get(endIndex + 1))) {
                endIndex ++;
            }

            return new int[]{startIndex, endIndex};
        }
        return new int[]{coords[1], coords[1]};
    }

    /**
     * Returns the line underneath the cursor as a string
     * @return the selected line
     */
    public String getLine() {
        StringBuilder sb = new StringBuilder();
        ArrayList<Character> line = contents.get(cursorLoc[0]);
        for (char ch : line) {
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * Returns the character underneath the cursor as a char
     * @return the selected character
     */
    public char getCharSelected() {
        return contents.get(cursorLoc[0]).get(cursorLoc[1]);
    }

    /**
     * Sets the cursor location to the given coordinates
     * @param cursorLoc - cursor location array in the format [row, col]
     */
    public void setCursorLoc(int[] cursorLoc) {
        clampCoords(cursorLoc);
        this.cursorLoc = cursorLoc;
    }

    /**
     * Sets the cursor location to the given coordinates
     * @param row - cursor row
     * @param col - cursor column
     */
    public void setCursorLoc(int row, int col) {
        setCursorLoc(new int[]{row, col});
    }

    /**
     * Inserts the given text after the given coordinates
     * @param insertion - text to insert
     * @param coords - location to insert after
     * @param moveCursor - if true moves the cursor to the end of the new insertion
     */
    public void insert(String insertion, int[] coords, boolean moveCursor) {
        if (insertion == null) {
            throw new IllegalArgumentException();
        }
        if (!isValidLoc(coords)) {
            throw new IllegalArgumentException("Invalid insertion location: " + Arrays.toString(coords));
        }
        
        int row = coords[0];
        int rowsAdded = 0;
        int insertionIndex = coords[1] + 1;
        for (int i = 0; i < insertion.length(); i++) {
            if (insertion.charAt(i) == '\n') {
                contents.add(row + 1, new ArrayList<>());
                rowsAdded ++;
                row ++;
                insertionIndex = 0;
            } else {
                contents.get(row).add(insertionIndex, insertion.charAt(i));
                insertionIndex ++;
            }
        }

        if (moveCursor) {
            cursorLoc[0] = coords[0] + rowsAdded;
            cursorLoc[1] = insertionIndex - 1;
        }
    }

    /**
     * Inserts the given text after the current cursor position
     * @param insertion - text to insert
     */
    public void insert(String insertion) {
        if (insertion == null) {
            throw new IllegalArgumentException();
        }
        insert(insertion, cursorLoc, true);
    }

    /**
     * Removes the text in between the given start and end coordinates (inclusive)
     * @param start - start of removal region
     * @param end - end of removal region
     */
    public void remove(int[] start, int[] end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException();
        }
        if (!isValidLoc(start) || !isValidLoc(end)) {
            throw new IllegalArgumentException("Invalid range: " + Arrays.toString(start) + " - " + Arrays.toString(end));
        }
        if (start[0] > end[0] || (start[0] == end[0] && start[1] > end[1])) {
            throw new IllegalArgumentException("Start of range comes after end");
        }

        if (start[0] == end[0]) {
            // remove from the middle of a line
            for (int i = start[1]; i <= end[1]; i++) {
                contents.get(start[0]).remove(start[1]);
            }
        } else {
            // removing at least parts of multiple lines

            // remove end of starting line
            int startLineLen = contents.get(start[0]).size();
            for (int i = start[1]; i < startLineLen; i++) {
                contents.get(start[0]).remove(start[1]);
            }

            // remove beginning of last line
            for (int i = 0; i < end[1] + 1; i++) {
                contents.get(end[0]).remove(0);
            }

            // remove newline (merge start and end lines)
            contents.get(start[0]).addAll(contents.get(end[0]));

            // remove lines in between
            for (int i = 0; i < end[0] - start[0]; i++) {
                contents.remove(start[0] + 1);
            }
        }
        clampCoords(cursorLoc);
    }

    /**
     * Replaces text in the given region (inclusive) with the given string
     * @param replacement - text to add
     * @param start - start of replacement region
     * @param end - end of replacement region
     * @param moveCursor - if true moves the cursor to the end of the replaced text
     */
    public void replace(String replacement, int[] start, int[] end, boolean moveCursor) {
        if (start == null || end == null || 
                replacement == null || !isValidLoc(start) || !isValidLoc(end)) {
            throw new IllegalArgumentException();
        }

        remove(start, end);
        insert(replacement, new int[]{start[0], start[1] - 1}, moveCursor);
    }

    public void replaceWord(String text) {
        if (text == null) {
            throw new IllegalArgumentException();
        }

        int[] bounds = getWordBounds(cursorLoc);
        replace(text, new int[]{cursorLoc[0], bounds[0]}, new int[]{cursorLoc[0], bounds[1]}, true);
    }

    public void replaceLine(String text) {
        if (text == null) {
            throw new IllegalArgumentException();
        }

        ArrayList<Character> newLine = new ArrayList<>();
        for (char c : text.toCharArray()) {
            newLine.add(c);
        }
        contents.set(cursorLoc[0], newLine);
        clampCoords(cursorLoc);
    }

    /**
     * Clamps the given int array to be a valid coordinate in the document
     * @param coords - array to be clamped
     */
    private void clampCoords(int[] coords) {
        if (coords == null || coords.length != 2) {
            throw new IllegalArgumentException("Specified cursor location is null or the wrong size");
        }
        if (coords[0] > contents.size() - 1) {
            coords[0] = contents.size() - 1;
        } else if (coords[0] < 0) {
            coords[0] = 0;
        }
        if (coords[1] > contents.get(coords[0]).size() - 1) {
            coords[1] = contents.get(coords[0]).size() - 1;
        } else if (coords[1] < 0) {
            coords[1] = 0;
        }
    }

    /**
     * Checks whether the given array is a valid coordinate in the document
     * @param coords - coordinates to check
     * @return true if coords is valid, otherwise false
     */
    private boolean isValidLoc(int[] coords) {
        if (coords == null || coords.length != 2) {
            throw new IllegalArgumentException();
        }
        if (coords[0] > contents.size() - 1 || coords[0] < 0) {
            return false;
        }
        if (coords[1] > contents.get(coords[0]).size() || coords[1] < -1) {
            return false;
        }
        return true;
    }
    
}
