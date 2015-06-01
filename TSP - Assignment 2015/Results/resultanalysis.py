from scipy.stats import normaltest, ttest_ind, wilcoxon
import scipy
import numpy

if __name__ == "__main__":
	f1 = open('finalresults', 'r')
	f2 = open('results_ion.out', 'r')

	my_values = f1.read().split('\n')
	my_values = map(float, my_values)
	my_results = numpy.array(my_values)
	f1.close()

	ions_values = f2.read().split('\n')
	ions_values = map(float, ions_values)
	ions_results = numpy.array(ions_values)
	f2.close()

	x1 = normaltest(my_results)
	x2 = normaltest(ions_results)
	t,p = wilcoxon(my_values, ions_values)

	if(x1[1] < 0.95):
		print '''The first dataset is not from a normal distribution,
the wilcox test will be used for statistical signifance testing'''
	
	if(x2[1] < 0.05):
		print "The second dataset is not from a normal distribution"

	print("My parameters:\nk2: %s\np-value: %s"%(x1[0], x1[1]))
	print
	print("Second set of parameters:\nk2: %s\np-value: %s"%(x2[0], x2[1]))
	print("")
	print("The Wilcoxon test reveals:\np-value: %s\n"%(p))
	if(p<0.01):
		print '''The two datasets are significantly different with
a p-value result of: %s'''%p