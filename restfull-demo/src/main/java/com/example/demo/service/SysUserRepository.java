package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.example.demo.entity.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SysUserRepository {

    private final static Logger logger = LoggerFactory.getLogger(SysUserRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> queryAllUser(User user) {
        List<User> result = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM TEST_API_USER WHERE ENABLED_FLAG = 'Y' ");
        List<Object> parameters = new ArrayList<>();
        if (null != user.getId()) {
            sqlBuilder.append("AND ID = ? ");
            parameters.add(user.getId());
        }

        if (null != user.getAge()) {
            sqlBuilder.append("AND AGE = ? ");
            parameters.add(user.getAge());
        }

        if (null != user.getSex()) {
            sqlBuilder.append("AND SEX = ? ");
            parameters.add(user.getSex());
        }

        if (StringUtils.isNotBlank(user.getName())) {
            sqlBuilder.append("AND NAME LIKE ? ");
            parameters.add("%" + user.getName() + "%");
        }

        List<?> queryResult = jdbcTemplate.queryForList(sqlBuilder.toString(), parameters.toArray());
        if (CollectionUtils.isNotEmpty(queryResult)) {
            for (Object obj : queryResult) {
                Long id = MapUtils.getLong((Map) obj, "ID");
                String name = MapUtils.getString((Map) obj, "NAME");
                String remark = MapUtils.getString((Map) obj, "REMARK");
                String sex = MapUtils.getString((Map) obj, "SEX");
                Integer age = MapUtils.getInteger((Map) obj, "AGE");
                String enabledFlag = MapUtils.getString((Map) obj, "ENABLED_FLAG");
                String createDate = MapUtils.getString((Map) obj, "CREATED_DATE");
                String updateDate = MapUtils.getString((Map) obj, "LAST_UPDATE_DATE");
                User temp = new User(id, name, age, remark, sex, enabledFlag, createDate, updateDate);
                result.add(temp);
            }
        }
        return result;
    }

    public void insertUser(User user) {
        String sql = "INSERT INTO TEST_API_USER (NAME, AGE, REMARK, SEX, ENABLED_FLAG, CREATED_DATE, LAST_UPDATE_DATE) VALUES (?, ?, ?, ?, 'Y', NOW(), NOW())";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, user.getName());
                ps.setInt(2, user.getAge());
                ps.setString(3, user.getRemark());
                ps.setString(4, user.getSex());
            }
        });

    }

    public User getUser(Long id) {
        String sql = "SELECT * FROM TEST_API_USER WHERE ENABLED_FLAG = 'Y' AND ID = " + id;
        try{
            Map<String, Object> queryResult = jdbcTemplate.queryForMap(sql);
            logger.info("Method getUser result:[{}]", JSON.toJSONString(queryResult));
            User service = new User();
            service.setId(MapUtils.getLong(queryResult, "ID"));
            service.setName(MapUtils.getString(queryResult, "NAME"));
            service.setAge(MapUtils.getInteger(queryResult, "AGE"));
            service.setRemark(MapUtils.getString(queryResult, "REMARK"));
            service.setSex(MapUtils.getString(queryResult, "SEX"));
            service.setEnabledFlag(MapUtils.getString(queryResult, "ENABLED_FLAG"));
            service.setCreateDate(MapUtils.getString(queryResult, "CREATED_DATE"));
            service.setUpdateDate(MapUtils.getString(queryResult, "LAST_UPDATE_DATE"));
            return service;
        }catch (Exception e){
            logger.error("invoke getUser method failed. " + e.getMessage(), e);
            return null;
        }
    }

    public void updateUser(User user) {
        String sql = "UPDATE TEST_API_USER SET NAME=?, AGE=?, REMARK=?, SEX=?, LAST_UPDATE_DATE=NOW() WHERE ID=?";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, user.getName());
                ps.setInt(2, user.getAge());
                ps.setString(3, user.getRemark());
                ps.setString(4, user.getSex());
                ps.setLong(5, user.getId());
            }
        });
    }

    public void deleteUser(Long id) {
        String sql = "DELETE FROM TEST_API_USER WHERE ID = " + id;
        jdbcTemplate.execute(sql);
    }

}
