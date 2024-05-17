package com.github.catvod.spider.base;

import com.github.catvod.crawler.Spider;
//import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import java.io.IOException;
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
    public static final String CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36";
    public static final String FIREFOX = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/112.0";
    public static final String IPHONE_SE = "Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1";

    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", CHROME);
        return header;
    }

    public Map<String, String> getHeader(String referer) {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", CHROME);
        header.put("Referer", referer);
        return header;
    }

    public static OkHttpClient getOkHttpClient() {
        //return OkHttp.client();
        return OkHttpUtil.defaultClient();
    }

    public static Response newCall(Request request) throws IOException {
        return getOkHttpClient().newCall(request).execute();
    }

    public static Response newCall(String url) throws IOException {
        return getOkHttpClient().newCall(new Request.Builder().url(url).build()).execute();
    }

    public static Response newCall(String url, Map<String, String> header) throws IOException {
        return getOkHttpClient().newCall(new Request.Builder().url(url).headers(Headers.of(header)).build()).execute();
    }

    public String string(String url) throws Exception {
        return string(newCall(url), null);
    }

    public String string(String url, Map<String, String> header) throws Exception {
        return string(newCall(url, header), null);
    }

    public String string(String url, Map<String, String> header, String encoding) throws Exception {
        return string(newCall(url, header), encoding);
    }

    public String string(Response response, String encoding) throws Exception {
        String result = "";
        try {
            byte[] bytes = response.body().bytes();
            result = new String(bytes, encoding == null ? "UTF-8" : encoding);
        } catch (Exception ignored) {
        } finally {
            response.close();
        }
        return result;
    }

    /**
     * 正则获取字符串
     *
     * @param pattern 正则表达式 pattern 对象
     * @param html    网页源码
     * @return 返回正则获取的字符串结果
     */
    public String find(Pattern pattern, String html) {
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
    public String find(String regex, String html) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(html);
        return m.find() ? m.group(1).trim() : "";
    }

    public String removeHtmlTag(String input) {
        return input.replaceAll("</?[^>]+>", "");
    }

}
