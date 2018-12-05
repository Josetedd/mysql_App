package api.interceptor;


import android.support.annotation.NonNull;
import android.util.Base64;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class AccessTokenIterceptor implements Interceptor {

    public AccessTokenIterceptor() {

    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        String keys = "A2IPhBB25Wc3N5Tcgw1pkrpWipJSQvUn:7LAQcFucPLvvpRMU";

        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Basic " + Base64.encodeToString(keys.getBytes(), Base64.NO_WRAP))
                .build();
        return chain.proceed(request);
    }
}
