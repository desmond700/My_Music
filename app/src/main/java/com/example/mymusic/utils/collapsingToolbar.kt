package com.example.mymusic.utils

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy

@Stable
interface ToolbarState {
    val offset: Float
    val height: Float
    val progress: Float
    val scrollValue: Int
}
abstract class ToolbarScrollFlagState(heightRange: IntRange, scrollValue: Int): ToolbarState {
    protected var scrollFlagValue by mutableStateOf(
        value = scrollValue.coerceAtLeast(0),
        policy = structuralEqualityPolicy()
    )
    private val rangeDifference = heightRange.last - heightRange.first
    protected val minHeight = heightRange.first
    protected val maxHeight = heightRange.last

    init {
        require(heightRange.first >= 0 && heightRange.last >= heightRange.first) {
            "The lowest height value must be >= 0 and the highest height value must be >= the lowest value."
        }
    }

    final override val progress: Float
        get() = 1 - (maxHeight - height) / rangeDifference
}

class ToolbarExitUntilCollapsedState(
    heightRange: IntRange,
    scrollValue: Int = 0,
) : ToolbarScrollFlagState(heightRange, scrollValue) {

    override val offset: Float = 0f
    override val height: Float
        get() = (maxHeight.toFloat() - scrollValue).coerceIn(minHeight.toFloat(), maxHeight.toFloat())

    override var scrollValue: Int
        get() = scrollFlagValue
        set(value) {
            scrollFlagValue = value.coerceAtLeast(0)
        }

    companion object {
        val Saver = run {
            val minHeightKey = "MinHeight"
            val maxHeightKey = "MaxHeight"
            val scrollValueKey = "ScrollValue"

            mapSaver(
                save = {
                    mapOf(
                        minHeightKey to it.minHeight,
                        maxHeightKey to it.maxHeight,
                        scrollValueKey to it.scrollValue
                    )
                },
                restore = {
                    ToolbarExitUntilCollapsedState(
                        heightRange = (it[minHeightKey] as Int)..(it[maxHeightKey] as Int),
                        scrollValue = it[scrollValueKey] as Int
                    )
                }
            )
        }
    }
}
