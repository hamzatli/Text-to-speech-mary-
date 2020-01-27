package crbn.tts;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import com.sun.media.sound.WaveFileWriter;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.modules.synthesis.Voice;
import marytts.signalproc.effects.AudioEffect;
import marytts.signalproc.effects.AudioEffects;

/**
 * Created by Ismayil Hamzatli on Jan, 2020
 */
public class TextToSpeech {

    private AudioPlayer tts;
    private MaryInterface marytts;
    private AudioInputStream stream;

    /**
     * Constructor
     */
    public TextToSpeech() {
        try {
            marytts = new LocalMaryInterface();

        } catch (MaryConfigurationException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    //----------------------GENERAL METHODS---------------------------------------------------//

    /**
     * Transform text to speech
     *
     * @param text
     *            The text that will be transformed to speech
     * @param daemon
     *            <br>
     *            <b>True</b> The thread that will start the text to speech Player will be a daemon Thread <br>
     *            <b>False</b> The thread that will start the text to speech Player will be a normal non daemon Thread
     * @param join
     *            <br>
     *            <b>True</b> The current Thread calling this method will wait(blocked) until the Thread which is playing the Speech finish <br>
     *            <b>False</b> The current Thread calling this method will continue freely after calling this method
     */
    public void speak(String text , float gainValue , boolean daemon , boolean join)  {

        // Stop the previous player
        stopSpeaking();



        try (AudioInputStream audio = marytts.generateAudio(text)) {



            AudioSystem.write(audio, AudioFileFormat.Type.WAVE, new File("test.mp3"));



            tts = new AudioPlayer();
            tts.setAudio(audio);
            tts.setGain(gainValue);
            tts.setDaemon(daemon);
            tts.start();
            if (join)
                tts.join();

        } catch (SynthesisException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error saying phrase.", ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "IO Exception", ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Interrupted ", ex);
            tts.interrupt();
        }
    }

    /**
     * Stop the MaryTTS from Speaking
     */
    public void stopSpeaking() {
        // Stop the previous player
        if (tts != null)
            tts.cancel();
    }

    //----------------------GETTERS---------------------------------------------------//

    /**
     * Available voices in String representation
     *
     * @return The available voices for MaryTTS
     */
    public Collection<Voice> getAvailableVoices() {
        return Voice.getAvailableVoices();
    }

    /**
     * @return the marytts
     */
    public MaryInterface getMarytts() {
        return marytts;
    }

    /**
     * Return a list of available audio effects for MaryTTS
     *
     * @return
     */
    public List<AudioEffect> getAudioEffects() {
        return StreamSupport.stream(AudioEffects.getEffects().spliterator(), false).collect(Collectors.toList());
    }

    //----------------------SETTERS---------------------------------------------------//

    /**
     * Change the default voice of the MaryTTS
     *
     * @param voice
     */
    public void setVoice(String voice) {
        marytts.setVoice(voice);
    }

//    public synchronized  byte[] convertAudioInputStream2ByteArray() {
//        stream = this.stream;
//        byte[] array;
//        try {
//            array = new byte[(int)(stream.getFrameLength() * stream.getFormat().getFrameSize())];   // initialize the byte array with the length of the stream
//            stream.read(array);         // write the stream's bytes into the byte array
//        } catch (IOException e) {       // in case of an IOException
//            e.printStackTrace();        // output error
//            return new byte[0];         // return empty array
//        }
//        return array;
//    }


}
