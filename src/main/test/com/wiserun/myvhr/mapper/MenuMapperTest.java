package com.wiserun.myvhr.mapper;

import com.wiserun.myvhr.bean.Menu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MenuMapperTest {
    @Autowired
    MenuMapper menuMapper;
    @Test
    public void a(){
        List<Menu> menus =menuMapper.getAllMenu();
        for(Menu menu:menus){
            System.err.println(menu);
        }

    }
}
