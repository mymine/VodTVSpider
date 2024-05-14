package com.github.catvod.spider.base;

import com.github.catvod.crawler.Spider;
//import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.m3u8.UA;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 这是一个中间的类，主要提供一些常用的公共方法
 * 这些方法不是必须重写的内容，
 * 编写爬虫要重写的模版方法只需看 Spider 类里面的方法
 */
public class BaseSpider extends Spider {
    protected final String userAgent = UA.FIREFOX;

    protected Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", userAgent);
        return header;
    }

    protected Map<String, String> getHeader(String referer) {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", userAgent);
        header.put("Referer", referer);
        return header;
    }

    protected OkHttpClient okClient() {
        //return OkHttp.client();
        return OkHttpUtil.defaultClient();
    }

    /*protected String req(String url) throws Exception {
        return req(url, getHeader());
    }*/

    protected String req(String url, Map<String, String> header) throws Exception {
        //return OkHttp.string(url, header);
        return OkHttpUtil.string(url, header);
    }

    protected String req(String url, Map<String, String> header, String encoding) throws Exception {
        Request request = new Request.Builder().get().url(url).headers(Headers.of(header)).build();
        return req(request, encoding);
    }

    protected Response req(Request request) throws Exception {
        return okClient().newCall(request).execute();
    }

    protected String req(Request request, String encoding) throws Exception {
        Response response = okClient().newCall(request).execute();
        return req(response, encoding);
    }

    protected String req(Response response, String encoding) throws Exception {
        if (!response.isSuccessful()) return "";
        byte[] bytes = response.body().bytes();
        response.close();
        return new String(bytes, encoding);
    }

    /**
     * 正则获取字符串
     *
     * @param pattern 正则表达式 pattern 对象
     * @param html    网页源码
     * @return 返回正则获取的字符串结果
     */
    protected String find(Pattern pattern, String html) {
        Matcher m = pattern.matcher(html);
        return m.find() ? m.group(1).trim() : "";
    }

    /**
     * 正则获取字符串
     *
     * @param regex 正则表达式字符串
     * @param html  网页源码
     * @return 返回正则获取的字符串结果
     */
    protected String find(String regex, String html) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(html);
        return m.find() ? m.group(1).trim() : "";
    }

    protected String removeHtmlTag(String input) {
        return input.replaceAll("</?[^>]+>", "");
    }

}
