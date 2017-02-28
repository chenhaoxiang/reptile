package cn.hncu.service;

import cn.hncu.dao.AnswerMapper;
import cn.hncu.dao.KeywordMapper;
import cn.hncu.dao.KeywordProblemMapper;
import cn.hncu.dao.ProblemMapper;
import cn.hncu.model.Answer;
import cn.hncu.model.Keyword;
import cn.hncu.model.KeywordProblemKey;
import cn.hncu.model.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 陈浩翔.
 * Date: 2017/2/27.
 * Time: 下午 7:52.
 * Explain:
 */
@Service("keywordService")
public class KeywordServiceImpl implements KeywordService{
    //其实分开写的，程序比较小，功能不多，就写一块了
    @Autowired
    private KeywordMapper keywordMapper;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private KeywordProblemMapper keywordProblemMapper;
    @Autowired
    private ProblemMapper problemMapper;

    @Override
    public int insertKeyword(Keyword keyword) {
        return keywordMapper.insert(keyword);
    }

    @Override
    public void insertAll(Problem problem,KeywordProblemKey keywordProblemKey,List<Answer> answers) {
        problemMapper.insert(problem);
        keywordProblemMapper.insert(keywordProblemKey);
        for(Answer answer:answers){
            answerMapper.insert(answer);
        }
    }

}
