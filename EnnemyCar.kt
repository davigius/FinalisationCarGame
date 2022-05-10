package com.example.oo_project_2022

import android.graphics.*

import kotlin.random.Random


class EnnemyCar(var view: RoadView, override var initialDescendingObjectSpeed: Float,
                override var descendingObjectSpeed: Float) : DescendingObject() {

    val w = view.screenWidth.toInt()
    val h = view.screenHeight.toInt()
    var ennemyCarOnScreen = true
    var ennemyWidth = (w/10f)

    val image = view.getResources().getIdentifier("policecar", "drawable", view.getContext().getPackageName());
    val bmp = BitmapFactory.decodeResource(view.getResources(), image);

    init {
        if(w!=0) {
            left = Random.nextInt(0, (w.toInt()- ennemyWidth.toInt())).toFloat()
            top = (h*1/8f)
            right = left + ennemyWidth
            bottom = (h*3/16f)
            initialDescendingObjectSpeed = (h/6f)
            descendingObjectSpeed = initialDescendingObjectSpeed*view.speedFactor

        }
    }

    override fun draw(canvas: Canvas) {
        if (isOnScreen) {
            descendingObjectPaint.color = Color.BLACK
            canvas.drawRect(this, descendingObjectPaint)
           // canvas.drawBitmap(bmp, null, this, null)
        }
    }
/*
    fun setRect() {

        ennemyCar.set(left, top,
            left + ennemyWidth, bottom)
        speedEnnemyCar = initialEnnemySpeed
    }*/

    override fun update(interval: Double) {
        var up = (interval * descendingObjectSpeed).toFloat()
        this.offset(0f, up)
        if (top > view.screenHeight) { // condition 1 pas vraiment nécessaire car voiture tjrs vitesse pos => y augmente
            //resetEnnemyCar()
            isOnScreen = false
        //    view.dodgedCar +=1
        }

        else if (RectF.intersects(this, view.playerCar.playerCar) && view.playerCar.playerCarBonus == false){
            view.gameOver()
        }

        else if(RectF.intersects(this, view.playerCar.playerCar) && view.playerCar.playerCarBonus == true){
            isOnScreen = false
//            resetEnnemyCar()
            view.dodgedCar += 1

        }

    }

    fun resetEnnemyCar() {

        descendingObjectSpeed = initialDescendingObjectSpeed*(2f*view.dodgedCar.toFloat()/10f)
        left = Random.nextInt(0, (w.toInt()- ennemyWidth.toInt())).toFloat()
        this.set(left, top,
            left + ennemyWidth, bottom)
        view.playerCar.playerCarBonus = false

    }

}

