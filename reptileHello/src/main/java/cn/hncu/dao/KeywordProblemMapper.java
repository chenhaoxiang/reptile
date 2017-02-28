package cn.hncu.dao;

import cn.hncu.model.KeywordProblemKey;

public interface KeywordProblemMapper {
    int deleteByPrimaryKey(KeywordProblemKey key);

    int insert(KeywordProblemKey record);

    int insertSelective(KeywordProblemKey record);
}