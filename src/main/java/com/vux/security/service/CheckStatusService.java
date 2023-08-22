package com.vux.security.service;


import com.vux.security.payload.CheckInfo;

import java.time.LocalDate;
import java.util.List;

public interface CheckStatusService {

    List<CheckInfo> getCheckList(Integer userId);

    List<CheckInfo> getCheckListBetween(Integer userId, LocalDate begin, LocalDate end);
}
