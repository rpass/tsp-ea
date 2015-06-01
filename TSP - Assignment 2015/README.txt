Evolutionary Computing Assignment - UCT Computer Science Honours 2015
Robert Passmore - PSSROB001 - PSSROB001@myuct.ac.za

--------------------- RESULTS -----------------------

MIN: 166769.0 AVG: 170757.44 MAX: 174488.0

------------- PARAMETERS & STRATEGIES ---------------

Number of Cities: 		1000
Number of Generations:	 100
Population Size:		1000

Parent Selection  --- Tournament:
Tournament Size:		 150
Parent Pop. Size:		  50

Recombination Strategy --- Cross-Fill

Mutation Strategy --- 3-point-exchange
Mutation probability:	   0.3

Surivor Selection --- Tournament:
Tournament Size:		 150

---------------- PARTNER'S DETAILS -----------------

I compared my experimental results with those of Ion Todd - TDDION001
His statistics were as follows: 

MIN: 187130.0 AVG: 190673.78 MAX: 197141.0

(His dataset can be found in Results/results_ion.out)


----------- RESULTS OF STATISTICAL TESTS ------------

Neither dataset fit a normal distribution. The Scipy.stats.normaltest package in Python was used to determine this. The results were:

My dataset: 	p-value = 0.228883507986
Ion's dataset: 	p-value = 0.511667500406

The Wilcoxon test (sciply.stats.wilcoxon) was used to judge the null hypothesis that both datasets come from the same distribution. It is effectively a paired T-Test for non-parametric datasets.

Wilxocon result: 	p-value = 7.55692945586e-10

This result shows that our two datasets are significantly different.

--------------- DISCUSSION [192 words]--------------

A breakdown of my strategies can be seen above in the PARAMETERS & STRATERGIES section.

Ion used elitest parent selection, a 1000 population size and utilised two mutation methods: inversion of substrings and swapping of cities. 

He uses the top 5% of parents to produce 1,000 children and then allows the top 1% of parents go through and fills the rest of the population with children. Then lastly he applies the mutation strategies at 2% probability of swapping and 20% probability of substring inversion.

I expect the two greatest differences between our algorithms are a) my use of tournament selection vs his use of elitest selection and b) that I breed a much larger population (2,500) from which I select 1,000 whereas he sticks to a population of 1,000. The difference in selection methods give me a more diverse selection of chromosomes rather than just the fittest. The higher offspring population also gives me more range for exploration, allowing me to find different local optima.

Furthermore, he allowed all of his mutations to pass through to the next generation which may work against his algorithm as these mutations might be completely useless.


-------- FILES INCLUDED --------

bin/
bin/Chromosome.class
bin/City.class
bin/TSP.class

README.txt

Results/
Results/finalresults
Results/results_ion.out
Results/resultsanalysis.py
Results/results.out

src/
src/Chromosome.java
src/City.java
src/TSP.java





