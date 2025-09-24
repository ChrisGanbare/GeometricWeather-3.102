package wangdaye.com.geometricweather.common.retrofit.interceptors;

import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

abstract class ReportExceptionInterceptor implements Interceptor {

    void handleException(Exception e) {
        e.printStackTrace();
        // 移除了对 BuglyHelper.report(e) 的调用
    }

    Response nullResponse(Request request) {
        return new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_2)
                .code(400)
                .message("Handle an error in GeometricWeather.")
                .body(null)
                .build();
    }
}
