package com.wiserun.myvhr.service;

import com.wiserun.myvhr.bean.Menu;
import com.wiserun.myvhr.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuService  {
        @Autowired
        private MenuMapper menuMapper;

        //获取所有菜单
        public List<Menu> getAllMenu(){
            List<Menu> menus=menuMapper.getAllMenu();
            return menus;
        }

}
