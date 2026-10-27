package com.novahire.config;

import com.novahire.domain.Company;
import com.novahire.domain.Job;
import com.novahire.repository.CompanyRepository;
import com.novahire.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;

    @Value("${app.seed.enabled:true}")
    private boolean enabled;

    public DataSeeder(CompanyRepository companyRepository, JobRepository jobRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!enabled) {
            return;
        }
        if (jobRepository.count() > 0) {
            log.info("Seed data already exists, skip.");
            return;
        }

        Company xingyun = companyRepository.save(new Company("星云科技", "企业服务", "500-999人",
                "linear-gradient(135deg,#22d3ee,#818cf8)"));
        Company shenzhi = companyRepository.save(new Company("深智未来", "人工智能", "100-499人",
                "linear-gradient(135deg,#a78bfa,#f472b6)"));
        Company guangnian = companyRepository.save(new Company("光年互动", "内容社区", "1000-9999人",
                "linear-gradient(135deg,#34d399,#22d3ee)"));
        Company yunfan = companyRepository.save(new Company("云帆网络", "云计算", "500-999人",
                "linear-gradient(135deg,#60a5fa,#818cf8)"));
        Company xiangsu = companyRepository.save(new Company("像素工坊", "设计服务", "50-99人",
                "linear-gradient(135deg,#fbbf24,#f472b6)"));
        Company shulan = companyRepository.save(new Company("数澜科技", "大数据", "100-499人",
                "linear-gradient(135deg,#2dd4bf,#a3e635)"));
        Company zhiqu = companyRepository.save(new Company("智驱科技", "智能硬件", "100-499人",
                "linear-gradient(135deg,#22d3ee,#a3e635)"));
        Company chaoxi = companyRepository.save(new Company("潮汐文化", "文化传媒", "20-49人",
                "linear-gradient(135deg,#f472b6,#fbbf24)"));

        LocalDateTime now = LocalDateTime.now();

        jobRepository.save(new Job(
                "高级前端工程师", xingyun, 25, 40, "25-40K · 16薪",
                "深圳", "3-5年", "本科", "全职",
                List.of("React", "TypeScript", "WebGL", "性能优化"),
                List.of("3 年以上前端开发经验，精通 React 与 TypeScript",
                        "熟悉 Canvas / WebGL 渲染与大规模数据可视化",
                        "有组件库、低代码平台建设经验者优先",
                        "具备良好的工程化意识与性能调优能力"),
                "负责公司核心数据可视化产品的前端架构设计与性能优化，参与组件库与低代码引擎建设，与产品、设计紧密协作打造极致体验。",
                true, now.minusHours(12)));

        jobRepository.save(new Job(
                "大模型算法工程师", shenzhi, 40, 70, "40-70K · 14薪",
                "北京", "3-5年", "硕士", "全职",
                List.of("LLM", "RAG", "PyTorch", "微调"),
                List.of("计算机、人工智能相关专业硕士及以上学历",
                        "熟悉 Transformer 架构与主流大模型训练/推理流程",
                        "有 RAG、Agent、多模态项目落地经验",
                        "熟练使用 PyTorch，具备扎实的工程实现能力"),
                "参与企业级大模型应用的算法研发，负责 RAG 检索增强、Agent 工作流与领域微调，推动模型在实际业务场景中的效果落地。",
                true, now.minusHours(24)));

        jobRepository.save(new Job(
                "AI 产品经理", guangnian, 20, 35, "20-35K · 15薪",
                "上海", "1-3年", "本科", "全职",
                List.of("AI 产品", "用户研究", "数据分析", "需求分析"),
                List.of("1-3 年产品经理经验，有 AI 相关产品经验优先",
                        "具备较强的用户洞察与数据分析能力",
                        "能独立完成需求调研、PRD 撰写与项目推进",
                        "对大模型能力边界有清晰理解"),
                "负责 AI 创作工具的产品规划与迭代，从用户场景出发定义功能，协同算法与研发团队推动产品上线并持续优化核心指标。",
                false, now.minusHours(48)));

        jobRepository.save(new Job(
                "Java 后端开发工程师", yunfan, 18, 30, "18-30K · 14薪",
                "杭州", "1-3年", "本科", "全职",
                List.of("Java", "Spring Cloud", "MySQL", "Redis"),
                List.of("扎实的 Java 基础，熟悉 Spring Boot / Spring Cloud",
                        "熟悉 MySQL、Redis、消息队列等常用中间件",
                        "了解分布式系统设计与高并发处理",
                        "有云原生、Kubernetes 经验者优先"),
                "参与云平台核心服务的研发与维护，负责高并发场景下的接口设计、性能优化与稳定性保障，持续打磨服务质量。",
                false, now.minusHours(24)));

        jobRepository.save(new Job(
                "UI/UX 设计师", xiangsu, 12, 20, "12-20K · 13薪",
                "广州", "1-3年", "本科", "全职",
                List.of("Figma", "交互设计", "视觉设计", "设计系统"),
                List.of("1 年以上 UI/UX 设计经验，作品集完整",
                        "熟练使用 Figma，理解组件化设计方法",
                        "具备良好的审美与交互逻辑思维",
                        "有 B 端产品设计经验者优先"),
                "负责 SaaS 产品的界面与交互设计，参与设计系统建设，从概念探索到高保真交付全程跟进，并与前端协作还原设计稿。",
                false, now.minusHours(72)));

        jobRepository.save(new Job(
                "数据分析师", shulan, 15, 25, "15-25K · 14薪",
                "成都", "1-3年", "本科", "全职",
                List.of("SQL", "Python", "BI", "指标体系"),
                List.of("熟练使用 SQL，掌握 Python 数据分析工具链",
                        "具备较强的业务理解与逻辑分析能力",
                        "有 BI 工具（Tableau / 观远等）使用经验",
                        "统计学、数学相关专业优先"),
                "面向业务搭建指标体系与数据看板，深入分析用户行为与增长链路，输出可执行的策略建议，支撑产品与运营决策。",
                false, now.minusHours(48)));

        jobRepository.save(new Job(
                "远程全栈工程师", yunfan, 30, 50, "30-50K · 美元结算",
                "远程", "3-5年", "不限", "远程",
                List.of("Node.js", "React", "PostgreSQL", "远程协作"),
                List.of("3 年以上全栈开发经验，中英文沟通顺畅",
                        "精通 React 与 Node.js，熟悉 PostgreSQL",
                        "具备良好的自我管理与异步协作能力",
                        "有海外产品或远程工作经验者优先"),
                "加入全远程团队，负责跨境 SaaS 产品的全栈开发，从需求拆解到上线运维独立推进，与全球成员异步协作。",
                true, now.minusHours(19)));

        jobRepository.save(new Job(
                "测试开发工程师", zhiqu, 20, 32, "20-32K · 15薪",
                "北京", "3-5年", "本科", "全职",
                List.of("自动化测试", "Python", "CI/CD", "安全测试"),
                List.of("3 年以上测试开发经验，熟悉 Python",
                        "有自动化测试框架设计与落地经验",
                        "熟悉 CI/CD 流程与容器化环境",
                        "了解常见安全测试方法与工具"),
                "负责安全产品的自动化测试体系建设，设计并维护测试框架与流水线，推动质量左移，保障核心业务稳定交付。",
                false, now.minusHours(96)));

        jobRepository.save(new Job(
                "运维开发工程师", yunfan, 15, 28, "15-28K · 14薪",
                "杭州", "1-3年", "本科", "全职",
                List.of("Linux", "Kubernetes", "Prometheus", "Go"),
                List.of("熟悉 Linux 系统与网络基础",
                        "掌握 Kubernetes、Docker 等容器技术",
                        "熟悉 Prometheus / Grafana 监控体系",
                        "会 Go 或 Python 进行运维工具开发"),
                "负责云平台的稳定性建设与自动化运维，参与监控告警、容量规划与故障演练，用工程化手段提升系统可用性。",
                false, now.minusHours(120)));

        jobRepository.save(new Job(
                "游戏客户端开发", guangnian, 22, 38, "22-38K · 16薪",
                "上海", "3-5年", "本科", "全职",
                List.of("Unity", "C#", "渲染", "性能优化"),
                List.of("3 年以上 Unity 客户端开发经验",
                        "精通 C#，熟悉常用设计模式与架构",
                        "有渲染优化、内存优化实战经验",
                        "有完整上线项目经验者优先"),
                "负责移动端游戏核心玩法与系统模块开发，参与渲染管线与性能优化，与策划、美术协作打磨高品质游戏体验。",
                false, now.minusHours(72)));

        jobRepository.save(new Job(
                "新媒体运营（实习）", chaoxi, 4, 8, "4-8K · 实习",
                "广州", "应届", "本科", "实习",
                List.of("内容运营", "短视频", "社群", "文案"),
                List.of("本科在读或应届毕业生，每周到岗 4 天以上",
                        "对内容与社交平台有热情，文案功底扎实",
                        "会基础剪辑与图片处理者优先",
                        "学习能力强，能快速响应热点"),
                "参与品牌新媒体矩阵的内容策划与运营，负责短视频脚本、社群互动与数据复盘，快速积累内容行业实战经验。",
                false, now.minusHours(7)));

        jobRepository.save(new Job(
                "嵌入式软件工程师", zhiqu, 18, 30, "18-30K · 14薪",
                "深圳", "1-3年", "本科", "全职",
                List.of("C/C++", "RTOS", "驱动开发", "物联网"),
                List.of("熟悉 C/C++，具备嵌入式开发经验",
                        "了解 RTOS 或 Linux 驱动开发",
                        "能看懂硬件原理图，会使用示波器等工具",
                        "有物联网、消费电子量产经验者优先"),
                "负责智能硬件产品的嵌入式软件开发，涵盖驱动移植、系统稳定性优化与低功耗设计，参与产品从原型到量产的全过程。",
                false, now.minusHours(144)));

        log.info("Seed data initialized: {} companies, {} jobs",
                companyRepository.count(), jobRepository.count());
    }
}
