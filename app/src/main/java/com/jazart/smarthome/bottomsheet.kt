package com.jazart.smarthome


import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottomsheet.*
import java.lang.ClassCastException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class bottomsheet : BottomSheetDialogFragment() {

    private var mBottomSheetListener: BottomSheetListener?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_bottomsheet, container, false)

        cmd1.setOnClickListener{
            mBottomSheetListener!!.onOptionClick("Call clicked...")
        }
        cmd2.setOnClickListener{
            mBottomSheetListener!!.onOptionClick("Call clicked...")
        }
        cmd3.setOnClickListener{
            mBottomSheetListener!!.onOptionClick("Call clicked...")
        }

        return v;
    }

    interface BottomSheetListener{
        fun onOptionClick(text:String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            mBottomSheetListener = context as BottomSheetListener?

        }
        catch (e:ClassCastException){
            throw ClassCastException(context!!.toString())
        }
    }


}
