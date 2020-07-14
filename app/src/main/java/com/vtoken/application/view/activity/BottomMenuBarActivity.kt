package com.vtoken.application.view.activity

import android.os.Bundle
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.trello.rxlifecycle2.components.RxFragment
import com.vtoken.application.R
import com.vtoken.application.util.resetItemSize
import com.vtoken.application.view.fragment.WalletFragment
//import com.ydit.lottilewidget.BottomNavigationBar;
//import com.ydit.lottilewidget.BottomNavigationItem;

open class BottomMenuBarActivity : BaseActivity(),BottomNavigationBar.OnTabSelectedListener {
    lateinit var mFragment: RxFragment
    lateinit var walletFragment:WalletFragment
    lateinit var  bottomMenuBar:BottomNavigationBar;
    override fun onTabReselected(position: Int) {

    }

    override fun onTabUnselected(position: Int) {

    }

    override fun onResume() {
        when(mFragment::class.java){
            WalletFragment::class.java->{
                bottomMenuBar.selectTab(0)
            }
        }
        super.onResume()
    }

    override fun onTabSelected(position: Int) {
        val  fm=fragmentManager
        val transaction=fm.beginTransaction()
        when(position){
            0->{
                switchFragment(walletFragment)
            }
        }
        transaction.commit()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomMenuBar=findViewById(R.id.bottom_menuBar)
        initFragment()
        initBottomMenuBar()
        bottomMenuBar.setTabSelectedListener(this)

    }

    fun initFragment(){
        val transition=fragmentManager.beginTransaction()
        walletFragment=WalletFragment()
        transition.add(R.id.main_constraint_container,walletFragment)
            .commit()
        mFragment=walletFragment
    }

    fun switchFragment(fragment:RxFragment){
        if(mFragment!=fragment){
            if(!fragment.isAdded){
                fragmentManager.beginTransaction().hide(mFragment).add(R.id.main_constraint_container,fragment).commit()
            }else{
                fragmentManager.beginTransaction().hide(mFragment).show(fragment).commit()
            }
            mFragment=fragment
        }

    }



    fun initBottomMenuBar(){
        bottomMenuBar.setActiveColor(R.color.resync_back_selected).setInActiveColor(R.color.text_normal)
                .setBarBackgroundColor(R.color.white)
        bottomMenuBar.setMode(BottomNavigationBar.MODE_DEFAULT)
//        bottomMenuBar.addItem(BottomNavigationItem.createLottieBottomNavigationItem(R.raw.bottom_bar_home,"钱包"))
//                .addItem(BottomNavigationItem.createLottieBottomNavigationItem(R.raw.bottom_bar_app,"应用"))
//                .setFirstSelectedPosition(0)
//                .initialise()
        bottomMenuBar.addItem(BottomNavigationItem(R.drawable.ic_zly_home1,"钱包"))
                .setFirstSelectedPosition(0)
                .initialise()
        bottomMenuBar.resetItemSize(6,24,11)
    }

}
