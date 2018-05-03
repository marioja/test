package net.mfjassociates.diskspace;

import static net.mfjassociates.diskspace.FileSystemHandler.createDir;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;

public class DiskSpaceController {
	
	@FXML private VBox treeViewVbox;
	@FXML private TreeView<CummulativeFile> fsTreeView;
	@FXML private Label statusMessageLabel;
	@FXML private ProgressBar progressBar;
	private CummulativeFile rootFile;
	private Path rootPath=Paths.get("c:/");
	private LongProperty usableSpace=new SimpleLongProperty(0l);
	private LongProperty totalSpace=new SimpleLongProperty(0l);
	private LongProperty usedSpace=new SimpleLongProperty(0l);
	
	public long getUsableSpace() {
		return usableSpace.get();
	}

	public void setUsableSpace(long usableSpace) {
		this.usableSpace.set(usableSpace);
	}

	public long getTotalSpace() {
		return totalSpace.get();
	}

	public void setTotalSpace(long totalSpace) {
		this.totalSpace.set(totalSpace);
	}
	
	public void setRootPath(Path aPath) {
		this.rootPath=aPath;
	}
	
	public LongProperty usableSpaceProperty() {
		return usableSpace;
	}
	
	public LongProperty totalSpaceProperty() {
		return totalSpace;
	}
	
	public LongProperty usedSpaceProperty() {
		return usedSpace;
	}
	
	public void setRootFile(CummulativeFile aRootFile) {
		this.rootFile=aRootFile;
	}
	
	@FXML private void initialize() throws IOException {
		NumberFormat nf = NumberFormat.getNumberInstance();
		FileStore store = Files.getFileStore(rootPath);
		usableSpace.set(store.getUsableSpace());
		totalSpace.set(store.getTotalSpace());
		usedSpace.set(totalSpace.get()-usableSpace.get());
		System.out.println("Used space="+nf.format(usedSpace.get()));
		rootFile=createDir(rootPath, fsTreeView);
		fsTreeView.setCellFactory(this::createCell);
		TreeItem<CummulativeFile> rootItem = rootFile.getTreeItem();
//		rootFile.addEventHandler(rootItem); // add event handler only to root since all events bubble back to root
		DiskSpaceController.this.setRootFile(rootFile);
		Platform.runLater(() -> {
			progressBar.managedProperty().bind(progressBar.visibleProperty());
			progressBar.setProgress(0d);
			progressBar.progressProperty().bind(rootFile.lengthProperty().divide(usedSpace.add(0d)));
			progressBar.setVisible(true);
			fsTreeView.setRoot(rootItem);
		});
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
