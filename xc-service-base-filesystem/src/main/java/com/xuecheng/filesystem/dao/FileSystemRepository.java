package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author wzg
 * @Date 2020/8/21
 * @Version 1.0
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
