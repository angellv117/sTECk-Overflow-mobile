package com.example.proyectoti.FramentsFeel.Adaptadores

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

//Esto es para la creacion de los fragmets
@Suppress("DEPRECATION")
class ViewAdaptador(supportFragmentManager: FragmentManager) : FragmentPagerAdapter(supportFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private val mFragmentList = ArrayList<Fragment>()
    private  val mFragmentituloList = ArrayList<String>()
    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getItem(position: Int): Fragment{
        return mFragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentituloList[position]
    }

    fun AgregarFrament (fragment: Fragment, titulo:String){
        mFragmentList.add(fragment)
        mFragmentituloList.add(titulo)
    }
}