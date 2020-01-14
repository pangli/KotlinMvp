package com.zorro.kotlin.samples.mvp.model

import com.zorro.kotlin.baselibs.mvp.BaseModel
import com.zorro.kotlin.samples.bean.DatabaseFiles
import com.zorro.kotlin.samples.bean.HttpResult
import com.zorro.kotlin.samples.bean.ProductData
import com.zorro.kotlin.samples.bean.SupplierData
import com.zorro.kotlin.samples.mvp.contract.DatabaseContract
import com.zorro.kotlin.samples.network.MainRetrofit
import io.reactivex.Observable

/**
 * @author Zorro
 * @date 2019/12/23
 * @desc 资料库
 */
class DatabaseModel : BaseModel(), DatabaseContract.Model {
    override fun getSupplier(): Observable<HttpResult<MutableList<SupplierData>>> {
        return MainRetrofit.service.getSupplier()
    }

    override fun getProductList(supplierId: String): Observable<HttpResult<List<ProductData>>> {
        return MainRetrofit.service.getProductList(supplierId = supplierId)
    }

    override fun getDatabaseFileList(fieldMap: Map<String, String?>): Observable<HttpResult<DatabaseFiles>> {
        return MainRetrofit.service.getDatabaseFileList(fieldMap = fieldMap)
    }
}