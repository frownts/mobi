package com.join.android.app.common.rest;

import android.util.Log;
import com.join.android.app.common.rest.dto.Recommend;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import java.io.IOException;
import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-2-21
 * Time: 下午3:01
 * {"breturn":true,"success":false,"errorinfo":null,"ireturn":-1,"object":[{"id":17,"stype":"1","vtype":"2","pid":10,"pname":"??","picurl":"http://218.1.73.212:8888/vimg/upload/2013/11/29/5179b5ce-e7da-44ff-8cbd-514e6e89e341.jpg","addtime":1390224356000,"edittime":1390224356000,"slock":"0"},{"id":16,"stype":"1","vtype":"2","pid":12,"pname":"??","picurl":"http://218.1.73.212:8888/vimg/upload/2013/11/28/e2c70188-a396-49c4-898a-86b7f20ee4b7.jpg","addtime":1390224299000,"edittime":1390224299000,"slock":"0"},{"id":15,"stype":"1","vtype":"2","pid":29,"pname":"?????","picurl":"http://218.1.73.212:8888/vimg/upload/2013/12/1/57549558-be33-48f5-9a93-2cb9ae7f055c.jpg","addtime":1390224246000,"edittime":1390224246000,"slock":"0"},{"id":14,"stype":"1","vtype":"2","pid":40,"pname":"???","picurl":"http://218.1.73.212:8888/vimg/upload/2013/12/15/0d4edcdc-98d6-4ead-a779-3732ffe19a91.jpg","addtime":1390224145000,"edittime":1390224145000,"slock":"0"},{"id":12,"stype":"1","vtype":"2","pid":23,"pname":"??","picurl":"http://218.1.73.212:8888/vimg/upload/2013/12/3/031345f6-bb36-422d-992e-76677e44d231.jpg","addtime":1390224083000,"edittime":1390224083000,"slock":"0"},{"id":11,"stype":"1","vtype":"2","pid":26,"pname":"??","picurl":"http://218.1.73.212:8888/vimg/upload/2013/12/3/8d2d74ae-d845-4ee3-a8b9-862f23dcd815.jpg","addtime":1390224044000,"edittime":1390224044000,"slock":"0"},{"id":10,"stype":"1","vtype":"2","pid":22,"pname":"??","picurl":"http://218.1.73.212:8888/vimg/upload/2013/11/29/c423a1a0-340b-4242-bb25-61f5b9610372.jpg","addtime":1390223974000,"edittime":1390223974000,"slock":"0"},{"id":5,"stype":"1","vtype":"2","pid":13,"pname":"???","picurl":"http://218.1.73.212:8888/vimg/upload/2013/11/29/d70caaea-5eb6-48a4-aeec-a06fe4932c4f.jpg","addtime":1388543906000,"edittime":1388543906000,"slock":"0"}]}
 */
@Rest(rootUrl = "http://192.168.1.102:8080/kl", converters = MappingJacksonHttpMessageConverter.class,interceptors = HttpBasicAuthenticatorInterceptor.class)
public interface BookmarkClient extends RestClientHeaders {
    @Get("/json")
    RPCResult<List<Recommend>> getAccounts();
//    @Get("/json")
//    ResponseEntity getAccoutsRSEntityResult();

}

 class HttpBasicAuthenticatorInterceptor implements ClientHttpRequestInterceptor {
     public HttpBasicAuthenticatorInterceptor(){}
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] data, ClientHttpRequestExecution execution) throws IOException {
        // do something
        Log.d(BookmarkClient.class.getName(),"retrieve from"+request.getURI().toString());
        return execution.execute(request, data);
    }
}
