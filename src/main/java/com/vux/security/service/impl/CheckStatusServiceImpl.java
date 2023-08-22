package com.vux.security.service.impl;

import com.vux.security.dto.CheckStatusDto;
import com.vux.security.entity.CheckStatus;
import com.vux.security.mapper.CheckStatusMapper;
import com.vux.security.payload.CheckInfo;
import com.vux.security.payload.ICheckInfo;
import com.vux.security.repository.CheckStatusRepository;
import com.vux.security.service.CheckStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckStatusServiceImpl implements CheckStatusService {
    private final CheckStatusRepository repository;


    @Override
    public List<CheckInfo> getCheckList(Integer userId) {
        return repository.findCheckListById(userId);
    }

    @Override
    public List<CheckInfo> getCheckListBetween(Integer userId, LocalDate begin, LocalDate end) {
        return repository.findCheckListByIdBetween(userId, begin, end);
    }
}
