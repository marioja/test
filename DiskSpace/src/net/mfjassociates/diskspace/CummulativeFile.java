package net.mfjassociates.diskspace;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.umd.cs.treemap.MapItem;
import edu.umd.cs.treemap.MapLayout;
import edu.umd.cs.treemap.MapModel;
import edu.umd.cs.treemap.Mappable;
import edu.umd.cs.treemap.Rect;
import edu.umd.cs.treemap.SquarifiedLayout;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CummulativeFile  extends MapItem implements ListChangeListener<CummulativeFile>, MapModel {

	private File theUnderlyingFile;
	private String filePathString;
	private Pane thePane;
	private CummulativeFile parent;
	private LongProperty length = new SimpleLongProperty(0l);
	private List<CummulativeFile> fileso = new ArrayList<CummulativeFile>();
	private ObservableList<CummulativeFile> files;
	private TreeItem<CummulativeFile> treeItem;
	private Comparator<TreeItem<CummulativeFile>> lengthComparator = Comparator.comparing(t->t.getValue().getLength());
	private static final DecimalFormat fsize=new DecimalFormat("0.0");
	private static final DecimalFormat bsize=new DecimalFormat(",###");
	
	static {
		fsize.setRoundingMode(RoundingMode.FLOOR);
	}


	public CummulativeFile(Path aPath) {
		this(aPath.toFile());
	}

	public CummulativeFile(File aFile) {
		this(null, aFile);// root
	}

	public CummulativeFile(CummulativeFile aParent, Path aPath) {
		this(aParent, aPath.toFile());
	}

	public CummulativeFile(CummulativeFile aParent, File aFile) {
		this.theUnderlyingFile = aFile;
		this.filePathString = aFile.toPath().toString();
		this.parent = aParent;
		this.thePane=new Pane();
		setupPane();
		this.treeItem=new TreeItem<CummulativeFile>(this);
		if (this.parent != null) {
			if (this.theUnderlyingFile.isFile()) {
				long l=this.theUnderlyingFile.length();
				this.parent.addLength(l);
				this.length.set(l);
			}
			Platform.runLater(() -> {
				this.parent.getTreeItem().getChildren().add(this.treeItem); // add this treeItem to parent treeItem children
			});
			this.parent.getFiles().add(this);
		}
	}
	public static void main(String[] args) {
		DecimalFormat df=new DecimalFormat(",###");
		System.out.println(df.format(30l));
		System.out.println(df.format(4888555666l));
		NumberFormat nf = NumberFormat.getNumberInstance();
		for (Path root : FileSystems.getDefault().getRootDirectories()) {

		    System.out.print(root + ": ");
		    try {
		        FileStore store = Files.getFileStore(root);
		        System.out.println("available=" + nf.format(store.getUsableSpace())
		                            + ", total=" + nf.format(store.getTotalSpace()));
		    } catch (IOException e) {
		        System.out.println("error querying space: " + e.toString());
		    }
		}
	}
	
	private ObservableList<CummulativeFile> getFiles() {
		if (files==null) {
			files=FXCollections.observableArrayList();
			files.addListener(this);
		}
		return files;
	}

	/**
	 * This event handler can be used to delete CummulativeFile from this.files when one the treeItem passed as argument's
	 * child is deleted
	 * @param treeItem
	 */
	public void addEventHandler(TreeItem<CummulativeFile> treeItem) {

		/*treeItem.addEventHandler(TreeItem.<CummulativeFile>childrenModificationEvent(), new EventHandler<TreeItem.TreeModificationEvent<CummulativeFile>>() {
			@Override
			public void handle(TreeModificationEvent<CummulativeFile> event) {
				if (event.wasAdded())
				{
					for(TreeItem<CummulativeFile> item : event.getAddedChildren())
					{
						System.out.println("Node " + item.getValue() + " has been added.");
					}
				}

				if (event.wasRemoved())
				{
					for(TreeItem<CummulativeFile> item : event.getRemovedChildren())
					{
						System.out.println("Node " + item.getValue() + " has been removed.");
					}
				}

			}
		});*/
		treeItem.addEventHandler(TreeItem.<CummulativeFile>branchExpandedEvent(), new EventHandler<TreeItem.TreeModificationEvent<CummulativeFile>>() {
			@Override
			public void handle(TreeModificationEvent<CummulativeFile> event) {
				if (event.wasExpanded()) {
					final TreeItem<CummulativeFile> item = event.getTreeItem();
					Platform.runLater(()->item.getChildren().sort(lengthComparator.reversed()));
				}
				
			}}

		);
	}
	
	public TreeItem<CummulativeFile> getTreeItem() {
		return treeItem;
	}

	public Pane getPane() {
		return this.thePane;
	}
	public File getFile() {
		return this.theUnderlyingFile;
	}

	@Override
	public double getSize() {
		return (double) length.get();
	}
	public long getLength() {
		return length.get();
	}

	public void setLength(long aLength) {
		length.set(aLength);
	}

	public LongProperty lengthProperty() {
		return length;
	}

	public CummulativeFile getParent() {
		return this.parent;
	}

	public String toString() {
		return this.filePathString+"("+getHumanLength()+")";
	}
	/**
	 * Print the size of the file/folder using units easy for human to understand 
	 * @return the length using the correct scaling followed by the units
	 */
	public String getHumanLength() {
		long l=length.get();
		if (l<twoExp(10)) { // bytes
			return l+" bytes";
		} else if (l<twoExp(20)) { // kilobytes
			return fsize.format(((double)l)/twoExp(10))+" Kb";
		} else if (l<twoExp(30)) { // megabytes
			return fsize.format(((double)l)/twoExp(20))+" Mb";			
		} else if (l<twoExp(40)) { // gigabytes
			return fsize.format(((double)l)/twoExp(30))+" Gb";			
		} else  { // terabytes
			return fsize.format(((double)l)/twoExp(40))+" Tb";			
		}
	}
	public String getBytesLength() {
		return bsize.format(getLength());
	}
	private long twoExp(int e) {
		return (long) Math.pow(2, e);
	}

	public String getFileType() {
		if (this.theUnderlyingFile.isDirectory())
			return "d:";
		else
			return "f:";
	}

	public synchronized void addLength(long bytes) {
		length.set(length.get() + bytes);
		if (this.treeItem!=null) {
			if (treeItem.getChildren().size()>1 && treeItem.expandedProperty().get()) {
				Platform.runLater(()->{					
					this.treeItem.getChildren().sort(lengthComparator.reversed());
				});
			}
		}
		Platform.runLater(()->
			algorithm.layout(this, this.getBounds())
		);
		if (this.parent != null) this.parent.addLength(bytes);
	}
	private static Border myBorder=new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));

	/**
	 * Setup the pane to update from its rectangle object (bounds)
	 */
	public void setupPane() {
//		Rect rect = getBounds();
//	    double x=rect.x; // x
//	    double y=rect.y; // y
//	    int w=(int) ((rect.x+rect.w)-x); // width
//	    int h=(int) ((rect.y+rect.h)-y); // height
//		thePane.setPrefWidth(w);
//		thePane.setPrefHeight(h);
//		thePane.setLayoutX(x);
//		thePane.setLayoutY(y);
		thePane.setBorder(myBorder);
		thePane.prefWidthProperty().bind(Bindings.add(xProperty(), wProperty()));
		thePane.prefHeightProperty().bind(Bindings.add(yProperty(), hProperty()));
		thePane.layoutXProperty().bind(xProperty());
		thePane.layoutYProperty().bind(yProperty());
	}
	private ObjectProperty<Rect> bounds=new SimpleObjectProperty<Rect>(new Rect());
	private DoubleProperty x=new SimpleDoubleProperty(0d);
	private DoubleProperty y=new SimpleDoubleProperty(0d);
	private DoubleProperty w=new SimpleDoubleProperty(0d);
	private DoubleProperty h=new SimpleDoubleProperty(0d);
	
	public DoubleProperty xProperty() {
		return x;
	}
	public double getX() {
		return x.get();
	}
	public void setX(double aX) {
		x.set(aX);
	}
	public DoubleProperty wProperty() {
		return w;
	}
	public double getW() {
		return w.get();
	}
	public void setW(double aW) {
		w.set(aW);
	}

	public DoubleProperty yProperty() {
		return y;
	}
	public double gety() {
		return y.get();
	}
	public void setY(double aY) {
		y.set(aY);
	}
	public DoubleProperty hProperty() {
		return h;
	}
	public double getH() {
		return h.get();
	}
	public void setH(double aH) {
		h.set(aH);
	}
	
	
	public ObjectProperty<Rect> boundsProperty() {
		return bounds;
	}
	@Override
	public Rect getBounds() {
		return bounds.get();
	}
	@Override
	public void setBounds(Rect aBounds) {
		bounds.set(aBounds);
		x.set(bounds.get().x);
		y.set(bounds.get().y);
		w.set(bounds.get().w);
		h.set(bounds.get().h);
	}
	private static MapLayout algorithm = new SquarifiedLayout();
	@Override
	public void onChanged(Change<? extends CummulativeFile> c) {
		while (c.next()) {
			if (c.wasPermutated()) {
				for (int i = c.getFrom(); i < c.getTo(); ++i) {
					// permutate
				}
			} else if (c.wasUpdated()) {
				// update item
			} else {
				for (CummulativeFile remitem : c.getRemoved()) {
					// remitem.remove(TreeItem.this);
					Platform.runLater(()->this.thePane.getChildren().remove(remitem.thePane));
				}
				int from=c.getFrom();
				for (CummulativeFile additem : c.getAddedSubList()) {
					 // additem.add(CummulativeFile.this);
					additem.setOrder(from++);
					Platform.runLater(()->this.thePane.getChildren().add(additem.thePane));
					;
				}
			}
		}
	}

	@Override
	public Mappable[] getItems() {
		// TODO Auto-generated method stub
		return getFiles().toArray(new CummulativeFile[] {});
	}

}
