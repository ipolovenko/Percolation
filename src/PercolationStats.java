public class PercolationStats {

    private int N, T;
    private double[] probabilityArray;

	// perform T independent experiments on an N-by-N grid
	public PercolationStats(int N, int T) {
		if (N <= 0 || T <= 0) throw new IllegalArgumentException();
		this.N = N;
        this.T = T;
        probabilityArray = new double[T];
	}
	
	// sample mean of percolation threshold
	public double mean() {
		return StdStats.mean(probabilityArray);
	}
	
	// sample standard deviation of percolation threshold
	public double stddev() {
        return T > 1 ? StdStats.stddev(probabilityArray) : Double.NaN;
	}
	
	// low  endpoint of 95% confidence interval
	public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
	}
	
	// high endpoint of 95% confidence interval
	public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
	}
	
	// test client
	public static void main(String[] args) {
		PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        stats.doExperiments();
        stats.printStats();
	}

    private void doExperiments() {
        for (int t = 0; t < T; t++) {
            Percolation percolation = new Percolation(N);
            do {
                int i, j;
                do {
                    i = StdRandom.uniform(1, N + 1);
                    j = StdRandom.uniform(1, N + 1);
                } while (percolation.isOpen(i, j));
                percolation.open(i, j);
                probabilityArray[t]++;
            } while (! percolation.percolates());
            probabilityArray[t] = probabilityArray[t] / (N * N);
        }
    }

    private void printStats() {
        StdOut.printf("%-25s = %-10.10f\n", "mean", mean());
        StdOut.printf("%-25s = %-10.10f\n", "stddev", stddev());
        StdOut.printf("%-25s = %-10.10f, %-10.10f\n", "95% confidence interval", confidenceLo(), confidenceHi());
    }
}
