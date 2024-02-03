package com.dicoding.myuserstory.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.myuserstory.R
import com.dicoding.myuserstory.R.*
import com.dicoding.myuserstory.databinding.FragmentRegisterBinding


class RegisterFragment : DialogFragment() {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            btnClose.setOnClickListener{
                dismiss()
            }

            btnRegister.setOnClickListener {
                val name = edNameRegister.text.toString()
                val email = edEmailRegister.text.toString()
                val password = edPasswordResigter.text.toString()

                if(edEmailRegister.text?.isEmpty()!! || edPasswordResigter.text?.isEmpty()!! || edNameRegister.text?.isEmpty()!!){
                    Toast.makeText(requireContext(),R.string.must_fill,Toast.LENGTH_SHORT).show()
                }else if(password.length < 8){
                    Toast.makeText(requireContext(),R.string.too_short,Toast.LENGTH_SHORT).show()
                }else{
                    showProgress(true)
                    if(edEmailRegister.text?.isNotEmpty()!!  && edPasswordResigter.text?.isNotEmpty()!! && edNameRegister.text?.isNotEmpty()!!) {
                        viewModel.apply {
                            register(requireContext(),name, email, password)

                            state.observe(viewLifecycleOwner) {
                                if(it == true) {
                                    showProgress(false)
                                    Toast.makeText(context, resources.getString(string.register_success_message), Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(requireContext(), AuthActivity::class.java))
                                    activity?.finish()
                                } else {
                                    showProgress(false)
                                    Toast.makeText(context, resources.getString(string.register_failed_message), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }else{
                        showProgress(false)
                        Toast.makeText(requireContext(), R.string.must_fill,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun showProgress(state: Boolean) {
        binding.progress.isVisible = state
    }
}