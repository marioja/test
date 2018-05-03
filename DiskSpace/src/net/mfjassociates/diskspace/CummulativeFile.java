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

import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;

public class CummulativeFile /*implements ListChangeListener<TreeItem<CummulativeFile>>*/ {

	private File theUnderlyingFile;
	private CummulativeFile parent;
	private LongProperty length = new SimpleLongProperty(0l);
	private List<CummulativeFile> files = new ArrayList<CummulativeFile>();
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
		this.parent = aParent;
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
			this.parent.files.add(this);
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

	public File getFile() {
		return this.theUnderlyingFile;
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

	public List<CummulativeFile> getFiles() {
		return files;
	}

	public String toString() {
		return this.theUnderlyingFile.toPath().toString()+"("+getHumanLength()+")";
	}
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
				Platform.runLater(()->this.treeItem.getChildren().sort(lengthComparator.reversed()));
			}
		}
		if (this.parent != null) this.parent.addLength(bytes);
	}
//	@Override
//	public void onChanged(Change<? extends TreeItem<CummulativeFile>> c) {
//		while (c.next()) {
//			if (c.wasPermutated()) {
//				for (int i = c.getFrom(); i < c.getTo(); ++i) {
//					// permutate
//				}
//			} else if (c.wasUpdated()) {
//				// update item
//			} else {
//				for (TreeItem<CummulativeFile> remitem : c.getRemoved()) {
//					// remitem.remove(TreeItem.this);
//				}
//				for (TreeItem<CummulativeFile> additem : c.getAddedSubList()) {
//					// additem.add(Outer.this);
//				}
//			}
//		}
//	}
}
