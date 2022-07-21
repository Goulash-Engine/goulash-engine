package com.barbarus.prosper

fun main(args: Array<String>) {
    println("Program arguments: ${args.joinToString()}")
    val simulation = Simulation()
    simulation.run(3000, 100, true)
}
