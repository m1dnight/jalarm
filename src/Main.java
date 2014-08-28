import java.net.URL;

import mp3.MP3;
import mp3.Mp3Player;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Main {

  public static void main(String[] args) throws InterruptedException {
	  //new MP3("C:\\song.mp3").play();
	  Mp3Player m = new Mp3Player("C:\\song.mp3");
	  m.play();
	  Thread.sleep(4000);
	  m.pause();
	  Thread.sleep(4000);
	  m.resume();
	  Thread.sleep(4000);
	  m.stop();
	  Thread.sleep(4000);
	  System.out.println("Done");
  }
}