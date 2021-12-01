package handler;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import model.Sounds;

public class SoundController {

	public static void play(Sounds sound) {
		new Thread() {
			@Override 
			public void run() {
				try {
					AudioInputStream ais = AudioSystem.getAudioInputStream(getFile(sound));
					Clip clip = AudioSystem.getClip();
					clip.stop();
					clip.open(ais);
					clip.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private static File getFile(Sounds sound) {
		switch (sound) {
		case BUTTON_CLICK:
			return new File("assets/sounds/button_click.wav");
		case GAME_START:
			return new File("assets/sounds/game_start.wav");
		default:
			assert (false);
			break;
		}
		return null;
	}
	
}
