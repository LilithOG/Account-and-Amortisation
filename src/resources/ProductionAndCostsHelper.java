package resources;

public class ProductionAndCostsHelper {
	Account acc;
	double m,k,d;
	public ProductionAndCostsHelper(Account acc, double m, double k, double d){
		this.acc = acc;
		this.m = m;
		this.k = k;
		this.d = d;
	}
	public double getAnsparphaseTage(int[] mc, int[] kc, int[] dc, int[] temps, int[] mn, int[] kn, int[] dn){
		double prodDaily = 0;
		double costs = 0;
		costs = getCosts(mc,kc,dc,mn,kn,dn);
		
		double prodDailyMet = getProdDailyMet(mc);
		double prodDailyKris = getProdDailyKris(kc);
		double prodDailyDeut = getProdDailyDeut(dc, temps);
		System.out.printf("prodDailyMet: %f\n",prodDailyMet);
		System.out.printf("prodDailyKris: %f\n",prodDailyKris);
		System.out.printf("prodDailyDeut: %f\n",prodDailyDeut);
		if(dc.length < mc.length)
			prodDailyDeut = prodDailyDeut*mc.length/dc.length;
		
		prodDaily = prodDailyMet+(m/k)*prodDailyKris+(m/d)*prodDailyDeut;
		//System.out.printf("DailyProd: %f\n", prodDaily);
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
		double base = 30+30*lvl*Math.pow(1.1, lvl);
		double mod = 1+(acc.getTechnologienI()[4]/100.0);
		if(acc.isGeologe())
			mod += 0.1;
		return base*mod*acc.getUnispeed();
	}
	public double getProdKris(int lvl){
		double base = 15+20*lvl*Math.pow(1.1, lvl);
		double mod = 1+(acc.getTechnologienI()[4]*0.66/100.0);
		if(acc.isGeologe())
			mod += 0.1;
		return base*mod*acc.getUnispeed();
	}
	public double getProdDeut(int lvl, int temp){
		double tempmod = 1.44 - 0.004*temp;
		double base = Math.ceil((double) 10*lvl*Math.pow(1.1, lvl)*tempmod);
		double mod = 1+(acc.getTechnologienI()[4]*0.33/100.0);
		if(acc.isGeologe())
			mod += 0.1;
		return base*mod*acc.getUnispeed();
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
		//if(j==41)
		//	System.out.printf("metalminecost i=%d j=%d: %f\n", i,j,metalminecost);
		return metalminecost;
		
	}
	private double getCrystalmineCost(int i, int j) {
		double crystalminecost = (80+(m/k)*40)*(Math.pow(1.6,j)-Math.pow(1.6,i));
		//if(j==34)
		//	System.out.printf("crystalminecost i=%d j=%d: %f\n", i,j,crystalminecost);
		return crystalminecost;
		
	}
	private double getDeuteriumsynthCost(int i, int j) {
		double deuteriumsynthcost = (450+(m/k)*150)*(Math.pow(1.5,j)-Math.pow(1.5,i));
//		System.out.printf("deuteriumsynthcost i=%d j=%d: %f\n", i,j,deuteriumsynthcost);
		return deuteriumsynthcost;
		
	}
}
