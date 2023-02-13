package com.example.mypekotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.mypekotlin.BuildConfig
import com.example.mypekotlin.R
import com.example.mypekotlin.databinding.FragmentProfileBinding
import com.example.mypekotlin.model.User
import com.example.mypekotlin.utils.getScaledBitmap
import com.example.mypekotlin.viewModel.ProfileViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class ProfileFragment: Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "binding is null"
        }

    private val viewModel: ProfileViewModel by viewModels()

    private var photoName: String? = null
    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { didTakePhoto: Boolean ->
        if (didTakePhoto && photoName != null) {
            viewModel.updateUser { user ->
                user.copy(photoFileName = photoName)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.user.collect{ user ->
                    user?.let { updateIU(it) }
                }
            }
        }

        binding.cardView.setOnClickListener{
            findNavController().navigate(
                R.id.inputDialogFragment
            )
        }

        binding.userImage.setOnClickListener {
            photoName = "IMG_${Date()}.JPG"
            val photoFile = File(requireContext().applicationContext.filesDir, photoName)
            val photoUri = FileProvider.getUriForFile(
                requireContext(),
                "${BuildConfig.APPLICATION_ID}.provider",
                photoFile
            )
            takePhoto.launch(photoUri)
        }
    }

    private fun updateIU(user: User) {
        binding.apply {
            personalInformation.text = if(user.personInformation.isEmpty()) {
                getString(R.string.empty_person_info)
            }else{
                user.personInformation
            }
            var result = if(user.name.isNotEmpty()){
                "${user.name} "
            }else{
                ""
            }
            result += user.surname.ifEmpty {
                ""
            }
            fullName.text = result.ifEmpty { getString(R.string.empty_full_name) }
        }
        updatePhoto(user.photoFileName)
    }

    private fun updatePhoto(photoFileName: String?) {
        if (binding.userImage.tag != photoFileName) {
            val photoFile = photoFileName?.let {
                File(requireContext().applicationContext.filesDir, it)
            }
            if (photoFile?.exists() == true) {
                binding.userImage.doOnLayout { measuredView ->
                    val scaledBitmap = getScaledBitmap(
                        photoFile.path,
                        measuredView.width,
                        measuredView.height
                    )
                    binding.userImage.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.userImage.setImageBitmap(scaledBitmap)
                    binding.userImage.tag = photoName
                }
            } else {
                binding.userImage.setImageBitmap(null)
                binding.userImage.tag = null
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}