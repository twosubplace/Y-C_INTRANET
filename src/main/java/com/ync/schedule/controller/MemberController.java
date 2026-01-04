package com.ync.schedule.controller;

import com.ync.schedule.dto.LeaveBalanceDto;
import com.ync.schedule.dto.MemberDto;
import com.ync.schedule.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberDto>> getAllMembers(@RequestParam(required = false) Boolean active) {
        List<MemberDto> members = active != null && active
                ? memberService.getActiveMembers()
                : memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> getMember(@PathVariable Long id) {
        MemberDto member = memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }

    @PostMapping
    public ResponseEntity<MemberDto> createMember(@RequestBody MemberDto dto) {
        MemberDto created = memberService.createMember(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long id, @RequestBody MemberDto dto) {
        MemberDto updated = memberService.updateMember(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/leave-balance")
    public ResponseEntity<LeaveBalanceDto> getLeaveBalance(@PathVariable Long id, @RequestParam int year) {
        LeaveBalanceDto balance = memberService.getLeaveBalance(id, year);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/leave-balances")
    public ResponseEntity<List<LeaveBalanceDto>> getAllLeaveBalances(@RequestParam int year) {
        List<LeaveBalanceDto> balances = memberService.getAllLeaveBalances(year);
        return ResponseEntity.ok(balances);
    }
}
