package jmm.jmmworkplan.net

import jmm.jmmworkplan.bean.WorkInfo
import retrofit2.http.*
import rx.Observable

interface Api {
    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("login")
    fun login(@FieldMap parms: Map<String, String>): Observable<BaseResp<String>>

    /**
     * 我的工作计划列表
     */
    @GET("ownWorkPlan/getList")
    fun getOwnWorkPlan(@QueryMap parms: Map<String, String>): Observable<WorkInfo>

    /**
     * 工作计划列表
     */
    @GET("workPlan/getList")
    fun getWorkPlan(@QueryMap parms: Map<String, String>): Observable<WorkInfo>

    /**
     * 创建工作计划
     */
    @FormUrlEncoded
    @POST("ownWorkPlan/save")
    fun ownWorkPlanSave(@FieldMap data: Map<String, String>): Observable<WorkInfo>

    /**
     * 删除工作计划
     */
    @GET("ownWorkPlan/delete")
    fun workPlanDelete(@QueryMap parms: Map<String, String>): Observable<BaseResp<String>>

    /**
     * 修改密码
     */
    @FormUrlEncoded
    @POST("password/changepwd1")
    fun changepwd(@FieldMap parms: Map<String, String>): Observable<BaseResp<String>>

    /**
     * 获取首页壁纸
     */
    @GET("https://cn.bing.com/HPImageArchive.aspx?format=xml&idx=0&n=1")
    fun getBizhi(): Observable<BaseResp<String>>

    /**
     * 检查更新
     */
    @GET("http://wanandroid.com/tools/mockapi/2345/checkUpdate")
    fun checkUpdate(): Observable<BaseResp<String>>

}
