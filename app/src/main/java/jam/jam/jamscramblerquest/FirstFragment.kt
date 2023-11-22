package jam.jam.jamscramblerquest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import jam.jam.jamscramblerquest.databinding.FragmentFirstBinding

class FirstFragment : Fragment(), ScramblerAdapter.ScramblerInterface {

    private lateinit var binding: FragmentFirstBinding
    private val category = arrayOf("Foods","Vehicles","Animals", "Sports", "Clothes" , "Body parts" )
    private val photo = arrayOf("foods","vehicles","animals", "sports", "clothes" , "body_parts" )
    private lateinit var categoryAdapter: ScramblerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)

        binding.apply {
            for (value in category) {
                categoryAdapter = ScramblerAdapter(category, this@FirstFragment)
                grid.adapter = categoryAdapter
            }
        }

        return binding.root
    }

    override fun onItemClick(category: String) {

        val bundle = Bundle().apply {
            putString("Items", category)
        }

        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
        Log.e("categoryItems1", category)
    }
}

