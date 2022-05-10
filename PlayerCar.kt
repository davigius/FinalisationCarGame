package com.example.oo_project_2022

import android.graphics.*
import android.view.MotionEvent

class PlayerCar(var view: RoadView, var playerTopLeftX: Float, var playerTopLeftY: Float, var playerWidth: Float, var playerBottomRightY: Float) {

    var playerCar = RectF()
    val playerCarPaint = Paint()
    var playerCarCrash = false
    var playerCarBonus = false

    val image = view.getResources().getIdentifier("car1", "drawable", view.getContext().getPackageName());
    val bmp = BitmapFactory.decodeResource(view.getResources(), image);

    fun draw(canvas: Canvas){
        playerCarPaint.color = Color.LTGRAY
        canvas.drawRect(playerCar, playerCarPaint)
        //canvas.drawBitmap(bmp, null, playerCar, null)

    }

    fun setRect(){
        playerCar.set(playerTopLeftX, playerTopLeftY , playerTopLeftX + playerWidth, playerBottomRightY)
    }

    fun update(event: MotionEvent) {
        val touchPoint = Point(event.x.toInt(), event.y.toInt())
        val offset = touchPoint.x.toFloat() - (playerCar.left+playerWidth/2)
        if(!view.carIced) //condition un peu inutile
            playerCar.offset(offset,0f)

    }


    fun resetplayerCar() {
        playerCar.set(playerTopLeftX, playerTopLeftY , playerTopLeftX + playerWidth, playerBottomRightY)

    }
}

