package net.mfjassociates.diskspace;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class CummulativeTreeItem<I> extends TreeItem<I> {
	private CummulativeFile file;
	public CummulativeTreeItem(I item, CummulativeFile aFile) {
		super(item);
		this.file=aFile;
	}
	@Override
	public ObservableList<TreeItem<I>> getChildren() {
		// TODO Auto-generated method stub
		return super.getChildren();
	}
	
}
