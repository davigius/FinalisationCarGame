package com.example.oo_project_2022

class Level() {

    var lvlDuration = listOf(20.0, 40.0, 60.0, 80.0)
    var speedLvlFactor = listOf(1.4, 1.7, 2.0, 2.5)
    var lvlcounter : Int = 0

    fun changeLevel() {
        lvlcounter +=1
    }
}