package com.example.oo_project_2022

import android.graphics.*
import kotlin.random.Random


class Glace(var view: RoadView, override var initialDescendingObjectSpeed: Float,
            override var descendingObjectSpeed: Float) : DescendingObject() {

    val w = view.screenWidth.toInt()
    val h = view.screenHeight.toInt()
    var glaceOnScreen = true
    var glaceWidth = (w/10f)


    val image = view.getResources().getIdentifier("snowflake", "drawable", view.getContext().getPackageName());
    val bmp = BitmapFactory.decodeResource(view.getResources(), image);

    init {
        if(w!=0) {
            left = Random.nextInt(0, (w.toInt() - glaceWidth.toInt())).toFloat()
            top = (h*1/8f)
            right = left + glaceWidth
            bottom = (w*4/24f)
            initialDescendingObjectSpeed = (h/5f)
            descendingObjectSpeed = initialDescendingObjectSpeed*view.speedFactor

        }
    }

    override fun draw(canvas: Canvas) {
        if (isOnScreen) {
            descendingObjectPaint.color = Color.BLUE
            canvas.drawRect(this, descendingObjectPaint)
            //canvas.drawBitmap(bmp, null, this, null)
        }
    }
/*
    fun setRect() {

        glaceCar.set(left, top,
            left + glaceWidth, bottom)
        speedglaceCar = initialglaceSpeed
    }*/

    override fun update(interval: Double) {
        var up = (interval * descendingObjectSpeed).toFloat()
        this.offset(0f, up)
        if (top > view.screenHeight) { // condition 1 pas vraiment nécessaire car voiture tjrs vitesse pos => y augmente
            //resetglaceCar()
            isOnScreen = false

        }

        else if(RectF.intersects(this, view.playerCar.playerCar)){
            isOnScreen = false
            view.carIced = true

        }

    }
//
//    fun resetglaceCar() {
//
//        descendingObjectSpeed = initialDescendingObjectSpeed*(2f*view.dodgedCar.toFloat()/10f)
//        left = Random.nextInt(0, (w.toInt()- glaceWidth.toInt())).toFloat()
//        this.set(left, top,
//            left + glaceWidth, bottom)
//        view.playerCar.playerCarglace = false
//
//    }

}

