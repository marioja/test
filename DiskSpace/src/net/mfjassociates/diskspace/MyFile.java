package net.mfjassociates.diskspace;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.LongProperty;

public class MyFile {
	
	private File theUnderlyingFile;
	private MyFile parent;
	private long length=0;
	private LongProperty lengthProperty;
	private List<MyFile> files=new ArrayList<MyFile>();
	
	public MyFile(Path aPath) {
		this(aPath.toFile());
	}
	public MyFile(File aFile) {
		this(null, aFile);// root
	}
	public MyFile(MyFile aParent, Path aPath) {
		this(aParent, aPath.toFile());
	}
	public MyFile(MyFile aParent, File aFile) {
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
		return length;
	}
	public MyFile getParent() {
		return this.parent;
	}
	public List<MyFile> getFiles() {
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
		length+=bytes;
		if (this.parent!=null && this.parent!=this) this.parent.addLength(bytes);
	}
}