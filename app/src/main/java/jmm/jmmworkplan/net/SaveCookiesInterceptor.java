package jmm.jmmworkplan.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jmm.jmmworkplan.common.Constant;
import jmm.jmmworkplan.utils.SpUtils;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * user:Administrator
 * time:2018 05 24 16:47
 * package_name:jmm.jmmworkplan.net
 */
public class SaveCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            List cookies = new ArrayList<String>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                if (!header.contains("JSESSIONID")) {
                    cookies.add(header);
                }
            }
            if (!cookies.isEmpty()) {
                SpUtils.INSTANCE.putString(Constant.COOKIES, cookies.get(0).toString());
            }

//            Preferences.getDefaultPreferences().edit()
//                    .putStringSet(Preferences.PREF_COOKIES, cookies)
//                    .apply();
        }

        return originalResponse;
    }
}