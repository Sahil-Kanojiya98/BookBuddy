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
import lombok.ToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 150)
	private String email;

	@Column(nullable = false)
	private String passwordHash;

	@Column(nullable = false, unique = true, length = 100)
	private String username;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@Column(nullable = false)
	private Instant updatedAt;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<UserBook> library = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<Rating> ratings = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<Review> reviews = new ArrayList<>();

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
