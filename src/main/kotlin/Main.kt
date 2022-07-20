import com.barbarus.prosper.Simulation

fun main(args: Array<String>) {
    println("Program arguments: ${args.joinToString()}")
    val simulation = Simulation()
    simulation.run(100)
}
