package com.SpringProject.reviewms.review.impl;
import com.SpringProject.reviewms.review.Review;
import com.SpringProject.reviewms.review.ReviewRepository;
import com.SpringProject.reviewms.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        List<Review> reviews = reviewRepository.findByCompanyId(companyId);
        return reviews;
    }

    @Override
    public boolean addReview(Long companyId, Review review) {
        if(companyId != null){
            review.setCompanyId(companyId);
            reviewRepository.save(review);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Review getReview(Long reviewId) {
       return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public boolean updateReview(Long reviewId, Review UpdatedReview) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(review != null){
           review.setTitle(UpdatedReview.getTitle());
           review.setDescription(UpdatedReview.getDescription());
           review.setRating(UpdatedReview.getRating());
           review.setCompanyId(UpdatedReview.getCompanyId());
           reviewRepository.save(review);
           return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(review != null ){
            reviewRepository.delete(review);
            return true;
        } else {
            return false;
        }
    }
}