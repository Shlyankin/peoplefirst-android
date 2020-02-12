package rokolabs.com.peoplefirst.di.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rokolabs.com.peoplefirst.di.qualifier.AppContext;
import rokolabs.com.peoplefirst.utils.Utils;

/**
 * Created by S on 17.05.2018.
 */

@Module
public class RetrofitModule {


    @Provides
    @Singleton
    public BaseUrl provideBaseUrl() {
        return new BaseUrl();
    }

	@Provides
	@Singleton
	public Retrofit provideRetrofit(Retrofit.Builder builder, BaseUrl baseUrl) {
		return builder.baseUrl(baseUrl.URL).build();
	}

	@Provides
	@Singleton
	public Retrofit.Builder provideRetrofitBuilder(Converter.Factory converterFactory, OkHttpClient client) {
		return new Retrofit.Builder()
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io())
				.addConverterFactory(converterFactory) // converterFactory
				.client(client);
	}

	@Provides
	@Singleton
	public OkHttpClient provideOkHttpClient(@AppContext Context context, BaseUrl baseUrl) {
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl oldUrl = request.url();
                        HttpUrl newUrl = HttpUrl.parse(baseUrl.URL);
                        HttpUrl.Builder result = oldUrl.newBuilder()
                                .scheme(newUrl.scheme())
                                .host(newUrl.host())
                                .port(newUrl.port());
                        request = request.newBuilder()
                                .url(result.build())
                                .build();
                        return chain.proceed(request);
                    }
                })
				.addInterceptor(new Interceptor() {

					@Override
					public Response intercept(Chain chain) throws IOException {
						Request.Builder builder = chain.request().newBuilder();
						builder.addHeader("x-api-key", "K/ktMWb9hr1M47rXH8c7+anYQq1SRIv/CNVH2Q9DwJQ=");
						if (Utils.getPermanentValue("x-access-token", context).equals("")) {
							return chain.proceed(builder.build());
						} else {
							builder
									//X-Roko-Mobi-Admin-Session 1dE/LQPxKw6VGmudnkSeqHr+d1ng3wjCkbuCR+CX8nE=
									.addHeader("x-access-token", Utils.getPermanentValue("x-access-token", context))
									.build();
							return chain.proceed(builder.build());
						}

					}
				})
				.readTimeout(15, TimeUnit.SECONDS)
                .build();
		return  client;
	}

	@Provides
	@Singleton
	public Converter.Factory provideConverterFactory(Gson gson) {
		return GsonConverterFactory.create(gson);
	}

	@Provides
	@Singleton
    Gson provideGson() {
		return new GsonBuilder()
				.serializeNulls()
				.create();
	}
}
