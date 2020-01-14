package com.zorro.kotlin.samples.mvp.presenter

import com.zorro.kotlin.baselibs.net.net
import com.zorro.kotlin.baselibs.mvp.BasePresenter
import com.zorro.kotlin.samples.mvp.contract.DatabaseContract
import com.zorro.kotlin.samples.mvp.model.DatabaseModel


/**
 * @author Zorro
 * @date 2019/12/23
 * @desc 资料库
 */
class DatabasePresenter : BasePresenter<DatabaseContract.Model, DatabaseContract.View>(),
    DatabaseContract.Presenter {


    override fun createModel(): DatabaseContract.Model? = DatabaseModel()


    override fun getSupplier() {
        mModel?.getSupplier()?.net(mModel, mView, onSuccess = {
            mView?.setSupplierResult(it.result)
        })
    }

    override fun getProductList(supplierId: String) {
        mModel?.getProductList(supplierId)?.net(mModel, mView, onSuccess = {
            mView?.setProductResult(it.result)
        })
    }

    override fun getDatabaseFileList(fieldMap: Map<String, String?>) {
        mModel?.getDatabaseFileList(fieldMap)?.net(mModel, mView, false, onSuccess = {
            mView?.setDatabaseResult(it.result)
        })
    }


}