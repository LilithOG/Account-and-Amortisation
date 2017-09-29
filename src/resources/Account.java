package resources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import userinterface.Main;

public class Account {
	private ArrayList<Planet> planets;
    private Object[] VersorgungNamen;// = {"Name",strings.getString("Metalmine"),strings.getString("Crystalmine"),strings.getString("Deutsynth"),strings.getString("Metalstorage"),strings.getString("Crystalstorage"),strings.getString("Deutstorage"),strings.getString("SolarPP"),strings.getString("FusionPP"),"FKW %","Sats","Maxtemp"};
    private final Object[] AnlagenNamen;// = {"Name","Robo","Werft","Flab","Raksilo","Nani","Terra","Raumdock"};
    private final Object[] VerteidigungNamen;// = {"Name","Raks","LL","SL","Gauß","Ionen","Plasma","IRak","ARak"};
    private final Object[] TechnologienNamen;// = {strings.getString("ETech"),strings.getString("LTech"),strings.getString("ITech"),strings.getString("HyperTech"),strings.getString("PTech"),strings.getString("DrCombustion"),strings.getString("DrImpulse"),strings.getString("DrHyper"),strings.getString("Espionage"),strings.getString("CTech"),strings.getString("Astro"),strings.getString("IGRN"),strings.getString("Gravi"),strings.getString("WeaponsTech"),strings.getString("ShieldTech"),strings.getString("ArmourTech")}; 
	private final Object[] NeuerPlanetNamen;// = {strings.getString("Metalstorage"),strings.getString("Crystalstorage"),strings.getString("Deutstorage"),strings.getString("SolarPP"),strings.getString("FusionPP"),"Robo","Werft","Flab","Raksilo","Nani","Terra","Raumdock","Raks","LL","SL","Gauß","Ionen","Plasma","IRak","ARak"};
    private final int unispeed = 4;
	public int getUnispeed(){return unispeed;}
	private int[] Technologien;
	private int[] NeuerPlanet;
	private boolean geologe;
	private double[] pow2, pow15, pow18;
	ResourceBundle strings;
	
