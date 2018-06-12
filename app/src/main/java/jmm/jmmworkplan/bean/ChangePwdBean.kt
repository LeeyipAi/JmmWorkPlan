package jmm.jmmworkplan.bean

/**
 * user:Administrator
 * time:2018 05 30 15:19
 * package_name:jmm.jmmworkplan.bean
 */
class ChangePwdBean {

    var data: List<DataBean>? = null

    class DataBean {
        /**
         * id :
         * newpwd1 : 1234
         * newpwd2 : 1234
         */

        constructor(id: String?, newpwd1: String?, newpwd2: String?) {
            this.id = id
            this.newpwd1 = newpwd1
            this.newpwd2 = newpwd2
        }

        var id: String? = null
        var newpwd1: String? = null
        var newpwd2: String? = null
    }
}
