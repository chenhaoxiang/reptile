package cn.hncu.reptile;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * Created with IntelliJ IDEA.
 * User: 陈浩翔.
 * Date: 2017/2/27.
 * Time: 下午 12:28.
 * Explain:抓取Github信息
 */
public class GithubRepoPageProcessor implements PageProcessor {
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setCharset("utf-8").setRetrySleepTime(3).setSleepTime(1000);


    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        Html html = page.getHtml();
        // \w 匹配任何字类字符，包括下划线。与"[A-Za-z0-9_]"等效。
        //这段代码使用了XPath，它的意思是“查找所有class属性为'repo js-repo'的span元素，并提取节点的文本信息”。
        page.putField("name", html.xpath("//span[@class='repo js-repo']/text()").all());
        page.putField("authorName", html.xpath("//span[@class='vcard-fullname d-block']/text()").toString());
        page.putField("title", html.xpath("//title/text()").toString());

        page.putField("explain", html.xpath("//p[@class='pinned-repo-desc text-gray text-small d-block mt-2 mb-3']/text()").toString());

        if (page.getResultItems().get("name") == null) {
            //skip this page
            page.setSkip(true);
        }


        // 部分三：从页面发现后续的url地址来抓取
        page.addTargetRequests(html.links().regex("(https://github\\.com/\\w+/\\w+)").all());

    }


    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        String str = "href=\"https://zhidao.baidu.com/question/452354960.html?fr=iks&word=%C9%E7%B1%A3%D7%AA%D2%C6&ie=gbk\"";
        String s = str.replaceAll("https://zhidao.baidu.com/question/.+[^\"]","    ");
        System.out.println(s);

        //从"https://github.com/code4craft"开始抓
//        Spider.create(new GithubRepoPageProcessor()).addUrl("https://github.com/chenhaoxiang")
//                //.addPipeline(new JsonFilePipeline("D:\\test\\"))
//                //开启5个线程抓取
//                .thread(5)
//                //启动爬虫
//                //同步启动
//                // .run();
//                //异步启动
//                .start();
    }

}
