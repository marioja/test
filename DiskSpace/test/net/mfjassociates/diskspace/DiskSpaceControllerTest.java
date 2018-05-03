package net.mfjassociates.diskspace;

import static org.junit.jupiter.api.Assertions.fail;

import java.text.NumberFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

class DiskSpaceControllerTest {
	
	private LongProperty usableSpaceProperty;
	private LongProperty totalSpaceProperty;
	private NumberFormat nf = NumberFormat.getNumberInstance();
	private StringProperty textProperty;
	private LongProperty lengthProperty;
	private DoubleProperty progressProperty;
	private DoubleProperty usedSpaceProperty;
	private LongProperty usedSpaceLongProperty;
	
	@BeforeEach
	public void setup() {
		usableSpaceProperty=new SimpleLongProperty(100000l); // one hundred thousands
		totalSpaceProperty =new SimpleLongProperty(1000000l); // one million
		textProperty=new SimpleStringProperty("");
		lengthProperty=new SimpleLongProperty(800000l);
		progressProperty=new SimpleDoubleProperty(0d);
		usedSpaceProperty=new SimpleDoubleProperty(totalSpaceProperty.get()-usableSpaceProperty.get());
		usedSpaceLongProperty=new SimpleLongProperty(totalSpaceProperty.get()-usableSpaceProperty.get());
		
	}

	@Test
	void testBindings() {
		textProperty.bind(Bindings.format("%f", Bindings.divide(lengthProperty, usedSpaceProperty)));
		usedSpaceProperty.bind(usedSpaceLongProperty);
		progressProperty.bind(lengthProperty.divide(usedSpaceLongProperty.add(0d)));
		System.out.println("used space property="+usedSpaceProperty.get());
		System.out.println("progress property="+progressProperty.get());
		lengthProperty.set(80000l);
		System.out.println("used space property="+usedSpaceProperty.get());
		System.out.println("progress property="+progressProperty.get());

	}
	@Test
	void testGetUsableSpace() {
		fail("Not yet implemented");
	}

	@Test
	void testSetUsableSpace() {
		fail("Not yet implemented");
	}

	@Test
	void testGetTotalSpace() {
		fail("Not yet implemented");
	}

	@Test
	void testSetTotalSpace() {
		fail("Not yet implemented");
	}

	@Test
	void testSetRootPath() {
		fail("Not yet implemented");
	}

	@Test
	void testUsableSpaceProperty() {
		fail("Not yet implemented");
	}

	@Test
	void testTotalSpaceProperty() {
		fail("Not yet implemented");
	}

	@Test
	void testUsedSpaceProperty() {
		fail("Not yet implemented");
	}

	@Test
	void testSetRootFile() {
		fail("Not yet implemented");
	}

}
