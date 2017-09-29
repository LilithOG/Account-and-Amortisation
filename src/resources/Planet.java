package resources;

public class Planet {
	private int[] Versorgung;
	private int[] Anlagen;
	private int[] Verteidigung;
	private String name;

	public Planet(String name){
		this.setName(name);
		Versorgung = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
		Anlagen = new int[]{0,0,0,0,0,0,0,0};
		Verteidigung = new int[]{0,0,0,0,0,0,0,0,0};
	}

	public int[] getVerteidigung() {
		return Verteidigung;
	}

	public void setVerteidigung(int[] verteidigung) {
		Verteidigung = verteidigung;
	}

	public int[] getVersorgung() {
		return Versorgung;
	}

	public void setVersorgung(int[] versorgung) {
		for(int i = 0; i < Versorgung.length; i++)
			Versorgung[i] = versorgung[i];
	}

	public int[] getAnlagen() {
		return Anlagen;
	}

	public void setAnlagen(int[] anlagen) {
		for(int i = 0; i < Anlagen.length; i++)
			Anlagen[i] = anlagen[i];
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public double getProductionDayMetal(){
		return 24*getProductionMetal();
	}
	private double getProductionMetal(){
		return productionMetal(Versorgung[1]);
	}
	private double productionMetal(int lvl){
		return Math.ceil((double) 30*lvl*Math.pow(1.1, lvl));		
	}
	
	public double getProductionDayCrystal(){
		return 24*getProductionCrystal();
	}
	private double getProductionCrystal(){
		return productionCrystal(Versorgung[2]);
	}
	private double productionCrystal(int lvl){
		return Math.ceil((double) 20*lvl*Math.pow(1.1, lvl));
	}
	
	public double getProductionDayDeut(){
		return 24*getProductionDeut();
	}
	private double getProductionDeut(){
		return productionDeut(Versorgung[3]);
	}
	private double productionDeut(int lvl){
		double tempmod = 1.44 - 0.004 * Versorgung[11];
		return Math.ceil((double) 10*lvl*Math.pow(1.1, lvl)*tempmod);		
	}
	public double getConsumptionFKWDay(){
		return 24*consumptionFKW(Versorgung[8]);
	}
	private double consumptionFKW(int lvl){
		//System.out.printf(getName()+" FKW %d auf %d %%\n", Versorgung[8], Versorgung[9]);
		return Math.ceil((double) 10 * lvl * Math.pow(1.1, lvl) * (Versorgung[9]/100.0));
	}
}
