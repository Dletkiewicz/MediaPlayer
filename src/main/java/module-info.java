module pl.dariusz.mediaplayer {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.media;

    opens pl.dariusz.mediaplayer to javafx.fxml;
    exports pl.dariusz.mediaplayer;

}