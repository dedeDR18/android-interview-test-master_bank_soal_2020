package com.tokopedia.climbingstairs

object Solution {
//        // TODO, return in how many distinct ways can you climb to the top. Each time you can either climb 1 or 2 steps.
//        // 1 <= n < 90


    private fun fib(n: Int): Int {
        return if (n <= 1) n else fib(n - 1) + fib(n - 2)
    }

    fun climbStairs(n: Int): Long {
        return fib(n + 1).toLong()
    }
}
