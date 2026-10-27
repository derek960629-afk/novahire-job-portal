package com.novahire.service;

import com.novahire.domain.Company;
import com.novahire.dto.CompanyDto;
import com.novahire.repository.CompanyRepository;
import com.novahire.repository.JobRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;

    public CompanyService(CompanyRepository companyRepository, JobRepository jobRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
    }

    @Transactional(readOnly = true)
    public List<CompanyDto> list() {
        return companyRepository.findAll(Sort.by("id")).stream()
                .map(this::toDto)
                .toList();
    }

    public long count() {
        return companyRepository.count();
    }

    private CompanyDto toDto(Company company) {
        return new CompanyDto(
                company.getId(),
                company.getName(),
                company.getIndustry(),
                company.getSize(),
                company.getColor(),
                jobRepository.countByCompanyId(company.getId())
        );
    }
}
