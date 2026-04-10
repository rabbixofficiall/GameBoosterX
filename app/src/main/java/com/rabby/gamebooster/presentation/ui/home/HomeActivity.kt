package com.rabby.gamebooster.presentation.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rabby.gamebooster.R
import com.rabby.gamebooster.domain.usecase.BoostGameUseCase
import com.rabby.gamebooster.presentation.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var boostButton: Button

    private val viewModel = HomeViewModel()
    private val boostUseCase = BoostGameUseCase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerView)
        boostButton = findViewById(R.id.boostButton)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = HomeAdapter(viewModel.getGames()) { game ->
            boostUseCase.execute(this)

            val intent = packageManager.getLaunchIntentForPackage(game.packageName)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        boostButton.setOnClickListener {
            boostUseCase.execute(this)
        }
    }
}
