package tech.mingxi.exam;


import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import tech.mingxi.exam.models.News;

@Component
public class NewsJob {
	Logger logger = LoggerFactory.getLogger(NewsJob.class);
	@Autowired
	NewsRepository newsRepository;

	/**
	 * 每天早上9点刷新
	 */

	@Scheduled(cron = "0 0 9 1/1 * ?")//9:00 every day
//	@Scheduled(cron = "0 * * * * ?")//1min for debug
	public void getNews() {
		logger.info("getNews");
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);//这里可以选择拦截级别
		builder.addInterceptor(loggingInterceptor);
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://3g.163.com/")
				.addConverterFactory(MoshiConverterFactory.create())//转为对象类型
				.client(builder.build())
				.build();
		GitHubService service = retrofit.create(GitHubService.class);
		service.getNewsJSON(0, 20).enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				JSONArray result = new JSONArray();
				try {
					StringBuilder stringBuilder = new StringBuilder(response.body().string());
					//delete "artiList("
					stringBuilder.delete(0, 0 + "artiList(".length());
					//delete ")"
					stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
					JSONObject jsonObject = new JSONObject(stringBuilder.toString());
					//flat json object into array
					Set<String> keySet = jsonObject.keySet();
					for (String key : keySet) {
						JSONArray jsonArray = jsonObject.optJSONArray(key);
						for (int i = 0; i < jsonArray.length(); i++) {
							result.put(jsonArray.optJSONObject(i));
						}
					}
				} catch (IOException e) {
					logger.error("Error", e);
				}
				Type type = Types.newParameterizedType(List.class, News.class);
				Moshi moshi = new Moshi.Builder().build();
				JsonAdapter<List<News>> adapter = moshi.adapter(type);
				try {
					List<News> news = adapter.fromJson(result.toString());
					newsRepository.saveAll(news);
//					logger.info(news.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				logger.error("onFailure", t);
			}
		});
	}

	public interface GitHubService {
		@GET("touch/reconstruct/article/list/BBM54PGAwangning/{start}-{end}.html")
		Call<ResponseBody> getNewsJSON(@Path("start") int start, @Path("end") int end);
	}

}
