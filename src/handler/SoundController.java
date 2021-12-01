package handler;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import model.Sounds;

public class SoundController {

	/**
	 * {@code sound}에 해당하는 소리를 새로운 스레드에서 재생한다.
	 * @param sound 재생할 소리
	 */
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
		case TYPING:
			return new File("assets/sounds/typing.wav");
		default:
			assert (false);
			break;
		}
		return null;
	}
	
}
