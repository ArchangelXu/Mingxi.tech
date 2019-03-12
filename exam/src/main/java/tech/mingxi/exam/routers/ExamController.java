package tech.mingxi.exam.routers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import tech.mingxi.exam.NewsRepository;
import tech.mingxi.exam.models.News;

@RestController
public class ExamController {
	Logger logger = LoggerFactory.getLogger(ExamController.class);
	@Autowired
	NewsRepository newsRepository;

	@RequestMapping(value = "/exam", method = RequestMethod.GET)
	public List<News.ExamNews> getNewsData(
			@RequestParam(name = "page",
					defaultValue = "0",
					required = false
			) int page,
			@RequestParam(name = "size",
					defaultValue = "20",
					required = false
			) int size) {
		Page<News.ExamNews> all = newsRepository.findAllProjectedByOrderByPtimeDesc(PageRequest.of(page, size));
		return all.getContent();
	}

}