package com.liwei.wiki.mapper;

import org.apache.ibatis.annotations.Param;

public interface DocMapperCust {
    public int increaseViewCount(@Param("id") Long id);
    public int increaseVoteCount(@Param("id") Long id);
    public void updateEbookInfo();
}
