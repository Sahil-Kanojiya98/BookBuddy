package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 250)
	private String title;

	@Column(nullable = false, length = 200)
	private String author;

	@Column(length = 20)
	private String isbn;

	@Column(length = 500)
	private String description;

	private Integer publishedYear;

	private Double averageRating = 0.0D;

	private Integer ratingCount = 0;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@Column(nullable = false)
	private Instant updatedAt;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
	private List<Review> reviews = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
	private List<Rating> ratings = new ArrayList<>();

	@PrePersist
	void onCreate() {
		Instant now = Instant.now();
		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	void onUpdate() {
		updatedAt = Instant.now();
	}
}
