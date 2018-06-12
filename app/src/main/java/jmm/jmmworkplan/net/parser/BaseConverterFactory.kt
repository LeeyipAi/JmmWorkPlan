package jmm.jmmworkplan.net.parser

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class BaseConverterFactory private constructor(private val gson: Gson?) : Converter.Factory() {

    init {
        if (gson == null) throw NullPointerException("gson == null")
    }

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *> {
        val adapter = gson!!.getAdapter(TypeToken.get(type!!))
        return BaseResponseConverter(this.gson, adapter)
    }

    companion object {

        @JvmOverloads
        fun create(gson: Gson = Gson()): BaseConverterFactory {
            return BaseConverterFactory(gson)
        }
    }


}
