# Submission details for find-the-maximum-number-of-elements-in-subset

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 48 ms (Better than 95.35% of submissions)
- **Memory:** 65.3 MB (Better than 75.58% of submissions)

## Solution Code
```java
class Solution {
    private static final int MAX_BASE = 31622;

    public int maximumLength(int[] nums) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums)
            freq.merge(n, 1, Integer::sum);

        int one = freq.getOrDefault(1, 0);
        int res = (one - 1) | 1;
        freq.remove(1);

        for (int f : freq.keySet()) {
            int sq = (int) Math.sqrt(f);
            if (sq * sq == f && freq.getOrDefault(sq, 0) > 1) continue;

            int n = 0;
            int x = f;

            while (x <= MAX_BASE && freq.containsKey(x) && freq.get(x) > 1) {
                n += 2;
                x *= x;
            }

            res = Math.max(res, n + (freq.containsKey(x) ? 1 : -1));
        }

        return res;
    }
}
```

## Detailed Explanation
User Safety: safe
