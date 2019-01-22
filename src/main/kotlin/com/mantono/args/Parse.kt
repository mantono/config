package com.mantono.args

import java.util.*

fun parseArgs(
    args: Array<String>,
    options: Collection<Flag>,
    ignoreUnknown: Boolean = false
): CommandLineArguments = parseArgs(LinkedList(args.toList()), options, ignoreUnknown)

private tailrec fun parseArgs(
    args: Deque<String>,
    options: Collection<Flag>,
    ignoreUnknown: Boolean = false,
    foundArgs: CommandLineArguments = CommandLineArguments()
): CommandLineArguments {
    val extendedArgs: CommandLineArguments = parseAndReduce(args, options, foundArgs)

    return if(args.isNotEmpty()) {
        parseArgs(args, options, ignoreUnknown, extendedArgs)
    } else {
        extendedArgs
    }
}

private fun parseAndReduce(
    args: Deque<String>,
    options: Collection<Flag>,
    foundArgs: CommandLineArguments
): CommandLineArguments {
    val head: String = args.pop()
    val firstIsFlag: Boolean = head.startsWith("-")
    return if(firstIsFlag) {
        val flag: Flag = options.firstOrNull { it.matchesKey(head) }
            ?: throw java.lang.IllegalArgumentException("No match for '$head'")
        if(flag is ValueFlag<*>) {
            val argument: String = args.pop()
            if(argument.startsWith("-")) {
                throw IllegalArgumentException("Got a flag ('$argument') when an argument was expected")
            }
            if(!flag.validateValue(argument)) {
                throw java.lang.IllegalArgumentException("Invalid value '$argument' for flag $flag")
            }
            val transformed: Any = flag.parseValue(argument)
            foundArgs.add(flag.longFlag, transformed)
        } else {
            foundArgs.add(flag.longFlag, true)
        }
    } else {
        foundArgs.add(head)
    }
}