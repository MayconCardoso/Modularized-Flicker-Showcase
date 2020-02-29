package com.mctech.showcase.feature.flicker_presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import com.mctech.library.architecture.ComponentState
import com.mctech.library.architecture.components.GridItemDecoration
import com.mctech.library.architecture.extention.bindState
import com.mctech.library.design_system.extentions.LoadNextPageScrollMonitor
import com.mctech.library.design_system.extentions.createDefaultRecyclerView
import com.mctech.library.design_system.extentions.refreshItems
import com.mctech.showcase.feature.flicker_domain.entity.FlickerPhoto
import com.mctech.showcase.feature.flicker_presentation.databinding.FragmentPhotosBinding
import com.mctech.showcase.feature.flicker_presentation.databinding.ListItemPhotoBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * @author MAYCON CARDOSO on 2020-02-29.
 */
class PhotosFragment : Fragment() {
    private lateinit var binding: FragmentPhotosBinding

    private val photosViewModel: PhotosViewModel by sharedViewModel()
    private val listItemDecorator by lazy {
        GridItemDecoration(
            spanCount 	= 2,
            spacing 	= resources.getDimensionPixelOffset(R.dimen.defaultItemSpace),
            includeEdge = false
        )
    }
    private val loadNextPageScrollMonitor by lazy{
        LoadNextPageScrollMonitor {
            photosViewModel.interact(PhotosViewInteraction.LoadNextPage)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        return FragmentPhotosBinding.inflate(inflater, container, false).also {
            binding = it
            it.viewModel = photosViewModel
            it.lifecycleOwner = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindState(photosViewModel.photosComponent) { renderScreen(it) }
        bindState(photosViewModel.tagComponent) { renderTagScreen(it) }

        binding.containerError.setOnClickListener {
            photosViewModel.reprocessLastInteraction()
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            photosViewModel.interact(PhotosViewInteraction.RefreshData)
        }
    }

    override fun onDestroyView() {
        binding.listPhotos.removeOnScrollListener(loadNextPageScrollMonitor)
        binding.listPhotos.removeItemDecoration(listItemDecorator)
        super.onDestroyView()
    }

    private fun renderTagScreen(state: ComponentState<String>) {
        when(state){
            is ComponentState.Success -> setScreenTitle(state.result)
            else -> setScreenTitle(getString(R.string.app_name))
        }
    }

    private fun renderScreen(state: ComponentState<PhotosState>) {
        when (state) {
            is ComponentState.Initializing -> {
                photosViewModel.interact(PhotosViewInteraction.LoadFirstPage)
            }
            is ComponentState.Success -> {
                // Create first list.
                if(thereIsNoItemOnList()){
                    createListOfPhotos(state.result)
                }

                // Update list.
                else {
                    updateListOfPhotos(state.result)
                    scrollListToTop(state.result)
                }

                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun createListOfPhotos(result: PhotosState) {
        // Item divider
        binding.listPhotos.removeItemDecoration(listItemDecorator)
        binding.listPhotos.addItemDecoration(listItemDecorator)

        // Pagination monitor
        binding.listPhotos.addOnScrollListener(loadNextPageScrollMonitor)

        createDefaultRecyclerView<FlickerPhoto, ListItemPhotoBinding>(
            items = result.photos,
            recyclerView = binding.listPhotos,
            viewBindingCreator = { parent, inflater ->
                ListItemPhotoBinding.inflate(inflater, parent, false)
            },
            prepareHolder = { item, viewBinding ->
                viewBinding.item = item
            }
        )
    }

    private fun updateListOfPhotos(result: PhotosState) = refreshItems(
        recyclerView = binding.listPhotos,
        newItems = result.photos,
        callback = object : DiffUtil.ItemCallback<FlickerPhoto>() {
            override fun areItemsTheSame(left: FlickerPhoto, right: FlickerPhoto): Boolean {
                return left.id == right.id
            }

            override fun areContentsTheSame(left: FlickerPhoto, right: FlickerPhoto): Boolean {
                return left.title == right.title && left.sourceUrl == right.sourceUrl
            }
        }
    )

    private fun scrollListToTop(result: PhotosState) {
        if (result.moveToTop) {
            binding.listPhotos.smoothScrollToPosition(0)
        }
    }

    private fun thereIsNoItemOnList(): Boolean {
        return binding.listPhotos.adapter == null
    }

    private fun setScreenTitle(screenTitle: String) {
        activity?.actionBar?.apply {
            title = screenTitle
        }
    }
}