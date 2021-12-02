package handler;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import model.Sounds;

public class SoundController {

	private static final String DIRECTORY = "assets/sounds/";
	
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
			return new File(DIRECTORY + "button_click.wav");
		case GAME_START:
			return new File(DIRECTORY + "game_start.wav");
		case TYPING:
			return new File(DIRECTORY + "typing.wav");
		case KILL:
			return new File(DIRECTORY + "kill.wav");
		case KILL_STOP_ITEM:
			return new File(DIRECTORY + "kill_stop_item.wav");
		case KILL_BOMB_ITEM:
			return new File(DIRECTORY + "kill_bomb_item.wav");
		case COLLIDE:
			return new File(DIRECTORY + "collide.wav");
		case WARNING:
			return new File(DIRECTORY + "warning.wav");
		case WRONG:
			return new File(DIRECTORY + "wrong.wav");
		case SUCCESS:
			return new File(DIRECTORY + "success.wav");
		case ALL_CLEAR:
			return new File(DIRECTORY + "all_clear.wav");
		case GAME_OVER:
			return new File(DIRECTORY + "game_over.wav");
		default:
			assert (false);
			break;
		}
		return null;
	}
	
}
