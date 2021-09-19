package net.lucraft.converter.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.lucraft.converter.Config;

import java.io.IOException;
import java.util.Objects;

public class ConverterApplication extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(ConverterApplication.class.getResource("converter.fxml"));
		Screen screen = Screen.getPrimary();
		Scene scene = new Scene(fxmlLoader.load(), screen.getBounds().getWidth() / 2, screen.getBounds().getHeight() / 2);
		stage.setTitle("Converter - Lucraft");
		stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/net/lucraft/converter/icons/icon.png"))));
		stage.setScene(scene);

		stage.setOnCloseRequest(event -> {
			try {
				Config.getInstance().save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		stage.show();
	}
}