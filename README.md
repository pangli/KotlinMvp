# Kotlin 版 MVP 基础框架简介

> 快速搭建 Kotlin+MVP+RxJava+Retrofit+EventBus 的框架，方便快速开发新项目、减少开发成本，让所写的代码更加简洁，让开发者只需要关注业务的实现。

## 代码结构

#### 1. View 层的基类封装

- `BaseActivity` 、 `BaseFragment` 是抽象类，封装了布局文件 `ID` 、初始化 `View` 、初始化数据、开始请求、是否使用 `EventBus` 、状态栏等；
- `BaseMvpActivity` 、 `BaseMvpFragment` 分别继承 `BaseActivity` 和 `BaseFragment` 并实现了 `IView` 接口，将 `MVP` 基础架构封装起来；注：如果想使用 `MVP` 架构就继承 `BaseMvpActivity` 或者 `BaseMvpFragment` ，如果不适用 `MVP` 架构就继承 `BaseActivity` 或者 `BaseFragment` ；

#### 2. net 相关的封装（主要用到了 Kotlin 扩展函数）

- 封装 `loge` 、 `showToast` 、 `showSnackMsg` 、`net` 、 `net2` 等通用方法，项目中可以直接调用；
- `net` 、 `net2` ，这两个方法主要是对网络请求的统一封装，使用起来非常方便；
```
 mModel?.getUserInfo()?.net(mModel, mView, onSuccess = {
            mView?.httpResultUserInfo(it.result)
        })
addDisposable(
     mModel?.getUserInfo()?.net2(mModel, mView, onSuccess = {
                mView?.httpResultUserInfo(it.result)
            })
)
```

> 这里贴上 `net` 和 `net2` 方法的代码：

```
fun <T : BaseBean> Observable<T>.net(
    model: IModel?,
    view: IView?,
    isShowLoading: Boolean = true,
    onSuccess: (T) -> Unit,
    onError: ((T) -> Unit)? = null
) {
    this.compose(SchedulerUtils.ioToMain())
        .retryWhen(RetryWithDelay())
        .subscribe(object : Observer<T> {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onSubscribe(d: Disposable) {
                if (isShowLoading) {
                    view?.showLoading()
                }
                model?.addDisposable(d)
                if (!NetWorkUtil.isConnected()) {
                    view?.showError("当前网络不可用，请检查网络设置")
                    d.dispose()
                    onComplete()
                }
            }

            override fun onNext(t: T) {
                when (t.code) {
                    HttpStatus.SUCCESS -> onSuccess.invoke(t)
                    HttpStatus.TOKEN_INVALID -> {
                        // Token 过期，重新登录
                        view?.tokenInvalid(t.msg)
                    }
                    else -> {
                        onError?.invoke(t)
                        if (!t.msg.isNullOrBlank())
                            view?.showError(t.msg)
                    }
                }
            }

            override fun onError(t: Throwable) {
                view?.hideLoading()
                view?.showError(ExceptionHandle.handleException(t))
            }
        })
}
```
```
fun <T : BaseBean> Observable<T>.net2(
    view: IView?,
    isShowLoading: Boolean = true,
    onSuccess: (T) -> Unit,
    onError: ((T) -> Unit)? = null
): Disposable {
    if (isShowLoading) {
        view?.showLoading()
    }
    return this.compose(SchedulerUtils.ioToMain())
        .retryWhen(RetryWithDelay())
        .subscribe({
            when (it.code) {
                HttpStatus.SUCCESS -> onSuccess.invoke(it)
                HttpStatus.TOKEN_INVALID -> {
                    // Token 过期，重新登录
                    view?.tokenInvalid(it.msg)
                }
                else -> {
                    onError?.invoke(it)
                    if (!it.msg.isNullOrBlank())
                        view?.showError(it.msg)
                }
            }
            if (isShowLoading) {
                view?.hideLoading()
            }
        }, {
            view?.hideLoading()
            view?.showError(ExceptionHandle.handleException(it))
        })
```

#### 3. 网络通讯通用类

- 封装 `RetrofitFactory` 来构建不同 `baseUrl` 的 `RetrofitService` （**注：项目中 baseUrl 很多的情况下，不建议使用，建议重新封装**） ；
- 封装 `cookie` 相关、统一的异常处理、 `CacheInterceptor` 、 `HeaderInterceptor` 、 `SaveCookieInterceptor` 等；
- 封装 请求重连 操作，详情请见类 `RetryWithDelay` 。

```
object MainRetrofit : RetrofitFactory<MainApi>() {
    override fun baseUrl(): String = Constant.BASE_URL
    override fun getService(): Class<MainApi> = MainApi::class.java
}
interface MainApi {
     /**
        * 换取用户信息
        */
       @GET("saagent/{token}")
       fun getUserInfo(@Header("x-access-token") header: String, @Path("token") token: String): Observable<HttpResult<UserInfo>>
}
```

#### 4. 网络下载相关

- 封装 文件下载管理，一行代码实现下载 `DownLoadManager` 。

```
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
            ActivityHelper.jumpToFileViewerActivity(context!!, bundel)
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
```

#### 5. MVP 基础架构

**MVP 即是 Model , View , Presenter 三层，把 Activity 中的 UI 逻辑抽象成 View 接口，把业务逻辑抽象成 Presenter 接口， Model 类还是原来的 Model ，实现了 Model 层和 View 层完全解耦。**

