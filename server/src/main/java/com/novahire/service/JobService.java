package com.novahire.service;

import com.novahire.domain.Company;
import com.novahire.domain.Favorite;
import com.novahire.domain.Job;
import com.novahire.dto.JobCreateRequest;
import com.novahire.dto.JobDto;
import com.novahire.repository.CompanyRepository;
import com.novahire.repository.FavoriteRepository;
import com.novahire.repository.JobRepository;
import com.novahire.web.ResourceNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final FavoriteRepository favoriteRepository;

    public JobService(JobRepository jobRepository, CompanyRepository companyRepository,
                      FavoriteRepository favoriteRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional(readOnly = true)
    public List<JobDto> search(String keyword, String city, String exp, String type,
                               String salary, String sort, String sessionId) {
        Specification<Job> spec = buildSpec(keyword, city, exp, type, salary);
        List<Job> jobs = jobRepository.findAll(spec, resolveSort(sort));
        Set<Long> favoriteIds = favoriteIds(sessionId);
        return jobs.stream().map(job -> toDto(job, favoriteIds.contains(job.getId()))).toList();
    }

    @Transactional(readOnly = true)
    public JobDto get(Long id, String sessionId) {
        Job job = jobRepository.findById(id)
                .filter(Job::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("职位不存在或已下线"));
        return toDto(job, favoriteIds(sessionId).contains(job.getId()));
    }

    @Transactional
    public JobDto create(JobCreateRequest request) {
        if (request.salaryMin() > request.salaryMax()) {
            throw new IllegalArgumentException("最低薪资不能高于最高薪资");
        }
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("企业不存在"));
        Job job = new Job(
                request.title().trim(),
                company,
                request.salaryMin(),
                request.salaryMax(),
                request.salaryText().trim(),
                request.city().trim(),
                request.exp().trim(),
                request.education().trim(),
                request.type().trim(),
                request.tags(),
                request.requirements(),
                request.description().trim(),
                request.hot(),
                LocalDateTime.now()
        );
        return toDto(jobRepository.save(job), false);
    }

    public long count() {
        return jobRepository.count();
    }

    private Set<Long> favoriteIds(String sessionId) {
        if (sessionId == null) {
            return Set.of();
        }
        return favoriteRepository.findBySessionIdOrderByCreatedAtDesc(sessionId).stream()
                .map(Favorite::getJobId)
                .collect(Collectors.toSet());
    }

    private Sort resolveSort(String sort) {
        if ("latest".equals(sort)) {
            return Sort.by(Sort.Direction.DESC, "postedAt");
        }
        if ("salary".equals(sort)) {
            return Sort.by(Sort.Direction.DESC, "salaryMax");
        }
        return Sort.by(Sort.Order.desc("hot"), Sort.Order.desc("postedAt"));
    }

    private Specification<Job> buildSpec(String keyword, String city, String exp, String type, String salary) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("active")));

            if (hasValue(keyword)) {
                predicates.add(cb.like(root.get("searchText"), "%" + keyword.trim().toLowerCase() + "%"));
            }
            addEqual(predicates, cb, root, "city", city);
            addEqual(predicates, cb, root, "exp", exp);
            addEqual(predicates, cb, root, "type", type);

            if (hasValue(salary)) {
                switch (salary) {
                    case "0-15" -> predicates.add(cb.lessThan(root.get("salaryMin"), 15));
                    case "15-25" -> predicates.add(cb.and(
                            cb.greaterThanOrEqualTo(root.get("salaryMax"), 15),
                            cb.lessThanOrEqualTo(root.get("salaryMin"), 25)));
                    case "25-40" -> predicates.add(cb.and(
                            cb.greaterThanOrEqualTo(root.get("salaryMax"), 25),
                            cb.lessThanOrEqualTo(root.get("salaryMin"), 40)));
                    case "40+" -> predicates.add(cb.greaterThanOrEqualTo(root.get("salaryMax"), 40));
                    default -> {
                    }
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void addEqual(List<Predicate> predicates, jakarta.persistence.criteria.CriteriaBuilder cb,
                          jakarta.persistence.criteria.Root<Job> root, String field, String value) {
        if (hasValue(value)) {
            predicates.add(cb.equal(root.get(field), value.trim()));
        }
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank() && !"不限".equals(value.trim());
    }

    private JobDto toDto(Job job, boolean favorite) {
        Company company = job.getCompany();
        double days = Math.max(0, Duration.between(job.getPostedAt(), LocalDateTime.now()).toMinutes() / 1440.0);
        double roundedDays = Math.round(days * 10) / 10.0;
        return new JobDto(
                job.getId(),
                job.getTitle(),
                company.getName(),
                company.getColor(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getSalaryText(),
                job.getCity(),
                job.getExp(),
                job.getEducation(),
                job.getType(),
                job.tagsList(),
                company.getIndustry(),
                company.getSize(),
                roundedDays,
                job.isHot(),
                favorite,
                job.getDescription(),
                job.requirementsList(),
                job.getPostedAt()
        );
    }
}
