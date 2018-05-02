package net.mfjassociates.diskspace;

import static net.mfjassociates.diskspace.FileSystemHandler.createDir;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;

public class DiskSpaceController {
	
	@FXML private VBox treeViewVbox;
	@FXML private TreeView<CummulativeFile> fsTreeView;
	@FXML private Label statusMessageLabel;
	private CummulativeFile rootFile;
	
	public void setRootFile(CummulativeFile aRootFile) {
		this.rootFile=aRootFile;
	}
	
	@FXML private void initialize() {
		CummulativeFile rootFile=createDir("c:/", fsTreeView);
		fsTreeView.setCellFactory(this::createCell);
		TreeItem<CummulativeFile> rootItem = rootFile.getTreeItem();
//		rootFile.addEventHandler(rootItem); // add event handler only to root since all events bubble back to root
		DiskSpaceController.this.setRootFile(rootFile);
		Platform.runLater(() -> {
			fsTreeView.setRoot(rootItem);
		});
		System.out.println("Thread started, exiting...");
	}
	
	private TreeCell<CummulativeFile> createCell(TreeView<CummulativeFile> tv) {
		final Tooltip tooltip = new Tooltip();
		TreeCell<CummulativeFile> cell=new TreeCell<CummulativeFile>() {
			@Override
			public void updateItem(CummulativeFile file, boolean empty) {
				super.updateItem(file, empty);
				if (empty) {
					setText(null);
					setTooltip(null);
				} else {
					setText(file.toString());
					tooltip.setText(file.getHumanLength());
					setTooltip(tooltip);
				}
			}
		};
		cell.setOnMouseClicked(e -> {
			if (e.getClickCount() == 1 && !cell.isEmpty()) {
				CummulativeFile f = cell.getItem();
				statusMessageLabel.setText(f.toString()+ " - "+f.getBytesLength());
				System.out.println("Clicked "+f);
			}
		});
		return cell;
	}

	@FXML private void closeFired(ActionEvent event) throws IOException {
	}
	@FXML private void openFired(ActionEvent event) throws IOException {
		System.out.println(fsTreeView.getCellFactory());
	}
	@FXML private void saveFired(ActionEvent event) throws IOException {
	}
	@FXML private void saveAsFired(ActionEvent event) throws IOException {
	}
	@FXML private void preferencesFired(ActionEvent event) throws IOException {
	}
	@FXML private void copyBase64Fired(ActionEvent event) throws IOException {
	}
	@FXML private void pasteBase64Fired(ActionEvent event) throws IOException {
	}
	@FXML private void base64Fired(ActionEvent event) throws IOException {
	}
	@FXML private void metadataFired(ActionEvent event) throws IOException {
	}
	@FXML private void aboutFired(ActionEvent event) throws IOException {
	}
}
