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

    @GET("ownWorkPlan/getList")
    fun getOwnWorkPlan(@QueryMap parms: Map<String, String>): Observable<WorkInfo>

    @GET("workPlan/getList")
    fun getWorkPlan(@QueryMap parms: Map<String, String>): Observable<WorkInfo>

    @FormUrlEncoded
    @POST("ownWorkPlan/save")
    fun ownWorkPlanSave(@FieldMap data: Map<String, String>): Observable<WorkInfo>
}
