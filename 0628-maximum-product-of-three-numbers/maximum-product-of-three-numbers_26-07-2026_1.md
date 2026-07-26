# Submission details for maximum-product-of-three-numbers

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 11 ms (Better than 81.04% of submissions)
- **Memory:** 47.1 MB (Better than 97.34% of submissions)

## Solution Code
```java
class Solution {
    public int maximumProduct(int[] A) {
        int a = -1001, b = a, c = b;
        int x =  1001, y = x;

        for (int n : A) {
            int pa = a, pb = b, px = x;
            
            a = Math.max(a, n);
            b = Math.max(b, Math.min(pa, n));
            c = Math.max(c, Math.min(pb, n));
            
            x = Math.min(x, n);
            y = Math.min(y, Math.max(px, n));
        }

        return Math.max(a * b * c, a * x * y);
    }
}
```

## Detailed Explanation
**1. Algorithm analysis**

The product of any three numbers in an array can be formed in only two ways that matter for the maximum:

* **Three largest values** – `max1 * max2 * max3`.  
  This is the answer when the array contains mostly positive numbers (or when the three biggest numbers are all positive).

* **Two smallest (most negative) values together with the largest value** – `max1 * min1 * min2`.  
  Two large‑magnitude negative numbers turn positive when multiplied, so the best product may involve the two smallest (most negative) numbers and the single largest positive number.

Therefore we only need:

| Variable | Meaning |
|----------|---------|
| `a`      | the **largest** value seen so far (`max1`) |
| `b`      | the **second largest** value seen so far (`max2`) |
| `c`      | the **third largest** value seen so far (`max3`) |
| `x`      | the **smallest** value seen so far (`min1`) |
| `y`      | the **second smallest** value seen so far (`min2`) |

The answer is  

```
max( a * b * c ,  a * x * y )
```

The code keeps these five extremes while scanning the array once.  
The temporary variables `pa, pb, px` hold the *old* values of `a, b, x` **before** they are overwritten, because the update of `b, c, y` must use the values that existed **before** the current element is considered.

The update formulas are:

```
a  = max(a, n)                                 // new max1
b  = max(b, min(pa, n))                        // new max2 (larger of old max1 or n)
c  = max(c, min(pb, n))                        // new max3 (larger of old max2 or n)

x  = min(x, n)                                 // new min1
y  = min(y, max(px, n))                        // new min2 (larger of old min1 or n)
```

All updates are constant‑time, so the whole algorithm runs in a single linear pass.

---

**2. Step‑by‑step walkthrough (DRY‑run) – sample `[1, 2, 3]`**

We start with the sentinel values that are outside the possible input range (the problem constraints are `-1000 … 1000`).

| Variable | Initial value |
|----------|----------------|
| `a`      | -1001 |
| `b`      | -1001 |
| `c`      | -1001 |
| `x`      |  1001 |
| `y`      |  1001 |

We will walk through each element, showing the *pre‑update* values (`pa, pb, px`) and the *post‑update* values.

| Step | `n` | `pa` (=old `a`) | `pb` (=old `b`) | `px` (=old `x`) | **Update** (new values) | `a` | `b` | `c` | `x` | `y` |
|------|-----|----------------|----------------|----------------|--------------------------|-----|-----|-----|-----|-----|
| 0    | –   | –              | –              | –              | –                        | -1001 | -1001 | -1001 | 1001 | 1001 |
| 1    | 1   | -1001 | -1001 | 1001 | `a = max(-1001,1)=1`<br>`b = max(-1001, min(-1001,1)) = -1001`<br>`c = max(-1001, min(-1001,1)) = -1001`<br>`x = min(1001,1)=1`<br>`y = min(1001, max(1001,1)) = 1001` | 1   | -1001 | -1001 | 1   | 1001 |
| 2    | 2   | 1   | -1001 | 1   | `a = max(1,2)=2`<br>`b = max(-1001, min(1,2)) = max(-1001,1)=1`<br>`c = max(-1001, min(-1001,2)) = -1001`<br>`x = min(1,2)=1`<br>`y = min(1001, max(1,2)) = 2` | 2   | 1   | -1001 | 1   | 2   |
| 3    | 3   | 2   | 1   | 1   | `a = max(2,3)=3`<br>`b = max(1, min(2,3)) = max(1,2)=2`<br>`c = max(-1001, min(1,3)) = max(-1001,1)=1`<br>`x = min(1,3)=1`<br>`y = min(2, max(1,3)) = min(2,3)=2` | 3   | 2   | 1   | 1   | 2   |

**Final values**

```
a = 3   (max1)
b = 2   (max2)
c = 1   (max3)
x = 1   (min1)
y = 2   (min2)
```

Now compute the two candidate products:

* `a * b * c = 3 * 2 * 1 = 6`
* `a * x * y = 3 * 1 * 2 = 6`

`max(6, 6) = 6`, which is the correct answer for `[1,2,3]`.

---

**3. Complexity analysis**

* **Time complexity** – `O(n)`  
  The algorithm makes a single pass over the `n` elements, performing a constant amount of work (a handful of comparisons and assignments) per element.

* **Space complexity** – `O(1)`  
  Only a fixed number of integer variables (`a, b, c, x, y` plus three temporaries) are stored, independent of the input size.

Thus the solution is optimal: linear time with constant extra space.
