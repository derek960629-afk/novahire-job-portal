package com.novahire.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs", indexes = {
        @Index(name = "idx_jobs_city", columnList = "city"),
        @Index(name = "idx_jobs_posted_at", columnList = "posted_at"),
        @Index(name = "idx_jobs_active", columnList = "active")
})
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "salary_min", nullable = false)
    private int salaryMin;

    @Column(name = "salary_max", nullable = false)
    private int salaryMax;

    @Column(name = "salary_text", nullable = false, length = 60)
    private String salaryText;

    @Column(nullable = false, length = 40)
    private String city;

    @Column(name = "experience", nullable = false, length = 20)
    private String exp;

    @Column(nullable = false, length = 20)
    private String education;

    @Column(name = "job_type", nullable = false, length = 20)
    private String type;

    @Column(name = "tags_csv", length = 500)
    private String tagsCsv;

    @Column(name = "requirements_text", length = 4000)
    private String requirementsText;

    @Column(nullable = false, length = 4000)
    private String description;

    @Column(nullable = false)
    private boolean hot;

    @Column(name = "posted_at", nullable = false)
    private LocalDateTime postedAt;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "search_text", length = 1000)
    private String searchText;

    protected Job() {
    }

    public Job(String title, Company company, int salaryMin, int salaryMax, String salaryText,
               String city, String exp, String education, String type, List<String> tags,
               List<String> requirements, String description, boolean hot, LocalDateTime postedAt) {
        this.title = title;
        this.company = company;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.salaryText = salaryText;
        this.city = city;
        this.exp = exp;
        this.education = education;
        this.type = type;
        this.tagsCsv = tags == null ? "" : String.join(",", tags);
        this.requirementsText = requirements == null ? "" : String.join("\n", requirements);
        this.description = description;
        this.hot = hot;
        this.postedAt = postedAt;
        this.active = true;
    }

    @PrePersist
    @PreUpdate
    void refreshSearchText() {
        StringBuilder sb = new StringBuilder();
        sb.append(title == null ? "" : title).append(' ');
        sb.append(company == null ? "" : company.getName()).append(' ');
        sb.append(company == null ? "" : company.getIndustry()).append(' ');
        sb.append(city == null ? "" : city).append(' ');
        sb.append(tagsCsv == null ? "" : tagsCsv.replace(',', ' '));
        String value = sb.toString().trim().toLowerCase();
        this.searchText = value.length() > 990 ? value.substring(0, 990) : value;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public int getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(int salaryMin) {
        this.salaryMin = salaryMin;
    }

    public int getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(int salaryMax) {
        this.salaryMax = salaryMax;
    }

    public String getSalaryText() {
        return salaryText;
    }

    public void setSalaryText(String salaryText) {
        this.salaryText = salaryText;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTagsCsv() {
        return tagsCsv;
    }

    public void setTagsCsv(String tagsCsv) {
        this.tagsCsv = tagsCsv;
    }

    public String getRequirementsText() {
        return requirementsText;
    }

    public void setRequirementsText(String requirementsText) {
        this.requirementsText = requirementsText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSearchText() {
        return searchText;
    }

    public List<String> tagsList() {
        return split(tagsCsv, ",");
    }

    public List<String> requirementsList() {
        return split(requirementsText, "\\n");
    }

    private static List<String> split(String value, String regex) {
        List<String> result = new ArrayList<>();
        if (value == null || value.isBlank()) {
            return result;
        }
        for (String part : value.split(regex)) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }
}
