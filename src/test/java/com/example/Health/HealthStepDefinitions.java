package com.example.Health;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

public class HealthStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    private MvcResult mvcResult;

    @Given("애플리케이션이 실행 중이다")
    public void 애플리케이션이_실행_중이다() {
        // WebMvcTest 기반으로 MockMvc가 주입되면 웹 테스트 환경이 준비된 것입니다.
        assertThat(mockMvc).isNotNull();
    }

    @When("클라이언트가 {string} 엔드포인트로 GET 요청을 보낸다")
    public void 클라이언트가_엔드포인트로_get_요청을_보낸다(String endpoint) throws Exception {
        // 실제 서버를 띄우지 않고 MockMvc로 컨트롤러를 호출합니다.
        mvcResult = mockMvc.perform(get(endpoint))
                .andReturn();
    }

    @Then("응답 상태 코드는 {int}이다")
    public void 응답_상태_코드는_이다(int expectedStatus) {
        // HTTP 상태 코드를 검증합니다.
        int actualStatus = mvcResult.getResponse().getStatus();
        assertThat(actualStatus).isEqualTo(expectedStatus);
    }

    @Then("응답 본문은 {string} 이다")
    public void 응답_본문은_이다(String expectedBody) throws Exception {
        // 응답 본문이 "ok"인지 검증합니다.
        String actualBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualBody).isEqualTo(expectedBody);
    }
}