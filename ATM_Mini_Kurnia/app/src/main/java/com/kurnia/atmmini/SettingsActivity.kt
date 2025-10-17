package com.kurnia.atmmini

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnSave = findViewById<Button>(R.id.btnSaveSettings)
        btnSave.setOnClickListener {
            // For demo: save simple prefs
            val rbUtf = findViewById<RadioButton>(R.id.rbUtf8)
            val rbCp = findViewById<RadioButton>(R.id.rbCp437)
            val prefs = getSharedPreferences("atm_prefs", MODE_PRIVATE)
            prefs.edit().putString("encoding", if (rbUtf.isChecked) "UTF-8" else "CP437").apply()
            finish()
        }
    }
}
