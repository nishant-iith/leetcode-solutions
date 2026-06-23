class MedianFinder {
    private PriorityQueue<Integer> lowerHalf; // max-heap
    private PriorityQueue<Integer> upperHalf; // min-heap

    public MedianFinder() {
        lowerHalf = new PriorityQueue<>(Collections.reverseOrder());
        upperHalf = new PriorityQueue<>();
    }

    public void addNum(int num) {
        if (lowerHalf.isEmpty() || num <= lowerHalf.peek()) {
            lowerHalf.offer(num);
        } else {
            upperHalf.offer(num);
        }

        // Balance the heaps
        if (lowerHalf.size() > upperHalf.size() + 1) {
            upperHalf.offer(lowerHalf.poll());
        } else if (upperHalf.size() > lowerHalf.size()) {
            lowerHalf.offer(upperHalf.poll());
        }
    }

    public double findMedian() {
        if (lowerHalf.size() > upperHalf.size()) {
            return lowerHalf.peek();
        }
        return (lowerHalf.peek() + upperHalf.peek()) / 2.0;
    }
}

/**
 * Your MedianFinder object will be instantiated and called as such:
 * MedianFinder obj = new MedianFinder();
 * obj.addNum(num);
 * double param_2 = obj.findMedian();
 */