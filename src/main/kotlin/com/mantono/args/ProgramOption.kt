package com.mantono.args

import java.util.*

/**
 * Interface for program options that can be loaded through the command line
 * argument vector. The purpose of this interface, and its implementing classes,
 * is to simplify argument loading and validation with settings that are sent to
 * the main method at application launch.
 */
interface ProgramOption {
    /**
     * Short flag representation for this option that is combined with a single dash ("-").
     */
    val shortFlag: Char

    /**
     * The long flag representation for this option that is combined with a double dash ("--").
     */
    val longFlag: String

    /**
     * A description that describes what this option does.
     */
    val description: String

    /**
     * The default value.
     */
    val defaultValue: String

    /**
     * `true` if this option takes an argument, else `false`.
     */
    val takesArgument: Boolean

    /**
     * Checks whether a [String] matches any of the options for this
     * class.
     * @param input the [String] that should be checked whether it matched
     * or not.
     *
     * @return true if the input is equal to the short flag or the long flag
     * without the dash prefix.
     */
    fun matches(input: String): Boolean = input == "-$shortFlag" || input == "--$longFlag"

    /**
     * Validate a given value for the key.
     *
     * @return true if the argument is a valid value for the this options,
     * else false. Default implementation always return true.
     */
    fun validateValue(arg: String): Boolean = true

    /**
     * @return a text that is printed whenever a user calls the help flag, if
     * such a flag exists for the implementing class or enumerate.
     */
    fun helpDescription(): String = "-$shortFlag, --$longFlag\n\t$description\n"
}

inline fun <reified E> E.parseArgs(
    args: Array<String>,
    ignoreUnknown: Boolean = false
): CommandLineArguments where E: ProgramOption, E: Enum<E> =
    parseArgs(args, enumValues<E>().toList(), ignoreUnknown)


fun parseArgs(
    args: Array<String>,
    options: Collection<ProgramOption>,
    ignoreUnknown: Boolean = false
): CommandLineArguments = parseArgs(LinkedList(args.toList()), options, ignoreUnknown)

private tailrec fun parseArgs(
    args: Deque<String>,
    options: Collection<ProgramOption>,
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
    options: Collection<ProgramOption>,
    foundArgs: CommandLineArguments
): CommandLineArguments {
    val head: String = args.pop()
    val firstIsFlag: Boolean = head.startsWith("-")
    return if(firstIsFlag) {
        val option: ProgramOption = options.first { it.matches(head) }
        if(option.takesArgument) {
            val argument: String = args.pop()
            if(argument.startsWith("-")) {
                throw IllegalArgumentException("Got a flag ('$argument') when an argument was expected")
            }
            foundArgs.add(option.longFlag, argument)
        } else {
            foundArgs.add(option.longFlag, option.defaultValue)
        }
    } else {
        foundArgs.add(head)
    }
}

interface FlagOption {
    /**
     * Short flag representation for this option that is combined with a single dash ("-").
     */
    val shortFlag: Char

    /**
     * The long flag representation for this option that is combined with a double dash ("--").
     */
    val longFlag: String

    /**
     * A description that describes what this option does.
     */
    val description: String

    /**
     * @return a text that is printed whenever a user calls the help flag, if
     * such a flag exists for the implementing class or enumerate.
     */
    fun helpDescription(): String = "-$shortFlag, --$longFlag\n\t$description\n"
}

sealed class Flag: FlagOption {
    data class Long(
        override val longFlag: String,
        override val shortFlag: Char = longFlag.removePrefix("--").first(),
        override val description: String
    ): Flag()

    data class Double(
        override val longFlag: String,
        override val shortFlag: Char = longFlag.removePrefix("--").first(),
        override val description: String
    ): Flag()


    data class String(
        override val longFlag: String,
        override val shortFlag: Char = longFlag.removePrefix("--").first(),
        override val description: String
    ): Flag()


}