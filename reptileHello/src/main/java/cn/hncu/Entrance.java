package cn.hncu;

import cn.hncu.model.Answer;
import cn.hncu.model.Keyword;
import cn.hncu.model.KeywordProblemKey;
import cn.hncu.model.Problem;
import cn.hncu.service.KeywordService;
import cn.hncu.service.KeywordServiceImpl;
import cn.hncu.tool.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import javax.xml.xpath.XPath;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 陈浩翔.
 * Date: 2017/2/27.
 * Time: 下午 6:29.
 * Explain:
 */
public class Entrance implements PageProcessor {
    private Logger logger = LoggerFactory.getLogger(Entrance.class);
    private static ApplicationContext context;
    private static KeywordService keywordService;

    //搜索的关键字
    private static String keyword = "社保转移";
    //百度知道的搜索前缀
    private static String url = "https://zhidao.baidu.com/search?word=";
    //设置抓取页数
    private static Integer pages = 20;

    private Site site = Site.me().setCharset("gbk").setRetrySleepTime(4).setSleepTime(100);
    private static String keywordId ;
    private List<Map<String,String>> lists;

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        //问题
        String problem = html.xpath("//span[@class='ask-title']/text()").toString();
        logger.info("问题："+problem);
        //提问者
        String problemAnthor = html.xpath("//a[@alog-action='qb-ask-uname']/text()").toString();
        //logger.info("提问者:"+problemAnthor);
        //问题描述
        String problemDescribe = html.xpath("//span[@class='con']/text()").toString();
        //logger.info("问题描述:"+problemDescribe);
        //提问时间
        if(problem==null) {
            lists = new ArrayList<Map<String, String>>();
            List<String> strs = html.xpath("//dd/span[@class='mr-8']/text()").all();
            List<String> quizTimes = new ArrayList<String>();
            for (int i=0;i<strs.size();i++) {
                //logger.info(strs.get(i)+"  "+strs.get(i).matches("[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]"));
                if(strs.get(i).matches("[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]")){
                    quizTimes.add(strs.get(i));
                }
            }
            List<String> portionProblems = html.xpath("//a[@class='ti']/text()").all();
            //logger.info("quizTimes.size():" + quizTimes.size());
            //logger.info("portionProblems.size():" + portionProblems.size());
            if(quizTimes.size()==portionProblems.size()) {
                for (int i=0;i<quizTimes.size();i++) {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("quizTimes",quizTimes.get(i));
                    map.put("portionProblems",portionProblems.get(i));
                    lists.add(map);
                    //logger.info("部分问题:" + lists.get(i).get("portionProblems"));
                    //logger.info("时间:" + lists.get(i).get("quizTimes"));
                }
            }
        }

        //最佳回答
        String bestAnswer =html.xpath("//pre[@class='best-text mb-10']").toString();//图片文字无法处理
        if(bestAnswer==null){
            bestAnswer =html.xpath("//div[@class='best-text mb-10']").toString();
        }
        //logger.info("最佳回答:"+bestAnswer);
        //回答者
        String answerAuthor = html.xpath("//a[@class='mavin-name f-14 mr-10']/text()").toString();
        //如果回答者为NULL-有可能是其他的没有认证的人回答的
        if(answerAuthor==null){
            answerAuthor = html.xpath("//div[@class='grid f-aid ff-arial']/p/a[@class='user-name']/text()").toString();
        }
        //如果回答者还是为NULL-有可能是网上的人贡献的
        if(answerAuthor==null){
            answerAuthor = html.xpath("//div[@class='grid f-aid ff-arial']/p[@class='mb-5']/span/text()").toString();
        }
        //logger.info("回答者:"+answerAuthor);

        //回答者等级
        String anthorGrade = html.xpath("//a[@class='mavin-reply-icon f-orange']/text()").toString();
        //如果回答者等级为NULL-有可能是其他的没有认证的人回答的
        if(anthorGrade==null){
            anthorGrade = html.xpath("//div[@class='grid f-aid ff-arial']/p[@class='mb-5']/a[@class='i-gradeIndex']").toString();
            if(anthorGrade!=null){
                anthorGrade = "LV"+anthorGrade.substring(anthorGrade.lastIndexOf("Index-")+6,anthorGrade.lastIndexOf(" mr-10"));
            }
        }
        if(answerAuthor!=null&&anthorGrade==null){
            anthorGrade ="0";
        }
        //logger.info("回答者等级:"+anthorGrade);

