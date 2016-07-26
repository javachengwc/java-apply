package com.webcollector;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
public class NewsCrawler extends BreadthCrawler {

    public NewsCrawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        /*种子页面*/
        this.addSeed("http://news.hfut.edu.cn/list-1-1.html");
        /*正则规则设置*/
        /*爬取符合 http://news.hfut.edu.cn/show-xxxxxxhtml的URL*/
        this.addRegex("http://news.hfut.edu.cn/show-.*html");
        /*不要爬取 jpg|png|gif*/
        this.addRegex("-.*\\.(jpg|png|gif).*");
        /*不要爬取包含 # 的URL*/
        this.addRegex("-.*#.*");
    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        String url = page.getUrl();
        if (page.matchUrl("http://news.hfut.edu.cn/show-.*html")) {
            String title = page.select("div[id=Article]>h2").first().text();
            String content = page.select("div#artibody", 0).text();
            System.out.println("URL:\n" + url);
            System.out.println("title:\n" + title);
            System.out.println("content:\n" + content);
        }
    }

    public static void main(String[] args) throws Exception {
        NewsCrawler crawler = new NewsCrawler("crawl", true);
        crawler.setThreads(50);
        crawler.setTopN(100);
        crawler.start(4);
    }
}