    public Account(){
        strings = ResourceBundle.getBundle("Strings",new Locale("de"));
        VersorgungNamen = new Object[] {strings.getString("Name"),strings.getString("Metalmine"),strings.getString("Crystalmine"),strings.getString("Deutsynth"),
        		strings.getString("Metalstorage"),strings.getString("Crystalstorage"),strings.getString("Deutstorage"),strings.getString("SolarPP"),strings.getString("FusionPP"),
        		strings.getString("FusionPP")+" %",strings.getString("Solarsats"),strings.getString("Maxtemperature")};
        AnlagenNamen = new Object[] {strings.getString("Name"),strings.getString("Robotics"),strings.getString("Shipyard"),strings.getString("Researchlab"),"Raksilo",strings.getString("Nanite"),
        		"Terra","Raumdock"};
        VerteidigungNamen = new Object[] {strings.getString("Name"),strings.getString("Rocketlauncher"),strings.getString("LightLaser"),strings.getString("HeavyLaser"),
        		strings.getString("Gausscannon"),strings.getString("Ioncannon"),strings.getString("Plasmaturret"),strings.getString("IPM"),strings.getString("ABM")};
        TechnologienNamen = new Object[] {strings.getString("ETech"),strings.getString("LTech"),strings.getString("ITech"),strings.getString("HyperTech"),strings.getString("PTech"),
        		strings.getString("DrCombustion"),strings.getString("DrImpulse"),strings.getString("DrHyper"),strings.getString("Espionage"),strings.getString("CTech"),strings.getString("Astro"),
        		strings.getString("IGRN"),strings.getString("Gravi"),strings.getString("WeaponsTech"),strings.getString("ShieldTech"),strings.getString("ArmourTech")};
        NeuerPlanetNamen = new Object[] {strings.getString("Metalstorage"),strings.getString("Crystalstorage"),strings.getString("Deutstorage"),strings.getString("SolarPP"),
        		strings.getString("FusionPP"),strings.getString("Robotics"),strings.getString("Shipyard"),strings.getString("Researchlab"),"Raksilo",strings.getString("Nanite"),
        		"Terra","Raumdock",strings.getString("Rocketlauncher"),strings.getString("LightLaser"),strings.getString("HeavyLaser"),strings.getString("Gausscannon"),
        		"Ionen",strings.getString("Plasmaturret"),strings.getString("IPM"),strings.getString("ABM")};
    	planets = new ArrayList<Planet>();
    	setGeologe(false);
		setTechnologien(new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
		setNeuerPlanet(new int[20]);
		pow2 = new double[30];
		pow2[0] = 1;
		for(int i = 1; i < 30; i++)
			pow2[i] = pow2[i-1]*2;
		pow15 = new double[30];
		pow15[0] = 1.5;
		for(int i = 1; i < 30; i++)
			pow15[i] = pow15[i-1]*1.5;
		pow18 = new double[30];
		pow18[0] = 1.8;
		for(int i = 1; i < 30; i++)
			pow18[i] = pow18[i-1]*1.8;
			
    	//planets.add(new Planet("Hauptplanet"));
    }
	public void addPlanet(){
		//System.out.println("function addPlanet");
		planets.add(new Planet(""));
	}
	
	public ArrayList<Planet> getPlanets(){
		return planets;
	}
	public void removePlanet(){
		planets.remove(planets.size()-1);
	}
	
	public String[][] getVersorgungAllePlaneten(){
		String[][] ret = new String[VersorgungNamen.length][planets.size()+1];
		//System.out.printf("getVersorgung für %d Planeten\n",planets.size());
		for(int i = 0; i < planets.size()+1; i++){
			//System.out.printf("getVAP planet %d\n", i);
			for(int j = 0; j < VersorgungNamen.length; j++){
				if(i == 0){
					ret[j][0] = (String) VersorgungNamen[j].toString();
				}
				else{
					if(j != 0){
						ret[j][i] = String.valueOf(planets.get(i-1).getVersorgung()[j]);
						//System.out.printf("getVAP: i=%d j=%d %s\n", i,j,ret[j][i].toString());
					}
					else{
						ret[j][i] = planets.get(i-1).getName();
						//System.out.printf("getVAP: i=%d j=%d %s\n", i,j,ret[j][i].toString());
					}
				}
			}
		}
		return ret;
	}
	
	public Object[][] getAnlagenAllePlaneten(){
		Object[][] ret = new Object[8][planets.size()+1];
		for(int i = 0; i < planets.size()+1; i++){
			//System.out.printf("getAAP planet %d\n", i);
			for(int j = 0; j < 8; j++){
				//System.out.printf("i=%d j=%d\n",i,j);
				if(i == 0){
					ret[j][0] = AnlagenNamen[j].toString();
				}
				else{
					if(j != 0)
						ret[j][i] = String.valueOf(planets.get(i-1).getAnlagen()[j]);
					else
						ret[j][i] = planets.get(i-1).getName();
				}
			}
		}
		return ret;
	}
	
	public int[] getLaboreAllePlaneten(){
		int[] ret = new int[planets.size()];
		for(int i = 0; i < planets.size(); i++){
			ret[i] = planets.get(i).getAnlagen()[3];
		}
		return ret;
	}
	
	public Object[][] getVerteidigungAllePlaneten(){
		Object[][] ret = new Object[9][planets.size()+1];
		for(int i = 0; i < planets.size()+1; i++){
			//System.out.printf("getDAP planet %d\n", i);
			for(int j = 0; j < 9; j++){
				//System.out.printf("i=%d j=%d\n",i,j);
				if(i == 0){
					ret[j][0] = VerteidigungNamen[j].toString();
				}
				else{
					if(j != 0)
						ret[j][i] = String.valueOf(planets.get(i-1).getVerteidigung()[j]);
					else
						ret[j][i] = planets.get(i-1).getName();
				}
			}
		}
		return ret;
	}
	
	public Object[][] getTechnologien(){
		Object[][] ret = new Object[16][2];
		for(int i = 0; i < 16; i++){
			ret[i][0] = (String) TechnologienNamen[i].toString();
			ret[i][1] = String.valueOf(getTechnologienI()[i]);
		}
		return ret;
	}	
	
	public String getSProductionDay(){
		int[] iProdu = getProductionDay();
		StringBuilder sb = new StringBuilder();
		if(iProdu[0] > 1000){
			iProdu[0] /= 1000;
			sb.append(iProdu[0]);
			sb.append("k / ");
		}
		else{
			sb.append(iProdu[0]);
			sb.append(" / ");
		}
		if(iProdu[1] > 1000){
			iProdu[1] /= 1000;
			sb.append(iProdu[1]);
			sb.append("k / ");
		}
		else{
			sb.append(iProdu[1]);
			sb.append(" / ");
		}
		if(iProdu[2] > 1000){
			iProdu[2] /= 1000;
			sb.append(iProdu[2]);
			sb.append("k");
		}
		else{
			sb.append(iProdu[2]);
			sb.append("");
		}
		return sb.toString();
	}
	
	public int[] getProductionDay(){
		int[] ret = new int[3];
		double geologe = 1;
		//System.out.printf("Geologe: ");
		//if(isGeologe())
		//	System.out.printf("true\n");
		//else
		//	System.out.printf("false\n");
		if(isGeologe())
			geologe += (double) 0.1;
		double plasmalvl = (double) Technologien[4];
		for(int i = 0; i < planets.size(); i++)
		{
			ret[0] += unispeed * (24*30 + (plasmalvl*0.01 * planets.get(i).getProductionDayMetal()) + (geologe * planets.get(i).getProductionDayMetal()));
			ret[1] += unispeed * (24*15 + (plasmalvl*0.0066 * planets.get(i).getProductionDayCrystal()) + (geologe * planets.get(i).getProductionDayCrystal()));
			ret[2] += unispeed * ((plasmalvl*0.0033 * planets.get(i).getProductionDayDeut()) + (geologe * planets.get(i).getProductionDayDeut()) - planets.get(i).getConsumptionFKWDay());
		}
		return ret;
	}
	
	public int[] getTechnologienI() {
		return Technologien;
	}

	public void setTechnologien(int[] technologien) {
		Technologien = technologien;
	}
	
	public Object[][] getNeuerPlanet(){
		Object[][] ret = new Object[20][2];
		for(int i = 0; i < 20; i++){
			ret[i][0] = (String) NeuerPlanetNamen[i].toString();
			ret[i][1] = String.valueOf(getNeuerPlanetI()[i]);
		}
		return ret;
	}
	
	public double getCostNeuerPlanet(double m, double k, double d){
		//strings.getString("Metalstorage"),strings.getString("Crystalstorage"),strings.getString("Deutstorage"),strings.getString("SolarPP"),strings.getString("FusionPP"),"Robo","Werft","Flab","Raksilo","Nani","Terra","Raumdock","Raks","LL","SL","Gauß","Ionen","Plasma","IRak","ARak"
		double cm = 0;
		double ck = 0;
		double cd = 0;
		int[] npi = getNeuerPlanetI();
		//           strings.getString("Metalstorage"),              strings.getString("Crystalstorage"),            strings.getString("Deutstorage"),                 strings.getString("SolarPP"),                        strings.getString("FusionPP"),
		cm = 1000*pow2[npi[0]]-1000 + 1000*pow2[npi[1]]-1000 + 1000*pow2[npi[2]]-1000 + 150*(pow15[npi[3]]-pow15[0]) + 1125*(pow18[npi[4]]-pow18[0]);
		//          strings.getString("Crystalstorage"),              strings.getString("Deutstorage"),                  strings.getString("SolarPP"),                      strings.getString("FusionPP"),
		ck = 500*pow2[npi[1]]-500   + 1000*pow2[npi[2]]-1000  + 60*(pow15[npi[3]]-pow15[0]) + 450*(pow18[npi[4]]-pow18[0]);
		cd = 225*(pow18[npi[4]]-pow18[0]);
		
		//             "Robo",               "Werft",             "Flab",                    "Raksilo",               "Nani",
		cm += 400*pow2[npi[5]]-400 + 400*pow2[npi[6]]-400 + 200*pow2[npi[7]]-200 + 20000*pow2[npi[8]]-20000 + 1000000*pow2[npi[9]]-1000000;
		//             "Robo",               "Werft",              "Flab",                   "Raksilo",              "Nani",
		ck += 120*pow2[npi[5]]-120 + 200*pow2[npi[6]]-200 + 400*pow2[npi[7]]-400 + 20000*pow2[npi[8]]-20000 + 500000*pow2[npi[9]]-500000;
		cd += 200*pow2[npi[5]]-200 + 100*pow2[npi[6]]-100 + 200*pow2[npi[7]]-200 + 1000*pow2[npi[8]]-1000   + 100000*pow2[npi[9]]-100000;
		
		//"Terra",
		ck += 50000*pow2[npi[10]]-50000;
		cd += 100000*pow2[npi[10]]-100000;
		//"Raumdock", fehlt noch
		
		//"Raks","LL","SL","Gauß","Ionen","Plasma",
		cm += 2000*npi[12] + 1500*npi[13] + 6000*npi[14] + 20000*npi[15] + 2000*npi[16] + 50000*npi[17];
		ck += 500*npi[13]  + 2000*npi[14] + 15000*npi[15] + 6000*npi[16] + 50000*npi[17];
		cd += 2000*npi[15] + 30000*npi[17];
		
		//"IRak","ARak" fehlen noch
		return cm+(m/k)*ck+(m/d)*cd;
	}
	public int[] getNeuerPlanetI() {
		return NeuerPlanet;
	}

	public void setNeuerPlanet(int[] NeuerPlanet) {
		this.NeuerPlanet = NeuerPlanet;
	}
	
	public void saveToFile(){
		//System.out.println("saving to file");
		String filename = "account.txt";
		File fOut = new File(filename);		
		BufferedWriter writer;
		try{
			if(!fOut.exists())
				fOut.createNewFile();
			writer = new BufferedWriter(new FileWriter(fOut, false));
			for(int i = 0; i < planets.size(); i++)
			{
				writer.write(planets.get(i).getName());
				writer.write(" Versorgung: ");
				for(int j = 0; j < VersorgungNamen.length; j++){
					writer.write(String.valueOf(planets.get(i).getVersorgung()[j]));
					writer.write(".");
				}
				writer.write(" Anlagen: ");
				for(int j = 0; j < 8; j++){
					writer.write(String.valueOf(planets.get(i).getAnlagen()[j]));
					writer.write(".");
				}
				writer.write(" Verteidigung: ");
				for(int j = 0; j < 9; j++){
					writer.write(String.valueOf(planets.get(i).getVerteidigung()[j]));
					writer.write(".");
				}
				writer.write(" Forschung: ");
				for(int j = 0; j < 16; j++){
					writer.write(String.valueOf(getTechnologienI()[j]));
					writer.write(".");
				}
				writer.write("\r\n");
			}
			writer.write("Geologe: ");
			if(isGeologe())
				writer.write("true");
			else
				writer.write("false");
			writer.write("\r\n");
			writer.write("Neuer Planet: ");
			for(int i = 0; i < NeuerPlanetNamen.length; i++)
			{
				writer.write(String.valueOf(NeuerPlanet[i])+".");
				
			}
			writer.write("\r\n");
			writer.close();
		}
		catch(IOException e){}	
	}
	
	public void loadFromFile(){
		//System.out.println("loading from file");
		String filename = "account.txt";			
		File fIn = new File(filename);
		BufferedReader reader = null;
		try{
			if(!fIn.exists())
				return;
			reader = new BufferedReader(new FileReader(fIn));
			String planet, versorgung, anlagen, verteidigung, forschung;
			while((planet = reader.readLine())!=null && planet.length() != 0 && !planet.substring(0, 3).equalsIgnoreCase("Geologe:".substring(0, 3)))
			{
				versorgung = planet.substring(planet.indexOf("Versorgung: ")+12, planet.indexOf(" Anlagen: "));
				//System.out.printf("lFF. Versorgung: %s\n",versorgung);
				planets.add(new Planet(planet.substring(0, planet.indexOf(" Versorgung"))));
				String[] versorgungS = versorgung.split("\\.");
				//System.out.println("versorgungS:");
				//for(int x = 0; x < versorgungS.length; x++)
					//System.out.println(versorgungS[x]);
				
				int[] versorgungI = new int[versorgungS.length];
				//System.out.printf("versorgungI: ");
				for(int i = 0; i < versorgungI.length; i++){
					versorgungI[i] = Integer.valueOf(versorgungS[i]);
					//System.out.printf("%d ", versorgungI[i]);
				}
				//System.out.println();
				anlagen =  planet.substring(planet.indexOf("Anlagen: ")+9, planet.indexOf(" Verteidigung: "));
				//System.out.printf("lFF. Anlagen: %s\n",anlagen);
				String[] anlagenS = anlagen.split("\\.");
				//System.out.println("anlagenS:");
				//for(int x = 0; x < anlagenS.length; x++)
				//	System.out.println(anlagenS[x]);
				
				int[] anlagenI = new int[anlagenS.length];
				//System.out.printf("anlagenI: ");
				for(int i = 0; i < anlagenI.length; i++){
					anlagenI[i] = Integer.valueOf(anlagenS[i]);
					//System.out.printf("%d ", anlagenI[i]);
				}
				//System.out.println();
				
				verteidigung = planet.substring(planet.indexOf("Verteidigung: ")+14, planet.indexOf(" Forschung: "));
				//System.out.printf("lFF. Verteidigung: %s\n\n",verteidigung);
				String[] verteidigungS = verteidigung.split("\\.");
				//System.out.println("verteidigungS:");
				//for(int x = 0; x < verteidigungS.length; x++)
				//	System.out.println(verteidigungS[x]);
				
				int[] verteidigungI = new int[verteidigungS.length];
				//System.out.printf("verteidigungI: ");
				for(int i = 0; i < verteidigungI.length; i++){
					verteidigungI[i] = Integer.valueOf(verteidigungS[i]);
					//System.out.printf("%d ", verteidigungI[i]);
				}
				//System.out.println();
				
				forschung = planet.substring(planet.indexOf("Forschung: ")+11);
				//System.out.printf("lFF. forschung: %s\n\n",forschung);
				String[] forschungS = forschung.split("\\.");
				//System.out.println("forschungS:");
				//for(int x = 0; x < forschungS.length; x++)
				//	System.out.println(forschungS[x]);
				
				int[] forschungI = new int[forschungS.length];
				//System.out.printf("forschungI: ");
				for(int i = 0; i < forschungI.length; i++){
					forschungI[i] = Integer.valueOf(forschungS[i]);
					//System.out.printf("%d ", forschungI[i]);
				}
				//System.out.println();
				

				planets.get(planets.size()-1).setVersorgung(versorgungI);
				planets.get(planets.size()-1).setAnlagen(anlagenI);
				planets.get(planets.size()-1).setVerteidigung(verteidigungI);
				setTechnologien(forschungI);
			}
			if(planet.equalsIgnoreCase("Geologe: true"))
				setGeologe(true);
			else
				setGeologe(false);
			if((planet = reader.readLine())!=null){
				String neuerPlanet = planet.substring(planet.indexOf("Neuer Planet: ")+14);
				String[] neuerPlanetS = neuerPlanet.split("\\.");
				int[] neuerPlanetI = new int[NeuerPlanetNamen.length];
				for(int i = 0; i < neuerPlanetI.length; i++)
					neuerPlanetI[i] = Integer.valueOf(neuerPlanetS[i]);
				setNeuerPlanet(neuerPlanetI);
			}
			else
				setNeuerPlanet(new int[NeuerPlanetNamen.length]);
			reader.close();
		}
		catch(IOException e){}	
		finally{}
	}
	public boolean isGeologe() {
		return geologe;
	}
	public void setGeologe(boolean geologe) {
		this.geologe = geologe;
	}
}
