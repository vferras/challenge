package com.n26.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StatisticsControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @InjectMocks
    private StatisticsController statisticsController;

    @Mock
    private StatisticsService statisticsService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .standaloneSetup(statisticsController)
                .build();
    }

    @Test
    public void given_valid_statistics_stored_when_getting_then_statistics_are_returned() throws Exception {

        Statistics statistics = Statistics.of(BigDecimal.valueOf (532.2), BigDecimal.valueOf (223.2), BigDecimal.valueOf (458.0), BigDecimal.valueOf (119.0), 3L);
        StatisticsResponse statisticsResponse = StatisticsResponse.map(statistics);

        when(statisticsService.getStatistics()).thenReturn(statistics);

        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(statisticsResponse)));
    }

    @Test
    public void given_no_statistics_stored_when_getting_then_statistics_empty_are_returned() throws Exception {

        StatisticsResponse statisticsResponse = StatisticsResponse.map(Statistics.empty());

        when(statisticsService.getStatistics()).thenReturn(Statistics.empty());

        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(statisticsResponse)));
    }
}