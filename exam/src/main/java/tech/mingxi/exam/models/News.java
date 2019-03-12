package tech.mingxi.exam.models;


import org.springframework.data.rest.core.config.Projection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "news")
public class News {
	@Column(name = "liveInfo")
	private String liveInfo;
	@Id
	@Column(name = "docid", length = 50)
	private String docid;
	@Column(name = "source")
	private String source;
	@Column(name = "title")
	private String title;
	@Column(name = "priority")
	private int priority;
	@Column(name = "hasImg")
	private int hasImg;
	@Column(name = "url")
	private String url;
	@Column(name = "commentCount")
	private int commentCount;
	@Column(name = "imgsrc3gtype")
	private String imgsrc3gtype;
	@Column(name = "stitle")
	private String stitle;
	@Column(name = "digest")
	private String digest;
	@Column(name = "imgsrc")
	private String imgsrc;
	@Column(name = "ptime")
	private String ptime;

	@Override
	public String toString() {
		return "News{" +
				"title='" + title + '\'' +
				'}';
	}

	@Projection(name = "ExamNews", types = {News.class})
	public interface ExamNews {
		String getDocid();

		String getTitle();

		String getUrl();

		String getImgsrc();

		String getPtime();
	}
}
