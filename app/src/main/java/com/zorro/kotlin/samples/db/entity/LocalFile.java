package com.zorro.kotlin.samples.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Zorro on 2019/11/5.
 * 备注：本地下载文件
 */
@Entity
public class LocalFile {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String fileUrl;
    private String localFileUrl;
    @Generated(hash = 1324325435)
    public LocalFile(Long id, String fileUrl, String localFileUrl) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.localFileUrl = localFileUrl;
    }
    @Generated(hash = 2106851084)
    public LocalFile() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFileUrl() {
        return this.fileUrl;
    }
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    public String getLocalFileUrl() {
        return this.localFileUrl;
    }
    public void setLocalFileUrl(String localFileUrl) {
        this.localFileUrl = localFileUrl;
    }
}
