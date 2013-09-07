Algorithm Course
===========

Week1 - Percolation
-----------------------------------  
Import algs4.jar and stdlib.jar into classpath.

This implementation extends the original WeightedQuickUnionUF class. 
When unioning two grids, will judge additionally whether the groups are connected to bottom or top.
Thus if two groups are connecting to the bottom or top in the same time, the the whole system percolates.

This implementation resolves the backwash issue when using a virtual top and virtual bottom to check percolation.
And has a quite similar memory consumption, but less time consumption, compared to add another WeightedQuickUnionUF object, connecting to virtual top only.

Passes all tests and style check.

Week2 - Queues
-----------------------------------  
Remember to clean up unreferenced objects when deleting nodes to avoid loitering.