package com.novahire;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novahire.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void listsSeededJobs() throws Exception {
        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(12))
                .andExpect(jsonPath("$[0].title").isNotEmpty())
                .andExpect(jsonPath("$[0].company").isNotEmpty())
                .andExpect(jsonPath("$[0].tags").isArray());
    }

    @Test
    void filtersByKeywordCityAndSalary() throws Exception {
        mockMvc.perform(get("/api/jobs").param("keyword", "大模型"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("大模型算法工程师"));

        mockMvc.perform(get("/api/jobs").param("city", "北京"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        mockMvc.perform(get("/api/jobs").param("salary", "40+"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void sortsBySalary() throws Exception {
        mockMvc.perform(get("/api/jobs").param("sort", "salary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].salaryMax").value(70));
    }

    @Test
    void returnsJobDetail() throws Exception {
        JsonNode jobs = objectMapper.readTree(
                mockMvc.perform(get("/api/jobs")).andReturn().getResponse().getContentAsString());
        long id = jobs.get(0).get("id").asLong();

        mockMvc.perform(get("/api/jobs/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.description").isNotEmpty())
                .andExpect(jsonPath("$.requirements").isArray());

        mockMvc.perform(get("/api/jobs/{id}", 999999))
                .andExpect(status().isNotFound());
    }

    @Test
    void listsCompaniesAndStats() throws Exception {
        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(8))
                .andExpect(jsonPath("$[0].count").isNumber());

        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobs").value(12))
                .andExpect(jsonPath("$.companies").value(8));
    }

    @Test
    void favoriteFlowUsesSession() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/favorites/{id}", 1L))
                .andExpect(status().isNoContent())
                .andReturn();
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        mockMvc.perform(get("/api/favorites").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(1));

        mockMvc.perform(delete("/api/favorites/{id}", 1L).session(session))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/favorites").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void submitsApplicationAndRejectsDuplicate() throws Exception {
        String body = """
                {
                  "jobId": 1,
                  "name": "测试候选人",
                  "email": "candidate@example.com",
                  "phone": "13800000000",
                  "resumeUrl": "https://example.com/resume.pdf",
                  "coverLetter": "希望加入贵公司"
                }
                """;

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUBMITTED"))
                .andExpect(jsonPath("$.jobTitle").value("高级前端工程师"));

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void rejectsInvalidApplication() throws Exception {
        String body = """
                {
                  "jobId": 1,
                  "name": "测试候选人",
                  "email": "not-an-email"
                }
                """;
        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createsJobForEmployer() throws Exception {
        long companyId = companyRepository.findAll().get(0).getId();
        String body = """
                {
                  "title": "资深平台工程师",
                  "companyId": %d,
                  "salaryMin": 30,
                  "salaryMax": 45,
                  "salaryText": "30-45K · 15薪",
                  "city": "深圳",
                  "exp": "3-5年",
                  "education": "本科",
                  "type": "全职",
                  "tags": ["Java", "Kubernetes"],
                  "requirements": ["5 年以上后端经验", "熟悉云原生"],
                  "description": "负责招聘平台核心服务建设。",
                  "hot": true
                }
                """.formatted(companyId);

        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("资深平台工程师"))
                .andExpect(jsonPath("$.hot").value(true));
    }

    @Test
    void healthEndpointWorks() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }
}
