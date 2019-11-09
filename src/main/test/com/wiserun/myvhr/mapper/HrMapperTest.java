package com.wiserun.myvhr.mapper;

import com.wiserun.myvhr.bean.Hr;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
@SpringBootTest
@RunWith(SpringRunner.class)
public class HrMapperTest {
    @Autowired
    HrMapper mapper;

    @Test
    public void findByName() {
        Hr hr = mapper.findByName("admin");
        //遍历出hr信息
        System.err.println(hr);
    }
}
