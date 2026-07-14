# Submission details for kth-largest-element-in-a-stream

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 22 ms (Better than 85.70% of submissions)
- **Memory:** 52.6 MB (Better than 6.31% of submissions)

## Solution Code
```java
class KthLargest {
    private int k;
    private PriorityQueue<Integer> minHeap;

    public KthLargest(int k, int[] nums) {
        this.k = k;
        minHeap = new PriorityQueue<>(k);
        for (int num : nums) {
            if (minHeap.size() < k) {
                minHeap.offer(num);
            } else if (num > minHeap.peek()) {
                minHeap.offer(num);
                if (minHeap.size() > k) {
                    minHeap.poll();
                }
            }
        }
    }

    public int add(int val) {
        if (minHeap.size() < k) {
            minHeap.offer(val);
        } else if (val > minHeap.peek()) {
            minHeap.offer(val);
            minHeap.poll();
        }
        return minHeap.peek();
    }
}

/**
 * Your KthLargest object will be instantiated and called as such:
 * KthLargest obj = new KthLargest(k, nums);
 * int param_1 = obj.add(val);
 */
```

## Detailed Explanation
User Safety: safe
