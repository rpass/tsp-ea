import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Arrays;
import java.awt.*; 

import javax.swing.*;

public class TSP {

    /**
     * How many cities to use.
     */
    protected static int cityCount;

    /**
     * How many chromosomes to use.
     */
    protected static int populationSize;

    /**
     * The part of the population eligable for mateing.
     */
    protected static int matingPopulationSize;

    protected static int parentSelectionPressure;

    protected static int survivorSelectionPressure;
    /**
     * The part of the population selected for mating.
     */
    protected static int[] selectedParents;

    /**
     * The current generation
     */
    protected static int generation;

    /**
     * The list of cities.
     */
    protected static City[] cities;

    /**
     * The list of chromosomes.
     */
    protected static Chromosome[] chromosomes;

    /**
    * Frame to display cities and paths
    */
    private static JFrame frame;

    /**
     * Integers used for statistical data
     */
    private static double min;
    private static double avg;
    private static double max;
    private static double sum;

    /**
     * Width and Height of City Map, DO NOT CHANGE THESE VALUES!
     */
    private static int width = 600;
    private static int height = 600;


    private static Panel statsArea;
    private static TextArea statsText;


    /*
     * Writing to an output file with the costs.
     */
    private static void writeLog(String content) {
        String filename = "results.out";
        FileWriter out;

        try {
            out = new FileWriter(filename, true);
            out.write(content + "\n");
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     *  Deals with printing same content to System.out and GUI
     */
    private static void print(boolean guiEnabled, String content) {
        if(guiEnabled) {
            statsText.append(content + "\n");
        }

        System.out.println(content);
    }

    public static void evolve() {
        //Write evolution code here.
        int numberOfParents = 100;//(int)((1/2.0) * populationSize);
        matingPopulationSize = 700;
        double mutationProbability = 0.3;
        parentSelectionPressure = 10;
        survivorSelectionPressure = 10;

        // Create random population
    	Chromosome[] newPop = createPopulation(matingPopulationSize, cities);

    	// Choose parents from chromosomes
        int[][] parents = tournamentParentSelection(numberOfParents);


        int[][] newGenes = Chromosome.crossfillRecombination(parents, matingPopulationSize);
        modifyPopulation(newPop, newGenes);
        mutatePopulation(newPop, mutationProbability);
        chromosomes = tournamentSurvivorSelection(newPop);
    }

    public static Chromosome[] createPopulation(int size, City[] cities){
    	Chromosome[] newPopulation = new Chromosome[size];
    	for (int i = 0; i < newPopulation.length; i++) {
    		newPopulation[i] = new Chromosome(cities);
    	}
    	return(newPopulation);
    }

	public static void mutatePopulation(Chromosome[] population, double mutationProbability){
		int[] genome;
		double dice;
		for (Chromosome chrom : population) {
			dice = (Math.random());
			if(dice < mutationProbability){
				genome = chrom.cityList; 
				chrom.setCityList(Chromosome.mutate(genome));
				chrom.calculateCost(cities);
			}
		}
	}

    public static void modifyPopulation(Chromosome[] population, int[][] genomes){
    	assert population.length == genomes.length;
    	//System.out.println("modifying");
    	// for (int j = 0; j < population[0].cityList.length; j++ ) {
    	// 	System.out.print(population[0].cityList[j]+",");
    	// }
    	// System.out.println();

    	int i = 0;
    	for (Chromosome chr : population) {
    		double before = chr.getCost();
    		chr.setCityList(genomes[i]);
    		i++;
    		chr.calculateCost(cities);
       		double after = chr.getCost();
       		// assert after < before;
    	}

    	// for (int j = 0; j < population[0].cityList.length; j++ ) {
    	// 	System.out.print(population[0].cityList[j]+",");
    	// }
    	//System.out.println();
    }

    public static int[][] stochasticParentSelection(){
    	// builds selectedParents with chosen parents
    	int numberOfParents = 10;
    	int parentsChosen = 0;
    	int top = (int)(populationSize / 10.0);
    	assert top > 0;    	

    	int[] selectedParents = new int[numberOfParents];
    	boolean[] visited = new boolean[top];
    	Arrays.fill(visited, false);
    	assert !visited[visited.length - 1];

    	int temp;

    	// Choose indexes from the top of the sorted chromosomes
    	while(parentsChosen < numberOfParents){
    		temp = (int)(Math.random() * top);
    		assert temp >= 0;


    		if(!visited[temp]){
    			visited[temp] = true;
    			selectedParents[parentsChosen] = temp;
    			parentsChosen++;
    		}
    	}

    	// build array of genomes
    	int[][] parents = new int[numberOfParents][cities.length];
        int i = 0;
        for (int parentId : selectedParents) {
        	parents[i] = chromosomes[parentId].cityList;
        }

    	return(parents);
    }

    public static int[][] elitestParentSelection(int numberOfParents){
    	Chromosome.sortChromosomes(chromosomes, chromosomes.length);

    	int[][] parents = new int[numberOfParents][cities.length];

    	for (int i = 0; i < numberOfParents ; i++) {
    		parents[i] = chromosomes[i].cityList;
    		// System.out.println("cost of parent["+i+"]:" +chromosomes[i].getCost());
    	}

    	return(parents);
    }

    public static Chromosome[] elitestSurvivorSelection(Chromosome[] competingChromosomes){
    	Chromosome.sortChromosomes(competingChromosomes, competingChromosomes.length);
    	Chromosome[] survivors = new Chromosome[populationSize];
    	for (int i = 0; i < populationSize ; i++) {
    		survivors[i] = competingChromosomes[i];
    	}

		// System.out.println("survivors:");

    	return(survivors);
    }

    public static int[][] tournamentParentSelection(int numberOfParents){

    	int selectionPressure = parentSelectionPressure;
    	Chromosome[] champions = tournamentSelection(chromosomes, selectionPressure, numberOfParents);

    	int[][] parentGenes = new int[numberOfParents][cities.length];
    	for (int i = 0; i < numberOfParents; i++) {
    		parentGenes[i] = champions[i].cityList;
    	}
    	return(parentGenes);
    }

    public static Chromosome[] tournamentSurvivorSelection(Chromosome[] competingChromosomes){
    	int[] candidates = new int[competingChromosomes.length];
    	for (int i = 0; i < candidates.length ; i++) {
    		candidates[i] = i;
    	}
    	int selectionPressure = survivorSelectionPressure;
    	int numberOfChampions = populationSize;

    	Chromosome[] champions = tournamentSelection(competingChromosomes, selectionPressure, numberOfChampions);

    	return(champions);
    }

	public static Chromosome[] tournamentSelection(Chromosome[] candidateChromosomes, int selectionPressure, int numberOfChampions){
		//System.out.println("candidate pool: " + candidateChromosomes.length);
		
		// keeps track of candidates already chosen
		boolean[] picked = new boolean[candidateChromosomes.length];
		Arrays.fill(picked, false);

		Chromosome[] champions = new Chromosome[numberOfChampions];
		int[] arena = new int[selectionPressure];

		int random;
		Chromosome champion;

		for (int i = 0; i < numberOfChampions; i++ ) {
			for (int j = 0; j < selectionPressure; j++ ) {
				random = (int)(Math.random()*candidateChromosomes.length);
				while(picked[random]){
					random = (int)(Math.random()*candidateChromosomes.length);
					//System.out.println("dam");
				}
				arena[j] = random;
			}

			double lowestCost = 300000.0;
			champion = candidateChromosomes[arena[0]];
			int pickedcompetitor = 0;
			for (int competitor : arena) {
				double cost = candidateChromosomes[competitor].getCost();
				if(cost < lowestCost){
					champion = candidateChromosomes[competitor];
					lowestCost = cost;
					pickedcompetitor = competitor;
				}
			}
			champions[i] = champion;
			picked[pickedcompetitor] = true;
		}

		return(champions);
	}

    /**
     * Update the display
     */
    public static void updateGUI() {
        Image img = frame.createImage(width, height);
        Graphics g = img.getGraphics();
        FontMetrics fm = g.getFontMetrics();

        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);

        if (true && (cities != null)) {
            g.setColor(Color.green);
            for (int i = 0; i < cityCount; i++) {
                int xpos = cities[i].getx();
                int ypos = cities[i].gety();
                g.fillOval(xpos - 5, ypos - 5, 10, 10);
            }

            g.setColor(Color.gray);
            for (int i = 0; i < cityCount; i++) {
                int icity = chromosomes[0].getCity(i);
                if (i != 0) {
                    int last = chromosomes[0].getCity(i - 1);
                    g.drawLine(
                        cities[icity].getx(),
                        cities[icity].gety(),
                        cities[last].getx(),
                        cities[last].gety());
                }
            }
        }
        frame.getGraphics().drawImage(img, 0, 0, frame);
    }


    public static void main(String[] args) {
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currentTime  = df.format(today);

        int runs;
        boolean display = false;
        String formatMessage = "Usage: java TSP 100 500 1 [gui] \n java TSP [NumCities] [PopSize] [Runs] [gui]";

        if (args.length < 3) {
            System.out.println("Please enter the arguments");
            System.out.println(formatMessage);
            display = false;
        } else {

            if (args.length > 3) {
                display = true; 
            }

            try {
                cityCount = Integer.parseInt(args[0]);
                populationSize = Integer.parseInt(args[1]);
                runs = Integer.parseInt(args[2]);

                if(display) {
                    frame = new JFrame("Traveling Salesman");
                    statsArea = new Panel();

                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setSize(width + 300, height);
                    frame.setResizable(false);
                    frame.setLayout(new BorderLayout());
                    
                    statsText = new TextArea(35, 35);
                    statsText.setEditable(false);

                    statsArea.add(statsText);
                    frame.add(statsArea, BorderLayout.EAST);
                    
                    frame.setVisible(true);
                }


                min = 0;
                avg = 0;
                max = 0;
                sum = 0;

                // create a random list of cities
                // Note: This is outside the run loop so that the multiple runs
                // are tested on the same city set
                cities = new City[cityCount];
                for (int i = 0; i < cityCount; i++) {
                    cities[i] = new City(
                        (int) (Math.random() * (width - 10) + 5),
                        (int) (Math.random() * (height - 50) + 30));
                }

                writeLog("Run Stats for experiment at: " + currentTime);
                for (int y = 1; y <= runs; y++) {
                    print(display,  "Run " + y + "\n");

                // create the initial population of chromosomes
                    chromosomes = new Chromosome[populationSize];
                    for (int x = 0; x < populationSize; x++) {
                        chromosomes[x] = new Chromosome(cities);
                    }

                    generation = 0;
                    double thisCost = 0.0;

                    while (generation < 100) {
                        evolve();                   // The population hasn't been evaluated?
                        generation++;

                        Chromosome.sortChromosomes(chromosomes, populationSize);
                        double cost = chromosomes[0].getCost();
                        thisCost = cost;

                        NumberFormat nf = NumberFormat.getInstance();
                        nf.setMinimumFractionDigits(2);
                        nf.setMinimumFractionDigits(2);

                        print(display, "Gen: " + generation + " Cost: " + (int) thisCost);

                        if(display) {
                            updateGUI();
                        }
                    }

                    writeLog(thisCost + "");

                    if (thisCost > max) {
                        max = thisCost;
                    }

                    if (thisCost < min || min == 0) {
                        min = thisCost;
                    }

                    sum +=  thisCost;

                    print(display, "");
                }

                avg = sum / runs;
                print(display, "Statistics after " + runs + " runs");
                print(display, "Solution found after " + generation + " generations." + "\n");
                print(display, "MIN: " + min + " AVG: " + avg + " MAX: " + max + "\n");

            } catch (NumberFormatException e) {
                System.out.println("Please ensure you enter integers for cities and population size");
                System.out.println(formatMessage);
            }
        }
    }
}