package com.avion.app.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avion.app.AlertWithTwoBtns
import com.avion.app.R
import com.avion.app.action.Address
import com.avion.app.action.Geo
import com.avion.app.adapter.FavoritesAdapter
import com.avion.app.adapter.viewholder.FavoriteListViewModel
import com.avion.app.entity.FaworiteAddressEntity
import com.avion.app.fragment.viewmodel.MakeOrderViewModel
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import kotlinx.android.synthetic.main.fragment_favorite_address_list.*

class FavoriteAddressListFragment : NekFragment() {

    override fun onCreateViewModel(): NekViewModel {
        makeOrderViewModel.nekFragment = this
        return makeOrderViewModel
    }

    override fun onReDoNetworkAction() {
    }

    override fun onStopProgress() {
    }

    override fun onStartProgress() {
    }

    private val faworiteListViewModel: FavoriteListViewModel by lazy { ViewModelProviders.of(activity!!).get(FavoriteListViewModel::class.java) }
    private val makeOrderViewModel: MakeOrderViewModel by lazy { ViewModelProviders.of(activity!!).get(MakeOrderViewModel::class.java) }
    private val faworiteAdapter: FavoritesAdapter by lazy { FavoritesAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_address_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        faworiteAdapter.setOnFavoriteCallBack(object : FavoritesAdapter.OnFavoriteCallBack {
            override fun onDelete(faworiteAddressEntity: FaworiteAddressEntity?) {
                AlertWithTwoBtns(
                        context!!,
                        "Вы уверены что хотите удалить этот адрес ?",
                        "",
                        "Да",
                        "Отменить",
                        DialogInterface.OnClickListener { _, _ ->
                            faworiteListViewModel.delete(faworiteAddressEntity)
                        },
                        DialogInterface.OnClickListener { _, _ ->
                        }
                ).show()
            }

            override fun onChoose(faworiteAddressEntity: FaworiteAddressEntity?) {
                if (arguments !== null) {
                    PickUpAddressFragment.wasChoosePoint = true
                    val ADDRESS_TYPE = arguments!!.getInt("address_type")
                    if (ADDRESS_TYPE == PickUpAddressFragment.FROM) {
                        makeOrderViewModel.setAddressFrom(Address(faworiteAddressEntity!!.address, ChooseTimeFragment.intAddressTypeToString(ChooseTimeFragment.TYPE_ADDRESS), Geo(0.0, 0.0)))
                        activity!!.onBackPressed()
                    } else if (ADDRESS_TYPE == PickUpAddressFragment.TO) {
                        makeOrderViewModel.setAddressTo(Address(faworiteAddressEntity!!.address, ChooseTimeFragment.intAddressTypeToString(ChooseTimeFragment.TYPE_ADDRESS), Geo(0.0, 0.0)))
                        activity!!.onBackPressed()
                    }
                }
            }

        })
        val linerManager = LinearLayoutManager(context)
        linerManager.orientation = RecyclerView.VERTICAL
        favoritelist_recycleview.layoutManager = linerManager
        favoritelist_recycleview.adapter = faworiteAdapter
        faworiteListViewModel.list.observe(this, Observer { faworiteAdapter.submitList(it) })


        favoritelist_btn.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_favoriteAddressListFragment_to_createNewFavoriteAddressFragment) }

    }


}
