package jmm.jmmworkplan.bean

import java.io.Serializable

/**
 * user:Administrator
 * time:2018 05 24 17:37
 * package_name:jmm.jmmworkplan.bean
 */
class SavePlan: Serializable {
    private var id: String? = null
    private var data: List<DataBean>? = null


    fun getId(): String? {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getData(): List<DataBean>? {
        return data
    }

    fun setData(data: List<DataBean>) {
        this.data = data
    }


    class DataBean : Serializable {
        constructor(pageType: String?, id: String, date: String?, plan_content: String?) {
            this.pageType = pageType
            this.id = id
            this.date = date
            this.plan_content = plan_content
        }

        var pageType: String? = null
        var id: String? = null
        var date: String? = null
        var plan_content: String? = null
    }
}