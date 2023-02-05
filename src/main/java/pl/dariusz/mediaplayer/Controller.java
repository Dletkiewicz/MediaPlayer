package pl.dariusz.mediaplayer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import javafx.scene.control.Slider;
import javafx.util.Duration;


public class Controller implements Initializable, MediaControls {



    @FXML
    private MediaPlayer mediaPlayer;
    @FXML
    private MediaView mediaView;
    @FXML
    private ComboBox<String> mediaSpeed;
    @FXML
    private Slider volumeSlider, progressBar;
    @FXML
    private Media media;
    @FXML
    private Label audioTitle;
    @FXML
    private StackPane pane;

    public static String title;

    @Override
    public void uploadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        String path = file.toURI().toString();
        String fileExtension = path.substring(path.lastIndexOf(".") + 1);
        title = file.getName().toString();

        if(path != null && fileExtension.equals("mp4") || fileExtension.equals("mov")){
            if(mediaPlayer != null){
                mediaPlayer.stop();
            }

                media = new Media(path);
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);


            fitMediaViewSize();
            setProgressBarDuration();
           mediaPlayer.play();

        } else if(path != null && fileExtension.equals("mp3")  || fileExtension.equals("wav")) {
            if(mediaPlayer != null) {
                mediaPlayer.stop();
            }
                media = new Media(path);
                mediaPlayer = new MediaPlayer(media);

            setProgressBarDuration();
            updateAudioTitle(title);

            audioTitle.widthProperty().addListener((obs, oldVal, newVal) -> {
                audioTitle.setFont(new Font(34));
            });
            mediaPlayer.play();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateAudioTitle(title);

        for (int speed : speeds) {
            mediaSpeed.getItems().add(speed + "%");
        }
            mediaSpeed.setOnAction(this::changeSpeed);
            volumeSlider.valueProperty().addListener((observableValue, number, t1) -> mediaPlayer.setVolume(volumeSlider.getValue() * 0.01));
    }

    @Override
    public void pauseMedia(ActionEvent event){
        mediaPlayer.pause();
    }

    @Override
    public void resetMedia(ActionEvent event){
        mediaPlayer.seek(Duration.seconds(0));
    }

    @Override
    public void playMedia(ActionEvent event){
        changeSpeed(null);
        mediaPlayer.play();
    }

    @Override
    public void changeSpeed(ActionEvent event){
        if(mediaSpeed.getValue() == null){
            mediaPlayer.setRate(1);
        }
        else {
            mediaPlayer.setRate(Integer.parseInt(mediaSpeed.getValue().substring(0, mediaSpeed.getValue().length() - 1)) * 0.01);
        }
    }

    @Override
    public void fitMediaViewSize(){
            media.widthProperty().addListener((obs, oldVal, newVal) -> {
            DoubleProperty widthProp = mediaView.fitWidthProperty();
            DoubleProperty heightProp = mediaView.fitHeightProperty();

            widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            heightProp.bind(Bindings.multiply(
                    widthProp,
                    media.getHeight() / media.getWidth()
            ));
        });
    }

    @Override
    public void setProgressBarDuration() {
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> progressBar.setValue(newValue.toSeconds())
        );

        progressBar.setOnMousePressed(event1 -> mediaPlayer.seek(Duration.seconds(progressBar.getValue())));

        progressBar.setOnMouseDragged(event2 -> mediaPlayer.seek(Duration.seconds(progressBar.getValue())));

        mediaPlayer.setOnReady(() -> {
            Duration total = media.getDuration();
            progressBar.setMax(total.toSeconds());
        });
    }

    public void updateAudioTitle(String title) {
        audioTitle.setText(title);
    }
}