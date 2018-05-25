package jmm.jmmworkplan.bean

/**
 * user:Administrator
 * time:2018 05 24 15:47
 * package_name:jmm.jmmworkplan.bean
 */
class WorkInfo {

    private var total: Int = 0
    private var data: List<DataBean>? = null
    fun getTotal(): Int {
        return total
    }

    fun setTotal(total: Int) {
        this.total = total
    }

    fun getData(): List<DataBean>? {
        return data
    }

    fun setData(data: List<DataBean>) {
        this.data = data
    }

    class DataBean {

        var date: String? = null
        var gmt_create: String? = null
        var user_id: String? = null
        var plan_content: String? = null
        var plan_order: String? = null
        var name: String? = null
        var id: String? = null
        var gmt_modified: String? = null
    }

}