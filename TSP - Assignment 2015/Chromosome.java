import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

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
		}

		int newPopSize = parents.length;

		// while(newPopSize < desiredPopulationSize){
		// 	int a = 0;
		// 	int b = 0;
		// 	while(a == b){
		// 		a = (int)(Math.random() * parents.length);
		// 		b = (int)(Math.random() * parents.length);
		// 	}
  			
  // 			if(a > b){ int temp = a; a = b; b = temp; }

  // 			if(paired[a][b]){
  // 				continue;
  // 			} else {
  // 				childrenOfAB = crossfillPair(parents[a], parents[b]);
  // 				newPopulation[newPopSize] = childrenOfAB[0];
  // 				newPopulation[newPopSize + 1] = childrenOfAB[1];
  // 				newPopSize += 2;
  // 				paired[a][b] = true;
  // 			}	    		
	 //    }

	    int a;
	    int b;
	    int temp;

	    while(newPopSize < desiredPopulationSize){
			a = (int)(Math.random() * parents.length);
			b = (int)(Math.random() * parents.length);
			while(paired[a][b] || a == b){
				a = (int)(Math.random() * parents.length);
				b = (int)(Math.random() * parents.length);
			}
			if(a>b){
				temp = b;
				b = a;
				a = temp;
			}

			childrenOfAB = crossfillPair(parents[a], parents[b]);
			newPopulation[newPopSize] = childrenOfAB[0];
			newPopulation[newPopSize + 1] = childrenOfAB[1];
			newPopSize += 2;
			paired[a][b] = true;
	    }
	    
	    return(newPopulation);
    }

	public static int[] mutate(int[] a){
		int arrayLength = a.length;

		int[] p = threeRandomPoints(arrayLength);
		int[] points = choose2(p[0], p[1], p[2]);
		//points[2] -= (p[1] - p[0]);

		ArrayList<Integer> arr = new ArrayList<Integer>();
		ArrayList<Integer> temp = new ArrayList<Integer>();

		for (int x : a) {
			arr.add(x);
		}

		int subArrLength = points[1] - points[0] + 1;
		int newP2 = points[2];
		for (int i = 0; i < subArrLength ; i++) {
			temp.add(arr.remove(points[0]));
			if(points[2] >= points[1]){
				newP2--;
			}
		}
		int minCap = arr.size() + temp.size();
		arr.ensureCapacity(minCap);
		//System.out.println("t:" + temp + "\narr: " + arr);
		for (int j = 0; j < subArrLength ; j++) {
			arr.add(j + newP2, temp.remove(0));
		}
		//System.out.println("arr: " + arr);
		Arrays.fill(a, -1);
		for (int i = 0; i < arr.size(); i++ ) {
			a[i] = arr.get(i);
		}

		//printa(a);
		return(a);
	}

	public static int[] threeRandomPoints(int max){
		int[] b = new int[3];

		b[0] = (int)(Math.random() * max);
		b[1] = (int)(Math.random() * max);
		while(b[1] == b[0]){
			b[1] = (int)(Math.random() * max);
		}

		b[2] = (int)(Math.random() * max);
		while(b[2] == b[1] || b[2] == b[0]){
			b[2] = (int)(Math.random() * max);
		}

		Arrays.sort(b);

		return(b);
	}

	public static int[] choose2(int a, int b, int c){
		int bottle = (int)(Math.floor(Math.random()*2));
		int[] pair = new int[3];
		if(bottle == 1){
			pair[0] = a;
			pair[1] = b;
			pair[2] = c;
		} else {
			pair[0] = b;
			pair[1] = c;
			pair[2] = a;
		}

		return(pair);
	}
}
