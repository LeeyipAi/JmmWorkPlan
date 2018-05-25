package jmm.jmmworkplan.net;

import java.io.IOException;

import jmm.jmmworkplan.common.Constant;
import jmm.jmmworkplan.utils.SpUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * user:Administrator
 * time:2018 05 24 16:47
 * package_name:jmm.jmmworkplan.net
 */
public class ReadCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String cookie = SpUtils.INSTANCE.getString(Constant.COOKIES);
        if (!cookie.equals("")) {
            builder.addHeader("Cookie", cookie);
        }
//        if (preferences.size() != 0) {
//            for (String cookie : preferences) {
//                builder.addHeader("Cookie", cookie);
//                Log.e("OkHttp", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
//            }
//        }

        return chain.proceed(builder.build());
    }

}