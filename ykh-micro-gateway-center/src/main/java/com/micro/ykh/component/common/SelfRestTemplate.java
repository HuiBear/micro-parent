package com.micro.ykh.component.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;


/**
 * @ClassName SelfRestTemplate
 * @Description 由于在InitlizerBean的时候，Ribbon还没有加载到spring容器中，所以自己实现一个简单的负载均衡
 * @Author xiongh
 * @Date 2020/11/18 22:16
 * @Version 1.0
 **/
public class SelfRestTemplate extends RestTemplate {

    private static Logger logger = LogManager.getLogger(SelfRestTemplate.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    protected <T> T doExecute(URI url, @Nullable HttpMethod method, @Nullable RequestCallback requestCallback,
                              @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException {

        Assert.notNull(url, "URI is required");
        Assert.notNull(method, "HttpMethod is required");
        ClientHttpResponse response = null;
        try {
            logger.info("替换前的url:{}", url.toString());
            url = replaceUrl(url);
            logger.info("替换后的url:{}", url.toString());
            ClientHttpRequest request = createRequest(url, method);
            if (requestCallback != null) {
                requestCallback.doWithRequest(request);
            }
            response = request.execute();
            handleResponse(url, method, response);
            return (responseExtractor != null ? responseExtractor.extractData(response) : null);
        } catch (IOException ex) {
            String resource = url.toString();
            String query = url.getRawQuery();
            resource = (query != null ? resource.substring(0, resource.indexOf('?')) : resource);
            throw new ResourceAccessException("I/O error on " + method.name() +
                    " request for \"" + resource + "\": " + ex.getMessage(), ex);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * 根据url中的微服务名称从注册中心获得所有该服务名下的url
     * 然后再随机取出一个
     *
     * @param url
     * @return
     */
    private URI replaceUrl(URI url) {
        String sourceUrl = url.toString();
        // 将url做截取，获得微服务名称
        String[] httpUrl = sourceUrl.split("//");
        int index = httpUrl[1].replaceFirst("/", "@").indexOf("@");
        String serviceName = httpUrl[1].substring(0, index);
        String apiUrl = httpUrl[1].substring(index,httpUrl[1].length());
        // 根据serviceName从注册中心获得URL
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(serviceName);
        if (CollectionUtils.isEmpty(serviceInstanceList)) {
            logger.warn("此服务没有注册到注册中心！");
            throw new RuntimeException("没有可用的微服务实例列表:" + serviceName);
        }
        ServiceInstance serviceInstance = null;
        if(serviceInstanceList.size() > 1){
            Random random = new Random(serviceInstanceList.size());
            serviceInstance = serviceInstanceList.get(random.nextInt());
        }else{
            serviceInstance = serviceInstanceList.get(0);
        }
        try {
            return new URI (serviceInstance.getUri().toString() + apiUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

}
