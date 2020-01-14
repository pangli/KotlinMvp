package com.zorro.kotlin.samples.ui.database.fragment

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.zorro.kotlin.baselibs.base.FragmentBackHandler
import com.zorro.kotlin.baselibs.http.download.DownLoadManager
import com.zorro.kotlin.baselibs.http.download.DownLoadSubscriber
import com.zorro.kotlin.baselibs.http.download.ProgressCallBack
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.bean.DatabaseFile
import com.zorro.kotlin.samples.bean.DatabaseFiles
import com.zorro.kotlin.samples.bean.ProductData
import com.zorro.kotlin.samples.bean.SupplierData
import com.zorro.kotlin.samples.constant.Constant
import com.zorro.kotlin.samples.mvp.contract.DatabaseContract
import com.zorro.kotlin.samples.mvp.presenter.DatabasePresenter
import com.zorro.kotlin.samples.ui.activity.FileViewerActivity
import com.zorro.kotlin.samples.ui.base.ZorroBaseMvpFragment
import com.zorro.kotlin.samples.ui.database.adapter.DatabaseAdapter
import com.zorro.kotlin.samples.ui.database.adapter.ProductDropDownMenuAdapter
import com.zorro.kotlin.samples.ui.database.adapter.SupplierDropDownMenuAdapter
import com.zorro.kotlin.samples.utils.ActivityHelper
import kotlinx.android.synthetic.main.fragment_filter.*
import okhttp3.ResponseBody
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.*


/**
 * @author Zorro
 * @date 2019/12/25
 * @desc 产品资料
 */
