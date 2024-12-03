package com.doclearn.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "course_topics")
public class CourseTopic {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Getter
		public Long id;
		
		@ManyToOne
		@JoinColumn(name = "course_id",nullable = false)
		@Getter
		@Setter
		private Course course;
		
		public CourseTopic(String title, String content,Course course) {
			super();
			this.title = title;
			this.content = content;
			this.course = course;
		}

		@Getter @Setter
		@Column(name = "title", nullable = false)
		private String title;
		
		@Getter @Setter
		@Column(name = "content")
		private String content;
		
		@Lob
		@Getter @Setter
		private byte[] image;

	public CourseTopic() {

	}
}
