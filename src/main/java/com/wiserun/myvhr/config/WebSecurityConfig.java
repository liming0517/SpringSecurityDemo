package com.wiserun.myvhr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiserun.myvhr.bean.RespBean;
import com.wiserun.myvhr.common.HrUtils;
import com.wiserun.myvhr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private HrService hrService;

    @Autowired
    private CustomMetadataSource metadataSource;

    @Autowired
    private UrlAccessDecisionManager accessDecisionManager;

    @Autowired
    private AuthenticationAccessDeniedHandler deniedHandler;

    //设置角色
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    //设置无需拦截的url
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().mvcMatchers("/index.html", "/login.html", "/static/**", "/templates/**");
    }

    //设置用户
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {

                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(metadataSource);
                        o.setAccessDecisionManager(accessDecisionManager);
                        return o;
                    }
                })
                .and()
                .formLogin().loginPage("/login.html").loginProcessingUrl("/login")//loginPage指定自己的登录页面，LoginProcessingUrl form表单提交的URL
                .usernameParameter("username").passwordParameter("password")//指定form表单提交的name名称
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        System.err.println("------------------------登录失败------------------------------");
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        RespBean respBean = null;
                        if (e instanceof BadCredentialsException || e instanceof UsernameNotFoundException) {
                            respBean = RespBean.ERROR("账户名或密码输入错误");
                        } else if (e instanceof LockedException) {
                            respBean = RespBean.ERROR("账户被锁定，请联系管理员!");
                        } else if (e instanceof CredentialsExpiredException) {
                            respBean = RespBean.ERROR("密码过期，请联系管理员!");
                        } else if (e instanceof AccountExpiredException) {
                            respBean = RespBean.ERROR("账户过期，请联系管理员!");
                        } else if (e instanceof DisabledException) {
                            respBean = RespBean.ERROR("账户被禁用，请联系管理员!");
                        } else {
                            respBean = RespBean.ERROR("登录失败!");
                        }
                        httpServletResponse.setStatus(401);
                        ObjectMapper objectMapper = new ObjectMapper();
                        PrintWriter out = httpServletResponse.getWriter();
                        out.write(objectMapper.writeValueAsString(respBean));
                        out.flush();
                        out.close();

                    }
                })
                .successHandler(new AuthenticationSuccessHandler() {
                                    @Override
                                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                                        httpServletResponse.setContentType("application/json;charset=utf-8");
                                        System.err.println("------------------------登录成功------------------------------");
                                      /*  RespBean respBean=RespBean.ok("登录成功", HrUtils.getCurrentHr());
                                        PrintWriter out=httpServletResponse.getWriter();
                                        out.write(new ObjectMapper().writeValueAsString(respBean));
                                        out.flush();
                                        out.close();*/
                                        //登录成功跳转到之前的访问页面
                                        RequestCache cache = new HttpSessionRequestCache();
                                        SavedRequest savedRequest = cache.getRequest(httpServletRequest, httpServletResponse);
                                        String url=savedRequest.getRedirectUrl();
                                        httpServletResponse.sendRedirect(url);
                                    }
                                }
                )
                .permitAll()
                .and()
                .logout()
                .logoutUrl("logout")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();
                        out.write(new ObjectMapper().writeValueAsString(RespBean.ok("注销成功")));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and().csrf().disable()
                .exceptionHandling().accessDeniedHandler(deniedHandler);
    }
}
