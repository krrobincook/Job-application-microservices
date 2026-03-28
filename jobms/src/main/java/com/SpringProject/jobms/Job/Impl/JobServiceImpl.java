package com.SpringProject.jobms.Job.Impl;
import com.SpringProject.jobms.Job.DTO.JobDTO;
import com.SpringProject.jobms.Job.Job;
import com.SpringProject.jobms.Job.JobRepository;
import com.SpringProject.jobms.Job.JobService;
import com.SpringProject.jobms.Job.clients.CompanyClient;
import com.SpringProject.jobms.Job.clients.ReviewClient;
import com.SpringProject.jobms.Job.external.Company;
import com.SpringProject.jobms.Job.external.Review;
import com.SpringProject.jobms.Job.mapper.JobMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    private CompanyClient companyClient;
    private ReviewClient reviewClient;

    public JobServiceImpl(JobRepository jobRepository, CompanyClient companyClient, ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

    @Override
    @CircuitBreaker(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobWithCompanyDTOS = new ArrayList<>();
        return jobs.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<JobDTO> companyBreakerFallback(Exception e) {
        List<JobDTO> list = new ArrayList<>();

        JobDTO dummy = new JobDTO();
        dummy.setTitle("Dummy Job");
        dummy.setDescription("Fallback response due to service failure");

        list.add(dummy);
        return list;
    }

    private JobDTO convertToDto(Job job){
        if(job == null){
            throw new RuntimeException("Job is null");
        }
        Company company = companyClient.getCompany(job.getCompanyId());
        List<Review> reviews = reviewClient.getReview(job.getCompanyId());
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