        //推荐时间
        String answerTime = html.xpath("//span[@class='grid-r f-aid pos-time answer-time f-pening']/text()").toString();
        //logger.info("推荐时间:"+answerTime);

        //最佳回答点赞数
        String pointPraise =html.xpath("//span[@alog-action='qb-zan-btnbestbox']").toString();
        if(pointPraise!=null) {
            pointPraise = pointPraise.substring(pointPraise.lastIndexOf("evaluate"))
                    .replaceAll("evaluate=\"", "")
                    .replaceAll("\"> </span>", "");
        }
        //logger.info("最佳回答点赞数:"+pointPraise);
        //最佳回答拍砖数
        String contemptNumber =html.xpath("//span[@alog-action='qb-evaluate-outer']").toString();
        if(contemptNumber!=null) {
            contemptNumber = contemptNumber.substring(contemptNumber.lastIndexOf("evaluate"))
                    .replaceAll("evaluate=\"", "")
                    .replaceAll("\"> </span>", "");
        }
        //logger.info("最佳回答拍砖数:"+contemptNumber);

        //将发布时间和问题放到一块去，这里需要匹配
        String quizTimes = null;//这个判断有漏洞的！！！
        if(problem!=null) {
            for (int i = 0; i < lists.size(); i++) {
                String portionProblem = lists.get(i).get("portionProblems");
                String time = lists.get(i).get("quizTimes");
                boolean isThisProblem = true;
                for (int j = 0; j < portionProblem.length(); j++) {
                    if(problem.indexOf(portionProblem.charAt(j))==-1){
                        isThisProblem = false;
                    }
                }
                if(isThisProblem){
                    quizTimes = time;
                    continue;
                }
            }
        }
        //发表时间
        //logger.info("发表时间："+quizTimes);

        //其他回答答案
        List<String> otherAnswers =html.xpath("//div[@class='answer-text']/span").all();
        //logger.info("其他回答答案："+otherAnswers);
        //其他回答的回答时间
        List<String> otherAnswerTime =html.xpath("//div[@class='grid pt-5']/span[@class='pos-time']/text()").all();
        //logger.info("其他回答的回答时间："+otherAnswerTime);
        //其他回答的回答者
        List<String> otherAnswerAnthors =html.xpath("//a[@alog-action='qb-username' and @class='user-name']/text()").all();
        //logger.info("其他回答的回答者："+otherAnswerAnthors);
        //其他回答的回答者等级
        List<String> otherAnswerAnthorGradesLists =html.xpath("//div[@class='grid pt-5']/span[@class='i-gradeIndex']").all();
        List<String> otherAnswerAnthorGrades = new ArrayList<String>();
        if(otherAnswerAnthorGradesLists!=null){
            for(int i=0;i<otherAnswerAnthorGradesLists.size();i++){
                otherAnswerAnthorGrades.add("LV"+otherAnswerAnthorGradesLists.get(i).substring(otherAnswerAnthorGradesLists.get(i).lastIndexOf("Index-")+6,otherAnswerAnthorGradesLists.get(i).lastIndexOf("\"></span>")));
            }
        }
        //logger.info("其他回答的回答者等级："+otherAnswerAnthorGrades);
        //其他回答的点赞数
        List<String> otherAnswerPointPraiseLists =html.xpath("//span[@alog-action='qb-zan-btn']").all();
        List<String> otherAnswerPointPraises = new ArrayList<String>();
        if(otherAnswerPointPraiseLists!=null) {
            for(int i=0;i<otherAnswerPointPraiseLists.size();i++) {
                otherAnswerPointPraises.add(otherAnswerPointPraiseLists.get(i).substring(otherAnswerPointPraiseLists.get(i).lastIndexOf("evaluate"))
                        .replaceAll("evaluate=\"", "")
                        .replaceAll("\"> </span>", ""));
            }
        }
        //logger.info("其他回答的点赞数："+otherAnswerPointPraises);
        //其他回答的拍砖数
        List<String> otherAnswerContemptNumberLists =html.xpath("//div[@class='pos-relative']/div[@class='qb-zan-eva']/span[@alog-action='qb-evaluate-outer']").all();
        List<String> otherAnswerContemptNumbers = new ArrayList<String>();
        if(otherAnswerContemptNumberLists!=null) {
            for(int i=0;i<otherAnswerContemptNumberLists.size();i++) {
                otherAnswerContemptNumbers.add( otherAnswerContemptNumberLists.get(i).substring(otherAnswerContemptNumberLists.get(i).lastIndexOf("evaluate"))
                        .replaceAll("evaluate=\"", "")
                        .replaceAll("\"> </span>", ""));
            }
        }
        //logger.info("其他回答的拍砖数："+otherAnswerContemptNumbers);

