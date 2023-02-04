package pl.dariusz.mediaplayer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;


public class Controller implements Initializable {

    private String path;
    private MediaPlayer mediaPlayer;
    private int[] speeds = {50,75,90,100,115,130,150,200};

    @FXML
    private MediaView mediaView;
    @FXML
    private ComboBox<String> mediaSpeed;
    @FXML
    private Slider volumeSlider, progressBar;
    @FXML
    private Button uploadButton;



    @FXML
    private void uploadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        path = file.toURI().toString();

        if(path != null){
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            DoubleProperty widthProp = mediaView.fitWidthProperty();
            DoubleProperty heightProp = mediaView.fitHeightProperty();

            widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            heightProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));


            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> progressBar.setValue(newValue.toSeconds())
            );

            progressBar.setOnMousePressed(event1 -> mediaPlayer.seek(Duration.seconds(progressBar.getValue())));

            progressBar.setOnMouseDragged(event12 -> mediaPlayer.seek(Duration.seconds(progressBar.getValue())));

            mediaPlayer.setOnReady(() -> {
                Duration total = media.getDuration();
                progressBar.setMax(total.toSeconds());
            });

            mediaPlayer.play();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

            for (int i=0; i<speeds.length; i++) {
            mediaSpeed.getItems().add(Integer.toString(speeds[i]) + "%");
        }
            mediaSpeed.setOnAction(this::changeSpeed);
            volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

                    mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
                }
            });
    }

    public void pauseMedia(ActionEvent event){
        mediaPlayer.pause();
    }

    public void resetMedia(ActionEvent event){
        mediaPlayer.seek(Duration.seconds(0));
    }

    public void playMedia(ActionEvent event){
        changeSpeed(null);
        mediaPlayer.play();
    }

    public void changeSpeed(ActionEvent event){
        if(mediaSpeed.getValue() == null){
            mediaPlayer.setRate(1);
        }
        else {
            mediaPlayer.setRate(Integer.parseInt(mediaSpeed.getValue().substring(0, mediaSpeed.getValue().length() - 1)) * 0.01);
        }
    }

}