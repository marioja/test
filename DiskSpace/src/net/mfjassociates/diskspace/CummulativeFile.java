package net.mfjassociates.diskspace;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

public class CummulativeFile {
	
	private File theUnderlyingFile;
	private CummulativeFile parent;
	private LongProperty length=new SimpleLongProperty(0l);
	private List<CummulativeFile> files=new ArrayList<CummulativeFile>();
	
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
		this.theUnderlyingFile=aFile;
		this.parent=aParent;
		if (this.parent!=null) {
			if (this.theUnderlyingFile.isFile()) {
				this.parent.addLength(this.theUnderlyingFile.length());
			}
			this.parent.files.add(this);
		}
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
		return this.theUnderlyingFile.toPath().toString();
	}
	public String getFileType() {
		if (this.theUnderlyingFile.isDirectory()) return "d:";
		else return "f:";
	}
	public synchronized void addLength(long bytes) {
		length.set(length.get()+bytes);
		if (this.parent!=null && this.parent!=this) this.parent.addLength(bytes);
	}
}