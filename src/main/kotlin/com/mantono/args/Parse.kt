package com.mantono.args

import java.util.*

fun parseArgs(
    args: Array<String>,
    options: Collection<Flag>,
    ignoreUnknown: Boolean = false
): CommandLineArguments {
    val defaults: Map<String, Any> = options.filter { it is ValueFlag<*> }
        .map { it as ValueFlag<*> }
        .map { it.longFlag to it.defaultValue }
        .toMap()
    val defaultBooleans: Map<String, Boolean> = options.filter { it !is ValueFlag<*> }
        .map { it.longFlag to false }
        .toMap()
    val parsed: CommandLineArguments = parseArgs(LinkedList(args.toList()), options, ignoreUnknown)
    return parsed.copy(flags = defaults + defaultBooleans + parsed.flags)
}

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
        val (flag: Flag, value: Any) = getFlagAndValue(head, args, options)
        foundArgs.add(flag.longFlag, value)
    } else {
        foundArgs.add(head)
    }
}

private fun getFlagAndValue(key: String, args: Deque<String>, options: Collection<Flag>): Pair<Flag, Any> {
    val flag: Flag = options.firstOrNull { it.matchesKey(key) }
        ?: throw java.lang.IllegalArgumentException("No match for '$key'")
    return if(flag is ValueFlag<*>) {
        val argument: String = args.pop()
        if(argument.startsWith("-")) {
            throw IllegalArgumentException("Got a flag ('$argument') when an argument was expected")
        }
        if(!flag.validateValue(argument)) {
            throw java.lang.IllegalArgumentException("Invalid value '$argument' for flag $flag")
        }
        flag to flag.parseValue(argument)
    } else {
        flag to true
    }
}