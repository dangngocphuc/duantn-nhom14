package com.example.DaPhone.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "email_job")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailJob {
	@Id
	@SequenceGenerator(name = "seqEmailJob", sequenceName = "SEQ_EMAIL_JOB", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqEmailJob")
    private Long id;
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	@Column(name = "subject")
	private String subject;
	@Column(name = "content")
	private String content;
	@Column(name = "status")
	private int status;
}
