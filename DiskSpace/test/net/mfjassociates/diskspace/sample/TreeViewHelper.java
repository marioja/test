package net.mfjassociates.diskspace.sample;
import java.util.ArrayList;

import javafx.scene.control.TreeItem;

public class TreeViewHelper 
{
	public TreeViewHelper()
	{
	}
	
	// This method creates an ArrayList of TreeItems (Products)
	public ArrayList<TreeItem<String>> getProducts()
	{
		ArrayList<TreeItem<String>> products = new ArrayList<TreeItem<String>>();
		
		TreeItem<String> cars = new TreeItem<String>("Cars");
		cars.getChildren().addAll(getCars());
		
		TreeItem<String> buses = new TreeItem<String>("Buses");
		buses.getChildren().addAll(getBuses());

		TreeItem<String> trucks = new TreeItem<String>("Trucks");
		trucks.getChildren().addAll(getTrucks());
		
		TreeItem<String> motorbikes = new TreeItem<String>("Motorcycles");
		motorbikes.getChildren().addAll(getMotorcycles());
		
		products.add(cars);
		products.add(buses);
		products.add(trucks);
		products.add(motorbikes);
		
		return products;
	}

	// This method creates an ArrayList of TreeItems (Cars)
	private ArrayList<TreeItem<String>> getCars()
	{
		ArrayList<TreeItem<String>> cars = new ArrayList<TreeItem<String>>();
		
		TreeItem<String> ferrari = new TreeItem<String>("Ferrari");
		TreeItem<String> porsche = new TreeItem<String>("Porsche");
		TreeItem<String> ford = new TreeItem<String>("Ford");
		TreeItem<String> mercedes = new TreeItem<String>("Mercedes");
		
		cars.add(ferrari);
		cars.add(porsche);
		cars.add(ford);
		cars.add(mercedes);
		
		return cars;		
	}

	// This method creates an ArrayList of TreeItems (Buses)
	private ArrayList<TreeItem<String>> getBuses()
	{
		ArrayList<TreeItem<String>> buses = new ArrayList<TreeItem<String>>();
		
		TreeItem<String> gm = new TreeItem<String>("GM");
		TreeItem<String> vw = new TreeItem<String>("VW");
		TreeItem<String> man = new TreeItem<String>("MAN");
		TreeItem<String> volvo = new TreeItem<String>("Volvo");
		
		buses.add(gm);
		buses.add(man);
		buses.add(volvo);
		buses.add(vw);
		
		return buses;		
	}
	
	// This method creates an ArrayList of TreeItems (Trucks)
	private ArrayList<TreeItem<String>> getTrucks()
	{
		ArrayList<TreeItem<String>> trucks = new ArrayList<TreeItem<String>>();
		
		TreeItem<String> scania = new TreeItem<String>("Scania");
		TreeItem<String> mercedes = new TreeItem<String>("Mercedes");
		TreeItem<String> gm = new TreeItem<String>("GM");
		TreeItem<String> ford = new TreeItem<String>("Ford");
		
		trucks.add(mercedes);
		trucks.add(scania);
		trucks.add(gm);
		trucks.add(ford);
		
		return trucks;
	}

	// This method creates an ArrayList of TreeItems (Motorbikes)
	private ArrayList<TreeItem<String>> getMotorcycles()
	{
		ArrayList<TreeItem<String>> motorcycles = new ArrayList<TreeItem<String>>();
		
		TreeItem<String> harley = new TreeItem<String>("Harley");
		TreeItem<String> suzuki = new TreeItem<String>("Suzuki");
		TreeItem<String> ktm = new TreeItem<String>("KTM");
		TreeItem<String> honda = new TreeItem<String>("Honda");
		
		motorcycles.add(harley);
		motorcycles.add(honda);
		motorcycles.add(ktm);
		motorcycles.add(suzuki);
		
		return motorcycles;
	}
}