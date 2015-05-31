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
    double calculateCost(City[] cities) {
        cost = 0;
        for (int i = 0; i < cityList.length - 1; i++) {
            double dist = cities[cityList[i]].proximity(cities[cityList[i + 1]]);
            cost += dist;
        }
        return(cost);
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

    void setCityList(int[] clist){
    	for (int i = 0; i < cityList.length; i++) {
    		setCity(i, clist[i]);
    	}
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

    public static int[][] crossfillPair(int[] a, int[] b){
		assert a.length > 0;
		assert a.length == b.length;

    	// For each chromosome, recombinate
    	int[] newa = new int[a.length];
		int[] newb = new int[b.length];
		// Arrays.fill(newa, 0);
		// Arrays.fill(newb, 0);
		int l = a.length;

		// System.out.println("Before");
		// System.out.print("a: ");
		// for (int x : a) {
		// 	System.out.print(x+",");
		// }
		// System.out.print("\nnewa: ");
		// for (int x : newa) {
		// 	System.out.print(x+",");
		// }
		// System.out.print("\nb: ");
		// for (int x : b) {
		// 	System.out.print(x+",");
		// }
		// System.out.print("\nnewb: ");
		// for (int x : newb) {
		// 	System.out.print(x+",");
		// }
		// System.out.println();

		// Choose random position in array, p
		int p = (int)(Math.random() * l);

		// System.out.println("p = " + p);
		// Create visited arrays for O(k) lookup in contains
		boolean[] visiteda = new boolean[l];
		boolean[] visitedb = new boolean[l];

		for (int i = 0; i < l; i++ ) {
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

		// for (int j : newa) {
		// 	System.out.print(j+",");
		// }
		// System.out.println();
		// for (int k : newb) {
		// 	System.out.print(k+",");
		// }
		// System.out.println();

		for (int i = p; i < l; i++) {
			int jb = 0;
			int ja = 0;
			while(jb<l && visiteda[b[jb]]){
				jb++;
			}
			newa[i] = b[jb];
			visiteda[b[jb]] = true;

			while(ja<l && visitedb[a[ja]]){
				ja++;
			}
			newb[i] = a[ja];
			visitedb[a[ja]] = true;
		}

		// System.out.println("After");
		// System.out.print("a: ");
		// for (int x : a) {
		// 	System.out.print(x+",");
		// }
		// System.out.println("\nnewa: ");
		// for (int x : newa) {
		// 	System.out.print(x+",");
		// }
		// System.out.print("\nb: ");
		// for (int x : b) {
		// 	System.out.print(x+",");
		// }
		// System.out.println("\nnewb: ");
		// for (int x : newb) {
		// 	System.out.print(x+",");
		// }
		// System.out.println();

		int[][] children = {newa, newb};

		return(children);
    }

    public static int[][] crossfillRecombination(int[][] parents, int desiredPopulationSize) {
    	// Check if we have enough parents to make desired population

    	// Check if there are an even number of parents
    	assert parents.length % 2 == 0;
    	assert desiredPopulationSize > parents.length;
    	assert parents.length > 0;
    	assert desiredPopulationSize > 0;

		boolean[][] paired = new boolean[parents.length][parents.length];
		int[][] childrenOfAB;
		int[][] newPopulation = new int[desiredPopulationSize][parents[0].length];

		// fill newPopulation with parent population
		for (int i = 0; i < parents.length ; i++ ) {
			newPopulation[i] = parents[i];

			// System.out.println("parent ["+i+"]");
			// for (int ele : parents[i]) {
			// 	System.out.print(ele + ",");
			// }
			// System.out.println();
		}

		int newPopSize = parents.length;

		while(newPopSize < desiredPopulationSize){
			int a = 0;
			int b = 0;
			while(a == b){
				a = (int)(Math.random() * parents.length);
				b = (int)(Math.random() * parents.length);
			}
  			
  			if(a > b){ int temp = a; a = b; b = temp; }

  			if(paired[a][b]){
  				continue;
  			} else {
  				childrenOfAB = crossfillPair(parents[a], parents[b]);
  				newPopulation[newPopSize] = childrenOfAB[0];
  				newPopulation[newPopSize + 1] = childrenOfAB[1];
  				newPopSize += 2;
  				paired[a][b] = true;
  			}	    		
	    }
	    
	    return(newPopulation);
    }
}
