public class Percolation {
	
	private int N;
	private boolean[] isOpenArray;
	private WeightedQuickUnionUF unionDs;
	
	// create N-by-N grid, with all sites blocked
	public Percolation(int N) {
		if (N <= 0) throw new IllegalArgumentException();
		
		this.N = N;
		// ~N^2 time
		isOpenArray = new boolean[N * N + 2]; // Add 2 elements for connecting nodes
		// ~N^2 time
		unionDs = new WeightedQuickUnionUF(N * N + 2);
		// union the bottom row
		// ~N time
		int bottomApex = N * N + 1;
		for (int i = 1; i <= N; i++) unionDs.union(bottomApex, buildDsIndex(N, i));
	}
	
	// open site (row i, column j) if it is not open already
	public void open(int i, int j) {
		validateIndexes(i, j);
		if (! isOpen(i, j)) {
			int index = buildDsIndex(i, j);
			isOpenArray[index] = true; // open site
			if ((i == 1) || (isAdjacentToFull(i, j))) unionDs.union(0, index); // fill the newly opened site
			unionWithAdjacentOpenSites(i, j);
		}
	}
	
	// is site (row i, column j) open?
	public boolean isOpen(int i, int j) {
		validateIndexes(i, j);
		return isOpenArray[buildDsIndex(i, j)];
	}

	// is site (row i, column j) full?
	public boolean isFull(int i, int j) {
		validateIndexes(i, j);
		return isOpen(i, j) && unionDs.connected(0, buildDsIndex(i, j));
	}

	// does the system percolate?
	public boolean percolates() {
		return unionDs.connected(0, N * N + 1);
	}             
	
	private void validateIndexes(int i, int j) {
		if (i <= 0 || i > N || j <= 0 || j > N) throw new IndexOutOfBoundsException();
	}
	
	private int buildDsIndex(int i, int j) {
		return ((i - 1) * N + j);
	}
	
	private boolean isAdjacentToFull(int i, int j) {
		if ((i - 1 > 0) && isFull(i - 1, j)) return true;
		if ((i + 1 <= N) && isFull(i + 1, j)) return true;
		if ((j - 1 > 0) && isFull(i, j - 1)) return true;
		if ((j + 1 <= N) && isFull(i, j + 1)) return true;
		return false;
	}
	
	private void unionWithAdjacentOpenSites(int i, int j) {
		if ((i - 1 > 0) && isOpen(i - 1, j)) unionDs.union(buildDsIndex(i, j), buildDsIndex(i - 1, j));
		if ((i + 1 <= N) && isOpen(i + 1, j)) unionDs.union(buildDsIndex(i, j), buildDsIndex(i + 1, j));
		if ((j - 1 > 0) && isOpen(i, j - 1)) unionDs.union(buildDsIndex(i, j), buildDsIndex(i, j - 1));
		if ((j + 1 <= N) && isOpen(i, j + 1)) unionDs.union(buildDsIndex(i, j), buildDsIndex(i, j + 1));
	}
}