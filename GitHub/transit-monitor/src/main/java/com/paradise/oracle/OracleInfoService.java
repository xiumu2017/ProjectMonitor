package com.paradise.oracle;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Service for Oracle Info
 *
 * @author dzhang
 */
@Service
public class OracleInfoService {

    @Resource
    private OracleInfoMapper oracleInfoMapper;

    public OracleInfo selectByProjectId(String projectId) {
        return oracleInfoMapper.selectByProjectId(projectId);
    }
}
