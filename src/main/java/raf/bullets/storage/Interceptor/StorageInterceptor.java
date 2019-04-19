package raf.bullets.storage.Interceptor;

import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;
import raf.bullets.LocalStorageOperations;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class StorageInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie cookie = WebUtils.getCookie(request, "storage");
        if(cookie != null) {
            String storageType = Objects.requireNonNull(WebUtils.getCookie(request, "storage")).getValue();

            request.setAttribute("FolderBasicOperations", new LocalStorageOperations());

            System.out.println(storageType);
        }

        return super.preHandle(request, response, handler);
    }
}
