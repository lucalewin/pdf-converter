package net.lucraft.converter.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.lucraft.converter.Config;

import java.io.IOException;

public class ConverterApplication extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		startup();

		FXMLLoader fxmlLoader = new FXMLLoader(ConverterApplication.class.getResource("converter.fxml"));
		Screen screen = Screen.getPrimary();
		Scene scene = new Scene(fxmlLoader.load(), screen.getBounds().getWidth() / 2, screen.getBounds().getHeight() / 2);
		stage.setTitle("Converter - Lucraft");
//		System.out.println(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("net/lucraft/converter/icons/icon.png")).getPath());
//		stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("net/lucraft/converter/icons/icon.png"))));
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

	public void startup() {
		Config.load();
	}

}