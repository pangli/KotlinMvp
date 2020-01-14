package com.zorro.kotlin.samples.db.manger


import com.zorro.kotlin.baselibs.base.BaseApplication
import com.zorro.kotlin.samples.db.DaoMaster
import com.zorro.kotlin.samples.db.DaoSession
import org.greenrobot.greendao.query.QueryBuilder


/**
 * Created by Zorro on 2019/11/5 13:27
 * 备注:数据库初始化
 */
class GreenDaoManager {
    var daoMaster: DaoMaster? = null
        private set
    var daoSession: DaoSession? = null
        private set
    private var devOpenHelper: DaoMaster.DevOpenHelper? = null


    companion object {
        //private var mInstance: GreenDaoManager? = null //单例
        private const val DATA_BASE_NAME = "club-db"

//        //保证异步处理安全操作
//        val instance: GreenDaoManager
//            get() {
//                if (mInstance == null) {
//                    synchronized(GreenDaoManager::class.java) {
//                        if (mInstance == null) {
//                            mInstance = GreenDaoManager()
//                        }
//                    }
//                }
//                return mInstance!!
//            }
        /**
         * 双重校验
         */
        val instance: GreenDaoManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GreenDaoManager()
        }
    }


    init {
//        if (instance == null) {
        devOpenHelper = DaoMaster.DevOpenHelper(BaseApplication.instance, DATA_BASE_NAME, null)
        daoMaster = DaoMaster(devOpenHelper?.writableDatabase)
        daoSession = daoMaster?.newSession()
//        }
    }

    val newDaoSession: DaoSession?
        get() {
            daoSession = daoMaster?.newSession()
            return daoSession
        }

    /**
     * 打开输出日志，默认关闭
     */
    fun setDebug() {
        QueryBuilder.LOG_SQL = true
        QueryBuilder.LOG_VALUES = true
    }

    /**
     * 关闭所有的操作，数据库开启后，使用完毕要关闭
     */
    fun closeConnection() {
        closeHelper()
        closeDaoSession()
    }

    private fun closeHelper() {
        if (devOpenHelper != null) {
            devOpenHelper?.close()
            devOpenHelper = null
        }
    }

    private fun closeDaoSession() {
        if (daoSession != null) {
            daoSession?.clear()
            daoSession = null
        }
    }


}
