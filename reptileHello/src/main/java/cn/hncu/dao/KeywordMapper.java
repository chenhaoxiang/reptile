package cn.hncu.dao;

import cn.hncu.model.Keyword;

public interface KeywordMapper {
    int deleteByPrimaryKey(String id);

    int insert(Keyword record);

    int insertSelective(Keyword record);

    Keyword selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Keyword record);

    int updateByPrimaryKey(Keyword record);
}