package net.mfjassociates.diskspace;

import static net.mfjassociates.diskspace.FileSystemHandler.createDir;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.text.NumberFormat;

import edu.umd.cs.treemap.MapLayout;
import edu.umd.cs.treemap.MapModel;
import edu.umd.cs.treemap.Mappable;
import edu.umd.cs.treemap.Rect;
import edu.umd.cs.treemap.SquarifiedLayout;
import edu.umd.cs.treemap.test.MyMap;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class DiskSpaceController {
	
	@FXML private VBox treeViewVbox;
	@FXML private TreeView<CummulativeFile> fsTreeView;
	@FXML private Label statusMessageLabel;
	@FXML private ProgressBar progressBar;
	@FXML private BorderPane mainBorderPane;
	private Pane mainPane;
	private CummulativeFile rootFile;
	private Path rootPath=Paths.get("c:/");
	private LongProperty usableSpace=new SimpleLongProperty(0l);
	private LongProperty totalSpace=new SimpleLongProperty(0l);
	private LongProperty usedSpace=new SimpleLongProperty(0l);
	private ObjectProperty<ProgressBar> progressBarProperty=new SimpleObjectProperty<ProgressBar>(progressBar);
	
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
	private static Border myBorder=new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
	
	@FXML private void initialize() throws IOException {
		NumberFormat nf = NumberFormat.getNumberInstance();
		FileStore store = Files.getFileStore(rootPath);
		usableSpace.set(store.getUsableSpace());
		totalSpace.set(store.getTotalSpace());
		usedSpace.set(totalSpace.get()-usableSpace.get());
		System.out.println("Used space="+nf.format(usedSpace.get()));
		rootFile=createDir(rootPath, fsTreeView.sceneProperty(), progressBarProperty);
		fsTreeView.setCellFactory(this::createCell);
		TreeItem<CummulativeFile> rootItem = rootFile.getTreeItem();
		rootFile.addEventHandler(rootItem); // add event handler only to root since all events bubble back to root
		DiskSpaceController.this.setRootFile(rootFile);
		Platform.runLater(() -> {
			progressBar.managedProperty().bind(progressBar.visibleProperty());
			progressBar.setProgress(0d);
			progressBar.progressProperty().bind(rootFile.lengthProperty().divide(usedSpace.add(0d)));
			progressBar.setVisible(true);
			fsTreeView.setRoot(rootItem);
		});
	}

	private void drawSampleRectangles() {
		mainPane=new Pane();
		mainBorderPane.setCenter(mainPane);
		MapModel map=new MyMap(new double[]{60, 10, 10, 20});
		Mappable[] items = map.getItems();
		MapLayout algorithm = new SquarifiedLayout();
		System.out.println(MessageFormat.format("mainPane W x H: {0} x {1}", mainPane.getWidth(), mainPane.getHeight()));
		algorithm.layout(map, new Rect(0, 0, 400, 400));
//	    rect = items[i].getBounds();
//	    int a=(int)rect.x;
//	    int b=(int)rect.y;
//	    int c=(int)(rect.x+rect.w)-a;
//	    int d=(int)(rect.y+rect.h)-b;
//	    if (b==0) b=0;
//	    g.drawRect(a,b,c,d); //x, y, w, h
		for (int i=0; i<4;i++) {
			Pane panen=new Pane();
			Rect rect = items[i].getBounds();
		    double a=rect.x; // x
		    double b=rect.y; // y
		    int c=(int) ((rect.x+rect.w)-a); // width
		    int d=(int) ((rect.y+rect.h)-b); // height
			panen.setPrefWidth(c);
			panen.setPrefHeight(d);
			panen.setLayoutX(a);
			panen.setLayoutY(b);
			panen.setBorder(myBorder);
			mainPane.getChildren().add(panen);
		}
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
