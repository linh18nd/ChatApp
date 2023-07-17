package com.kit18.chatapp.common.util

object AppConvert{
    fun convertTime(time: Long): String {
        val currentTime = System.currentTimeMillis()
        val timeDiff = currentTime - time
        val second = timeDiff / 1000
        val minute = second / 60
        val hour = minute / 60
        val day = hour / 24
        val month = day / 30
        val year = month / 12

        return when {
            year > 0 -> {
                "$year năm trước"
            }
            month > 0 -> {
                "$month tháng trước"
            }
            day > 0 -> {
                "$day ngày trước"
            }
            hour > 0 -> {
                "$hour giờ trước"
            }
            minute > 0 -> {
                "$minute phút trước"
            }
            else -> {
                "$second giây trước"
            }
        }
    }
}