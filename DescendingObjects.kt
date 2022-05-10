package com.example.oo_project_2022
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF


abstract class DescendingObject () : RectF(){

    abstract var initialDescendingObjectSpeed : Float
    abstract var descendingObjectSpeed : Float
    open var descendingObjectPaint = Paint()
    open var isOnScreen = true


    abstract fun draw(canvas: Canvas)
    abstract fun update(interval: Double)
}