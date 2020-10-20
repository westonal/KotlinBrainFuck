package io.github.westonal.brainfuck

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.PrintWriter
import java.io.StringWriter

class BrainFuckTests {

    @Test
    fun `can append command`() {
        assertEquals("+", n.append(Command.Add).toString())
    }

    @Test
    fun `can append 2 commands`() {
        assertEquals("+-", n.append(Command.Add).append(Command.Subtract).toString())
    }

    @Test
    fun `can append a loop`() {
        assertEquals(
            "+[.]", n.append(Command.Add)
                .append(Command.Loop(o)).toString()
        )
    }

    @Test
    fun `add command`() {
        assertEquals("+", (n + n).toString())
    }

    @Test
    fun `add commands preserve left and right programs`() {
        assertEquals("<+>", (l + r).toString())
    }

    @Test
    fun `multiple plus symbols`() {
        assertEquals("<++>", (n.append(Command.MoveLeft) + + n.append(Command.MoveRight)).toString())
    }

    @Test
    fun `8 plus symbols`() {
        assertEquals("++++++++", (+ + + + + + + + n).toString())
    }

    @Test
    fun `loops using array syntax`() {
        assertEquals(">[-]", (n.append(Command.MoveRight) [ n.append(Command.Subtract) ] ).toString())
    }

    val helloWorld: Program =
        + + + + + + + + n [ r + + + + n [ r + + r + + + r + + + r +
        l l l l - n ] r + r + r - r r + n [ l ] l - n ] r r o r - -
        - o + + + + + + + o o + + + o r r o l - o l o + + + o - - -
        - - - o - - - - - - - - o r r + o r + + o

    @Test
    fun `can build hello world program structure`() {
        assertEquals(
            "++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.",
            helloWorld.toString()
        )
    }

    @Test
    fun `run hello world`() {
        val stringWriter = StringWriter()

        helloWorld(Tape(), writer = PrintWriter(stringWriter))

        assertEquals("Hello World!\n", stringWriter.toString())
    }
}
