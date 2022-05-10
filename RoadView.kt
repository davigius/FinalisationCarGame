package com.example.oo_project_2022

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class RoadView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback, Runnable {

    lateinit var canvas: Canvas
    lateinit var thread: Thread
    lateinit var countDownTimer: CountDownTimer
    lateinit var myTimer: Timer


    val mainHandler = Handler(Looper.getMainLooper())
    val backgroundPaint = Paint()
    val textPaint = Paint()
    var screenWidth = 0f
    var screenHeight = 0f
    var drawing = false
    val activity = context as FragmentActivity
    val playerCar = PlayerCar(this, 0f, 0f, 0f, 0f)
    val listDescendingObjects = Collections.synchronizedList(ArrayList<DescendingObject>())
    //val ennemiCar = EnnemyCar(this, 0f, 0f, 0f, 0f, 0f)
    var dodgedCar = 0
    var bonusCatched = 0
    var timeLeft = 0.0
    var speedFactor : Float
    var gameOver = false
    var totalElapsedTime = 0.0
    var level = Level()

    var carIced = false  //permettra de décider de freeze la voiture


    var lastEnnemyTotalElapsedTime = 0.0
    var lastBonusTotalElapsedTime = 0.0
    var lastGlaceTotalElapsedTime = 0.0
    var lastFeuTotalElapsedTime = 0.0

    init {
        backgroundPaint.color = Color.WHITE
        textPaint.textSize= screenWidth/20
        textPaint.color = Color.BLACK
        timeLeft = level.lvlDuration[level.lvlcounter]
        speedFactor = level.speedLvlFactor[level.lvlcounter].toFloat()
      }

    fun pause() {
        drawing = false
        thread.join()
    }

    fun resume() {
        drawing = true
        thread = Thread(this)
        thread.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenWidth = w.toFloat()
        screenHeight = h.toFloat()

        playerCar.playerTopLeftX = (w/6f)
        playerCar.playerTopLeftY = (h*7/8f)
        playerCar.playerBottomRightY = (h*15/16f)
        playerCar.playerWidth =  (w/4f)
        playerCar.setRect()

        textPaint.setTextSize(w/20f)
        textPaint.isAntiAlias = true
    }


    fun createDescendingObject(descobj : DescendingObject){
        synchronized(listDescendingObjects) {
            listDescendingObjects.add(descobj)
        }
    }

    fun draw(){
        if (holder.surface.isValid){
            canvas = holder.lockCanvas()
            canvas.drawRect(0f,0f, canvas.width.toFloat(),
                canvas.height.toFloat(),backgroundPaint)
            val formatted = String.format("%.2f", timeLeft)
            canvas.drawText("Il reste $formatted secondes!", 30f, 50f, textPaint)
            canvas.drawText("Bonus attrappés : $bonusCatched ", 30f, 130f, textPaint)
            playerCar.draw(canvas)
            synchronized(listDescendingObjects) {
            for (i in listDescendingObjects) {
                i.draw(canvas)
                }
            }
            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun updatePositions(elapsedTimeMS: Double){
        val interval = elapsedTimeMS / 1000

        var rndintvl = Random.nextInt(10,50)/10.0
        if(totalElapsedTime - lastEnnemyTotalElapsedTime > rndintvl) {
            createDescendingObject(EnnemyCar(this, 0f, 0f))
            lastEnnemyTotalElapsedTime = totalElapsedTime;
        }
        rndintvl = Random.nextInt(10,50)/10.0
        if(totalElapsedTime - lastBonusTotalElapsedTime > rndintvl) {
            createDescendingObject(Bonus(this, 0f, 0f))
            lastBonusTotalElapsedTime = totalElapsedTime;
        }
        rndintvl = Random.nextInt(30,80)/10.0
        if(totalElapsedTime - lastGlaceTotalElapsedTime > rndintvl) {
            createDescendingObject(Glace(this, 0f, 0f))
            lastGlaceTotalElapsedTime = totalElapsedTime;
        }
        rndintvl = Random.nextInt(10,200)/10.0
        if(totalElapsedTime - lastFeuTotalElapsedTime > rndintvl) {
            createDescendingObject(Feu(this, 0f, 0f))
            lastFeuTotalElapsedTime = totalElapsedTime;
        }

        synchronized(listDescendingObjects) {
            //for (i in listEnnemyCar) {
            var it = listDescendingObjects.iterator()
            while ( it.hasNext() ) {
                val i = it.next()
                i.update(interval)
                if(!i.isOnScreen) {
                    if(i is EnnemyCar) {
                        dodgedCar += 1
                    }
                    it.remove()
                }
            }
        }

        timeLeft -= interval


        if (timeLeft<=0.0){
            timeLeft = 0.0
            gameOver = true
            drawing = false
            showGameOverDialog(R.string.win)
        }
    }

    override fun run() {
        var previousFrameTime = System.currentTimeMillis()
        while (drawing) {
            val currentTime = System.currentTimeMillis()
            var elapsedTimeMS: Double = (currentTime - previousFrameTime).toDouble()
            totalElapsedTime += elapsedTimeMS / 1000.0
            updatePositions(elapsedTimeMS)
            draw()
            previousFrameTime = currentTime
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val action = e.action
        if (action == MotionEvent.ACTION_DOWN  || action == MotionEvent.ACTION_MOVE) {
            playerCar.update(e)
        }
        return true
    }


    /*fun reduceTimeLeft() {
        timeLeft -= MISS_PENALTY
    }

    fun increaseTimeLeft() {
        timeLeft += HIT_REWARD
    }*/

    fun gameOver() {
        drawing = false
        showGameOverDialog(R.string.lose)
        gameOver = true
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {}

    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    fun showGameOverDialog(messageId: Int) {
        class GameResult: DialogFragment() {
            override fun onCreateDialog(bundle: Bundle?): Dialog {
                val builder = AlertDialog.Builder(getActivity())
                builder.setTitle(resources.getString(messageId))
                builder.setMessage(resources.getString(R.string.results_format, dodgedCar, totalElapsedTime, level.lvlcounter))
                builder.setPositiveButton(R.string.reset_game,
                    DialogInterface.OnClickListener { _, _->newGame()}
                )
                return builder.create()
            }
        }

        activity.runOnUiThread(
            Runnable {
                val ft = activity.supportFragmentManager.beginTransaction()
                val prev =
                    activity.supportFragmentManager.findFragmentByTag("dialog")
                if (prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                val gameResult = GameResult()
                gameResult.setCancelable(false)
                gameResult.show(ft,"dialog")
            }
        )
    }

    fun newGame() {
        level.changeLevel()
        playerCar.resetplayerCar()
        synchronized(listDescendingObjects) {
            listDescendingObjects.clear()
        }
        timeLeft = level.lvlDuration[level.lvlcounter]
        speedFactor = level.speedLvlFactor[level.lvlcounter].toFloat()
        totalElapsedTime = 0.0
        lastEnnemyTotalElapsedTime = 0.0
        bonusCatched = 0

        drawing = true
        if (gameOver) {
            gameOver = false
            thread = Thread(this)
            thread.start()
        }
    }





}