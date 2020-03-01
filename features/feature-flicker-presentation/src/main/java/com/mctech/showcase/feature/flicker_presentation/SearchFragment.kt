package com.mctech.showcase.feature.flicker_presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import br.com.unicred.feature.arq.ViewCommand
import com.mctech.library.architecture.ComponentState
import com.mctech.library.architecture.extention.bindCommand
import com.mctech.library.architecture.extention.bindState
import com.mctech.library.design_system.extentions.createDefaultRecyclerView
import com.mctech.library.design_system.extentions.hide
import com.mctech.library.design_system.extentions.refreshItems
import com.mctech.library.design_system.extentions.show
import com.mctech.showcase.feature.flicker_presentation.databinding.FragmentSearchBinding
import com.mctech.showcase.feature.flicker_presentation.databinding.ListItemSearchBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * @author MAYCON CARDOSO on 2020-03-01.
 */
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private val photosViewModel: PhotosViewModel by sharedViewModel()
    private val navigator : PhotosNavigation by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentSearchBinding.inflate(inflater, container, false).also {
            binding = it
            it.viewModel = photosViewModel
            it.lifecycleOwner = this
        }.root
    }

    override fun onStart() {
        super.onStart()
        setScreenTitle()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindState(photosViewModel.tagHistoryComponent) { renderTagHistoryScreen(it) }
        bindCommand(photosViewModel) { handleCommands(it) }

        binding.etSearch.setOnEditorActionListener { _, actionId, _->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun renderTagHistoryScreen(state: ComponentState<List<String>>) {
        when(state){
            is ComponentState.Initializing ->{
                photosViewModel.interact(PhotosViewInteraction.LoadTagHistory)
            }
            is ComponentState.Success -> {
                handleTagHistoryList(state.result)
            }
        }
    }

    private fun handleCommands(command: ViewCommand) {
        when(command){
            is PhotosCommands.NavigateToPhotos -> {
                navigator.navigateToPhotosList()
            }
        }
    }

    private fun handleTagHistoryList(result: List<String>) {
        binding.tagHistoryList.show()
        binding.noResults.hide()

        // Create first list.
        if(thereIsNoItemOnList()){

            if(result.isEmpty()){
                binding.noResults.show()
                binding.tagHistoryList.hide()
                return
            }

            createListOfTagHistory(result)
        }

        // Update list.
        else {
            updateListOfTagHistory(result)
        }
    }

    private fun performSearch() {
        val tag = binding.etSearch.text.toString().trim()
        if(tag.isEmpty()){
            return
        }

        photosViewModel.interact(PhotosViewInteraction.SearchTag(tag))
    }

    private fun setScreenTitle() {
        activity?.let {
            if(it is AppCompatActivity){
                it.supportActionBar?.title = getString(R.string.app_name)
            }
        }
    }

    private fun thereIsNoItemOnList(): Boolean {
        return binding.tagHistoryList.adapter == null
    }

    private fun createListOfTagHistory(result: List<String>) {
        createDefaultRecyclerView<String, ListItemSearchBinding>(
            items = result,
            recyclerView = binding.tagHistoryList,
            viewBindingCreator = { parent, inflater ->
                ListItemSearchBinding.inflate(inflater, parent, false)
            },
            prepareHolder = { item, viewBinding ->
                viewBinding.item = item
                viewBinding.root.setOnClickListener {
                    photosViewModel.interact(PhotosViewInteraction.SearchTag(item))
                }
            }
        )
    }

    private fun updateListOfTagHistory(result: List<String>) = refreshItems(
        recyclerView = binding.tagHistoryList,
        newItems = result,
        callback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(left: String, right: String): Boolean {
                return left == right
            }

            override fun areContentsTheSame(left: String, right: String): Boolean {
                return left == right
            }
        }
    )
}