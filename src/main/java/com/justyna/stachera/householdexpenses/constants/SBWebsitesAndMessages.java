package com.justyna.stachera.householdexpenses.constants;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Interface which contains the logical view names and repeated messages.
 */
public interface SBWebsitesAndMessages
{
    String loginPage = "00a-loginPage";
    String logoutPage = "00b-logoutPage";
    String registration = "00c-registration";
    String registrationSuccess = "00d-registrationSuccess";
    String removePage = "00e-removePage";

    String infoAndHelp = "01a-infoAndHelp";

    String expenses = "02a-expenses";
    String expensesAdd = "02b-expenses";
    String expensesModify = "02c-expenses";

    String loans = "05a-loans";
    String loansAdd = "05b-loans";
    String loansModify = "05c-loans";
    String loansDetails = "05d-loans";

    String taxes = "06a-taxes";
    String taxesAdd = "06b-taxes";
    String taxesModify = "06c-taxes";

    String statisticsMenu = "07a-statistics";
    String statisticsExpenses = "07b-statistics-expenses";
    String statisticsLoans = "07c-statistics-loans";
    String statisticsTaxes = "07d-statistics-taxes";

    String forecasting = "08a-forecasting";

    String menuEdit = "09a-menuEdit";

    String menuEditExpenseCategories = "09ba-menuEditExpenseCategories";
    String menuEditExpenseCategoriesAdd = "09bb-menuEditExpenseCategories";
    String menuEditExpenseCategoriesModify = "09bc-menuEditExpenseCategories";

    String menuEditFormsOfPayment = "09da-menuEditFormsOfPayment";
    String menuEditFormsOfPaymentAdd = "09db-menuEditFormsOfPayment";
    String menuEditFormsOfPaymentModify = "09dc-menuEditFormsOfPayment";

    String menuEditKindsOfOperation = "09ea-menuEditKindsOfOperation";
    String menuEditKindsOfOperationAdd = "09eb-menuEditKindsOfOperation";
    String menuEditKindsOfOperationModify = "09ec-menuEditKindsOfOperation";

    String menuEditBanks = "09fa-menuEditBanks";
    String menuEditBanksAdd = "09fb-menuEditBanks";
    String menuEditBanksModify = "09fc-menuEditBanks";

    String menuEditKindsOfLoan = "09ga-menuEditKindsOfLoan";
    String menuEditKindsOfLoanAdd = "09gb-menuEditKindsOfLoan";
    String menuEditKindsOfLoanModify = "09gc-menuEditKindsOfLoan";

    String menuEditUsers = "09ia-menuEditUsers";
    String menuEditUsersAdd = "09ib-menuEditUsers";
    String menuEditUsersModify = "09ic-menuEditUsers";
    String menuEditUsersPassword = "09id-menuEditUsers";

    String menuEditCapitalisations = "09ja-menuEditCapitalisations";
    String menuEditCapitalisationsAdd = "09jb-menuEditCapitalisations";
    String menuEditCapitalisationsModify = "09jc-menuEditCapitalisations";

    String accountData = "10a-accountData";

    String ADMIN_RIGHTS_ACCESS_MSG = "Nie można sobie odebrać praw administratora!";
    String ADMIN_MODIFY_ACCESS_MSG = "Brak uprawnień do modyfikacji konta innego administratora!";
    String ADMIN_REMOVE_ACCESS_MSG = "Brak uprawnień do usunięcia konta innego administratora!";
    String ADMIN_REMOVE_ACCOUNT_MSG = "W celu usunięcia własnego konta wejdź w: PANEL UŻYTKOWNIKA > USUŃ KONTO";
    String SUPER_ADMIN_REMOVE_MSG = "Nie można usunąć konta tego użytkownika z poziomu aplikacji!";
    String PASSWORD_MODIFY_ACCESS_MSG = "Brak uprawnień do resetowania hasła innego administratora!";
    String PASSWORD_MODIFY_MSG = "Hasło zmodyfikowane pomyślnie!";
    String PASSWORD_RESET_MSG = "Hasło zresetowane pomyślnie!";
    String DATA_ADD_MSG = "Rekord dodany pomyślnie!";
    String DATA_MODIFY_MSG = "Rekord zmodyfikowany pomyślnie!";
    String DATA_REMOVE_MSG = "Rekord usunięty pomyślnie!";
}
