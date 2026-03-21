package com.SpringProject.jobms.Job.Impl;
import com.SpringProject.jobms.Job.DTO.JobDTO;
import com.SpringProject.jobms.Job.Job;
import com.SpringProject.jobms.Job.JobRepository;
import com.SpringProject.jobms.Job.JobService;
import com.SpringProject.jobms.Job.external.Company;
import com.SpringProject.jobms.Job.external.Review;
import com.SpringProject.jobms.Job.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    JobRepository jobRepository;

    @Autowired
    RestTemplate restTemplate;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobWithCompanyDTOS = new ArrayList<>();
        return jobs.stream().map(job -> {
            try {
                return convertToDto(job);
            } catch (Exception e){
                System.out.println("Skipped job Id: "+ job.getId());
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private JobDTO convertToDto(Job job){
        if(job == null){
            throw new RuntimeException("Job is null");
        }
        Company company = null;

        try {
            company = restTemplate.getForObject(
                    "http://COMPANY-SERVICE:8081/companies/" + job.getCompanyId(),
                    Company.class
            );
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Company not found: " + job.getCompanyId());
        }

        ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
                "http://REVIEW-SERVICE:8083/reviews?companyId=" + job.getCompanyId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Review>>() {}
        );
        List<Review> reviews = reviewResponse.getBody();
        JobDTO jobDTO = JobMapper.mapToJobWithCompanyDto(job, company, reviews);
        return jobDTO;
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        return convertToDto(job);
    }

    @Override
    public boolean deleteJobById(Long id) {
        try {
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateJobById(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if(jobOptional.isPresent()){
            Job job = jobOptional.get();
                job.setTitle(updatedJob.getTitle());
                job.setDescription(updatedJob.getDescription());
                job.setMinSalary(updatedJob.getMinSalary());
                job.setMaxSalary(updatedJob.getMaxSalary());
                job.setLocation(updatedJob.getLocation());
                jobRepository.save(job);
                return true;
            }
        return false;
    }
}