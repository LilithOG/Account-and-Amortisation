package resources;

public class ProductionAndCostsHelper {
	private static final boolean debug_prints = false;
	Account acc;
	double m,k,d;
	double[] pow11;
	public ProductionAndCostsHelper(Account acc, double m, double k, double d){
		this.acc = acc;
		this.m = m;
		this.k = k;
		this.d = d;

		pow11 = new double[60];
		pow11[0] = 1;
		for(int i = 1; i < 60; i++)
			pow11[i] = pow11[i-1]*1.1;
	}
	public double getAnsparphaseTage(int[] mc, int[] kc, int[] dc, int[] temps, int[] mn, int[] kn, int[] dn){
		double prodDaily = 0;
		double costs = 0;
		costs = getCosts(mc,kc,dc,mn,kn,dn);
		
		double prodDailyMet = getProdDailyMet(mc);
		double prodDailyKris = getProdDailyKris(kc);
		double prodDailyDeut = getProdDailyDeut(dc, temps);
		if(dc.length < mc.length)
			prodDailyDeut = prodDailyDeut*mc.length/dc.length;
		
		prodDaily = prodDailyMet+(m/k)*prodDailyKris+(m/d)*prodDailyDeut;
		if(prodDaily == 0)
			return 0.0;
		return costs/(prodDaily+prodDailyDeut);
	}
	
	private double getProdDailyMet(int[] mc){
		double prodMetDaily = 0;
		for(int i = 0; i < mc.length; i++)
			prodMetDaily += getProdMet(mc[i]);
		return 24.0*prodMetDaily;
	}
	private double getProdDailyKris(int[] kc){
		double prodKrisDaily = 0;
		for(int i = 0; i < kc.length; i++)
			prodKrisDaily += getProdKris(kc[i]);
		return 24.0*prodKrisDaily;
	}
	private double getProdDailyDeut(int[] dc, int[] temps){
		double prodDeutDaily = 0;
		for(int i = 0; i < dc.length; i++)
			prodDeutDaily += getProdDeut(dc[i], temps[i]);
		return 24.0*prodDeutDaily;
	}
	
	public double getProdMet(int lvl){
		if(debug_prints) System.out.println("getProdMet:");
		//double base = 30*lvl*Math.pow(1.1, lvl)*acc.getUnispeed();
		double base = 30*lvl*pow11[lvl]*acc.getUnispeed();
		double prodPlasma = Math.round((acc.getTechnologienI()[4]/100.0)*base);
		double prodGeologist = 0;
		if(acc.isGeologe())
			prodGeologist += Math.round(0.1*base);
		if(debug_prints) System.out.printf("base: %d plasma: %f geologe: %f\n",Math.round(base),prodPlasma,prodGeologist);
		return 30*acc.getUnispeed()+Math.round(base)+prodPlasma+prodGeologist;
	}
	public double getProdKris(int lvl){
		if(debug_prints) System.out.println("getProdKris:");
		//double base = 20*lvl*Math.pow(1.1, lvl)*acc.getUnispeed();
		double base = 20*lvl*pow11[lvl]*acc.getUnispeed();
		double prodPlasma = Math.round((acc.getTechnologienI()[4]*0.66)*base/100.0);
		double prodGeologist = 0;
		if(acc.isGeologe())
			prodGeologist += Math.round(0.1*base);
		if(debug_prints) System.out.printf("base: %d plasma: %f geologe: %f\n",Math.round(base),prodPlasma,prodGeologist);
		return 15*acc.getUnispeed()+Math.round(base)+prodPlasma+prodGeologist;
	}
	public double getProdDeut(int lvl, int temp){
		if(debug_prints) System.out.println("getProdDeut:");
		double tempmod = 1.44 - 0.004*temp;
		//double base = 10*lvl*Math.pow(1.1, lvl)*tempmod*acc.getUnispeed();
		double base = 10*lvl*pow11[lvl]*tempmod*acc.getUnispeed();
		double prodPlasma = Math.round((acc.getTechnologienI()[4]*0.33/100.0)*base);
		double prodGeologist = 0;
		if(acc.isGeologe())
			prodGeologist += Math.round(0.1*base);
		if(debug_prints) System.out.printf("base: %d plasma: %f geologe: %f\n",Math.round(base),prodPlasma,prodGeologist);
		return Math.round(base)+prodPlasma+prodGeologist;
	}
	
	private double getCosts(int[] mc, int[] kc, int[] dc, int[] mn, int[] kn, int[] dn){
		double totalCosts = 0;
		double deuteriummineCosts = 0;
		for(int i = 0; i < mc.length; i++){
			if(mc[i] != mn[i])
				totalCosts += getMetalmineCost(mc[i], mn[i]);
			if(kc[i] != kn[i])
				totalCosts += getCrystalmineCost(kc[i], kn[i]);
			if(i < dc.length && dc[i] != dn[i])
				deuteriummineCosts += getDeuteriumsynthCost(dc[i], dn[i]);
		}
		if(mc.length > dc.length)
			deuteriummineCosts = deuteriummineCosts/dc.length*mc.length;
		return totalCosts+deuteriummineCosts;
	}
	private double getMetalmineCost(int i, int j) {
		double metalminecost = (120+(m/k)*30)*(Math.pow(1.5,j)-Math.pow(1.5,i));
		return metalminecost;
		
	}
	private double getCrystalmineCost(int i, int j) {
		double crystalminecost = (80+(m/k)*40)*(Math.pow(1.6,j)-Math.pow(1.6,i));
		return crystalminecost;
		
	}
	private double getDeuteriumsynthCost(int i, int j) {
		double deuteriumsynthcost = (450+(m/k)*150)*(Math.pow(1.5,j)-Math.pow(1.5,i));
		return deuteriumsynthcost;
		
	}
}