> 用户触发 V 层事件， V 层把事件通知 P 层， P 层通知 M 层处理这个事件， M 层处理完之后把结果发送给 P 层， P 层再发送给 V 层，最后 V 层做出相应的处理，这是 MVP 架构的一整套流程。

- M 层：模型层，负责数据的请求、解析、过滤等操作；
- V 层：视图层，负责视图部分展示、视图时间处理， `Activity` 、 `Fragment` 、 `Dialog` 、 `ViewGroup` 等呈现视图的组件都可以承担该角色；
- P 层：模型层和视图层交互的桥梁。  

##### MVP 架构的优缺点

**A. 优点**

- 模块职责划分明显,层次清晰,接口功能清晰；

- Model层和View层分离,解耦.修改 `View` 而不影响 `Model` ；

- 功能复用度高,方便.一个 `Presenter` 可以复用于多个 `View` ,而不用更改 `Presenter` 的逻辑；

- 有利于测试驱动开发,以前的 `Android` 开发是难以进行单元测试；

- 如果后台接口还未写好,但已知返回数据类型的情况下,完全可以写出此接口完整的功能。

**B. 缺点**

- `MVP` 中接口过多；

- 每一个功能,相比于 `MVC` 要多写好几个文件；

- 如果某一个界面中需要请求多个服务器接口,这个界面文件中会实现很多的回调接口,导致代码繁杂；

- 如果更改了数据源和请求中参数,会导致更多的代码修改；

- 额外的代码复杂度及学习成本。

#### 6. rx 相关

- 封装 `SchedulerUtils` 工具类、 `IoMainScheduler` 等；
- `BaseObserver` 和 `BaseSubscriber` 对 `ResourceObserver` 和 `ResourceSubscriber` 通用封装。

#### 7. 工具类和帮助类

- 封装 `Preference` 类，主要采用了 `kotlin` 委托属性和 `SharedPreference` 的实例；
- 封装 `AppManagerDelegate` （Activity管理类） 、`CacheDataUtil` （缓存相关） 、`StatusBarUtil` （适配状态栏） 、 `KeyBoardUtil` （键盘相关） 、 `NetWorkUtil` （网络相关） 、 `RomUtil` （手机ROM相关） 、 `FileProvider7` （7.0手机文件适配） 等。

#### 8. 自定义控件
- 封装 `DropDownMenu` 、`EasySwipeMenuLayout` 、`DialogUtils` 、`Toast` 、 `LoadingView` 和 `OnNoDoubleClickListener` （防止连续点击）。

#### 9. adapter
- 封装 `IMAdapter` 快速高效实现聊天室界面UI的搭建。

#### 10. Kotlin Coroutines
-  `Run` kotlin协程实现定时任务测试。

#### 11. WebView
-  `WebViewController` WebView的基础封装，防止内存泄漏。

## 如何使用？

> 只需要五步就可以实现 MVP 架构。

##### 第一步：导入baselibs库

> `Clone or Download` 后导入 `baselibs` 库，再根据需求自行修改即可。

##### 第二步：定义一个Contract

需要定义一个 `Contract` 接口，来抽象 `View`、`Presenter`、`Model` 的方法，并且它们需要分别继承 `IView`、`IPresenter`、`IModel` 接口。
> 案例： MainContract

```
interface MainContract {
    interface View : IView {
        fun httpResultUserInfo(userInfo: UserInfo)
    }

    interface Presenter : IPresenter<View> {
        fun getUserInfo()
    }

    interface Model : IModel {
        fun getUserInfo(): Observable<HttpResult<UserInfo>>
    }

}
```

##### 第三步：创建一个Model接口的实现类，需要继承 BaseModel 类 并实现Model接口
> 案例： MainModel

```
class MainModel : BaseModel(), MainContract.Model {
    override fun getUserInfo(): Observable<HttpResult<UserInfo>> {
        return MainRetrofit.service.getUserInfo(Constant.token, Constant.token)
    }
}
```

##### 第四步：创建一个Presenter接口的实现类，需要继承 BasePresenter<M : IModel, V : IView> 并实现 MainContract.Presenter 接口

> 案例： MainPresenter

```
class MainPresenter :
    BasePresenter<MainContract.Model, MainContract.View>(),
    MainContract.Presenter {

    override fun createModel(): MainContract.Model? = MainModel()

    override fun getUserInfo() {
        mModel?.getUserInfo()?.net(mModel, mView, onSuccess = {
            mView?.httpResultUserInfo(it.result)
        })
    }

}
```

##### 第五步：创建一个View的接口实现类，这是一个Activity或者Fragment，需要继承BaseMvpActivity<in V : IView, P : IPresenter<V>> 或者 BaseMvpFragment<in V : IView, P : IPresenter<V>> 并实现 View 接口
> 案例：MainActivity

```
class MainActivity : BaseMvpActivity<MainContract.View, MainContract.Presenter>(), MainContract.View {
    override fun attachChildLayoutRes(): Int = R.layout.activity_main
    override fun createPresenter(): MainContract.Presenter = MainPresenter()
    override fun initView() {
    }
    override fun bindListener() {
    }
    override fun initData() {
    }
    override fun start() {
        mPresenter?.getUserInfo()
    }
    override fun httpResultUserInfo(userInfo: UserInfo) {
        tv_result.text = userInfo.email
    }
}
```

**至此， `Kotlin` 基础框架已经搭建完成，如有错误之处还请指正。**

## LICENSE

```
Copyright 2020 Zorro 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```