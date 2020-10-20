package io.github.westonal.brainfuck

import java.io.PrintWriter

val n = Program(emptyList())

val o = n.append(Command.Print)
val r = n.append(Command.MoveRight)
val l = n.append(Command.MoveLeft)

infix fun Program.l(program: Program) = append(Command.MoveLeft).append(program)
infix fun Program.r(program: Program) = append(Command.MoveRight).append(program)
infix fun Program.o(program: Program) = append(Command.Print).append(program)

operator fun Program.unaryPlus() = n.append(Command.Add).append(this)
operator fun Program.plus(program: Program) = append(Command.Add).append(program)

operator fun Program.unaryMinus() = n.append(Command.Subtract).append(this)
operator fun Program.minus(program: Program) = append(Command.Subtract).append(program)

operator fun Program.get(loopInner: Program) = append(Command.Loop(loopInner))

class Tape(size: Int = 30000) {
    private val data = Array(size) { 0 }
    private var pointer = 0;

    fun add() {
        data[pointer]++
    }

    fun subtract() {
        data[pointer]--
    }

    fun moveRight() {
        pointer++
    }

    fun moveLeft() {
        pointer--
    }

    fun current() = data[pointer]

    fun currentAsChar() = current().toChar()
}

operator fun Program.invoke(tape: Tape = Tape(), writer: PrintWriter = PrintWriter(System.out.writer())) {
    for (command in commands) {
        when (command) {
            is Command.Add -> tape.add()
            is Command.Subtract -> tape.subtract()
            is Command.MoveRight -> tape.moveRight()
            is Command.MoveLeft -> tape.moveLeft()
            is Command.Print -> writer.print(tape.currentAsChar())
            is Command.Loop -> while (tape.current() != 0) {
                command.loopContent(tape, writer)
            }
        }
    }
    writer.flush()
}

class Program(val commands: List<Command> = emptyList()) {
    override fun toString(): String {
        return commands.joinToString(separator = "")
    }
}

sealed class Command {
    object Add : Command() {
        override fun toString() = "+"
    }

    object Subtract : Command() {
        override fun toString() = "-"
    }

    object MoveRight : Command() {
        override fun toString() = ">"
    }

    object MoveLeft : Command() {
        override fun toString() = "<"
    }

    object Print : Command() {
        override fun toString() = "."
    }

    class Loop(val loopContent: Program) : Command() {
        override fun toString() = "[$loopContent]"
    }
}

fun Program.append(command: Command) = Program(commands + command)
fun Program.append(program: Program) = Program(commands + program.commands)
