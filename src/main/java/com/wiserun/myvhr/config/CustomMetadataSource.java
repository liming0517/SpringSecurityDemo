package com.wiserun.myvhr.config;

import com.wiserun.myvhr.bean.Menu;
import com.wiserun.myvhr.bean.Role;
import com.wiserun.myvhr.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

@Component
public class CustomMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private MenuService menuService;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        String requestUrl=((FilterInvocation) o).getRequestUrl();
        List<Menu> menus=menuService.getAllMenu();
        for(Menu menu:menus){
            if(antPathMatcher.match(requestUrl,menu.getUrl())&&menu.getRoles().size()>0){
                    List<Role> roles=menu.getRoles();
                    //创建一个String数组，将菜单角色的值放入数组
                Integer size=roles.size();
                String[] values= new String[size];
                for(int i =0;i<size;i++){
                    values[i]=roles.get(i).getName();
                }
                //菜单的角色
                return SecurityConfig.createList(values);
            }
        }
        //没有匹配上的资源，都是登录访问
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
