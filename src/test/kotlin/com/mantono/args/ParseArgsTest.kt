package com.mantono.args

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ParseArgsTest {

    private enum class Options(
        override val defaultValue: String,
        override val shortFlag: Char,
        override val longFlag: String,
        override val description: String,
        override val takesArgument: Boolean
    ): ProgramOption {
        Foo("x", 'f', "foo", "Foo foo", true),
        Bar("x", 'b', "bar", "Bar bar", true),
        Dee("x", 'd', "dee", "Dee dee", true),
        Bool("x", 't', "bool", "Bii Bii", false);
    }

    @Test
    fun testParseArgs() {
        val args = arrayOf("1", "2", "3", "--foo", "alice", "-t", "--bool", "--bar", "bob", "-d", "20")
        val cli: CommandLineArguments = parseArgs(args, Options.values().toList(), false)

        cli.flags.apply {
            assertEquals("alice", get("foo"))
            assertEquals("bob", get("bar"))
            assertEquals("20", get("dee"))
            assertEquals(true, get("bool"))
        }

        assertTrue("1" in cli.args)
        assertTrue("2" in cli.args)
        assertTrue("3" in cli.args)
    }
}