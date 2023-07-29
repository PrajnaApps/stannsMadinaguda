package prajna.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import org.json.JSONObject
import prajna.app.R
import prajna.app.ui.adapter.HomeOptionAdapter
import prajna.app.utills.customFonts.Text_Bold

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var rv_home: RecyclerView
    private lateinit var tvHeadertitle: Text_Bold
    private lateinit var tvUsername: Text_Bold
    private lateinit var llUser: LinearLayout

    private var mTokenData: String = ""
    private var mUserName: String = ""
    private var mStudName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        mTokenData = UserSession(requireActivity()).getStudentKey()
        mUserName = UserSession(requireActivity()).getUsername()

        var dict_filterdata = JSONObject()
        try {
            dict_filterdata = JSONObject(mTokenData)
            mStudName = dict_filterdata.getString("stud_name")

        } catch (e: Exception) {
            e.printStackTrace()
        }

        initView(rootView)


//        textView.setOnClickListener {
//            (requireActivity() as Homescreen).setSelectedItem(2)
//            (requireActivity() as Homescreen).removeBadge(2)
//        }

//        (requireActivity() as Homescreen).setBadge(2)
//        (requireActivity() as Homescreen).setBadge(0)
        return rootView

    }

    fun initView(v: View) {
        rv_home = v.findViewById(R.id.rv_home)
        tvHeadertitle = v.findViewById(R.id.tvHeadertitle)
        tvUsername = v.findViewById(R.id.tvUsername)
        llUser = v.findViewById(R.id.llUser)
        tvHeadertitle.setText(R.string.menu_home)
        tvUsername.visibility = View.VISIBLE

        tvUsername.setText(mUserName)

        llUser.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), Homescreen::class.java).putExtra(
                    "id", "profile")
            )
        })

        val layoutmanager = LinearLayoutManager(requireActivity())
        rv_home.layoutManager = layoutmanager
        rv_home.layoutManager = GridLayoutManager(requireActivity(), 3)
        val eventadapter = HomeOptionAdapter(requireActivity())
        rv_home.adapter = eventadapter

    }

}
