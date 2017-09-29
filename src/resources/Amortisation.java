package resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class Amortisation {
	private ResourceBundle strings;
	private ArrayList<String> amortisationsliste;
	private int plasmalvl;
	private double m, k, d;
	private ProductionAndCostsHelper ap;
	public double getM() {
		return m;
	}

	public double getK() {
		return k;
	}

	public double getD() {
		return d;
	}

	public Amortisation(Account acc, double m, double k, double d){
		ap = new ProductionAndCostsHelper(acc, m, k, d);
	    strings = ResourceBundle.getBundle("Strings",new Locale("de"));
		this.m = m;
		this.k = k;
		this.d = d;
		plasmalvl = acc.getTechnologienI()[4];
		amortisationsliste = new ArrayList<String>();
		calcAmortisation(acc);
	}
	
	private void calcAmortisation(Account acc){
		int lengthPrecalc = 10;
		int[] nextMet = new int[lengthPrecalc];
		int[] nextKris = new int[lengthPrecalc];
		double minDeutAmort = 0;
		nextMet[0] = 0;
		nextKris[0] = 0;
		int[] nextDeut = new int[lengthPrecalc*acc.getPlanets().size()];
		double[] amortMet = new double[lengthPrecalc];
		double[] amortKris = new double[lengthPrecalc];
		double[] amortDeut = new double[lengthPrecalc*acc.getPlanets().size()];
		int currMet = 0;
		int currKris = 0;
		int currPlasma = acc.getTechnologienI()[4];
		int[] currDeut = new int[acc.getPlanets().size()];
		for(int i = 0; i < acc.getPlanets().size(); i++)
		{
			nextMet[0] += acc.getPlanets().get(i).getVersorgung()[1];
			nextKris[0] += acc.getPlanets().get(i).getVersorgung()[2];
		}
		for(int i = 0; i < acc.getPlanets().size(); i++)
		{
			nextDeut[i] = acc.getPlanets().get(i).getVersorgung()[3]+1;
			currDeut[i] = nextDeut[i]-1;
			for(int j = 0; j < lengthPrecalc; j++)
			{
				nextDeut[j*acc.getPlanets().size()+i] = nextDeut[i]+j;
				double zpD = zusatzprodDeut(nextDeut[j*acc.getPlanets().size()+i], acc, i);
				//System.out.printf("zusatzprodu Deut stufe %d %f\n", nextDeut[i], zpD);
				double zcD = zusatzcostDeut(nextDeut[j*acc.getPlanets().size()+i]);
				//System.out.printf("zusatzcost Deut stufe %d %f\n", nextDeut[i], zcD);
				amortDeut[j*acc.getPlanets().size()+i] = zcD/zpD/24;
				//System.out.printf("Deut %d amortisiert sich auf dem Planeten "+acc.getPlanets().get(i).getName()+" nach %f Tagen!\n",nextDeut[j*acc.getPlanets().size()+i],amortDeut[i+(j*acc.getPlanets().size())]);
				if(j == lengthPrecalc-1 && i == 0){
					minDeutAmort = amortDeut[j*acc.getPlanets().size()+i];
				}
				if(j == lengthPrecalc-1){
					if(amortDeut[j*acc.getPlanets().size()+i] > minDeutAmort)
						amortDeut[j*acc.getPlanets().size()+i] = Double.MAX_VALUE;
				}
			}
		}
		nextMet[0] = (int) (Math.floor(nextMet[0])/acc.getPlanets().size())+1;
		nextKris[0] = (int) (Math.floor(nextKris[0])/acc.getPlanets().size())+1;
		currMet = nextMet[0]-1;
		currKris = nextKris[0]-1;
		
		for(int i = 0; i < lengthPrecalc; i++){
			nextMet[i] = nextMet[0]+i;
			double zpM = zusatzprodMet(nextMet[i], acc);
			//System.out.printf("zusatzprodu met stufe %d %f\n", nextMet[0], zpM);
			double zcM = zusatzcostMet(nextMet[i]);
			//System.out.printf("zusatzcost met stufe %d %f\n", nextMet[0], zcM);
			amortMet[i] = zcM/zpM/24;
			//System.out.printf("Met %d amortisiert sich nach %f Tagen!\n",nextMet[i],amortMet[i]);
			if(amortMet[i] > minDeutAmort)
				amortMet[i] = Double.MAX_VALUE;
			
			nextKris[i] = nextKris[0]+i;
			double zpK = zusatzprodKris(nextKris[i], acc);
			//System.out.printf("zusatzprodu Kris stufe %d %f\n", nextKris[0], zpK);
			double zcK = zusatzcostKris(nextKris[i]);
			//System.out.printf("zusatzcost Kris stufe %d %f\n", nextKris[0], zcK);
			amortKris[i] = zcK/zpK/24;
			//System.out.printf("Kris %d amortisiert sich nach %f Tagen!\n",nextKris[i],amortKris[i]);
			if(amortKris[i] > minDeutAmort)
				amortKris[i] = Double.MAX_VALUE;
		}
		boolean metdone = false;
		boolean krisdone = false;
		boolean deutdone = false;
		int numPlasma = 0;
		boolean plasmadone = false;
		int numAstro = 0;
		int currAstro = acc.getTechnologienI()[10];
		boolean astrodone = false;
		
		int i = 0;
		while(!(metdone&&krisdone&&deutdone))
		{
			double[] minAmortDeutA = findSmallestDouble(amortDeut);
			double[] minAmortMetA = findSmallestDouble(amortMet);
			double[] minAmortKrisA = findSmallestDouble(amortKris);
			if(!plasmadone && numPlasma < (2*lengthPrecalc)){
				//System.out.println("Checking Plasma");
				double zcP = zusatzcostPlasma(currPlasma+1);
				double zpP = zusatzprodPlasma(currPlasma, currMet, currKris, currDeut, acc);
				double amortPlasma = zcP/zpP/24;
				boolean a = false;
				boolean b = false;
				boolean c = false;
				if(!deutdone && amortPlasma < minAmortDeutA[1])
					a = true;
				if(!krisdone && amortPlasma < minAmortKrisA[1])
					b = true;
				if(!metdone && amortPlasma < minAmortMetA[1])
					c = true;
				if(a&&b&&c){
					String add = String.format(strings.getString("amortAfter")+" %d "+strings.getString("daysAkk")+": Plasma %d", (int) amortPlasma, currPlasma+1);
					amortisationsliste.add(add);
					currPlasma++;
					numPlasma++;
					System.out.println(add);
					continue;
				}
			}
			else
				plasmadone = true;
			

			if(!astrodone && numAstro < (2*lengthPrecalc)){
				//System.out.println("Checking astro");
				double zcA = zusatzcostAstro(currAstro+1, currMet, currKris, currDeut, acc);
				//System.out.printf("zusatzcostAstro: %f\n", zcA);
				double zpA = zusatzprodAstro(currPlasma, currAstro+2, numAstro, currMet, currKris, currDeut, acc);
				//System.out.printf("zusatzprodAstro: %f\n", zpA);
				double amortAstro = zcA/zpA/24; // zusatzprodastro = tagesprodu in mse durch anzahl planis
				boolean a = false;
				boolean b = false;
				boolean c = false;
				if(!deutdone && amortAstro < minAmortDeutA[1])
					a = true;
				if(!krisdone && amortAstro < minAmortKrisA[1])
					b = true;
				if(!metdone && amortAstro < minAmortMetA[1])
					c = true;
				if(a&&b&&c){
					if((currAstro+1)%2 == 1){
						String add = String.format(strings.getString("amortAfter")+" %d "+strings.getString("daysAkk")+": Astro %d", (int) amortAstro, currAstro+1);
						amortisationsliste.add(add);
						currAstro += 1;
						numAstro += 1;
						System.out.println(add);
					}
					else{
						String add = String.format(strings.getString("amortAfter")+" %d "+strings.getString("daysAkk")+": Astro %d+%d", (int) amortAstro, currAstro+1, currAstro+2);
						amortisationsliste.add(add);
						currAstro += 2;
						numAstro += 2;
						System.out.println(add);
					}
					continue;
				}
			}
			else
				astrodone = true;
			
			if(minAmortDeutA[1] >= Double.MAX_VALUE || (!deutdone && !(minAmortDeutA[1] < Double.MAX_VALUE) && !(i < lengthPrecalc*acc.getPlanets().size()-1))){
				//System.out.println("deutdone");
				deutdone = true;
			}
			if(minAmortKrisA[1] >= Double.MAX_VALUE || (!krisdone && !(minAmortKrisA[1] < Double.MAX_VALUE))){
				//System.out.println("krisdone");
				krisdone = true;
			}
			if(minAmortMetA[1] >= Double.MAX_VALUE || (!metdone && !(minAmortMetA[1] < Double.MAX_VALUE))){
				//System.out.println("metdone");
				metdone = true;
			}
			int planet = (int) Math.floor(((int) minAmortDeutA[0]%acc.getPlanets().size()));
			//if(!deutdone)
			//		System.out.printf("Found smallest deutamort for "+acc.getPlanets().get(planet).getName()+": %f\n",minAmortDeutA[1]);
			//System.console().readLine();
			if(deutdone == false && (minAmortDeutA[1] < minAmortMetA[1] && minAmortDeutA[1] < minAmortKrisA[1]) || (metdone&&krisdone)){
				//System.out.println("deut is smallest");
				if(minAmortDeutA[1] < Double.MAX_VALUE){
					String add = String.format(strings.getString("amortAfter")+" %d "+strings.getString("daysAkk")+": <font color=\"#04B486\">Deut %d</font> "+strings.getString("on")+" "+acc.getPlanets().get(planet).getName(), (int) amortDeut[(int) minAmortDeutA[0]], nextDeut[(int) minAmortDeutA[0]]);
					amortisationsliste.add(add);
					currDeut[planet]++;
					amortDeut[(int) minAmortDeutA[0]] = Double.MAX_VALUE;
					i++;
					//System.out.println(add);
				}
				else
					deutdone = true;
			}
			else{
				//System.out.println("deut is not smallest");
				if(metdone == false && minAmortMetA[1] < minAmortDeutA[1] && minAmortMetA[1] < minAmortKrisA[1]){
					//System.out.println("met is smallest");
					if(minAmortMetA[1] < Double.MAX_VALUE){
						// Ansparphase berechnen
						int numPlanets = (int) Math.floor((currAstro+1)/2)+1;
						System.out.printf("numPlanets=%d\n", numPlanets);
						int[] temps = new int[acc.getPlanets().size()];
						int[] metmines = new int[numPlanets];
						int[] crysmines = new int[numPlanets];
						int[] metminesNew = new int[numPlanets];
						int[] crysminesNew = new int[numPlanets];
						double daystobuild = 0;
						
						for(int x = 0; x < numPlanets; x++){
							if(x < acc.getPlanets().size())
								temps[x] = acc.getPlanets().get(x).getVersorgung()[11];
							metmines[x] = currMet;
							metminesNew[x] = currMet;
							crysmines[x] = currKris;
							crysminesNew[x] = currKris;
						}
						for(int y = 0; y < numPlanets; y++){
							metminesNew[y]++;
							daystobuild += ap.getAnsparphaseTage(metmines, crysmines, currDeut, temps, metminesNew, crysminesNew, currDeut);
							metmines[y]++;
						}
						String add = String.format(strings.getString("amortAfter")+" %d "+strings.getString("daysAkk")+": <font color=\"#424242\">Met&nbsp; %d</font> <u>"+strings.getString("onAllPlanets")+"</u>", (int) amortMet[(int) minAmortMetA[0]], nextMet[(int) minAmortMetA[0]]);
						amortisationsliste.add(add);
						add = String.format("Kummulative Ansparphase bis alle Metminen ausgebaut sind: %f Tage", daystobuild);
						amortisationsliste.add(add);
						currMet++;
						amortMet[(int) minAmortMetA[0]] = Double.MAX_VALUE;
						if((int) minAmortMetA[0] == amortMet.length-1)
							metdone = true;
						//System.out.println(add);
					}
					else
						metdone = true;
				}
				else{
					//System.out.println("met is not smallest");
					if(krisdone == false && minAmortKrisA[1] < minAmortDeutA[1] && minAmortKrisA[1] < minAmortMetA[1]){
						//System.out.println("kris is smallest");
						if(minAmortKrisA[1] < Double.MAX_VALUE){
							// Ansparphase berechnen
							int numPlanets = (int) Math.floor((currAstro+1)/2)+1;
							int[] temps = new int[acc.getPlanets().size()];
							int[] metmines = new int[numPlanets];
							int[] crysmines = new int[numPlanets];
							int[] metminesNew = new int[numPlanets];
							int[] crysminesNew = new int[numPlanets];
							double daystobuild = 0;
							
							for(int x = 0; x < numPlanets; x++){
								if(x < acc.getPlanets().size())
									temps[x] = acc.getPlanets().get(x).getVersorgung()[11];
								metmines[x] = currMet;
								metminesNew[x] = currMet;
								crysmines[x] = currKris;
								crysminesNew[x] = currKris;
							}
							for(int y = 0; y < numPlanets; y++){
								crysminesNew[y]++;
								daystobuild += ap.getAnsparphaseTage(metmines, crysmines, currDeut, temps, metminesNew, crysminesNew, currDeut);
								crysmines[y]++;
							}
							
							// Add entry to amortisation list
							String add = String.format(strings.getString("amortAfter")+" %d "+strings.getString("daysAkk")+": <font color=\"#2E9AFE\">Kris %d</font> <u>"+strings.getString("onAllPlanets")+"</u>", (int) amortKris[(int) minAmortKrisA[0]], nextKris[(int) minAmortKrisA[0]]);
							amortisationsliste.add(add);
							add = String.format("Kummulative Ansparphase bis alle Krisminen ausgebaut sind: %f Tage", daystobuild);
							amortisationsliste.add(add);
							currKris++;
							amortKris[(int) minAmortKrisA[0]] = Double.MAX_VALUE;
							if((int) minAmortKrisA[0] == amortKris.length-1)
								krisdone = true;
							//System.out.println(add);
						}
						else
							krisdone = true;
					}
					else{
						//System.out.println("kris is not smallest");
						//System.out.println("NOONE IS SMALLEST!");
					}
				}
			}
			
		}
		
		//System.out.println("Reihenfolge der Amortisationen:");
		//for(String x : amortisationsliste)
			//System.out.println(x);
		return;
	}
	
	public ArrayList<String> getAmortisationsliste(){
		return amortisationsliste;
	}
	
	private double[] findSmallestDouble(double[] amortDeut){
		double[] ret = new double[2];
		ret[1] = amortDeut[0];
		for(int i = 1; i < amortDeut.length; i++){
			if(amortDeut[i] < ret[1]){
				ret[1] = amortDeut[i];
				ret[0] = i;
			}
		}
		return ret;
	}
	
	private double zusatzprodMet(int lvl, Account acc){
		return ap.getProdMet(lvl)-ap.getProdMet(lvl-1);
		/*
		double base = (30*lvl*Math.pow(1.1, lvl)-(30*(lvl-1)*Math.pow(1.1, lvl-1)));
		double mod = 1+(acc.getTechnologienI()[4]/100.0);
		if(acc.isGeologe())
			mod += 0.1;
		return base*mod*acc.getUnispeed();
		*/
	}
	private double zusatzprodKris(int lvl, Account acc){
		return (m/k)*(ap.getProdKris(lvl)-ap.getProdKris(lvl-1));
		/*
		double base = (20*lvl*Math.pow(1.1, lvl)-(20*(lvl-1)*Math.pow(1.1, lvl-1)));
		double mod = 1+(acc.getTechnologienI()[4]*0.66/100.0);
		if(acc.isGeologe())
			mod += 0.1;
		return (m/k)*base*mod*acc.getUnispeed();*/
	}
	private double zusatzprodDeut(int lvl, Account acc, int plani){
		return (m/d)*(ap.getProdDeut(lvl, acc.getPlanets().get(plani).getVersorgung()[11])-ap.getProdDeut(lvl-1, acc.getPlanets().get(plani).getVersorgung()[11]));
		/*
		double tempmod = 1.44 - 0.004*acc.getPlanets().get(plani).getVersorgung()[11];
		double base = Math.ceil((double) 10*lvl*Math.pow(1.1, lvl)*tempmod)- Math.ceil((double) 10*(lvl-1)*Math.pow(1.1, lvl-1)*tempmod);
		double mod = 1+(acc.getTechnologienI()[4]*0.33/100.0);
		if(acc.isGeologe())
			mod += 0.1;
		return (m/d)*base*mod*acc.getUnispeed();
		*/
	}
	private double zusatzcostMet(int lvl){
		return (40+(m/k)*10) * Math.pow(1.5, lvl);
	}
	private double zusatzcostKris(int lvl){
		return (30+(m/k)*15) * Math.pow(1.6, lvl);
	}
	private double zusatzcostDeut(int lvl){
		return (150+(m/k)*50) * Math.pow(1.5, lvl);
	}
	private double zusatzcostPlasma(int lvl){
		return (1000+(m/k)*2000+(m/d)*500)*Math.pow(2, lvl);
	}
	private double zusatzprodPlasma(int lvl, int lvlm, int lvlk, int[] lvld, Account acc){
		int np = acc.getPlanets().size();
		double mod1m = 1+(lvl/100.0);
		double mod2m = 1+((lvl+1)/100.0);
		double mod1k = 1+(lvl*0.66/100.0);
		double mod2k = 1+((lvl+1)*0.66/100.0);
		double mod1d = 1+(lvl*0.33/100.0);
		double mod2d = 1+((lvl+1)*0.33/100.0);
		if(acc.isGeologe()){
			mod1m += 0.1;
			mod2m += 0.1;
			mod1k += 0.1;
			mod2k += 0.1;
			mod1d += 0.1;
			mod2d += 0.1;
		}
		double pm = np*acc.getUnispeed()*( mod2m*30*lvlm*Math.pow(1.1, lvlm) - mod1m*30*lvlm*Math.pow(1.1, lvlm) );
		double pk = (m/k)*np*acc.getUnispeed()*( mod2k*20*lvlk*Math.pow(1.1, lvlk) - mod1k*20*lvlk*Math.pow(1.1, lvlk) );
		double pd = 0;
		for(int i = 0; i < acc.getPlanets().size(); i++)
		{
			double tempmod = 1.44 - 0.004*acc.getPlanets().get(i).getVersorgung()[11];
			double base = Math.ceil((double) mod2d*10*lvld[i]*Math.pow(1.1, lvld[i])*tempmod) - Math.ceil((double) mod1d*10*(lvld[i])*Math.pow(1.1, lvld[i])*tempmod);
			pd += (m/d)*acc.getUnispeed()*base;
		}
		return pm+pk+pd;
	}
	
	// gets called with currentAstro+2
	private double zusatzprodAstro(int plasma, int astro, int numastro, int met, int kris, int[] deut, Account acc){
		int np = (int) Math.floor((astro+1)/2); // #planets without new astro
		double modPm = 1+(plasma/100.0);
		double modPk = 1+(plasma*0.66/100.0);
		double modPd = 1+(plasma*0.33/100.0);
		if(acc.isGeologe()){
			modPm += 0.1;
			modPk += 0.1;
			modPd += 0.1;
		}
		double pm = np*acc.getUnispeed()*( modPm*30*met*Math.pow(1.1, met));
		//System.out.printf("prodAstro %d: pm=%f\n", astro, pm);
		double pk = (m/k)*np*acc.getUnispeed()*( modPk*20*kris*Math.pow(1.1, kris));
		//System.out.printf("prodAstro %d: pk=%f\n", astro, pk);
		double pd = 0;
		for(int i = 0; i < acc.getPlanets().size(); i++)
		{
			double tempmod = 1.44 - 0.004*acc.getPlanets().get(i).getVersorgung()[11];
			double base = Math.ceil((double) modPd*10*deut[i]*Math.pow(1.1, deut[i])*tempmod);
			pd += (m/d)*acc.getUnispeed()*base;
		}
		pd = pd*np/acc.getPlanets().size();
		//if(astro < 26)
			//System.out.printf("prodAstro %d with np=%d, m=%d, k=%d, modpm=%f, modpk=%f, modpd=%f: pm=%f pk=%f pd=%f\n", astro, np, met, kris, modPm, modPk, modPd, pm, pk, pd);
		return (pm+pk+pd)/np;
	}
	
	// gets called with lvl = currAstro+1
	private double zusatzcostAstro(int lvl, int met, int kris, int[] deut, Account acc){
		int avdeut = 0;
		for(int i = 0; i < deut.length; i++)
			avdeut += deut[i];
		avdeut = (int) Math.floor(1.0*avdeut/deut.length);
		double costAstro = (4000 + (m/k)*8000 + (m/d)*4000) * Math.floor(Math.pow(1.75, lvl-1));
		if(lvl%2 == 0)
			costAstro += (4000 + (m/k)*8000 + (m/d)*4000) * Math.floor(Math.pow(1.75, lvl));
		if(lvl == 1)
			costAstro += zusatzcostAstroPrerequisites(acc);
		double costMinen = 120*Math.pow(1.5, met)-120 + (m/k)*30*Math.pow(1.5, met)-30  +  80*Math.pow(1.6, kris)-80 + (m/k)*(40*Math.pow(1.6, kris)-40)  +  450*Math.pow(1.5, avdeut)-450 + (m/k)*(150*Math.pow(1.5, avdeut)-150);
		double costGebaeude = acc.getCostNeuerPlanet(m, k, d);
		return costAstro+costMinen+costGebaeude;
	}
	private double zusatzcostAstroPrerequisites(Account acc){
		double etech1 = 0;
		if(acc.getTechnologienI()[0] == 0)
			etech1 += (m/k)*800+(m/d)*400;
		double labor3 = 1400+(m/k)*2800+(m/d)*1400;
		int maxlab = 0;
		int[] labore = acc.getLaboreAllePlaneten();
		for(int i = 0; i < labore.length; i++)
		{
			if(labore[i] > maxlab)
				maxlab = labore[i];
		}
		switch(maxlab){
			case 0: break;
			case 1: labor3 -= 200+(m/k)*400+(m/d)*200;break;
			case 2: labor3 -= 600+(m/k)*1200+(m/d)*600;break;
			case 3: labor3 = 0;break;
			default: labor3 = 0;break;
		}
			
		double impuls3 = 14000+(m/k)*28000+(m/d)*4200;
		switch(acc.getTechnologienI()[6]){
			case 0: break;
			case 1: impuls3 -= 2000+(m/k)*4000+(m/d)*600;break;
			case 2: impuls3 -= 6000+(m/k)*12000+(m/d)*1800;break;
			case 3: impuls3 = 0;break;
			default: impuls3 = 0;break;
		}
		double spiotech4 = 0;
		
		return etech1+labor3+impuls3+spiotech4;
	}
}
