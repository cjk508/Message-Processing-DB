package com.codeiscoffee.processing;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessingApplicationTest {

    @Test
    public void contextLoads() {
        ProcessingApplication.main(new String[]{
                // Override any other environment properties according to your needs
        });
        Assert.assertTrue("If this fails then the context cannot be loaded", true);
    }

}
