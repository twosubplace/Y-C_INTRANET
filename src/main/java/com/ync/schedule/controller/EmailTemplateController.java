package com.ync.schedule.controller;

import com.ync.schedule.dto.EmailTemplateDto;
import com.ync.schedule.service.EmailTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/email-templates")
public class EmailTemplateController {

    private final EmailTemplateService emailTemplateService;

    public EmailTemplateController(EmailTemplateService emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
    }

    @GetMapping
    public ResponseEntity<List<EmailTemplateDto>> getAllTemplates() {
        return ResponseEntity.ok(emailTemplateService.getAllTemplates());
    }

    @GetMapping("/active")
    public ResponseEntity<List<EmailTemplateDto>> getActiveTemplates() {
        return ResponseEntity.ok(emailTemplateService.getActiveTemplates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailTemplateDto> getTemplateById(@PathVariable Long id) {
        EmailTemplateDto template = emailTemplateService.getTemplateById(id);
        return template != null ? ResponseEntity.ok(template) : ResponseEntity.notFound().build();
    }

    @GetMapping("/by-name/{templateName}")
    public ResponseEntity<EmailTemplateDto> getTemplateByName(@PathVariable String templateName) {
        EmailTemplateDto template = emailTemplateService.getTemplateByName(templateName);
        return template != null ? ResponseEntity.ok(template) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<EmailTemplateDto> createTemplate(@RequestBody EmailTemplateDto dto) {
        return ResponseEntity.ok(emailTemplateService.createTemplate(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmailTemplateDto> updateTemplate(
            @PathVariable Long id,
            @RequestBody EmailTemplateDto dto) {
        return ResponseEntity.ok(emailTemplateService.updateTemplate(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        emailTemplateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
