package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement heading1 = $("[data-test-id=dashboard]");
    private final SelenideElement heading2 = $("h1");
    private final SelenideElement amountField = $("[data-test-id=amount] .input__control").shouldBe(visible); // Поле для ввода суммы
    private final SelenideElement cardNumberField = $("[data-test-id=from] .input__control").shouldBe(visible); //Поле для ввода карты с которой переводим
    private final SelenideElement buttonReplenish = $(".button"); // кнопка
    private final SelenideElement errorMassage = $("[data-test-id='error-notification'] .notification__content");

    public TransferPage() {
        heading1.shouldBe(visible);
        heading2.shouldBe(visible);
    }

    public DashboardPage makeValidTransfer(String amountToTransfer, DataHelper.CardInfo cardSecondInfo) {
        enterTransferAmount(amountToTransfer, cardSecondInfo);
        return new DashboardPage();
    }

    // Метод для заполнения поля суммы перевода
    public void enterTransferAmount(String amountToTransfer, DataHelper.CardInfo cardSecondInfo) {
        amountField.setValue(amountToTransfer);
        cardNumberField.setValue(cardSecondInfo.getNumber());
        buttonReplenish.click();
    }

    public void findErrorMassage(String expectedText) {
        errorMassage.should(Condition.and("Проверка сообщения об ошибке", Condition.text(expectedText), visible),
                Duration.ofSeconds(15));
    }
}

