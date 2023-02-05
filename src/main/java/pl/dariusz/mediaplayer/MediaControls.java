package pl.dariusz.mediaplayer;

import javafx.event.ActionEvent;

public interface MediaControls {

    int[] speeds = {50,75,90,100,115,130,150,200};

    void pauseMedia(ActionEvent event);

    void resetMedia(ActionEvent event);

    void playMedia(ActionEvent event);

    void changeSpeed(ActionEvent event);

    void uploadFile(ActionEvent event);

    void fitMediaViewSize();

    void setProgressBarDuration();
}
