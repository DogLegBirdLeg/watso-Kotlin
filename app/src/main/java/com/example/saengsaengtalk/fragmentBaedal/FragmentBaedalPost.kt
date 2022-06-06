package com.example.saengsaengtalk.fragmentBaedal

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.saengsaengtalk.MainActivity
import com.example.saengsaengtalk.R
import com.example.saengsaengtalk.adapterBaedal.*
import com.example.saengsaengtalk.databinding.FragBaedalPostBinding
import org.json.JSONArray
import org.json.JSONObject
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class FragmentBaedalPost :Fragment() {
    private var postNum: String? = null

    val dec = DecimalFormat("#,###")

    var postInfo = JSONObject()
    private var mBinding: FragBaedalPostBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postNum = it.getString("postNum")
        }

        Log.d("배달 포스트", "게시물 번호: ${postNum}")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragBaedalPostBinding.inflate(inflater, container, false)

        refreshView()


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshView() {
        binding.btnPrevious.setOnClickListener { onBackPressed() }
        binding.btnOrder.setOnClickListener {
            setFrag(FragmentBaedalMenu(), mapOf( "postNum" to postNum!!, "member" to "3"))
        }
        /*val baedalPostData = mapOf(
            "title" to "네네치킨 먹을 사람",
            "writer" to "주넝이",
            "created" to LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
            "time" to LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
            "store" to "네네치킨",
            "member" to "2",
            "fee" to "10000",
            "content" to "네네치킨 드실분~",
            "likeUserList" to "주넝이, 동동이"
        )

        val today = LocalDate.now().atTime(0,0)
        val postCreated = LocalDateTime.parse(baedalPostData["created"], DateTimeFormatter.ISO_DATE_TIME)
        val baedalTime = LocalDateTime.parse(baedalPostData["time"], DateTimeFormatter.ISO_DATE_TIME)
        binding.tvPostTitle.text = baedalPostData["title"]
        binding.tvPostCreated.text = when(postCreated.isBefore(today)) {
            true -> postCreated.format(DateTimeFormatter.ofPattern("MM/dd"))
            else -> postCreated.format(DateTimeFormatter.ofPattern("HH:mm"))
        }
        binding.tvPostWriter.text = baedalPostData["writer"]
        binding.tvTime.text = baedalTime.format(DateTimeFormatter.ofPattern("MM/dd(E) HH:mm").withLocale(
            Locale.forLanguageTag("ko")))
        binding.tvStore.text = baedalPostData["store"]
        binding.tvMember.text = baedalPostData["member"]
        binding.tvFee.text = (baedalPostData["fee"]!!.toInt() / baedalPostData["member"]!!.toInt()).toString()
        binding.tvContent.text = baedalPostData["content"]
        if (baedalPostData["likeUserList"]!!.contains("주넝이"))
            binding.ivLike.setImageResource(R.drawable.heart_red)
        binding.tvLike.text = (baedalPostData["likeUserList"]!!.count{ c -> c == ','} + 1).toString()*/

        getPostInfo()
        val today = LocalDate.now().atTime(0,0)
        val postCreated = LocalDateTime.parse(postInfo.getString("created"), DateTimeFormatter.ISO_DATE_TIME)
        val baedalTime = LocalDateTime.parse(postInfo.getString("baedalTime"), DateTimeFormatter.ISO_DATE_TIME)
        binding.tvPostTitle.text = postInfo.getString("title")
        binding.tvPostCreated.text = when(postCreated.isBefore(today)) {
            true -> postCreated.format(DateTimeFormatter.ofPattern("MM/dd"))
            else -> postCreated.format(DateTimeFormatter.ofPattern("HH:mm"))
        }
        binding.tvPostWriter.text = postInfo.getString("writer")
        binding.tvTime.text = baedalTime.format(DateTimeFormatter.ofPattern("MM/dd(E) HH:mm").withLocale(
            Locale.forLanguageTag("ko")))
        binding.tvStore.text = postInfo.getString("store")
        binding.tvMember.text = postInfo.getString("member")
        binding.tvFee.text = (postInfo.getInt("baedalFee") / postInfo.getInt("member")).toString()
        binding.tvContent.text = postInfo.getString("content")
        val likeUserList = postInfo.getJSONArray("likeUserList")
        for (i in 0 until likeUserList.length()) {
            if (likeUserList.getString(i) == "wnsjd")
                binding.ivLike.setImageResource(R.drawable.heart_red)
        }
        binding.tvLike.text = (likeUserList.length()).toString()

        /* 주문 내역 */
        binding.rvOrderList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvOrderList.setHasFixedSize(true)

        var baedalOrderer = mutableListOf<BaedalOrderer>()                  // 어댑터용
        val orderList = postInfo.getJSONArray("orderList")      // 실제 데이터
        for (i in 0 until orderList.length()){
            val order = orderList.getJSONObject(i)
            val ordererName = order.getString("orderer")
            val orders = order.getJSONArray("orders")

            val baedalOrder = mutableListOf<BaedalOrder>()
            var totalPrice = 0
            for (j in 0 until orders.length()) {
                val aOrder = orders.getJSONObject(j)
                val menuName = aOrder.getString("menuName")
                totalPrice += aOrder.getInt("price")
                val count = aOrder.getInt("count")

                val optString = aOrder.getJSONArray("optString")
                val baedalOrderOpt = mutableListOf<BaedalOrderOpt>()
                for (k in 0 until optString.length()) {
                    baedalOrderOpt.add(BaedalOrderOpt(optString.getString(k)))
                }
                baedalOrder.add(BaedalOrder(menuName, count, baedalOrderOpt))
            }

            baedalOrderer.add(BaedalOrderer(
                "주문자 : ${ordererName}   주문금액: ${dec.format(totalPrice)}원", baedalOrder))
        }
        val adapter = BaedalOrdererAdapter(requireContext(), baedalOrderer)

        //binding.rvMenu.addItemDecoration(BaedalOptAreaAdapter.BaedalOptAreaAdapterDecoration())
        //adapter.notifyDataSetChanged()

        binding.rvOrderList.adapter = adapter


        /* 댓글 */
        val comment = arrayListOf(
            BaedalComment("동동이", "네네치킨 먹을 사람 드루와~ 123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789", LocalDateTime.now(), 1,0, 0, "동동이"),
            BaedalComment("주넝이", "네네치킨 먹을 사람~ 123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789", LocalDateTime.now(), 2, 1, 0, "주넝이"),
            BaedalComment("동동이", "먹을 사람 드루와~", LocalDateTime.now(), 3, 1, 0, "동동이"),
            BaedalComment("동동이", "네네치킨~", LocalDateTime.now(), 4, 0, 1, "동동이")

        )
        binding.rvComment.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvComment.setHasFixedSize(true)
        binding.rvComment.adapter = BaedalCommentAdapter(comment)
    }

    @JvmName("getPostInfo1")
    fun getPostInfo() {
        val assetManager = resources.assets
        postInfo = JSONObject(assetManager.open("baedal_post.json").bufferedReader().use { it.readText() })
    }

    fun jArrayToList(array: JSONArray): MutableList<String> {
        var list = mutableListOf<String>()
        for (i in 0 until array.length())
            list.add(array.getString(i))

        return list
    }

    fun setFrag(fragment: Fragment, arguments: Map<String, String>? = null) {
        val mActivity = activity as MainActivity
        mActivity.setFrag(fragment, arguments)
    }

    fun onBackPressed() {
        val mActivity =activity as MainActivity
        mActivity.onBackPressed()
    }
}