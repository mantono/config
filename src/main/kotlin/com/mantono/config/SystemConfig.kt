package com.mantono.config

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config as TypeSafeConfig


object SystemConfig: Config {

    init {
        loadProperties()
    }

    private fun loadProperties() {
        val appEnv: String = System.getProperty("APP_ENV") ?: System.getenv("APP_ENV") ?: "development"
        val env = Environment(appEnv)
        val config = ConfigFactory.load().forEnvironment(env)
        config.entrySet().asSequence()
            .map { it.key to it.value.unwrapped().toString() }
            .forEach { System.setProperty(it.first, it.second) }
    }

    private fun TypeSafeConfig.forEnvironment(env: Environment): TypeSafeConfig {
        val envKeyPrefix: String = env.name.toLowerCase()
        return if(hasPath(envKeyPrefix)) {
            this
                .getConfig(env.name.toLowerCase())
                .withFallback(this)
        } else {
            this
        }
    }

    override fun get(key: String, default: Any): String = getOrNull(key) ?: default.toString()

    override fun get(key: String): String = getOrNull(key)
        ?: throw RuntimeException("No value found for property '$key' and no default value given")

    override fun getOrNull(key: String): String? = System.getProperty(key) ?: System.getenv(key)
}