package com.wiserun.myvhr.common;

import com.wiserun.myvhr.bean.Hr;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.context.SecurityContextHolder;

public class HrUtils {
    public  static Hr getCurrentHr(){
        return (Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
