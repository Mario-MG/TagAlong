package com.hfad.tagalong.config

enum class Optionality(val value: Boolean) {
    ALL(false), ANY(true);

    companion object {
        fun forValue(value: Boolean): Optionality {
            return values().find { op -> op.value == value }!!
        }
    }
}