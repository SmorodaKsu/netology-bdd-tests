package ru.netology.web.test;


import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.netology.web.data.DataHelper.*;

class MoneyTransferTest {
    DashboardPage dashboardPage;
    DataHelper.CardInfo cardFirstInfo;
    DataHelper.CardInfo cardSecondInfo;
    int firstCardBalance;
    int secondCardBalance;

    @BeforeEach
    void setup() {
        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class); //запускам страницу и используем пейдж с сохраненными полями страницы
        var info = getAuthInfo(); //добавляем в переменную info - логин и пароль пользователя (пользователь Вася)
        var verificationPage = loginPage.validLogin(info);
        var verificationCode = DataHelper.getVerificationCodeFor(); // в переменную verificationCode - добавляем проверочный код
        dashboardPage = verificationPage.validVerification(verificationCode);
        cardFirstInfo = DataHelper.getFerstCardInfo(); //номер первой карты
        cardSecondInfo = DataHelper.getSecondCardInfo(); //номер второй карты
        firstCardBalance = dashboardPage.getCardBalance(cardFirstInfo);
        secondCardBalance = dashboardPage.getCardBalance(cardSecondInfo);

    }
    @Test
    void shouldTransferMoneyBetweenOwnCardsV1() { // проверка перевода с первой карты на вторую
        var amount = generateValidAmount(firstCardBalance);
        var balanceFirstCard = firstCardBalance - amount;
        var balanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCard(cardSecondInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), cardFirstInfo);
        dashboardPage.reloadDashboard();
        assertAll(() -> dashboardPage.checkCardBalance(cardFirstInfo, balanceFirstCard),
                () -> dashboardPage.checkCardBalance(cardSecondInfo, balanceSecondCard));
    }
    @Test
    void shouldTransferMoneyBetweenOwnCardsV2() { // проверка перевода со второй карты на первую
        var amount = generateValidAmount(secondCardBalance);
        var balanceFirstCard = firstCardBalance + amount;
        var balanceSecondCard = secondCardBalance - amount;
        var transferPage = dashboardPage.selectCard(cardFirstInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), cardSecondInfo);
        dashboardPage.reloadDashboard();
        assertAll(() -> dashboardPage.checkCardBalance(cardFirstInfo, balanceFirstCard),
                () -> dashboardPage.checkCardBalance(cardSecondInfo, balanceSecondCard));
    }
    @Test
    void shouldTransferMoneyBetweenOwnCardsInvalid () {
        var amount = generateInvalidAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCard(cardFirstInfo);
        transferPage.makeValidTransfer(String.valueOf(amount), cardFirstInfo);
        assertAll(() -> transferPage.findErrorMassage("Выполнена попытка перевода суммы, превышающей остаток на карте списания"));
    }
}