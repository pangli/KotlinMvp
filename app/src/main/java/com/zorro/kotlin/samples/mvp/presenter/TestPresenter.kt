package com.zorro.kotlin.samples.mvp.presenter

import com.zorro.kotlin.baselibs.mvp.BasePresenter
import com.zorro.kotlin.samples.mvp.contract.TestContract
import com.zorro.kotlin.samples.mvp.model.TestModel

/**
 * @author Zorro
 * @date 2018/12/1
 * @desc
 */
class TestPresenter : BasePresenter<TestContract.Model, TestContract.View>(), TestContract.Presenter {

    override fun createModel(): TestContract.Model? = TestModel()

}