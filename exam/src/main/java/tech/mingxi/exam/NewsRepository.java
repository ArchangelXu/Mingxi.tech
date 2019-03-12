package tech.mingxi.exam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.mingxi.exam.models.News;

@Repository
public interface NewsRepository extends JpaRepository<News, String> {
	//Method name is IMPORTANT! Don't change!
	//But I really DO NOT think it's a good idea...
	Page<News.ExamNews> findAllProjectedByOrderByPtimeDesc(Pageable of);
}