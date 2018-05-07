package com.codeiscoffee.processing.controller;

import com.codeiscoffee.processing.service.MessageCountService;
import com.codeiscoffee.processing.service.SalesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class SalesContollerErrorTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MessageCountService messageCountService;
    @MockBean
    private SalesService salesService;

    @Test
    public void testMessageLimit() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/sale").param("productType", "apple").param("value", "0.10");
        Mockito.when(messageCountService.getSuccessfulMessages()).thenReturn(50);
        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isForbidden());
        result.andExpect(content().json("{\"errorMessage\":\"There have been 50 successfully processed messages. Therefore this process has been paused.\",\"productType\":\"apple\",\"value\":0.1}"));
    }

    @Test
    public void testInternalServerError() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/sale").param("productType", "apple").param("value", "0.10");
        String message = "Error occurred when attempting to retrieve sales data";
        Mockito.when(salesService.getSales()).thenThrow(new RuntimeException(message));
        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isInternalServerError());
        result.andExpect(content().json("{\"errorMessage\":\"" + message + "\",\"productType\":\"apple\",\"value\":0.1}"));
    }
}