package com.example.oo_project_2022

import android.graphics.*

import kotlin.random.Random


class Feu(var view: RoadView, override var initialDescendingObjectSpeed: Float,
            override var descendingObjectSpeed: Float) : DescendingObject() {

    val w = view.screenWidth.toInt()
    val h = view.screenHeight.toInt()
    var feuOnScreen = true
    var feuWidth = (w/3f)


    val image = view.getResources().getIdentifier("snowflake", "drawable", view.getContext().getPackageName());
    val bmp = BitmapFactory.decodeResource(view.getResources(), image);

    init {
        if(w!=0) {
            left = Random.nextInt(0, (w.toInt() - feuWidth.toInt())).toFloat()
            top = (h*1/8f)
            right = left + feuWidth
            bottom = (w*7/24f)
            initialDescendingObjectSpeed = (h/5f)
            descendingObjectSpeed = initialDescendingObjectSpeed*view.speedFactor

        }
    }

    override fun draw(canvas: Canvas) {
        if (isOnScreen) {
            descendingObjectPaint.color = Color.RED
            canvas.drawRect(this, descendingObjectPaint)
            //canvas.drawBitmap(bmp, null, this, null)
        }
    }
/*
    fun setRect() {

        feuCar.set(left, top,
            left + feuWidth, bottom)
        speedfeuCar = initialfeuSpeed
    }*/

    override fun update(interval: Double) {
        var up = (interval * descendingObjectSpeed).toFloat()
        this.offset(0f, up)
        if (top > view.screenHeight) { // condition 1 pas vraiment nécessaire car voiture tjrs vitesse pos => y augmente
            //resetfeuCar()
            isOnScreen = false

        }

        else if(RectF.intersects(this, view.playerCar.playerCar)){
            isOnScreen = false
            view.carIced = false

        }

    }
//
//    fun resetfeuCar() {
//
//        descendingObjectSpeed = initialDescendingObjectSpeed*(2f*view.dodgedCar.toFloat()/10f)
//        left = Random.nextInt(0, (w.toInt()- feuWidth.toInt())).toFloat()
//        this.set(left, top,
//            left + feuWidth, bottom)
//        view.playerCar.playerCarfeu = false
//
//    }

}