class ProductInformationFragment :
    ZorroBaseMvpFragment<DatabaseContract.View, DatabaseContract.Presenter>(),
    DatabaseContract.View, FragmentBackHandler {

    companion object {
        fun newInstance(): ProductInformationFragment = ProductInformationFragment()
    }

    private var rootView: View? = null
    private var smartRefreshLayout: SmartRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    private val popupViews = ArrayList<View>()
    private var currentPager = 1
    private lateinit var adapter: DatabaseAdapter

    //下载
    private var fileName: String? = ""
    private var downLoadUrl: String? = ""


    override fun createPresenter(): DatabaseContract.Presenter = DatabasePresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_filter
    override fun initView(view: View) {
        super.initView(view)
        rootView = layoutInflater.inflate(R.layout.refresh_recycler_layout, null, false)
        smartRefreshLayout = rootView?.findViewById(R.id.smart_refresh_layout)
        recyclerView = rootView?.findViewById(R.id.recyclerView)
        initFilter()
    }


    override fun bindListener(view: View) {
        smartRefreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                currentPager = 1
                requestData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                requestData()
            }
        })
    }

    override fun initData() {
        super.initData()
        adapter = DatabaseAdapter()
        adapter.bindToRecyclerView(recyclerView)
        adapter.onItemClickListener = MyItemClickListener(3)
    }

    override fun lazyLoad() {
        requestData()
    }

    /**
     * 请求接口数据
     */
    private fun requestData() {
        val map = mutableMapOf<String, String?>()
        map["category"] = "2"
        map["supplier_id"] = currentCompany
        map["product_id"] = currentProduct
        map["pageSize"] = Constant.PAGE_SIZE.toString()
        map["page"] = currentPager.toString()
        mPresenter?.getDatabaseFileList(map)
    }


    /////////////////////////////////////////筛选////////////////////////////////////////////////////
    /**
     * 筛选 保险公司 产品种类  投保年龄
     */
    private val filter = mutableListOf("保险公司", "产品名称")
    private val companyData = mutableListOf<SupplierData>()
    private val productData = mutableListOf<ProductData>()
    private var companyMenuAdapter: SupplierDropDownMenuAdapter? = null
    private var productMenuAdapter: ProductDropDownMenuAdapter? = null
    private var currentCompany: String? = ""
    private var currentProduct: String? = ""
    private fun initFilter() {
        //保险公司
        companyData.add(SupplierData("全部", ""))
        val companyRecycler = RecyclerView(context!!)
        companyRecycler.layoutManager = LinearLayoutManager(context!!)
        companyMenuAdapter = SupplierDropDownMenuAdapter(companyData)
        companyMenuAdapter?.bindToRecyclerView(companyRecycler)
        companyMenuAdapter?.onItemClickListener = MyItemClickListener(1)
        mPresenter?.getSupplier()

        //产品种类
        productData.add(ProductData(arrayListOf(), "全部", arrayListOf(), ""))
        val productRecycler = RecyclerView(context!!)
        productRecycler.layoutManager = LinearLayoutManager(context!!)
        productMenuAdapter = ProductDropDownMenuAdapter(productData)
        productMenuAdapter?.bindToRecyclerView(productRecycler)
        productMenuAdapter?.onItemClickListener = MyItemClickListener(2)

        //
        popupViews.add(companyRecycler)
        popupViews.add(productRecycler)
        dropDownMenu.setDropDownMenu(filter, popupViews, rootView!!)
    }

    /**
     * 筛选列表点击事件
     */
    private inner class MyItemClickListener internal constructor(private val type: Int) :
        BaseQuickAdapter.OnItemClickListener {
        override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            when (type) {
                1 -> {
                    val item = adapter?.getItem(position) as SupplierData
                    companyMenuAdapter?.setCheckItem(position)
                    currentCompany = item.supplier_id
                    requestProductData()
                    recoverProduct()
                    selectTabAndRefresh(item.name)
                }
                2 -> {
                    val item = adapter?.getItem(position) as ProductData
                    productMenuAdapter?.setCheckItem(position)
                    currentProduct = item.product_id
                    selectTabAndRefresh(item.name)
                }
                3 -> {
                    val item = adapter?.getItem(position) as DatabaseFile
                    fileName = item.file_name
                    downLoadUrl = item.url
                    filePermission()
                }
            }

        }

    }

    /**
     * 设置tab选中文字和刷新列表
     */
    private fun selectTabAndRefresh(name: String) {
        dropDownMenu.setTabText(name)
        dropDownMenu.closeMenu()
        currentPager = 1
        showLoading()
        requestData()
    }

    /**
     * 请求接口数据
     */
    private fun requestProductData() {
        productData.clear()
        productData.add(ProductData(arrayListOf(), "全部", arrayListOf(), ""))
        productMenuAdapter?.setNewData(productData)
        if (!currentCompany.isNullOrBlank()) {
            mPresenter?.getProductList(currentCompany!!)
        }
    }

    /**
     * 重置产品筛选
     */
    private fun recoverProduct() {
        productMenuAdapter?.setCheckItem(0)
        currentProduct = ""
        dropDownMenu.recoverTabText(1, filter[1])
    }
    //////////////////////////////////////////筛选///////////////////////////////////////////////////

    /**
     * Fragment返回键
     */
    override fun onBackPressed(): Boolean {
        if (dropDownMenu != null && dropDownMenu.isShowing) {
            dropDownMenu.closeMenu()
            return true
        }
        return false
    }

    /**
     * 公司数据
     */
    override fun setSupplierResult(supplierData: MutableList<SupplierData>) {
        companyMenuAdapter?.addData(supplierData)
    }

    /**
     * 产品数据
     */
    override fun setProductResult(productData: List<ProductData>) {
        productMenuAdapter?.addData(productData)
    }

    /**
     * 列表数据
     */
    override fun setDatabaseResult(databaseFiles: DatabaseFiles) {
        recyclerViewBindData(databaseFiles.list)
    }

    /**
     * 绑定数据
     */
    private fun recyclerViewBindData(data: List<DatabaseFile>) {
        if (data.isNullOrEmpty()) {
            if (currentPager == 1) {
                adapter.setNewData(null)
                adapter.emptyView = getEmptyView(recyclerView!!)
            } else {
                smartRefreshLayout?.finishLoadMoreWithNoMoreData()
            }
        } else {
            if (currentPager == 1) {
                adapter.setNewData(data)
            } else {
                adapter.addData(data)
            }
            currentPager++
        }
        finishRefreshOrLoadMoreAnimation()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        finishRefreshOrLoadMoreAnimation()
    }

    /**
     * 隐藏刷新或加载动画
     */
    private fun finishRefreshOrLoadMoreAnimation() {
        if (smartRefreshLayout?.state == RefreshState.Refreshing) {
            smartRefreshLayout?.finishRefresh()
        }
        if (smartRefreshLayout?.state == RefreshState.Loading) {
            smartRefreshLayout?.finishLoadMore()
        }
    }
    ///////////////////////////////////////////////////////////////
    /**
     * 文件权限
     */
    @AfterPermissionGranted(102)
    private fun filePermission() {
        val perms = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (hasPermissions(*perms)) {//检查是否获取该权限
            //获取权限成功
            downLoadFile()
        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "读写文件权限未开启,请先在设置中打开存储权限", 102, *perms)
        }
    }

    private var downLoadProgressCallBack: DownLoadProgressCallBack? = null
    private var downLoadSubscriber: DownLoadSubscriber<ResponseBody>? = null
    /**
     * 文件下载
     */
    private fun downLoadFile() {
        val destFileDir = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            Environment.getExternalStorageDirectory().absolutePath + File.separatorChar + "eWealth"
        } else context?.cacheDir?.path + File.separatorChar + "eWealth"
        if (!fileName.isNullOrBlank() && !downLoadUrl.isNullOrBlank()) {
            downLoadProgressCallBack =
                DownLoadProgressCallBack(destFileDir, fileName!!.replace("/", "_"))
            downLoadSubscriber =
                DownLoadManager.getInstance().load(downLoadUrl, downLoadProgressCallBack)
        }
    }

    /**
     * 下载进度监听
     */
    private inner class DownLoadProgressCallBack(destFileDir: String, destFileName: String) :
        ProgressCallBack<ResponseBody>(destFileDir, destFileName) {
        override fun onStart() {
            showLoading()
        }

        override fun onCompleted() {
            hideLoading()
        }

        override fun onSuccess(responseBody: ResponseBody?, saveFile: File?) {
            //成功后操作
            val bundel = Bundle().apply {
                putBoolean(FileViewerActivity.SHARED, true)
                putString(FileViewerActivity.FILE_NAME, fileName)
                putString(FileViewerActivity.FILE_PATH, saveFile?.absolutePath)
            }
            ActivityHelper.jumpToPDFViewActivity(context!!, bundel)
        }

        override fun progress(progress: Long, total: Long, tag: String) {

        }

        override fun onError(e: Throwable) {
            e.printStackTrace()
            showMsg("查看失败！请重试")
            hideLoading()
        }
    }

    override fun onDestroyView() {
        popupViews.clear()
        downLoadProgressCallBack = null
        downLoadSubscriber = null
        super.onDestroyView()
    }
}