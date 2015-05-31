import java.util.ArrayList;
import java.util.Random;

class Chromosome {

    /**
     * The list of cities, which are the genes of this chromosome.
     */
    protected int[] cityList;

    /**
     * The cost of following the cityList order of this chromosome.
     */
    protected double cost;

    /**
     * @param cities The order that this chromosome would visit the cities.
     */
    Chromosome(City[] cities) {
        Random generator = new Random();
        cityList = new int[cities.length];
        //cities are visited based on the order of an integer representation [o,n] of each of the n cities.
        for (int x = 0; x < cities.length; x++) {
            cityList[x] = x;

        }

        //shuffle the order so we have a random initial order
        for (int y = 0; y < cityList.length; y++) {
            int temp = cityList[y];
            int randomNum = generator.nextInt(cityList.length);
            cityList[y] = cityList[randomNum];
            cityList[randomNum] = temp;
        }

        calculateCost(cities);
    }

    Chromosome(Chromosome original){
    	this.cityList = original.cityList;
    	this.cost = original.getCost();
    }
    /**
     * Calculate the cost of the specified list of cities.
     *
     * @param cities A list of cities.
     */
    void calculateCost(City[] cities) {
        cost = 0;
        for (int i = 0; i < cityList.length - 1; i++) {
            double dist = cities[cityList[i]].proximity(cities[cityList[i + 1]]);
            cost += dist;
        }
    }

    /**
     * Get the cost for this chromosome. This is the amount of distance that
     * must be traveled.
     */
    double getCost() {
        return cost;
    }

    /**
     * @param i The city you want.
     * @return The ith city.
     */
    int getCity(int i) {
        return cityList[i];
    }

    /**
     * Set the order of cities that this chromosome would visit.
     *
     * @param list A list of cities.
     */
    void setCities(int[] list) {
        for (int i = 0; i < cityList.length; i++) {
            cityList[i] = list[i];
        }
    }

    /**
     * Set the index'th city in the city list.
     *
     * @param index The city index to change
     * @param value The city number to place into the index.
     */
    void setCity(int index, int value) {
        cityList[index] = value;
    }

    /**
     * Sort the chromosomes by their cost.
     *
     * @param chromosomes An array of chromosomes to sort.
     * @param num         How much of the chromosome list to sort.
     */
    public static void sortChromosomes(Chromosome chromosomes[], int num) {
        Chromosome ctemp;
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (int i = 0; i < num - 1; i++) {
                if (chromosomes[i].getCost() > chromosomes[i + 1].getCost()) {
                    ctemp = chromosomes[i];
                    chromosomes[i] = chromosomes[i + 1];
                    chromosomes[i + 1] = ctemp;
                    swapped = true;
                }
            }
        }
    }

    public static Chromosome[] crossfillPair(Chromosome parentA, Chromosome parentB){
    	// Get each chromosome's encoding
    	int[] a = parentA.cityList;
    	int[] b = parentB.cityList;

    	// For each chromosome, recombinate
    	int[] newa = new int[a.length];
		int[] newb = new int[b.length];

		int l = a.length;

		// Choose random position in array, p
		int p = (int)(Math.random() * (l-1));

		// Create visited arrays for O(k) lookup in contains
		boolean[] visiteda = new boolean[parentA.cityList.length];
		boolean[] visitedb = new boolean[parentB.cityList.length];

		for (int i = 0; i < parentA.cityList.length - 1 ; i++ ) {
			visiteda[i] = false;
			visitedb[i] = false;
		}

		// Copy over first part of parent to child
		// And mark each gene off in visited array
		for (int i = 0; i < l; i++) {
			if(i < p){
				newa[i] = a[i];
				newb[i] = b[i];
				visiteda[a[i]] = true;
				visitedb[b[i]] = true;
			} else {
				newa[i] = 0;
				newb[i] = 0;
			}			
		}		

		// placekeepers
		int ka = p;
		int kb = p;

		// fill remainder of chromosome with genes from other
		// parent
		for (int i = 0; i < l ; i++) {
			if(!visiteda[b[i]]){
				newa[ka] = b[i];
				ka++;
			} 
			if(!visitedb[a[i]]){
				newb[kb] = a[i];
				kb++;
			} 
		}

		// // Check that children visit each city
		// boolean[] va = new boolean[cityCount];
		// boolean[] vb = new boolean[cityCount];
		// for (int i = 0; i <  ; ) {
			
		// }	

		Chromosome childA = new Chromosome(parentA);
		childA.setCities(newa);

		Chromosome childB = new Chromosome(parentB);
		childB.setCities(newb);

		Chromosome[] children = {childA, childB};

		return(children);
    }

    public static Chromosome[] crossfillRecombination(Chromosome[] chromosomes, int desiredPopulationSize) {
    	// Check if we have enough parents to make desired population

    	// Check if there are an even number of chromosomes
    	if(chromosomes.length % 2 != 0){
    		System.out.println("crossfill failure, odd number of parents");
    		return(0);
    	} else {

    		boolean[][] paired = new boolean[chromosomes.length][chromosomes.length];
    		Chromosome[] childrenOfAB;
    		Chromosome[] newPopulation = new Chromosome[desiredPopulationSize];
    		for (int i = 0; i < chromosomes.length ; i++ ) {
    			newPopulation[i] = chromosomes[i];
    		}

    		int newPopSize = chromosomes.length;

    		while(newPopSize < desiredPopulationSize){
    			int a = (int)(Math.random() * chromosomes.length) - 1;
	    		int b = (int)(Math.random() * chromosomes.length) - 1;

	  			if(a == b){ b++; }
	  			else if(a > b){ int temp = a; a = b; b = temp; }

	  			if(paired[a][b]){
	  				continue;
	  			} else {
	  				childrenOfAB = crossfillPair(chromosomes[a], chromosomes[b]);
	  				newPopulation[newPopSize] = childrenOfAB[0];
	  				newPopulation[newPopSize + 1] = childrenOfAB[1];
	  				newPopSize += 2;
	  				paired[a][b] = true;
	  			}
    		}

    		return(newPopulation);
	    }
    }
}
