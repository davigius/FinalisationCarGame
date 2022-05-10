package com.example.oo_project_2022

import android.graphics.*

import kotlin.random.Random


class Bonus(var view: RoadView, override var initialDescendingObjectSpeed: Float,
                override var descendingObjectSpeed: Float) : DescendingObject() {

    val w = view.screenWidth.toInt()
    val h = view.screenHeight.toInt()
    var bonusOnScreen = true
    var bonusWidth = (w/6f)

    val image = view.getResources().getIdentifier("bonus", "drawable", view.getContext().getPackageName());
    val bmp = BitmapFactory.decodeResource(view.getResources(), image);

    init {
        if(w!=0) {
            left = Random.nextInt(0, (w.toInt() - bonusWidth.toInt())).toFloat()
            top = (h*1/8f)
            right = left + bonusWidth
            bottom = (w*4/24f)
            initialDescendingObjectSpeed = (h/5f)
            descendingObjectSpeed = initialDescendingObjectSpeed*view.speedFactor

        }
    }

    override fun draw(canvas: Canvas) {
        if (isOnScreen) {
            descendingObjectPaint.color = Color.GREEN
            canvas.drawRect(this, descendingObjectPaint)
            //canvas.drawBitmap(bmp, null, this, null)
        }
    }
/*
    fun setRect() {

        bonusCar.set(left, top,
            left + bonusWidth, bottom)
        speedbonusCar = initialbonusSpeed
    }*/

    override fun update(interval: Double) {
        var up = (interval * descendingObjectSpeed).toFloat()
        this.offset(0f, up)
        if (top > view.screenHeight) { // condition 1 pas vraiment nécessaire car voiture tjrs vitesse pos => y augmente
            //resetbonusCar()
            isOnScreen = false

        }


        else if(RectF.intersects(this, view.playerCar.playerCar)){
            isOnScreen = false
            view.bonusCatched += 1
            view.playerCar.playerCarBonus = true
        }

    }
//
//    fun resetbonusCar() {
//
//        descendingObjectSpeed = initialDescendingObjectSpeed*(2f*view.dodgedCar.toFloat()/10f)
//        left = Random.nextInt(0, (w.toInt()- bonusWidth.toInt())).toFloat()
//        this.set(left, top,
//            left + bonusWidth, bottom)
//        view.playerCar.playerCarBonus = false
//
//    }

}

