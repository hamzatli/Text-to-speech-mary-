package crbn.tts;

import marytts.exceptions.SynthesisException;
import marytts.modules.synthesis.Voice;

import javax.sound.sampled.AudioInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Ismayil Hamzatli on Jan, 2020
 */
public class Main {

    public static void main(String[] args) throws IOException, SynthesisException {

        String s = "I managed to find an answer to my question";


        TextToSpeech tts = new TextToSpeech();

        tts.setVoice("cmu-rms-hsmm");

        tts.speak(s, 2.0f, false, false);


    }

}
