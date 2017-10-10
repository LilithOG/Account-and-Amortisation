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
	private static final boolean debug_prints = false;
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
		pow2 = new double[60];
		pow2[0] = 1;
		for(int i = 1; i < 60; i++)
			pow2[i] = pow2[i-1]*2;
		pow15 = new double[60];
		pow15[0] = 1.5;
		for(int i = 1; i < 60; i++)
			pow15[i] = pow15[i-1]*1.5;
		pow18 = new double[60];
		pow18[0] = 1.8;
		for(int i = 1; i < 60; i++)
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
		if(debug_prints) System.out.printf("getVersorgung für %d Planeten\n",planets.size());
		for(int i = 0; i < planets.size()+1; i++){
			if(debug_prints) System.out.printf("getVAP planet %d\n", i);
			for(int j = 0; j < VersorgungNamen.length; j++){
				if(i == 0){
					ret[j][0] = (String) VersorgungNamen[j].toString();
				}
				else{
					if(j != 0){
						ret[j][i] = String.valueOf(planets.get(i-1).getVersorgung()[j]);
						if(debug_prints) System.out.printf("getVAP: i=%d j=%d %s\n", i,j,ret[j][i].toString());
					}
					else{
						ret[j][i] = planets.get(i-1).getName();
						if(debug_prints) System.out.printf("getVAP: i=%d j=%d %s\n", i,j,ret[j][i].toString());
					}
				}
			}
		}
		return ret;
	}
	
	public Object[][] getAnlagenAllePlaneten(){
		Object[][] ret = new Object[8][planets.size()+1];
		for(int i = 0; i < planets.size()+1; i++){
			if(debug_prints) System.out.printf("getAAP planet %d\n", i);
			for(int j = 0; j < 8; j++){
				if(debug_prints) System.out.printf("i=%d j=%d\n",i,j);
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
			if(debug_prints) System.out.printf("getDAP planet %d\n", i);
			for(int j = 0; j < 9; j++){
				if(debug_prints) System.out.printf("i=%d j=%d\n",i,j);
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
		if(isGeologe())
			geologe += (double) 0.1;
		double plasmalvl = (double) Technologien[4];
		ProductionAndCostsHelper prod = new ProductionAndCostsHelper(this, 3, 2, 1);
		for(int i = 0; i < planets.size(); i++)
		{
			int[] versorgung = planets.get(i).getVersorgung();
			ret[0] += prod.getProdMet(versorgung[1]);
			if(debug_prints) System.out.printf("Stundenprodu Met: %d\n", ret[0]);
			ret[1] += prod.getProdKris(versorgung[2]);
			if(debug_prints) System.out.printf("Stundenprodu Kris: %d\n", ret[1]);
			ret[2] += prod.getProdDeut(versorgung[3], versorgung[11]);
			if(debug_prints) System.out.printf("Stundenprodu Deut: %d\n", ret[2]);
		}
		ret[0] = ret[0]*24;
		if(debug_prints) System.out.printf("Tagesprodu Met: %d\n", ret[0]);
		ret[1] = ret[1]*24;
		if(debug_prints) System.out.printf("Tagesprodu Kris: %d\n", ret[1]);
		ret[2] = ret[2]*24;
		if(debug_prints) System.out.printf("Tagesprodu Deut: %d\n", ret[2]);
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
		double cm = 0;
		double ck = 0;
		double cd = 0;
		int[] npi = getNeuerPlanetI();
		cm = 1000*pow2[npi[0]]-1000 + 1000*pow2[npi[1]]-1000 + 1000*pow2[npi[2]]-1000 + 150*(pow15[npi[3]]-pow15[0]) + 1125*(pow18[npi[4]]-pow18[0]);
		ck = 500*pow2[npi[1]]-500   + 1000*pow2[npi[2]]-1000  + 60*(pow15[npi[3]]-pow15[0]) + 450*(pow18[npi[4]]-pow18[0]);
		cd = 225*(pow18[npi[4]]-pow18[0]);
		
		cm += 400*pow2[npi[5]]-400 + 400*pow2[npi[6]]-400 + 200*pow2[npi[7]]-200 + 20000*pow2[npi[8]]-20000 + 1000000*pow2[npi[9]]-1000000;
		ck += 120*pow2[npi[5]]-120 + 200*pow2[npi[6]]-200 + 400*pow2[npi[7]]-400 + 20000*pow2[npi[8]]-20000 + 500000*pow2[npi[9]]-500000;
		cd += 200*pow2[npi[5]]-200 + 100*pow2[npi[6]]-100 + 200*pow2[npi[7]]-200 + 1000*pow2[npi[8]]-1000   + 100000*pow2[npi[9]]-100000;
		ck += 50000*pow2[npi[10]]-50000;
		cd += 100000*pow2[npi[10]]-100000;
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
				planets.add(new Planet(planet.substring(0, planet.indexOf(" Versorgung"))));
				String[] versorgungS = versorgung.split("\\.");				
				int[] versorgungI = new int[versorgungS.length];
				for(int i = 0; i < versorgungI.length; i++){
					versorgungI[i] = Integer.valueOf(versorgungS[i]);
				}

				anlagen =  planet.substring(planet.indexOf("Anlagen: ")+9, planet.indexOf(" Verteidigung: "));
				String[] anlagenS = anlagen.split("\\.");				
				int[] anlagenI = new int[anlagenS.length];
				for(int i = 0; i < anlagenI.length; i++){
					anlagenI[i] = Integer.valueOf(anlagenS[i]);
				}
				
				verteidigung = planet.substring(planet.indexOf("Verteidigung: ")+14, planet.indexOf(" Forschung: "));
				String[] verteidigungS = verteidigung.split("\\.");				
				int[] verteidigungI = new int[verteidigungS.length];
				for(int i = 0; i < verteidigungI.length; i++){
					verteidigungI[i] = Integer.valueOf(verteidigungS[i]);
				}
				
				forschung = planet.substring(planet.indexOf("Forschung: ")+11);
				String[] forschungS = forschung.split("\\.");				
				int[] forschungI = new int[forschungS.length];
				for(int i = 0; i < forschungI.length; i++){
					forschungI[i] = Integer.valueOf(forschungS[i]);
				}				

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
