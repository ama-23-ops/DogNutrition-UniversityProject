package com.example.dognutrionapp.ui.educational

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dognutrionapp.databinding.FragmentEducationalContentBinding
import com.example.dognutrionapp.ui.models.EducationalContent
import com.example.dognutrionapp.ui.educational.EducationalContentAdapter

class EducationalContentFragment : Fragment() {
    private var _binding: FragmentEducationalContentBinding? = null
    private val binding get() = _binding!!

    private lateinit var contentAdapter: EducationalContentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEducationalContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dummy data for educational content
        val contentList = listOf(
            EducationalContent("Dog Nutrition Basics", "Learn the basics of dog nutrition..."),
            EducationalContent("Choosing the Right Food", "Guide to selecting the right food for your dog..."),
            EducationalContent("Healthy Treats", "Discover healthy treat options for your pet...")
        )

        contentAdapter = EducationalContentAdapter(contentList)
        binding.recyclerViewEducationalContent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = contentAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
