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
    String LOGIN_PAGE = "00a-loginPage";
    String LOGOUT_PAGE = "00b-logoutPage";
    String REGISTRATION = "00c-registration";
    String REGISTRATION_SUCCESS = "00d-registrationSuccess";
    String REMOVE_PAGE = "00e-removePage";

    String INFO_AND_HELP = "01a-infoAndHelp";

    String EXPENSES = "02a-expenses";
    String EXPENSES_ADD = "02b-expenses";
    String EXPENSES_MODIFY = "02c-expenses";

    String LOANS = "05a-loans";
    String LOANS_ADD = "05b-loans";
    String LOANS_MODIFY = "05c-loans";
    String LOANS_DETAILS = "05d-loans";

    String TAXES = "06a-taxes";
    String TAXES_ADD = "06b-taxes";
    String TAXES_MODIFY = "06c-taxes";

    String STATISTICS_MENU = "07a-statistics";
    String STATISTICS_EXPENSES = "07b-statistics-expenses";
    String STATISTICS_LOANS = "07c-statistics-loans";
    String STATISTICS_TAXES = "07d-statistics-taxes";

    String FORECASTING = "08a-forecasting";

    String MENU_EDIT = "09a-menuEdit";

    String MENU_EDIT_EXPENSE_CATEGORIES = "09ba-menuEditExpenseCategories";
    String MENU_EDIT_EXPENSE_CATEGORIES_ADD = "09bb-menuEditExpenseCategories";
    String MENU_EDIT_EXPENSE_CATEGORIES_MODIFY = "09bc-menuEditExpenseCategories";

    String MENU_EDIT_FORMS_OF_PAYMENT = "09da-menuEditFormsOfPayment";
    String MENU_EDIT_FORMS_OF_PAYMENT_ADD = "09db-menuEditFormsOfPayment";
    String MENU_EDIT_FORMS_OF_PAYMENT_MODIFY = "09dc-menuEditFormsOfPayment";

    String MENU_EDIT_KINDS_OF_OPERATION = "09ea-menuEditKindsOfOperation";
    String MENU_EDIT_KINDS_OF_OPERATION_ADD = "09eb-menuEditKindsOfOperation";
    String MENU_EDIT_KINDS_OF_OPERATION_MODIFY = "09ec-menuEditKindsOfOperation";

    String MENU_EDIT_BANKS = "09fa-menuEditBanks";
    String MENU_EDIT_BANKS_ADD = "09fb-menuEditBanks";
    String MENU_EDIT_BANKS_MODIFY = "09fc-menuEditBanks";

    String MENU_EDIT_KINDS_OF_LOAN = "09ga-menuEditKindsOfLoan";
    String MENU_EDIT_KINDS_OF_LOAN_ADD = "09gb-menuEditKindsOfLoan";
    String MENU_EDIT_KINDS_OF_LOAN_MODIFY = "09gc-menuEditKindsOfLoan";

    String MENU_EDIT_USERS = "09ia-menuEditUsers";
    String MENU_EDIT_USERS_ADD = "09ib-menuEditUsers";
    String MENU_EDIT_USERS_MODIFY = "09ic-menuEditUsers";
    String MENU_EDIT_USERS_PASSWORD = "09id-menuEditUsers";

    String MENU_EDIT_CAPITALISATIONS = "09ja-menuEditCapitalisations";
    String MENU_EDIT_CAPITALISATIONS_ADD = "09jb-menuEditCapitalisations";
    String MENU_EDIT_CAPITALISATIONS_MODIFY = "09jc-menuEditCapitalisations";

    String ACCOUNT_DATA = "10a-accountData";

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
