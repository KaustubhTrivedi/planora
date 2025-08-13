package com.planora.planora.repository;

import com.planora.planora.entity.DocumentCategory;
import com.planora.planora.entity.Trip;
import com.planora.planora.entity.TripDocument;
import com.planora.planora.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripDocumentRepository extends JpaRepository<TripDocument, Long> {
    
    /**
     * Find documents by trip, ordered by creation date
     */
    List<TripDocument> findByTripOrderByCreatedAtDesc(Trip trip);
    
    /**
     * Find documents by trip and category
     */
    List<TripDocument> findByTripAndCategory(Trip trip, DocumentCategory category);
    
    /**
     * Find documents by category
     */
    List<TripDocument> findByCategory(DocumentCategory category);
    
    /**
     * Find documents uploaded by a specific user
     */
    List<TripDocument> findByUploadedByOrderByCreatedAtDesc(User uploadedBy);
    
    /**
     * Find documents by trip and uploaded by user
     */
    List<TripDocument> findByTripAndUploadedBy(Trip trip, User uploadedBy);
    
    /**
     * Find document by S3 key
     */
    Optional<TripDocument> findByS3Key(String s3Key);
    
    /**
     * Find documents by file type
     */
    List<TripDocument> findByFileType(String fileType);
    
    /**
     * Find documents by content type
     */
    List<TripDocument> findByContentType(String contentType);
    
    /**
     * Find documents larger than specified size
     */
    List<TripDocument> findByFileSizeGreaterThan(Long fileSize);
    
    /**
     * Find documents by original file name (case-insensitive)
     */
    List<TripDocument> findByOriginalFileNameContainingIgnoreCase(String fileName);
    
    /**
     * Find documents uploaded after a specific date
     */
    List<TripDocument> findByCreatedAtAfter(LocalDateTime date);
    
    /**
     * Calculate total file size for a trip
     */
    @Query("SELECT COALESCE(SUM(td.fileSize), 0) FROM TripDocument td WHERE td.trip = :trip")
    Long calculateTotalFileSizeForTrip(@Param("trip") Trip trip);
    
    /**
     * Calculate total file size for a user
     */
    @Query("SELECT COALESCE(SUM(td.fileSize), 0) FROM TripDocument td WHERE td.uploadedBy = :user")
    Long calculateTotalFileSizeForUser(@Param("user") User user);
    
    /**
     * Count documents by trip
     */
    long countByTrip(Trip trip);
    
    /**
     * Count documents by category
     */
    long countByCategory(DocumentCategory category);
    
    /**
     * Count documents by user
     */
    long countByUploadedBy(User uploadedBy);
    
    /**
     * Find image documents (by content type)
     */
    @Query("SELECT td FROM TripDocument td WHERE td.contentType LIKE 'image/%'")
    List<TripDocument> findImageDocuments();
    
    /**
     * Find PDF documents
     */
    @Query("SELECT td FROM TripDocument td WHERE td.contentType = 'application/pdf'")
    List<TripDocument> findPdfDocuments();
    
    /**
     * Find documents with description
     */
    @Query("SELECT td FROM TripDocument td WHERE td.description IS NOT NULL AND td.description != ''")
    List<TripDocument> findDocumentsWithDescription();
    
    /**
     * Find documents by trip ID
     */
    @Query("SELECT td FROM TripDocument td WHERE td.trip.id = :tripId ORDER BY td.createdAt DESC")
    List<TripDocument> findByTripId(@Param("tripId") Long tripId);
    
    /**
     * Check if S3 key exists
     */
    boolean existsByS3Key(String s3Key);
    
    /**
     * Delete all documents for a trip
     */
    void deleteByTrip(Trip trip);
    
    /**
     * Find documents by S3 bucket
     */
    List<TripDocument> findByS3Bucket(String s3Bucket);
    
    /**
     * Find recent documents for a user
     */
    @Query("SELECT td FROM TripDocument td WHERE td.uploadedBy = :user " +
           "ORDER BY td.createdAt DESC")
    List<TripDocument> findRecentDocumentsByUser(@Param("user") User user);
}