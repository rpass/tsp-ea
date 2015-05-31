import java.util.Arrays;

public class ECTest{
	public static void main(String[] args){

		/**
		*	Testing threepointMutate
		**/

		int[] a = {0,1,2,3,4,5,6,7,8,9};
		int[] b = threeRandomPoints(a.length);

		int[] c = threepointMutate(a);

		for (int i : c) {
			System.out.print(i+", ");
		}

		/**
		* Testing crossfillpair
		**/
		// int size = Integer.parseInt(args[0]);
		// int[] a = new int[size];
		// int[] b = new int[size];

		// for (int i = 0; i< a.length ; i++) {
		// 	a[i] = i;
		// 	b[i] = a.length - 1 - i;
		// }

		// // for (int temp : b) {
		// // 	System.out.print(temp+",");
		// // }

		// int[][] x = crossfillPair(a,b);
		// int[] one = x[0];
		// int[] two = x[1];

		// for (int ele : one) {
		// 	System.out.print(ele+",");
		// }
		// System.out.println();

		// for (int ele : two) {
		// 	System.out.print(ele+",");
		// }
		// System.out.println();
	}

	public static int[] threepointMutate(int[] a){
		int len = a.length;
		int[] points = threeRandomPoints(len);
		int[] result = new int[a.length];

		// create temp array to store the numbers from the 
		// starting point up to and including the final point
		int[] temp = new int[(points[1] - points[0]) + 1];

		// load temp with the elements from a between points 0 and 1
		for (int i = 0; i < temp.length; i++) {
			temp[i] = a[points[0] + i];
		}

		for (int j = 0; j < a.length; j++) {
			if(j < points[0]){
				result[j] = a[j];
			} else if(j > points[1]) { 
				result[j] = a[points[1] + j];
			} 
		}

		for (int k = 0; k < temp.length; k++){
			result[k + points[2]] = temp[k];
		}

		return(result);
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

		System.out.println("random points: ");
		for (int x : b) {
			System.out.print(x + ",");
		}
		System.out.println();
		return(b);
	}

	// public static int[] choose3ints(int m){
	// 	int max = a.length - 1 - 2;
	// 	int min = 0;
	// 	int range = (max - min) + 1;
		
	// 	int p1 = (int)(Math.random()*(range));
	// 	min = p1;
	// 	max += 1;
	// 	range = (max - min) + 1;

	// 	int p2 = (int)(Math.random()*(range));
	// 	min = p2;
	// 	max += 1;
	// 	range = (max - min) + 1;

	// 	int p3 = (int)(Math.random()*(range));
		
	// 	System.out.println(a.length + ": " + p1 + ", " + p2 + ", " + p3);
	// 	int[] result = {p1, p2, p3};
	// 	return(result);
	// }

	public static int[][] crossfillPair(int[] a, int[] b){
		assert a.length > 0;
		assert a.length == b.length;

    	// For each chromosome, recombinate
    	int[] newa = new int[a.length];
		int[] newb = new int[b.length];
		System.out.println("a leng = " + a.length);
		System.out.println("b leng = " + b.length);
		int l = a.length;

		// Choose random position in array, p
		int p = (int)(Math.random() * l);

		System.out.println("p = " + p);
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
		// System.out.println("new:");
		// for (int x : newa) {
		// 	System.out.print(x+",");
		// }
		// for (int y : newb) {
		// 	System.out.print(y+",");
		// }
		int[][] children = {newa, newb};

		return(children);
    }
}
