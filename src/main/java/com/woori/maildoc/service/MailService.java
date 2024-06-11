package com.woori.maildoc.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.woori.maildoc.dto.response.ResultRes;
import com.woori.maildoc.dto.resquest.MailReq;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailService {
	
	public List<ResultRes> sendKakao(MailReq mailReq) {
		return tokenizer(mailReq.getContents()).stream()
				.map(word -> ResultRes.builder()
						.original(word)
						.corrected(callAPI(word))
						.build())
				.collect(Collectors.toList());
	}
	
	public List<String> tokenizer(String contents) {
		return Arrays.asList(contents.replaceAll("[^a-zA-Z가-힣0-9\\s]", "").split("\\s+"));
	}
	
	public String callAPI(String word) {
		log.info("callAPI 호출");
		
		String apiKey = "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";
		String basePath = "http://192.168.0.10:5000/";
		HttpClient client = HttpClient.newHttpClient();
		
		HttpRequest request = HttpRequest.newBuilder()
					.header("Content-Type", "application/json")
				  .uri(URI.create(basePath))
				  .POST(HttpRequest.BodyPublishers.ofString("{\"word\":\"" + word + "\"}"))
				  .build();
		
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			return response.body().replaceAll("[^a-zA-Z가-힣0-9\\s]", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
}
