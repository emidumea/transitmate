package ro.unibuc.transitmate.ticket;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class TicketCatalogTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void visitorSeesSingleDayAndMonthlyTicketsWithPricesInRon() throws Exception {
        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(view().name("tickets"))
                .andExpect(model().attribute("offers", hasSize(3)))
                .andExpect(content().string(containsString("Bilet o calatorie")))
                .andExpect(content().string(containsString("Bilet o zi")))
                .andExpect(content().string(containsString("Abonament lunar")))
                .andExpect(content().string(containsString("3,00 RON")));
    }

    @Test
    void studentDiscountRuleIsShown() throws Exception {
        mockMvc.perform(get("/tickets"))
                .andExpect(content().string(containsString("50% reducere pentru studenti: 40,00 RON")));
    }
}
