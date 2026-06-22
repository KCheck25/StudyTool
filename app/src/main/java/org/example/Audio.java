package org.example;
import java.io.*;
import javax.sound.sampled.*;
public class Audio {
    public static void playSound(String fileName) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        InputStream stream = Audio.class.getClassLoader().getResourceAsStream("sounds/" + fileName);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(stream);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
        clip.drain();
    }
}