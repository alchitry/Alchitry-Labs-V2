package com.alchitry.labs2

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Instant
import java.util.*

private val API_KEY: String? = System.getProperty("app.analytics.key", null)
private val APTABASE_HOST = "https://us.aptabase.com"

object Analytics {
    val enabled: Boolean get() = !API_KEY.isNullOrBlank() && Settings.sendAnalytics == true

    private val apiUrl = "$APTABASE_HOST/api/v0/events"
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val httpClient: HttpClient by lazy { HttpClient.newHttpClient() }
    private val json = Json { ignoreUnknownKeys = true }

    private var sessionId: String = generateSessionId()

    private val systemProps: JsonObject by lazy {
        buildJsonObject {
            put("osName", Env.os.name)
            put("osVersion", System.getProperty("os.version", "unknown"))
            put("locale", Locale.getDefault().toLanguageTag())
            put("appVersion", Env.version)
            put("sdkVersion", "alchitry-labs-custom@1.0.0")
            put("isDebug", Env.release == Env.Release.Unknown)
        }
    }

    private fun generateSessionId(): String {
        val epoch = Instant.now().epochSecond
        val random = (10000000..99999999).random()
        return "$epoch$random"
    }

    fun newSession() {
        sessionId = generateSessionId()
    }

    fun trackEvent(eventName: String, props: Map<String, JsonElement> = emptyMap()) {
        if (!enabled) return

        scope.launch {
            try {
                val event = buildJsonObject {
                    put("timestamp", Instant.now().toString())
                    put("sessionId", sessionId)
                    put("eventName", eventName)
                    put("systemProps", systemProps)
                    put("props", JsonObject(props))
                }

                val body = json.encodeToString(JsonArray(listOf(event)))

                val request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("App-Key", API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build()

                httpClient.send(request, HttpResponse.BodyHandlers.discarding())
            } catch (e: Exception) {
                Log.printlnError("Analytics error: ${e.message}")
            }
        }
    }
}
