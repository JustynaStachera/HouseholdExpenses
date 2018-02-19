package com.justyna.stachera.householdexpenses.service;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBBankDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBCapitalisationDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBKindOfLoanDao;
import com.justyna.stachera.householdexpenses.domain.joined.SBKindOfLoanJoined;
import com.justyna.stachera.householdexpenses.domain.main.SBBank;
import com.justyna.stachera.householdexpenses.domain.main.SBCapitalisation;
import com.justyna.stachera.householdexpenses.domain.main.SBKindOfLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 * <p>
 * Class which contains implemented methods from {@link SBKindOfLoan}JoinedService interface.
 */
@Service
public class SBKindOfLoanJoinedImpl implements SBKindOfLoanJoinedService
{
    private SBKindOfLoanDao kindOfLoanDao;
    private SBBankDao bankDao;
    private SBCapitalisationDao capitalisationDao;

    /**
     * Argument constructor.
     *
     * @param kindOfLoanDao     It provides methods related with 'sbkind_of_loan' table from database.
     * @param bankDao           It provides methods related with 'sbbank' table from database.
     * @param capitalisationDao It provides methods related with 'sbcapitalisation' table from database.
     */
    @Autowired
    public SBKindOfLoanJoinedImpl(SBKindOfLoanDao kindOfLoanDao,
                                  SBBankDao bankDao,
                                  SBCapitalisationDao capitalisationDao)
    {
        this.kindOfLoanDao = kindOfLoanDao;
        this.bankDao = bankDao;
        this.capitalisationDao = capitalisationDao;
    }

    @Override
    @Transactional
    public void addKindOfLoanJoined(SBKindOfLoanJoined kindOfLoanJoined)
    {
        SBBank bank = bankDao.getOne(kindOfLoanJoined.getBankId());
        SBCapitalisation capitalisation = capitalisationDao.getOne(kindOfLoanJoined.getCapitalisationId());

        SBKindOfLoan kindOfLoan = SBKindOfLoan
                .builder()
                .name(kindOfLoanJoined.getName())
                .percent(kindOfLoanJoined.getPercent())
                .durationTime(kindOfLoanJoined.getDurationTime())
                .bank(bank)
                .capitalisation(capitalisation)
                .build();

        kindOfLoanDao.save(kindOfLoan);
    }
}