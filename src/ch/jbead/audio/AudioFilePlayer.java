package ch.jbead.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

/**
 * AudioFilePlayer.
 * Copyright (C) 2017  Casimir Forelli.
 *
 * Based on sample code taken from "Oliver Doepner's IT Blog"
 *
 * @see https://odoepner.wordpress.com/2013/07/19/play-mp3-or-ogg-using-javax-sound-sampled-mp3spi-vorbisspi/
 */
public class AudioFilePlayer {

    public static void main(String[] args) {
        final AudioFilePlayer player = new AudioFilePlayer();
        player.play("..\\source\\jbead_audio_cache\\4_0.wav");
        player.play("..\\source\\jbead_audio_cache\\Rot_0.wav");
    }

    /**
     *
     * @param filePath
     */
    public void play(String filePath) {
        final File file = new File(filePath);

        try (final AudioInputStream in = getAudioInputStream(file)) {

            final AudioFormat outFormat = getOutFormat(in.getFormat());
            final Info info = new Info(SourceDataLine.class, outFormat);

            try (final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {

                if (line != null) {
                    line.open(outFormat);
                    line.start();
                    stream(getAudioInputStream(outFormat, in), line);
                    line.drain();
                    line.stop();
                }
            }

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     *
     * @param inFormat
     * @return
     */
    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();

        final float rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    /**
     *
     * @param in
     * @param line
     * @throws IOException
     */
    private void stream(AudioInputStream in, SourceDataLine line) throws IOException {
        final byte[] buffer = new byte[4096];
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            line.write(buffer, 0, n);
        }
    }
}