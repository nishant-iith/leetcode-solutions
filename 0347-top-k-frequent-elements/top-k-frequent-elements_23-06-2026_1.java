class Solution {
    public int[] topKFrequent(int[] nums, int k) {

        // Step 1: count how many times each number appears
        Map<Integer,Integer> hm=new HashMap<>();
        for(int num : nums)
        {
            hm.put(num,hm.getOrDefault(num,0)+1);
        }
        
        // Step 2: MIN-heap ordered by frequency (least frequent stays on top)
        PriorityQueue<Integer> pq=new PriorityQueue<>(
            (a,b) -> hm.get(a) - hm.get(b)
        );
        
        // Step 3: keep only the k most frequent in the heap
        for(int num : hm.keySet())
        {
            pq.offer(num);
            if(pq.size() > k)
            {
               pq.poll();   // heap too big -> remove the LEAST frequent (top of min-heap)
            }
        }
        
        // Step 4: heap now holds exactly the top k -> copy into result
        int res[]=new int[k];
        for(int i=0;i<k;i++)
        {
            res[i]=pq.poll();
        }

        return res;
    }
}