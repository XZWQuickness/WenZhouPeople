package com.exz.wenzhoupeople.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import cn.com.szw.lib.myframework.app.MyApplication
import cn.com.szw.lib.myframework.base.BaseActivity
import cn.com.szw.lib.myframework.base.MyBaseFragment
import cn.com.szw.lib.myframework.config.Constants
import cn.com.szw.lib.myframework.utils.DialogUtils
import cn.com.szw.lib.myframework.utils.RecycleViewDivider
import cn.com.szw.lib.myframework.utils.net.NetEntity
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback
import com.blankj.utilcode.util.EncryptUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.wenzhoupeople.R
import com.exz.wenzhoupeople.activity.OrderActivity
import com.exz.wenzhoupeople.activity.OrderDetailActivity
import com.exz.wenzhoupeople.activity.OrderEvaluateActivity
import com.exz.wenzhoupeople.activity.pay.PaymentActivity
import com.exz.wenzhoupeople.adapter.OrderAdapter
import com.exz.wenzhoupeople.config.Urls
import com.exz.wenzhoupeople.connection.OrderCtrl
import com.exz.wenzhoupeople.entity.OrderBean
import com.exz.wenzhoupeople.utils.KtDialog
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.refresh_recycler.*
import java.util.*

class OrderFragment : MyBaseFragment(), BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    var refreshState = Constants.RefreshState.STATE_REFRESH
    var currentPage = 1
    lateinit var mAdapter: OrderAdapter<OrderBean>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.refresh_recycler, container, false)
        }
        return rootView
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            refresh()
        }
    }
    fun refresh(){
        Thread{
            kotlin.run {
                if (rootView==null) {
                    Thread.sleep(200)
                    refresh()
                }else{
                    onRefresh()
                }
            }
        }.start()
    }
    override fun initView() {
        mAdapter = OrderAdapter()
        mAdapter.bindToRecyclerView(recyclerView)
        mAdapter.emptyView = LayoutInflater.from(context).inflate(R.layout.view_empty, RelativeLayout(context), false)
        mAdapter.setOnLoadMoreListener(this,recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(context, R.color.line_bg)))
        refresh.setOnRefreshListener(this)
        recyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val intent = Intent(context, OrderDetailActivity::class.java)
                OrderActivity.orderId=mAdapter.data[position].id
                startActivityForResult(intent,100)

            }

            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
                editOrder(activity,view,mAdapter.data[position]){
                    onRefresh()
                }
            }
        })
    }
    override fun onRefresh() {
        currentPage = 1
        refreshState = Constants.RefreshState.STATE_REFRESH
        setData()
    }
    override fun onLoadMoreRequested() {
        refresh.isEnabled = false
        refreshState = Constants.RefreshState.STATE_LOAD_MORE
        setData()
    }


    private fun setData() {
        /**
         * id	string	必填	用户id
         * state	string	必填	订单状态（0全部 1待付款 2待发货 3待收货 4待评价）
         * page	string	选填	分页（从第1页开始，每页20条数据）
         * requestCheck	string	必填	验证请求
         *
         * requestCheck	“用户id+秘钥”的 MD5加密
         * state	1待付款 2待发货 3待收货 4待评价 5已完成 6已取消 7已删除 （已删除的订单不需要返回给APP）
         * App/Order/List.aspx
         */
        val params = HashMap<String, String>()
        params.put("id", MyApplication.getLoginUserId())
        params.put("state", arguments.getString(Fragment_Type))
        params.put("page", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId(), MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<List<OrderBean>>>(Urls.OrderList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<OrderBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<OrderBean>>>) {
                        try {
                            refresh.isEnabled = true
                            refresh.isRefreshing = false
                        } catch (e: Exception) {
                        }
                        if (response.body().code == Constants.NetCode.SUCCESS) {
                            if (refreshState == Constants.RefreshState.STATE_REFRESH) {
                                mAdapter.setNewData(response.body().data)
                            } else {
                                mAdapter.addData(response.body().data)

                            }
                            if (response.body().data.isNotEmpty()) {
                                mAdapter.loadMoreComplete()
                                currentPage++
                            } else {
                                mAdapter.loadMoreEnd()
                            }
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<OrderBean>>>?) {
                        try {
                            refresh.isEnabled = true
                            refresh.isRefreshing = false
                        } catch (e: Exception) {
                        }
                        mAdapter.loadMoreFail()
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK||requestCode==100)
            onRefresh()
    }
    companion object {
        private val Fragment_Type = "Fragment_Type"
        fun newInstance(fragment_type: String): OrderFragment {
            val bundle = Bundle()
            val fragment = OrderFragment()
            bundle.putString(Fragment_Type, fragment_type)
            fragment.arguments = bundle
            return fragment
        }

        /**
         * [context] 上下文
         * [entity]   订单实体
         * [refresh]   刷新监听
         */
        fun editOrder(context:Activity,view:View,entity:OrderBean,refresh:()->Unit) {

            /**         btLeft    btRight
             * 1待付款  取消订单，去付款
             * 2待发货  联系客服
             * 3待收货  联系客服，确认收货
             * 4待评价  去评价
             * 5已完成  联系客服, 删除订单
             * 其他     删除订单
             */
            when (entity.state) {
                "1" -> {
                    when (view.id) {
                        R.id.btLeft -> {
                            KtDialog.hint(context,"确认取消订单？"){
                                OrderCtrl.editOrder(context,entity.id,"0"){
                                    refresh.invoke()
                                }
                            }
                        }
                        R.id.btRight -> {
                            val intent = Intent(context, PaymentActivity::class.java)//结算页面
                            intent.putExtra("orderId", entity.id)
                            context.startActivityForResult(intent,100)
                        }
                    }
                }
                "2" -> {
                    when (view.id) {
                        R.id.btRight -> {
                            DialogUtils.Call(context as BaseActivity, "057762792277")
                        }
                    }
                }
                "3" -> {
                    when (view.id) {
//                        R.id.btLeft -> {
//                            val intent = Intent(context, MyWebActivity::class.java)
//                            intent.putExtra(MyWebActivity.Intent_Title, "物流追踪")
//                            intent.putExtra(MyWebActivity.Intent_Url, "https://m.kuaidi100.com/index_all.html?postid="+ entity.logistics)
//                            context.startActivity(intent)
//                        }
                        R.id.btLeft -> {
                            DialogUtils.Call(context as BaseActivity, "057762792277")
                        }
                        R.id.btRight -> {
                            KtDialog.hint(context,"确认收货？"){
                                OrderCtrl.editOrder(context,entity.id,"2"){
                                    refresh.invoke()
                                }
                            }
                        }
                    }
                }
                "4" -> {
                    when (view.id) {
                        R.id.btRight -> {
                            val intent = Intent(context, OrderEvaluateActivity::class.java)
                            intent.putExtra("data",entity)
                            context.startActivityForResult(intent,100)
                        }
                    }
                }
                "5" -> {
                    when (view.id) {
                        R.id.btLeft -> {
                            DialogUtils.Call(context as BaseActivity, "057762792277")
                        }
                        R.id.btRight -> {
                            KtDialog.hint(context,"确认删除？"){
                                OrderCtrl.editOrder(context,entity.id,"1"){
                                    refresh.invoke()
                                }
                            }
                        }
                    }
                }

                else -> {
                    KtDialog.hint(context,"确认删除？"){
                        OrderCtrl.editOrder(context,entity.id,"1"){
                            refresh.invoke()
                        }
                    }
                }

            }
        }
    }
}
