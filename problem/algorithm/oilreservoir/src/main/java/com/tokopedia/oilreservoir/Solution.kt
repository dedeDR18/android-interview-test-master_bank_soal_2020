package com.tokopedia.oilreservoir

import android.util.Log

/**
 * Created by fwidjaja on 2019-09-24.
 */
object Solution {
    fun collectOil(height: IntArray): Int {
        // TODO, return the amount of oil blocks that could be collected
        // below is stub
        return findOil(height.size, height)
    }


    private fun findOil(n: Int, arr: IntArray): Int {
        val left = IntArray(n)
        val right = IntArray(n)

        var oil = 0

        left[0] = arr.get(0)
        for (i in 1 until n) left[i] = Math.max(left[i - 1], arr.get(i))

        right[n - 1] = arr.get(n - 1)
        for (i in n - 2 downTo 0) right[i] = Math.max(right[i + 1], arr.get(i))

        for (i in 0 until n) oil += Math.min(left[i], right[i]) - arr.get(i)
        Log.d("TAG", "jumlah = $oil")
        return oil
    }
}
