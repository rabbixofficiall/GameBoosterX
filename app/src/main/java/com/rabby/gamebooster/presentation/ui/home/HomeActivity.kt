package com.rabby.gamebooster.presentation.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rabby.gamebooster.R
import com.rabby.gamebooster.domain.usecase.BoostGameUseCase
import com.rabby.gamebooster.presentation.ui.settings.SettingsActivity
import com.rabby.gamebooster.presentation.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var boostButton: Button
    private lateinit var settingsButton: Button
    private lateinit var emptyText: TextView

    private val viewModel = HomeViewModel()
    private val boostUseCase = BoostGameUseCase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerView)
        boostButton = findViewById(R.id.boostButton)
        settingsButton = findViewById(R.id.settingsButton)
        emptyText = findViewById(R.id.emptyText)

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadGames()

        boostButton.setOnClickListener {
            boostUseCase.execute(this)
        }

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadGames()
    }

    private fun loadGames() {
        val games = viewModel.getGames(this)

        if (games.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyText.visibility = View.VISIBLE
            emptyText.text = "No installed games detected."
            return
        }

        recyclerView.visibility = View.VISIBLE
        emptyText.visibility = View.GONE

        val adapter = HomeAdapter(games) { game ->
            boostUseCase.execute(this)

            val intent = packageManager.getLaunchIntentForPackage(game.packageName)
            if (intent != null) {
                startActivity(intent)
            }
        }

        recyclerView.adapter = adapter
    }
}
