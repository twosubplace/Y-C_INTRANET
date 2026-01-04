package com.ync.schedule.service;

import com.ync.schedule.domain.EmailTemplate;
import com.ync.schedule.dto.EmailTemplateDto;
import com.ync.schedule.mapper.EmailTemplateMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EmailTemplateService {

    private final EmailTemplateMapper emailTemplateMapper;

    public EmailTemplateService(EmailTemplateMapper emailTemplateMapper) {
        this.emailTemplateMapper = emailTemplateMapper;
    }

    public List<EmailTemplateDto> getAllTemplates() {
        return emailTemplateMapper.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmailTemplateDto> getActiveTemplates() {
        return emailTemplateMapper.findByIsActiveTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public EmailTemplateDto getTemplateById(Long id) {
        EmailTemplate template = emailTemplateMapper.findById(id);
        return template != null ? convertToDto(template) : null;
    }

    public EmailTemplateDto getTemplateByName(String templateName) {
        EmailTemplate template = emailTemplateMapper.findByTemplateName(templateName);
        return template != null ? convertToDto(template) : null;
    }

    @Transactional
    public EmailTemplateDto createTemplate(EmailTemplateDto dto) {
        EmailTemplate template = EmailTemplate.builder()
                .templateName(dto.getTemplateName())
                .subject(dto.getSubject())
                .content(dto.getContent())
                .description(dto.getDescription())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();

        emailTemplateMapper.insert(template);
        return convertToDto(template);
    }

    @Transactional
    public EmailTemplateDto updateTemplate(Long id, EmailTemplateDto dto) {
        EmailTemplate template = emailTemplateMapper.findById(id);
        if (template == null) {
            throw new RuntimeException("Template not found: " + id);
        }

        template.setTemplateName(dto.getTemplateName());
        template.setSubject(dto.getSubject());
        template.setContent(dto.getContent());
        template.setDescription(dto.getDescription());
        template.setIsActive(dto.getIsActive());

        emailTemplateMapper.update(template);
        return convertToDto(template);
    }

    @Transactional
    public void deleteTemplate(Long id) {
        emailTemplateMapper.deleteById(id);
    }

    private EmailTemplateDto convertToDto(EmailTemplate template) {
        return EmailTemplateDto.builder()
                .id(template.getId())
                .templateName(template.getTemplateName())
                .subject(template.getSubject())
                .content(template.getContent())
                .description(template.getDescription())
                .isActive(template.getIsActive())
                .build();
    }
}
