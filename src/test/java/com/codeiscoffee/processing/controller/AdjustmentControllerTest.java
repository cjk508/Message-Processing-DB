package com.codeiscoffee.processing.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) //just in case
@AutoConfigureMockMvc
@SpringBootTest
public class AdjustmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testValidData() throws Exception {
        String productType = "apple";
        String value = "0.10";
        MockHttpServletRequestBuilder addSale = MockMvcRequestBuilders.post("/sale").param("productType", productType).param("value", value);
        mockMvc.perform(addSale);

        MockHttpServletRequestBuilder adjustment = MockMvcRequestBuilders.post("/adjustment")
                .param("productType", productType)
                .param("value", value)
                .param("operator", "ADD");
        ResultActions sutResult = mockMvc.perform(adjustment);

        sutResult.andExpect(status().isCreated());
        sutResult.andExpect(content().json("{\"adjustment\":{\"productType\":\"apple\",\"operator\":\"ADD\",\"value\":0.1,\"affectedSales\":1},\"successfullyProcessedMessages\":2}"));
    }

    @Test
    public void testInvalidSubtraction() throws Exception {
        String productType = "apple";
        String value = "0.10";
        MockHttpServletRequestBuilder addSale = MockMvcRequestBuilders.post("/sale").param("productType", productType).param("value", value);
        mockMvc.perform(addSale);

        MockHttpServletRequestBuilder adjustment = MockMvcRequestBuilders.post("/adjustment")
                .param("productType", productType)
                .param("value", "0.20")
                .param("operator", "SUBTRACT");
        ResultActions sutResult = mockMvc.perform(adjustment);

        sutResult.andExpect(status().isBadRequest());
        sutResult.andExpect(content().json("{\"errorMessage\":\"After adjustment the value for this product would be less than 0. All values must remain greater than or equal to 0\"," +
                "\"productType\":\"apple\",\"value\":0.2,\"operator\":\"subtract\",\"successfullyProcessedMessages\":1}"));
    }
}