package com.ync.schedule.mapper;

import com.ync.schedule.domain.EmailTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmailTemplateMapper {

    List<EmailTemplate> findAll();

    EmailTemplate findById(Long id);

    EmailTemplate findByTemplateName(@Param("templateName") String templateName);

    List<EmailTemplate> findByIsActiveTrue();

    void insert(EmailTemplate template);

    void update(EmailTemplate template);

    void deleteById(Long id);
}
