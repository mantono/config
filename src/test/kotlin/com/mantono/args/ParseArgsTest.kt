package com.mantono.args

import com.mantono.args.implementations.flag
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ParseArgsTest {

    private val flags: List<Flag> = listOf(
        flag<String>('f', "foo", "aaa", "Foo foo", parser = String::toString),
        flag<String>('b', "bar", "bbb", "Bar bar", parser = String::toString),
        flag<Long>('l', "limit", 67L, "Buzz", parser = String::toLong),
        flag('d', "bool", "Bii Bii")
    )

    @Test
    fun testParseArgs() {
        val args = arrayOf("1", "2", "3", "--foo", "alice", "--bool", "--bar", "bob", "-l", "20")
        val cli: CommandLineArguments = parseArgs(args, flags, false)

        cli.flags.apply {
            assertEquals("alice", get("foo"))
            assertEquals("bob", get("bar"))
            assertEquals(20L, get("limit"))
            assertEquals(true, get("bool"))
        }

        assertTrue("1" in cli.args)
        assertTrue("2" in cli.args)
        assertTrue("3" in cli.args)
    }
}