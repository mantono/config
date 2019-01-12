package com.mantono.config

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config as TypeSafeConfig

interface Config {
	operator fun get(key: String, default: Any? = null): String
}

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

	override fun get(key: String, default: Any?): String =
		System.getProperty(key)
			?: System.getenv(key)
			?: default?.toString()
			?: throw RuntimeException("No value found for '$key' and no default value given")
}