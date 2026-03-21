package com.SpringProject.jobms.Job.mapper;
import com.SpringProject.jobms.Job.DTO.JobDTO;
import com.SpringProject.jobms.Job.Job;
import com.SpringProject.jobms.Job.external.Company;
import com.SpringProject.jobms.Job.external.Review;
import java.util.List;
public class JobMapper {
    public static JobDTO mapToJobWithCompanyDto(Job job, Company company, List<Review> reviews){
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(job.getId());
        jobDTO.setTitle(job.getTitle());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setMaxSalary(job.getMaxSalary());
        jobDTO.setMinSalary(job.getMinSalary());
        jobDTO.setCompany(company);
        jobDTO.setReviews(reviews);
        return jobDTO;
    }
}
