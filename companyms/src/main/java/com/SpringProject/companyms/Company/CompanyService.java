package com.SpringProject.companyms.Company;
import com.SpringProject.companyms.dto.ReviewMessage;

import java.util.List;
public interface CompanyService {
    List<Company> getAllCompanies();

    void createCompany(Company company);
    boolean UpdateCompany(Company company, Long id);

    boolean deleteCompanyById(Long id);
    Company getCompanyById(Long id);

    public void updateCompanyRating(ReviewMessage reviewMessage);
}
