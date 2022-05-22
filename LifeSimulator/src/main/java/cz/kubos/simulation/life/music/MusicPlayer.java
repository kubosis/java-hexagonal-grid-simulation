package cz.kubos.simulation.life.music;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


/**
 * Simple music player
 */
public class MusicPlayer {
    Clip clip = null;

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(MusicPlayer.class);

    public MusicPlayer(String musicFilePath) {
        logger.info("Initializing music player");

        changeMusic(musicFilePath);
    }

    /**
     * Changes music that is currently played with another one
     */
    public void changeMusic(String musicFilePath) {
        stop();
        try {
            clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(musicFilePath));
            clip.open(inputStream);
            clip.start();
            clip.loop(1000);

            logger.info("Music player changed music");
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            logger.error("Audio could not play because: " + e.getMessage());
        }

    }

    /**
     * Stop and safely destroy the music player
     */
    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
            logger.info("Music player stopped");
        }
    }
}
