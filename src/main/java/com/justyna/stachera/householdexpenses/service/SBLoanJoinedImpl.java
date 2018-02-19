package com.justyna.stachera.householdexpenses.service;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBKindOfLoanDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBLoanDao;
import com.justyna.stachera.householdexpenses.domain.joined.SBLoanJoined;
import com.justyna.stachera.householdexpenses.domain.main.SBLoan;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.utils.SBEconomicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 * <p>
 * Class which contains implemented methods from {@link SBLoan}JoinedService interface.
 */
@Service
public class SBLoanJoinedImpl implements SBLoanJoinedService
{
    private SBLoanDao loanDao;
    private SBKindOfLoanDao kindOfLoanDao;

    /**
     * Argument constructor.
     *
     * @param loanDao       It provides methods related with 'sbloan' table from database.
     * @param kindOfLoanDao It provides methods related with 'sbkind_of_loan' table from database.
     */
    @Autowired
    public SBLoanJoinedImpl(SBLoanDao loanDao,
                            SBKindOfLoanDao kindOfLoanDao)
    {
        this.loanDao = loanDao;
        this.kindOfLoanDao = kindOfLoanDao;
    }

    @Override
    @Transactional
    public void addLoanJoined(SBLoanJoined loanJoined, SBUser user)
    {
        SBLoan loan = SBLoan
                .builder()
                .initialAmount(loanJoined.getInitialAmount())
                .beginDate(loanJoined.getBeginDate())
                .paidUpMonths(loanJoined.getPaidUpMonths())
                .description(loanJoined.getDescription())
                .kindOfLoan(kindOfLoanDao.getOne(loanJoined.getKindOfLoanId()))
                .isActive(loanJoined.getIsActive())
                .user(user)
                .build();

        SBEconomicUtils.calculateLoan(loan);

        loanDao.save(loan);
    }
}
