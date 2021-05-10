package ca.gustavo.sketchit.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import ca.gustavo.sketchit.R
import ca.gustavo.sketchit.databinding.FragmentMenuBinding
import ca.gustavo.sketchit.util.viewBinding

class MenuFragment : Fragment(R.layout.fragment_menu) {

    private val binding by viewBinding(FragmentMenuBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnCreate.setOnClickListener {
            val name = binding.name.text.toString()
            if (name.isNotEmpty()) {
                val action = MenuFragmentDirections.actionMenuFragmentToLobbyFragment(name)
                view.findNavController().navigate(action)
            }
        }

        binding.btnJoin.setOnClickListener {
            val name = binding.name.text.toString()
            if (name.isNotEmpty()) {
                val action = MenuFragmentDirections.actionMenuFragmentToLobbyFragment(name)
                view.findNavController().navigate(action)
            }
        }
    }

}