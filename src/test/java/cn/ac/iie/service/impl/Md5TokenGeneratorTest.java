package cn.ac.iie.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Md5TokenGeneratorTest {

    @Autowired
    Md5TokenGenerator md5TokenGenerator;

    @Autowired
    RsaServiceImpl rsaService;

    @Test
    public void generate() {
        String generate = md5TokenGenerator.generateWithTimeMills("123456");
        System.out.println(generate);

    }
}