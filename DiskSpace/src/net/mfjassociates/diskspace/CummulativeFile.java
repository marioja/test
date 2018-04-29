package net.mfjassociates.diskspace;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.event.EventHandler;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;

public class CummulativeFile /*implements ListChangeListener<TreeItem<CummulativeFile>>*/ {

	private File theUnderlyingFile;
	private CummulativeFile parent;
	private LongProperty length = new SimpleLongProperty(0l);
	private List<CummulativeFile> files = new ArrayList<CummulativeFile>();
	private TreeItem<CummulativeFile> treeItem;

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
		System.out.println(Math.pow(2,20));
	}

	/**
	 * This event handler can be used to delete CummulativeFile from this.files when one the treeItem passed as argument's
	 * child is deleted
	 * @param treeItem
	 */
	public void addEventHandler(TreeItem<CummulativeFile> treeItem) {

		treeItem.addEventHandler(TreeItem.<CummulativeFile>childrenModificationEvent(), new EventHandler<TreeItem.TreeModificationEvent<CummulativeFile>>() {
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
		});
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

	public LongProperty getLengthProperty() {
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
			return (long)(((double)l)/twoExp(10))+" Kb";
		} else if (l<twoExp(30)) { // megabytes
			return (long)(((double)l)/twoExp(20))+" Mb";			
		} else if (l<twoExp(40)) { // gigabytes
			return (long)(((double)l)/twoExp(30))+" Gb";			
		} else  { // terabytes
			return (long)(((double)l)/twoExp(40))+" Tb";			
		}
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
		if (this.parent != null && this.parent != this)
			this.parent.addLength(bytes);
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
