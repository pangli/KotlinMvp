package com.zorro.kotlin.samples.mvp.contract


import com.zorro.kotlin.baselibs.mvp.IModel
import com.zorro.kotlin.baselibs.mvp.IPresenter
import com.zorro.kotlin.baselibs.mvp.IView
import com.zorro.kotlin.samples.bean.DatabaseFiles
import com.zorro.kotlin.samples.bean.HttpResult
import com.zorro.kotlin.samples.bean.ProductData
import com.zorro.kotlin.samples.bean.SupplierData
import io.reactivex.Observable


/**
 * @author Zorro
 * @date 2019/12/23
 * @desc 资料库
 */
interface DatabaseContract {

    interface View : IView {
        fun setSupplierResult(supplierData: MutableList<SupplierData>)
        fun setProductResult(productData: List<ProductData>)

        fun setDatabaseResult(databaseFiles: DatabaseFiles)
    }

    interface Presenter : IPresenter<View> {
        fun getSupplier()
        fun getProductList(supplierId: String)

        fun getDatabaseFileList(fieldMap: Map<String, String?>)
    }

    interface Model : IModel {
        fun getSupplier(): Observable<HttpResult<MutableList<SupplierData>>>
        fun getProductList(supplierId: String): Observable<HttpResult<List<ProductData>>>
        fun getDatabaseFileList(fieldMap: Map<String, String?>): Observable<HttpResult<DatabaseFiles>>
    }

}