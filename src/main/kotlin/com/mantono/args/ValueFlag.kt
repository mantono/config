package com.mantono.args

interface ValueFlag<T: Any>: Flag {
    /**
     * The default value.
     */
    val defaultValue: T

    /**
     * Validate a given value for the key.
     *
     * @return true if the argument is a valid value for the this options,
     * else false. Default implementation always return true.
     */
    fun validateValue(arg: String): Boolean = true

    /**
     * Parse the given value on the command line to type T
     */
    fun parseValue(arg: String): T
}