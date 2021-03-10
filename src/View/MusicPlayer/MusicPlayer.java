package View.MusicPlayer;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class MusicPlayer {

    private Media backgroundMusic;
    private Media winingMusic;
    private MediaPlayer mediaPlayer;

    public MusicPlayer(String backgroundMusic,String winingMusic){
        URL winingUrl= getClass().getResource(winingMusic);
        URL backgroundUrl= getClass().getResource(backgroundMusic);
        this.winingMusic = new Media(winingUrl.toString());
        this.backgroundMusic = new Media(backgroundUrl.toString());
    }

    /**Play the wining music**/
    public void playWiningMusic(){
        if(mediaPlayer != null ){mediaPlayer.pause();};
        mediaPlayer = new MediaPlayer(this.winingMusic);
        mediaPlayer.setCycleCount(AudioClip.INDEFINITE);
        mediaPlayer.play();
    }

    /**Play the wining music**/
    public void playBackgroundMusic(){
        if(mediaPlayer != null ){mediaPlayer.pause();};
        mediaPlayer = new MediaPlayer(this.backgroundMusic);
        mediaPlayer.setCycleCount(AudioClip.INDEFINITE);
        mediaPlayer.play();
    }


}