        //如果为了效率，可以开线程去存数据的
        if(answerAuthor!=null || otherAnswers.size()!=0){
            //问题表数据
            Problem p = new Problem();
            String problemId = cn.hncu.tool.UUID.getId();
            p.setId(problemId);
            p.setProblem(problem);
            p.setQuizTime(quizTimes);
            p.setProblemAnthor(problemAnthor);
            p.setProblemDescribe(problemDescribe);
            //关键字—问题关系表数据
            KeywordProblemKey keywordProblemKey = new KeywordProblemKey();
            keywordProblemKey.setKeywordId(keywordId);
            keywordProblemKey.setProblemId(problemId);
            //最佳答案数据
            List<Answer> answers = new ArrayList<Answer>();

            Answer answer = new Answer();
            answer.setId(cn.hncu.tool.UUID.getId());
            answer.setProblemId(problemId);
            answer.setAnswerAnthor(answerAuthor);
            answer.setAnswer(bestAnswer);
            answer.setAnthorGrade(anthorGrade);
            answer.setPointPraise(pointPraise);
            answer.setAnswerTime(answerTime);
            answer.setContemptNumber(contemptNumber);
            answers.add(answer);

            //其他答案数据
            Integer otherAnswersSize = otherAnswers.size();
            for(int i=0;i<otherAnswersSize;i++){
                answer = new Answer();
                answer.setId(cn.hncu.tool.UUID.getId());
                answer.setProblemId(problemId);
                if(otherAnswerAnthors.size()>i)
                    answer.setAnswerAnthor(otherAnswerAnthors.get(i));
                if(otherAnswers.size()>i)
                    answer.setAnswer(otherAnswers.get(i));
                if(otherAnswerAnthorGrades.size()>i)
                    answer.setAnthorGrade(otherAnswerAnthorGrades.get(i));
                if(otherAnswerPointPraises.size()>i)
                    answer.setPointPraise(otherAnswerPointPraises.get(i));
                if(otherAnswerTime.size()>i)
                    answer.setAnswerTime(otherAnswerTime.get(i));
                if(otherAnswerContemptNumbers.size()>i)
                    answer.setContemptNumber(otherAnswerContemptNumbers.get(i));
                answers.add(answer);
            }
            keywordService.insertAll(p,keywordProblemKey,answers);

        }
//        if (page.getResultItems().get("name") == null) {
//            //skip this page
//            page.setSkip(true);
//        }

        // 从搜索页面发现后续的url地址来抓取
//        List<String> urls = html.links().regex("(http://zhidao.baidu.com/question/.+"+"\\?"+".+[^\"])").all();
//        for(String url:urls){
//            logger.info(url);
//        }
        if(problem==null) {
            page.addTargetRequests(html.links().regex("(http://zhidao.baidu.com/question/.+"+"\\?"+".+[^\"])").all());
            for(int i=1;i<pages;i++){
                List<String> lists = new ArrayList<String>();
                lists.add(url+keyword+"&site=-1&sites=0&date=0&pn="+(i*10));
                page.addTargetRequests(lists);
            }
            pages=0;//只加一次进队列！
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws IOException {
        //使用"spring.xml"和"spring-mybatis.xml"这两个配置文件创建Spring上下文
        context = new ClassPathXmlApplicationContext("spring.xml", "spring-mybatis.xml");
        keywordService = (KeywordService) context.getBean("keywordService");

        //存储关键字表
        Date searchTime = new Date();//搜索的时间
        keywordId = cn.hncu.tool.UUID.getId();
        Keyword keyword = new Keyword();
        keyword.setId(keywordId);
        keyword.setKeyword(Entrance.keyword);
        keyword.setSearchTime(searchTime);
        keywordService.insertKeyword(keyword);

        Spider.create(new Entrance()).addUrl(url+Entrance.keyword)
                //.addPipeline(new JsonFilePipeline("D:\\test\\"))
                //开启 *个线程抓取
                .thread(50)
                //同步启动爬虫
                .run();
    }
}
