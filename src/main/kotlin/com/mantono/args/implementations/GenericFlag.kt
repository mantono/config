package com.mantono.args.implementations

import com.mantono.args.Flag
import com.mantono.args.ValueFlag

data class GenericFlag<T: Any>(
    override val shortFlag: Char,
    override val longFlag: String,
    override val defaultValue: T,
    override val description: String = "",
    private val match: Regex? = null,
    private val parser: (String) -> T
): ValueFlag<T> {
    override fun validateValue(arg: String): Boolean = match?.matches(arg) ?: true
    override fun parseValue(arg: String): T = parser(arg)
}

fun <T: Any> flag(
    shortFlag: Char,
    longFlag: String,
    defaultValue: T,
    description: String = "",
    match: Regex? = null,
    parser: (String) -> T
): ValueFlag<T> =
    GenericFlag<T>(shortFlag, longFlag, defaultValue, description, match, parser)

fun flag(
    shortFlag: Char,
    longFlag: String,
    description: String = ""
): Flag {
    return object: Flag {
        override val shortFlag: Char = shortFlag
        override val longFlag: String = longFlag
        override val description: String = description
    }
}