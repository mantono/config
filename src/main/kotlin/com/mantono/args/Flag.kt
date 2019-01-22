package com.mantono.args

/**
 * Interface for program options that can be loaded through the command line
 * argument vector. The purpose of this interface, and its implementing classes,
 * is to simplify argument loading and validation with settings that are sent to
 * the main method at application launch.
 */
interface Flag {
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
     * Checks whether a [String] matches any of the options for this
     * class.
     * @param input the [String] that should be checked whether it matched
     * or not.
     *
     * @return true if the input is equal to the short flag or the long flag
     * without the dash prefix.
     */
    fun matchesKey(input: String): Boolean = input == "-$shortFlag" || input == "--$longFlag"

    /**
     * @return a text that is printed whenever a user calls the help flag, if
     * such a flag exists for the implementing class or enumerate.
     */
    fun helpDescription(): String = "-$shortFlag, --$longFlag\n\t$description\n"
}