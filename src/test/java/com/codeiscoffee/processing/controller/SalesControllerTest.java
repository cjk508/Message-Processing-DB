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
public class SalesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testProductTypeMissingIsInvalid() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/sale").param("value", "5");
        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testValueMissingisInvalid() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/sale").param("productType", "apple");
        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testValueEmptyIsInvalid() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/sale").param("productType", "apple").param("value", "");
        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isBadRequest());
        result.andExpect(content().json("{\"errorMessage\":\"Value cannot be null\",\"productType\":\"apple\",\"value\":null}"));
    }

    @Test
    public void testProductTypeEmptyIsInvalid() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/sale").param("productType", "").param("value", "12");
        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isBadRequest());
        result.andExpect(content().json("{\"errorMessage\":\"ProductType cannot be blank\",\"productType\":\"\",\"value\":12.0}"));
    }

    @Test
    public void testValidData() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/sale").param("productType", "apple").param("value", "0.10");
        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isCreated());
        result.andExpect(content().json("{\"sale\":\"{\\\"productType\\\":\\\"apple\\\",\\\"value\\\":0.1}\",\"successfullyProcessedMessages\":1}"));
    }
}