package train;

/**
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * 
 * Main function to give an example of the model's functioning 
 * 
 * Authors of the new implementation 
 * @author Othmane EL AKRABA <othmane.el-akraba@imt-atlantique.net>
 * @author Soufiane LACHGUER <soufiane.lachguer@imt-atlantique.net>
 */
public class Main {
	public static void main(String[] args) {
		
		// Initializing Elements (stations + sections)
		Station A = new Station("GareA", 3);
		Station D = new Station("GareD", 3);
		Section AB = new Section("AB");
		Section BC = new Section("BC");
		Section CD = new Section("CD");
		
		// Initializing railway
		Railway r = new Railway(new Element[] { A, AB, BC, CD, D });
		System.out.println("The railway is:");
		System.out.println("\t" + r);
		
		// Initial position of all trains
		Position p = new Position(A, Direction.LR);
		try {
			
			// Initializing and starting trains
			Train t1 = new Train("1", p);
			t1.start();
			Train t2 = new Train("2", p);
			t2.start();
			Train t3 = new Train("3", p);
			t3.start();
			
		} catch (BadPositionForTrainException e) {
			System.out.println("Le train " + e.getMessage());
		}

	}
}
