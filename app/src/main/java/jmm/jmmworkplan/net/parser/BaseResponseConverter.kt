package jmm.jmmworkplan.net.parser


import com.google.gson.Gson
import com.google.gson.TypeAdapter
import jmm.jmmworkplan.net.BaseResp
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset

class BaseResponseConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val UTF8 = Charset.forName("UTF-8")
        var resp = value.string()
        if (resp.startsWith("{") || resp.startsWith("[")) {

        } else {
            var statusCode = ""
            when (resp) {
                "1" -> statusCode = "1"
                else -> statusCode = "0"
            }
            resp = gson.toJson(BaseResp(statusCode, resp, 0))
        }
        val reader = InputStreamReader(ByteArrayInputStream(resp.toByteArray()), UTF8)
        val jsonReader = gson.newJsonReader(reader)
        try {
            return adapter.read(jsonReader)
        } finally {
            value.close()
        }
    }

}
