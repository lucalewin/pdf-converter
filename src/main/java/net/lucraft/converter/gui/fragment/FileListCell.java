package net.lucraft.converter.gui.fragment;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import net.lucraft.converter.gui.ConverterApplication;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileListCell extends ListCell<File> {

	@FXML private GridPane gpRoot;
	@FXML private ImageView imgFileIcon;
	@FXML private Label lblFilePath;

	private FXMLLoader loader;

	@Override
	protected void updateItem(File file, boolean empty) {
		super.updateItem(file, empty);

		if (empty || file == null) {
			setText(null);
			setGraphic(null);
		} else {
			if (loader == null) {
				loader = new FXMLLoader(ConverterApplication.class.getResource("file_list_cell.fxml"));
				loader.setController(this);

				try {
					loader.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);

			BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);

			Image image = SwingFXUtils.toFXImage(bufferedImage, null);

			imgFileIcon.setImage(image);
			lblFilePath.setText(file.getAbsolutePath());

			setText(null);
			setGraphic(gpRoot);
		}
	}
}
