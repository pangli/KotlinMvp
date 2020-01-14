package com.zorro.kotlin.samples.bean

import com.contrarywind.interfaces.IPickerViewData

/**
 * Created by Zorro on 2019/12/24.
 * 备注：资料库
 */


//其他筛选条件
class OtherFilterData : IPickerViewData {
    var name: String = ""
    var code: String? = ""
    var startAge: String? = ""
    var endAge: String? = ""

    constructor(name: String, code: String?) {
        this.name = name
        this.code = code
    }

    constructor(name: String, startAge: String, endAge: String?) {
        this.name = name
        this.startAge = startAge
        this.endAge = endAge
    }

    override fun getPickerViewText(): String = name
}


class OtherFilterDataList {
    var orderStutasList: MutableList<OtherFilterData> = mutableListOf()

    constructor() {
        orderStutasList.add(OtherFilterData("全部", ""))
        orderStutasList.add(OtherFilterData("预约中", "0"))
        orderStutasList.add(OtherFilterData("预约中", "1"))
        orderStutasList.add(OtherFilterData("已接收", "2"))
        orderStutasList.add(OtherFilterData("已预约", "3"))
        orderStutasList.add(OtherFilterData("取消", "4"))
        orderStutasList.add(OtherFilterData("退保", "5"))
        orderStutasList.add(OtherFilterData("已生效", "6"))
        orderStutasList.add(OtherFilterData("已过犹", "7"))
        orderStutasList.add(OtherFilterData("已结算", "8"))
        orderStutasList.add(OtherFilterData("照会中", "9"))
        orderStutasList.add(OtherFilterData("已签已付", "10"))
        orderStutasList.add(OtherFilterData("已签未完成付款", "11"))
        orderStutasList.add(OtherFilterData("核保中", "12"))
        orderStutasList.add(OtherFilterData("未签单", "13"))
        orderStutasList.add(OtherFilterData("退回", "14"))
        orderStutasList.add(OtherFilterData("已签部分付款", "15"))
        orderStutasList.add(OtherFilterData("搁置受保", "16"))
        orderStutasList.add(OtherFilterData("保险公司拒保", "17"))
    }

    fun getName(code: String?): String {
        var name = ""
        orderStutasList.forEach {
            if (code == it.code) {
                name = it.name
            }
        }
        return name

    }

}


//资料
data class DatabaseFiles(
    var count: Int,
    var list: List<DatabaseFile>
)

data class DatabaseFile(
    var category: Int,
    var create_time: String,
    var datum_type: String,
    var describe: String,
    var file_name: String,
    var file_size: Int,
    var id: Int,
    var local_path: String,
    var modify_time: String,
    var mx_supplier_id: String,
    var product_id: String,
    var product_name: String,
    var status: Int,
    var supplier_id: String,
    var supplier_name: String,
    var url: String,
    var use_type: String
)

//产品列表
data class ProductData(
    var currency_list: List<Currency>,
    var name: String,
    var period_list: List<Period>,
    var product_id: String?
)

data class Currency(
    var currencyCode: String
)

data class Period(
    var sublineItemName: String
)

//公司信息
class SupplierData(var name: String, supplier_id: String) : IPickerViewData {
    var supplier_id: String? = supplier_id
    var child: MutableList<PlanProductData>? = null
    override fun getPickerViewText(): String = name
}

//产品信息
class PlanProductData : IPickerViewData {
    var id: String? = ""
    var product_id: String? = ""
    var name: String = ""
    var name_hk: String? = ""
    var supplier_id: String? = ""
    var data: String? = ""
    var sname: String? = ""
    var tag_id: String? = ""
    var age_type: String? = ""
    var tag_type: String? = ""
    var prdItemPaymode: MutableList<PrdItemData>? = null
    var prdItemSublineList: MutableList<PrdItemSublineData>? = null
    override fun getPickerViewText(): String = name
}

data class PrdItemData(
    var currencyCode: String
)

data class PrdItemSublineData(
    var yearPeriod: String,
    var sublineItemName: String
)