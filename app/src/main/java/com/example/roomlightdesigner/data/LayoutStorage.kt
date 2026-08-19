package com.example.roomlightdesigner.data

import android.content.Context
import com.example.roomlightdesigner.domain.LightOrb
import com.example.roomlightdesigner.domain.LightPreset
import com.example.roomlightdesigner.domain.MAX_LIGHT_ORBS
import com.example.roomlightdesigner.domain.OrbPosition
import com.example.roomlightdesigner.domain.PersistedLayout
import org.json.JSONArray
import org.json.JSONObject

interface LayoutStorage {
    fun load(): PersistedLayout?
    fun save(layout: PersistedLayout)
    fun clear()
}

class SharedPreferencesLayoutStorage(context: Context) : LayoutStorage {
    private val preferences = context.getSharedPreferences("room_light_layout", Context.MODE_PRIVATE)

    override fun load(): PersistedLayout? = runCatching {
        val raw = preferences.getString(KEY_LAYOUT, null) ?: return null
        val root = JSONObject(raw)
        val storedSchemaVersion = root.optInt("schemaVersion", -1)
        if (storedSchemaVersion !in MIN_SUPPORTED_SCHEMA_VERSION..SCHEMA_VERSION) return null
        val array = root.optJSONArray("orbs") ?: JSONArray()
        val orbs = buildList {
            for (index in 0 until minOf(array.length(), MAX_LIGHT_ORBS)) {
                val item = array.optJSONObject(index) ?: continue
                val preset = runCatching { LightPreset.valueOf(item.getString("preset")) }.getOrNull() ?: continue
                add(
                    LightOrb(
                        id = item.optString("id").ifBlank { "restored-$index" },
                        preset = preset,
                        colorArgb = item.optString("colorArgb").toULongOrNull() ?: preset.defaultColor.value,
                        brightness = item.optDouble("brightness", preset.defaultBrightness.toDouble()).toFloat(),
                        radiusMeters = item.optDouble("radiusMeters", preset.defaultRadius.toDouble()).toFloat(),
                        position = OrbPosition(
                            item.optDouble("x", 0.0).toFloat(),
                            item.optDouble("y", 1.35).toFloat(),
                            item.optDouble("z", -1.8).toFloat(),
                        ),
                    ).sanitized(),
                )
            }
        }
        PersistedLayout(
            schemaVersion = storedSchemaVersion,
            orbs = orbs,
            layoutLocked = root.optBoolean("layoutLocked", false),
            // Schema 1/2 installs did not have starter content. Existing
            // non-empty layouts are already initialized and must be preserved.
            starterContentInitialized = root.optBoolean(
                "starterContentInitialized",
                orbs.isNotEmpty(),
            ),
        )
    }.getOrNull()

    override fun save(layout: PersistedLayout) {
        val array = JSONArray()
        layout.orbs.take(MAX_LIGHT_ORBS).forEach { orb ->
            array.put(JSONObject().apply {
                put("id", orb.id)
                put("preset", orb.preset.name)
                put("colorArgb", orb.colorArgb.toString())
                put("brightness", orb.brightness)
                put("radiusMeters", orb.radiusMeters)
                put("x", orb.position.x)
                put("y", orb.position.y)
                put("z", orb.position.z)
            })
        }
        preferences.edit().putString(
            KEY_LAYOUT,
            JSONObject().apply {
                put("schemaVersion", SCHEMA_VERSION)
                put("layoutLocked", layout.layoutLocked)
                put("starterContentInitialized", layout.starterContentInitialized)
                put("orbs", array)
            }.toString(),
        ).apply()
    }

    override fun clear() {
        preferences.edit().remove(KEY_LAYOUT).apply()
    }

    private companion object {
        const val KEY_LAYOUT = "latest_layout"
        const val MIN_SUPPORTED_SCHEMA_VERSION = 1
        const val SCHEMA_VERSION = 4
    }
}

class InMemoryLayoutStorage(initial: PersistedLayout? = null) : LayoutStorage {
    var value: PersistedLayout? = initial
        private set

    override fun load(): PersistedLayout? = value
    override fun save(layout: PersistedLayout) { value = layout }
    override fun clear() { value = null }
}
