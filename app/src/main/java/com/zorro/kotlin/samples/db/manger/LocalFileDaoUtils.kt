package com.zorro.kotlin.samples.db.manger

import com.orhanobut.logger.Logger
import com.zorro.kotlin.samples.db.LocalFileDao
import com.zorro.kotlin.samples.db.entity.LocalFile


/**
 * Created by Zorro on 2019/11/5 13:27
 * 备注：数据库文件下载表处理工具
 */
class LocalFileDaoUtils {

    private val mManager: GreenDaoManager = GreenDaoManager.instance
    /**
     * 完成LocalFile记录的插入，如果表未创建，先创建LocalFile表
     *
     * @param localFile
     * @return
     */
    fun insertLocalFile(localFile: LocalFile): Boolean {
        var flag = false
        try {
            flag = mManager.daoSession?.localFileDao?.insert(localFile) != -1L
            Logger.i("insert LocalFile :$flag->$localFile")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return flag
    }

    /**
     * 完成LocalFile记录的插入或者更新，如果表未创建，先创建LocalFile表
     *
     * @param localFile
     * @return
     */
    fun insertOrReplaceLocalFile(localFile: LocalFile): Boolean {
        var flag = false
        try {
            flag = mManager.daoSession?.localFileDao?.insertOrReplace(localFile) != -1L
            Logger.i("insert LocalFile :$flag->$localFile")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return flag
    }

    /**
     * 插入多条数据，在子线程操作
     *
     * @param localFileList
     * @return
     */
    fun insertMultLocalFile(localFileList: List<LocalFile>): Boolean {
        var flag = false
        try {
            mManager.daoSession?.runInTx {
                for (localFile in localFileList) {
                    mManager.daoSession?.insertOrReplace(localFile)
                }
            }
            flag = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return flag
    }

    /**
     * 修改一条数据
     *
     * @param localFile
     * @return
     */
    fun updateLocalFile(localFile: LocalFile): Boolean {
        var flag = false
        try {
            mManager.daoSession?.update(localFile)
            flag = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return flag
    }

    /**
     * 删除单条记录
     *
     * @param localFile
     * @return
     */
    fun deleteLocalFile(localFile: LocalFile): Boolean {
        var flag = false
        try {
            //按照id删除
            mManager.daoSession?.delete(localFile)
            flag = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return flag
    }

    /**
     * 删除所有记录
     *
     * @return
     */
    fun deleteAll(): Boolean {
        var flag = false
        try {
            //按照id删除
            mManager.daoSession?.deleteAll(LocalFile::class.java)
            flag = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return flag
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    fun queryAllLocalFile(): List<LocalFile>? {
        return mManager.daoSession?.loadAll<LocalFile, Any>(LocalFile::class.java)
    }

    /**
     * 根据主键_id查询记录
     *
     * @param key
     * @return
     */
    fun queryLocalFileById(key: Long): LocalFile? {
        return mManager.daoSession?.load(LocalFile::class.java, key)
    }

    /**
     * 使用native sql进行查询操作
     */
    fun queryLocalFileByNativeSql(
        sql: String,
        conditions: Array<String>
    ): List<LocalFile>? {
        return mManager.daoSession?.queryRaw<LocalFile, Any>(
            LocalFile::class.java,
            sql,
            *conditions
        )
    }

    /**
     * 根据fileUrl查询记录
     *
     * @param fileUrl
     * @return
     */
    fun queryLocalFileByQueryBuilder(fileUrl: String): List<LocalFile>? {
        val queryBuilder = mManager.daoSession?.queryBuilder(LocalFile::class.java)
        return queryBuilder?.where(LocalFileDao.Properties.FileUrl.eq(fileUrl))?.list()
    }

    /**
     * 根据fileUrl查询记录
     *
     * @param fileUrl
     * @return
     */
    fun queryOnlyLocalFileByQueryBuilder(fileUrl: String): LocalFile? {
        val queryBuilder = mManager.daoSession?.queryBuilder(LocalFile::class.java)
        return queryBuilder?.where(LocalFileDao.Properties.FileUrl.eq(fileUrl))?.unique()
    }

    /**
     * 根据fileUrl删除记录
     *
     * @param fileUrl
     * @return
     */
    fun deleteLocalFileByQueryBuilder(fileUrl: String) {
        val queryBuilder = mManager.daoSession?.queryBuilder(LocalFile::class.java)
        val localFile =
            queryBuilder?.where(LocalFileDao.Properties.FileUrl.eq(fileUrl))?.unique()
        if (localFile != null) {
            deleteLocalFile(localFile)
        }
    }
}
