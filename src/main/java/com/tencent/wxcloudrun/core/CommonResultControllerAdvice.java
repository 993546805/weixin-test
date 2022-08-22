package com.tencent.wxcloudrun.core;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * 全局返回类
 *
 * @author tu
 * @date 2022-05-12 17:23:24
 */
@ControllerAdvice
public class CommonResultControllerAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        MappingJacksonValue container = getOrCreateContainer(body);
        beforeBodyWriteInternal(container, selectedContentType, returnType, request, response);
        return container;
    }

    private void beforeBodyWriteInternal(MappingJacksonValue container, MediaType selectedContentType, MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
        Object returnBody = container.getValue();

        if(returnBody instanceof Result){
            return;
        }
        HttpStatus status = HttpStatus.OK;
        if (response instanceof ServletServerHttpResponse) {
            HttpServletResponse servletResponse =
                    ((ServletServerHttpResponse) response).getServletResponse();
            status = HttpStatus.resolve(servletResponse.getStatus());
            if (status == null) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        Result<Object> result = new Result<>(returnBody,status.value(),status.getReasonPhrase());
        container.setValue(result);
    }

    private MappingJacksonValue getOrCreateContainer(Object body) {
        return body instanceof MappingJacksonValue ? (MappingJacksonValue) body : new MappingJacksonValue(body);
    }


}
