package com.zorro.kotlin.samples.bean

/**
 * 用户信息
 */
data class UserInfo(
    var coop_id: String,
    var coop_level: Int,
    var email: String,
    var ext_data: ExtData,
    var full_ext_data: FullExtData,
    var full_head: String,
    var head: String,
    var mobile: String,
    var permission: Permission,
    var sa_agent_id: String,
    var status: Int,
    var type: Int,
    var version_num: String
)

data class ExtData(
    var banner: String,
    var logo_big: String,
    var logo_small: String
)

data class FullExtData(
    var banner: String,
    var logo_big: String,
    var logo_small: String
)

/**
 * 用户权限1表示有权限
 */
data class Permission(
    var account_add: Int,// 账号创建
    var account_list: Int,// 账号列表
    var account_save: Int, // 账号编辑
    var account_save_password: Int,// 账号修改密码
    var account_save_status: Int,// 账号停用/启用
    var brokerage: Int,// 我的账单
    var commission_save: Int,// 渠道分成设置
    var coop: Int,// 团队管理
    var coop_add: Int,// 团队创建
    var coop_list: Int,// 团队列表
    var coop_save: Int, // 团队编辑
    var coop_save_password: Int,// 团队修改密码
    var coop_save_status: Int, // 团队停用/启用
    var distribution_order: Int,//差额账单
    var face_manager: Int,//系统设置->界面管理
    var is_admin: Int,//预留,暂未使用
    var my_commission: Int,// 我的介绍费
    var platform: Int,// 工作台
    var system_setup: Int// 系统设置
)