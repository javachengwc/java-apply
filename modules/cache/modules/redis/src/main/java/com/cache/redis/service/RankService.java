package com.cache.redis.service;

import com.cache.model.Achieve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RankService {

    @Autowired
    private RedisTemplate redisTemplate;

    //入学生课程成绩入sorted set
    public void putAchieve(Achieve achieve) {
        ZSetOperations<String, String>  opt =redisTemplate.opsForZSet();
        //课程
        String course= achieve.getCourse();
        //学生
        String student =achieve.getStudent();
        opt.add(course,student,achieve.getScore());
    }

    //增加学生课程分数
    public Double addStudentScore(Achieve achieve) {
        ZSetOperations<String, String>  opt =redisTemplate.opsForZSet();
        //课程
        String course= achieve.getCourse();
        //学生
        String student =achieve.getStudent();

        //是负数也也可
        Integer addCount =achieve.getScore();

        //之前没存在此记录也可以
        return opt.incrementScore(course,student,addCount);
    }

    //查询课程成绩靠前的学生
    public List<Achieve> queryCourseRank(String course,long size) {
        ZSetOperations<String, String>  opt =redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>>  result =opt.reverseRangeByScoreWithScores(course, 0L, Long.MAX_VALUE, 0L, size);
        if(result!=null) {
            for(ZSetOperations.TypedTuple<String> typedTuple:result) {
                System.out.println("------------ "+typedTuple.getValue()+" "+typedTuple.getScore());
            }
        }
        return new ArrayList<Achieve>();
    }

}
