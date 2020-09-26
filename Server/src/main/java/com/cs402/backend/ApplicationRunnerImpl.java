package com.cs402.backend;

import com.sun.media.sound.WaveFileReader;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
/**
   @description
    This class prompts the sound when server is up.
*/
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("---[Server is up!]---");
		this.playSound();
	}
	
	public void playSound() {
		try {
			Clip clip = AudioSystem.getClip();
			InputStream is= this.getClass().getResourceAsStream("GameOn.wav");
			clip.open(AudioSystem.getAudioInputStream(is));
			clip.start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
