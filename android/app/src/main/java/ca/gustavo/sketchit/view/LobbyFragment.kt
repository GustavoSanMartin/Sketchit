package ca.gustavo.sketchit.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ca.gustavo.sketchit.R
import ca.gustavo.sketchit.databinding.FragmentLobbyBinding
import ca.gustavo.sketchit.di.injector
import ca.gustavo.sketchit.domain.MainViewModel
import ca.gustavo.sketchit.util.viewBinding

class LobbyFragment : Fragment(R.layout.fragment_lobby) {

    private val viewModel by viewModels<MainViewModel> { injector.mainViewModelFactory() }
    private val binding by viewBinding(FragmentLobbyBinding::bind)
    var playerList = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val name = arguments?.getString("name")!!

        binding.btnStart.setOnClickListener {
            val action = LobbyFragmentDirections.actionLobbyFragmentToDrawingFragment()
            view.findNavController().navigate(action)
        }

//        viewModel.startListeningToNames()


        val adapter = PlayerAdapter(playerList)
        binding.rvPlayers.adapter = adapter
        binding.rvPlayers.layoutManager = LinearLayoutManager(context)

        viewModel.names.observe(activity as AppCompatActivity, {
            println("observed player name list: $it")
            playerList.clear()
            playerList.addAll(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.sendName(name)
    }

}