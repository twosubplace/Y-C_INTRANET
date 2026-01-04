package com.ync.intranet.controller;

import com.ync.intranet.domain.DepartmentIntranet;
import com.ync.intranet.service.DepartmentIntranetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 부서 관리 컨트롤러 (인트라넷)
 */
@RestController("departmentIntranetController")
@RequestMapping("/api/intranet/departments")
@CrossOrigin(origins = "*")
public class DepartmentIntranetController {

    private final DepartmentIntranetService departmentService;

    public DepartmentIntranetController(DepartmentIntranetService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 전체 부서 조회
     * GET /api/intranet/departments
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDepartments() {
        List<DepartmentIntranet> departments = departmentService.findAll();
        return ResponseEntity.ok(Map.of("success", true, "departments", departments));
    }

    /**
     * 활성화된 부서만 조회
     * GET /api/intranet/departments/active
     */
    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveDepartments() {
        List<DepartmentIntranet> departments = departmentService.findActiveDepartments();
        return ResponseEntity.ok(Map.of("success", true, "departments", departments));
    }

    /**
     * 최상위 부서 조회 (Root 부서)
     * GET /api/intranet/departments/root
     */
    @GetMapping("/root")
    public ResponseEntity<Map<String, Object>> getRootDepartments() {
        List<DepartmentIntranet> departments = departmentService.findRootDepartments();
        return ResponseEntity.ok(Map.of("success", true, "departments", departments));
    }

    /**
     * 하위 부서 조회 (parent_id 기준)
     * GET /api/intranet/departments/children/{parentId}
     */
    @GetMapping("/children/{parentId}")
    public ResponseEntity<Map<String, Object>> getChildrenDepartments(@PathVariable Long parentId) {
        List<DepartmentIntranet> departments = departmentService.findByParentId(parentId);
        return ResponseEntity.ok(Map.of("success", true, "departments", departments));
    }

    /**
     * ID로 부서 조회
     * GET /api/intranet/departments/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDepartment(@PathVariable Long id) {
        DepartmentIntranet department = departmentService.findById(id);
        if (department == null) {
            return ResponseEntity.ok(Map.of("success", false, "message", "부서를 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(Map.of("success", true, "department", department));
    }
}
