package cn.hncu.service;

import cn.hncu.model.Answer;
import cn.hncu.model.Keyword;
import cn.hncu.model.KeywordProblemKey;
import cn.hncu.model.Problem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 陈浩翔.
 * Date: 2017/2/27.
 * Time: 下午 7:49.
 * Explain:关键字操作接口
 */
public interface KeywordService {
    //插入关键字数据
    public int insertKeyword(Keyword keyword);

    //插入问题数据_答案数据还有关键字_问题关系表数据
    @Transactional
    public void insertAll(Problem problem, KeywordProblemKey keywordProblemKey,List<Answer> answers);

}
