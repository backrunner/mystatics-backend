package top.backrunner.installstat.config.shiro;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class OptionsRequestFilter extends FormAuthenticationFilter {
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue){
        if ("OPTIONS".equals(((HttpServletRequest)request).getMethod().toUpperCase())){
            return true;
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }
}
