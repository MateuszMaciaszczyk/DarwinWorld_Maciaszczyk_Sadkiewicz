package world.presenter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import javax.swing.*;
import java.io.IOException;

public abstract class AbstractApp extends Application {
    protected FXMLLoader loader;
    protected abstract String getFXMLPath();

    @Override
    public void start(Stage primaryStage) throws IOException {
        loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(getFXMLPath()));
        BorderPane viewRoot = loader.load();

        Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
        primaryStage.getIcons().add(icon);

        configureStage(primaryStage, viewRoot);
        primaryStage.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Animals World");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
