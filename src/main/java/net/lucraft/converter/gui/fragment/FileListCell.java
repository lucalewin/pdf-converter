package net.lucraft.converter.gui.fragment;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import net.lucraft.converter.gui.ConverterApplication;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileListCell extends ListCell<File> {

	@FXML private GridPane gpRoot;
	@FXML private ImageView imgFileIcon;
	@FXML private Label lblFilePath;

	private FXMLLoader loader;

	public FileListCell() {
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		setOnDragDetected(this::handleOnDragDetected);
		setOnDragOver(this::handleOnDragOver);
		setOnDragEntered(this::handleOnDragEntered);
		setOnDragExited(this::handleOnDragExited);
		setOnDragDropped(this::handleOnDragDropped);
		setOnDragDone(DragEvent::consume);
	}

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

	private void handleOnDragDetected(MouseEvent event) {
		if (getItem() == null) {
			return;
		}

		Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
		ClipboardContent content = new ClipboardContent();
		content.putFiles(List.of(getItem()));
		content.putString(String.valueOf(getIndex()));
		dragboard.setDragView(imgFileIcon.getImage(), -10, 3);
		dragboard.setContent(content);

		event.consume();
	}

	public void handleOnDragOver(DragEvent event) {
		if (event.getGestureSource() != this && event.getDragboard().hasFiles()) {
			event.acceptTransferModes(TransferMode.MOVE);
		}
		event.consume();
	}

	private void handleOnDragEntered(DragEvent event) {
		if (event.getGestureSource() != this && event.getDragboard().hasString()) {
			setOpacity(0.3);
		}
	}

	private void handleOnDragExited(DragEvent event) {
		if (event.getGestureSource() != this && event.getDragboard().hasFiles()) {
			setOpacity(1);
		}
	}

	private void handleOnDragDropped(DragEvent event) {
		Dragboard dragboard = event.getDragboard();

		if (dragboard.hasFiles() && getItem() != null) {
			List<File> files = getListView().getItems();
			File file = dragboard.getFiles().get(0);

			int draggedIndex = 0;

			if (dragboard.hasString()) {
				draggedIndex = Integer.parseInt(dragboard.getString());
			} else {
				draggedIndex = files.indexOf(file);
			}

//			int draggedIndex = files.indexOf(file);
			int thisIndex = getIndex(); //files.indexOf(getItem());

			files.set(draggedIndex, getItem());
			files.set(thisIndex, file);

			getListView().getItems().setAll(List.copyOf(files));
		}

		event.setDropCompleted(true);
		event.consume();
	}
}
