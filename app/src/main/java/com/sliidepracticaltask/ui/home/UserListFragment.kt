package com.sliidepracticaltask.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.livinglifetechway.k4kotlin.core.hide
import com.livinglifetechway.k4kotlin.core.isNetworkAvailable
import com.livinglifetechway.k4kotlin.core.onClick
import com.livinglifetechway.k4kotlin.core.show
import com.ravikoradiya.liveadapter.LiveAdapter
import com.sliidepracticaltask.BR
import com.sliidepracticaltask.R
import com.sliidepracticaltask.databinding.DialogAddUserBinding
import com.sliidepracticaltask.databinding.FragmentUserListBinding
import com.sliidepracticaltask.databinding.RowUserBinding
import com.sliidepracticaltask.model.request.AddUserRequest
import com.sliidepracticaltask.model.response.UserListResponse
import com.sliidepracticaltask.network.resource.Resource
import com.sliidepracticaltask.network.resource.ifFailure
import com.sliidepracticaltask.network.resource.ifSuccess
import com.sliidepracticaltask.utils.showError

class UserListFragment : Fragment() {
    lateinit var mBinding: FragmentUserListBinding

    private val mNavController: NavController by lazy { Navigation.findNavController(mBinding.root) }
    private var fragmentInitialized: Boolean = false
    lateinit var mViewModel: UserListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (!fragmentInitialized) {
            mBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_user_list, container, false)
            mViewModel = ViewModelProviders.of(this).get(UserListViewModel::class.java)
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!fragmentInitialized) {
            fragmentInitialized = true
            setup()
            listeners()
        }

    }

    private fun setup() {

        LiveAdapter(mViewModel.liveDataUserList, BR.item)
            .map<UserListResponse.User, RowUserBinding>(R.layout.row_user) {
                onBind {
                }
                onClick {

                }
                onLongClick {
                    showDeleteConfirmationDialog(it.binding!!.item!!.id!!)
                }
            }
            .into(mBinding.rvUserList!!)

        getUserList()

    }

    private fun listeners() {
        mBinding.cardAddUser.onClick {

            Dialog(requireActivity()).apply {
                var dialogBinding: DialogAddUserBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(requireActivity()),
                    R.layout.dialog_add_user,
                    null,
                    false
                )
                setContentView(dialogBinding.root)
                window!!.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                window!!.setBackgroundDrawableResource(R.drawable.bg_rounded_dialog)
                setCancelable(true)
                dialogBinding.buttonAdd.onClick {

                    if (dialogBinding.editName.text.toString().trim().isEmpty()) {
                        dialogBinding.editName.requestFocus()
                        showError(dialogBinding.root, context.getString(R.string.name_validation))
                    } else if (dialogBinding.editEmail.text.toString().trim().isEmpty()) {
                        dialogBinding.editEmail.requestFocus()
                        showError(dialogBinding.root, context.getString(R.string.email_validation))
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(
                            dialogBinding.editEmail.text.toString().trim()
                        ).matches()
                    ) {
                        dialogBinding.editEmail.requestFocus()
                        showError(
                            dialogBinding.root,
                            context.getString(R.string.email_validation_invalid)
                        )
                    } else {
                        dismiss()
                        addUser(
                            AddUserRequest(
                                email = dialogBinding.editEmail.text.toString().trim(),
                                name = dialogBinding.editName.text.toString().trim(),
                                gender = "Male",
                                status = "Active"
                            ),
                        )
                    }

                }
                dialogBinding.buttonCancel.onClick {
                    dismiss()
                }
            }.show()
        }
    }

    private fun getUserList() {
        if (!requireActivity().isNetworkAvailable()) {
            showError(mBinding.root, getString(R.string.no_network_available))
            return
        }
        mViewModel.getUsersListData().observe(viewLifecycleOwner, Observer {

            if (it.state == Resource.State.LOADING) {
                mBinding.layoutState!!.viewBlockUi.show()
                mBinding.layoutState!!.textLoading.show()
            } else {
                mBinding.layoutState!!.textLoading.hide()
                mBinding.layoutState!!.viewBlockUi.hide()
            }

            it.ifSuccess { response ->
                if (response!!.data.isNullOrEmpty()) {
                    mBinding.layoutState!!.textNoData.text = "No Users found"
                    mBinding.layoutState!!.textNoData.visibility = View.VISIBLE
                    mBinding.rvUserList!!.visibility = View.GONE
                } else {
                    mBinding.layoutState!!.textNoData.visibility = View.GONE
                    mBinding.rvUserList!!.visibility = View.VISIBLE
                    mViewModel.setUsersList(response)
                }

            }
            it.ifFailure { throwable, errorData ->
                showError(mBinding.root, errorData?.message.orEmpty())
            }
        })


    }

    private fun addUser(
        addUserRequest: AddUserRequest,
    ) {
        if (!requireActivity().isNetworkAvailable()) {
            showError(
                mBinding.root,
                getString(R.string.no_network_available)
            )
            return
        }

        mViewModel.addUser(
            addUserRequest
        ).observe(viewLifecycleOwner, Observer {

            if (it.state == Resource.State.LOADING) {
                mBinding.layoutState!!.viewBlockUi.show()
                mBinding.layoutState!!.textLoading.show()
            } else {
                mBinding.layoutState!!.textLoading.hide()
                mBinding.layoutState!!.viewBlockUi.hide()
            }

            it.ifSuccess { response ->
                mViewModel.setUsersList(mViewModel!!.liveDataUsers!!.value.apply {
                    this!!.data!!.add(0, response!!.data!!)
                })
            }
            it.ifFailure { throwable, errorData ->
                showError(mBinding.root, errorData?.message.orEmpty())
            }
        })

    }


    private fun showDeleteConfirmationDialog(id: Int) {
        AlertDialog.Builder(requireActivity()).apply {
            setMessage("Are you sure you want to remove this user?")
            setCancelable(true)
            setPositiveButton(
                getString(R.string.yes),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                    deleteUser(id)
                })
            setNegativeButton(
                getString(R.string.no),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
        }.show()
    }

    private fun deleteUser(
        id: Int,
    ) {
        if (!requireActivity().isNetworkAvailable()) {
            showError(
                mBinding.root,
                getString(R.string.no_network_available)
            )
            return
        }

        mViewModel.deleteUser(
            id
        ).observe(viewLifecycleOwner, Observer {

            if (it.state == Resource.State.LOADING) {
                mBinding.layoutState!!.viewBlockUi.show()
                mBinding.layoutState!!.textLoading.show()
            } else {
                mBinding.layoutState!!.textLoading.hide()
                mBinding.layoutState!!.viewBlockUi.hide()
            }

            it.ifSuccess { response ->
                mViewModel.setUsersList(mViewModel!!.liveDataUsers!!.value.apply {
                    this!!.data!!.removeAt(this!!.data!!.indexOfFirst { element -> element.id == id })
                })
            }
            it.ifFailure { throwable, errorData ->
                showError(mBinding.root, errorData?.message.orEmpty())
            }
        })

    }


}